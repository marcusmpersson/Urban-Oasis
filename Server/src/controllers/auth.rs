use super::{SuccessResponse, ErrorResponse, Response};
use jsonwebtoken::{encode, Header, EncodingKey, Claims};
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
};
use std::time::{SystemTime, UNIX_EPOCH, Duration, strftime};
use mongodb::bson::doc;


use crate::{ database::db::DB, entities::data_models::User };
use crate::auth::token::AuthenticatedUser;
use crate::private::{JWT_SECRET, REFRESH_SECRET};

#[post("/login", data="<req_sign_in>")]
pub async fn login(
    db: &State<DB>,
    req_sign_in: Json<ReqSignIn>,
) -> Response<Json<ResSignIn>> {
    let db: &DB = db as &DB;
    let u: User = db.fetch_user(&req_sign_in.email).await.unwrap();
    if u.password != req_sign_in.password {
       return Err(ErrorResponse((Status::Unauthorized, "Invalid credentials".to_string())));
    }

    let claims = Claims {
        sub: u.id,
        role: "user".to_string(),
        exp: SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap().as_secs() + 4*60*60,
    };

    let token = encode(
        &Header::default(),
        &claims, 
        &EncodingKey::from_secret(JWT_SECRET),
    ).unwrap();

    Ok(SuccessResponse((Status::Ok, Json(ResSignIn { token }))))
}

#[post("/register", data="<req_sign_up>")]
pub async fn register(
    db: &State<DB>,
    req_sign_up: Json<ReqSignUp>,
) -> Response<String> {
    let db = db as &DB;

    if db.fetch_user(&req_sign_up.email).await.is_ok() {
        return Err(ErrorResponse((Status::UnprocessableEntity, "User already exists".to_string())));
    }

    db.insert_user(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username).await.unwrap();

    Ok(SuccessResponse((Status::Created, "Registered".to_string())))
}

#[post("/logout")]
pub async fn logout() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "Logout".to_string())))
}

#[get("/me")]
pub async fn me(
    db: &State<DB>, 
    user: AuthenticatedUser
) -> Response<Json<ResMe>> {
    let db = db as &DB;

    let u: User = db.fetch_user_by_id(user.id).await.unwrap();

    Ok(SuccessResponse((
        Status::Ok,
        Json(ResMe {
            id: u.id,
            email: u.email,
            username: u.username,
        }),
    )))
}

#[post("/delete")]
pub async fn delete(
    db: &State<DB>, 
    user: AuthenticatedUser
) -> Response<String> {
    let db = db as &DB;

    db.delete_user(user.id).await.unwrap();

    Ok(SuccessResponse((Status::Ok, "Deleted".to_string())))
}

#[derive(Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqSignIn {
    email: String,
    password: String,
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResSignIn {
    token: String,
}

#[derive(Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqSignUp {
    email: String,
    password: String,
    username: String,
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResMe {
    id: i32,
    email: String,
    username: String,
}

impl DB {
    async fn fetch_user(&self, email: &str) -> Result<User, String> {
        let filter = doc! { "email": email };
        let user = self.database.collection("users").find_one(filter, None).await.unwrap().unwrap();
        Ok(mongodb::bson::from_document(user).unwrap())
    }

    async fn fetch_user_by_id(&self, id: i32) -> Result<User, String> {
        let filter = doc! { "id": id };
        let user = self.database.collection("users").find_one(filter, None).await.unwrap().unwrap();
        Ok(mongodb::bson::from_document(user).unwrap())
    }

    async fn insert_user(&self, email: &str, password: &str, username: &str) -> Result<(), String> {

        let user = User {
            id: self.database.collection("users").count_documents(None, None).await.unwrap() as i32,
            email: email.to_string(),
            password: password.to_string(),
            username: username.to_string(),
            created_at: SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap().as_secs().to_string(),
        };

        self.database.collection("users").insert_one(mongodb::bson::to_document(&user).unwrap(), None).await.unwrap();
        Ok(())
    }

    async fn update_user(&self, id: i32, email: &str, password: &str, username: &str) -> Result<(), String> {
        let filter = doc! { "id": id };
        let update = doc! { "$set": { "email": email, "password": password, "username": username } };
        self.database.collection("users").update_one(filter, update, None).await.unwrap();
        Ok(())
    }

    async fn delete_user(&self, id: i32) -> Result<(), String> {
        let filter = doc! { "id": id };
        self.database.collection("users").delete_one(filter, None).await.unwrap();
        Ok(())
    }
}

