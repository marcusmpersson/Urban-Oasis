use super::{SuccessResponse, ErrorResponse, Response};
use jsonwebtoken::{encode, Header, EncodingKey};
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
};
use std::time::SystemTime;
use mongodb::bson::{doc, oid::ObjectId};
use mongodb::bson;
use bcrypt::{hash, verify, DEFAULT_COST};
use chrono::{DateTime, offset::Utc};


use crate::{ database::db::DB, entities::data_models::User };
use crate::auth::token::{ AuthenticatedUser, Claims };
use crate::private_cons::{JWT_SECRET, REFRESH_SECRET};
use crate::controllers::validators::{check_valid_login, check_valid_signup};

#[post("/login", data="<req_sign_in>")]
pub async fn login(
    db: &State<DB>,
    req_sign_in: Json<ReqSignIn>,
) -> Response<Json<ResSignIn>> {
    let db: &DB = db as &DB;

    let u: User = match db.fetch_user(&req_sign_in.email).await.unwrap() {
        Some(u) => u,
        None => return Err(ErrorResponse((Status::Unauthorized, "Invalid credentials".to_string()))),
    };

    match check_valid_login(&req_sign_in.email, &req_sign_in.password) {
        Ok(_) => {
            if !verify(&req_sign_in.password, &u.password).unwrap() {
                return Err(ErrorResponse((Status::Unauthorized, "Invalid credentials".to_string())));
            }

            let claims = Claims {
                sub: u._id,
                role: "user".to_string(),
                exp: SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap().as_secs() + 4*60*60,
            };

            let token = encode(
                &Header::default(),
                &claims, 
                &EncodingKey::from_secret(JWT_SECRET.as_ref()),
            ).unwrap();

            Ok(SuccessResponse((Status::Ok, Json(ResSignIn { token }))))
        },
        Err(e) => Err(ErrorResponse((Status::UnprocessableEntity, e.to_string()))),
    }
}

#[post("/register", data="<req_sign_up>")]
pub async fn register(
    db: &State<DB>,
    req_sign_up: Json<ReqSignUp>,
) -> Response<String> {
    let db = db as &DB;

        match check_valid_signup(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username) {
        Ok(_) => {
            if db.fetch_user(&req_sign_up.email).await.unwrap().is_some() {
                return Err(ErrorResponse((Status::Conflict, "Email already exists".to_string())));
            } 

            db.insert_user(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username).await.unwrap();
        
            Ok(SuccessResponse((Status::Created, "Registered".to_string())))
        },
        Err(e) => Err(ErrorResponse((Status::UnprocessableEntity, e.to_string()))),
    } 
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
    email: String,
    username: String,
}

impl DB {
    async fn fetch_user(&self, email: &str) -> mongodb::error::Result<Option<User>> {
        let collection_user = self.database.collection::<User>("users"); 

        Ok(collection_user.find_one(bson::doc! { "email": email }, None).await?)
    }

    async fn fetch_user_by_id(&self, id: ObjectId) -> Result<User, String> {
        let filter = doc! { "id": id };
        let user = self.database.collection("users").find_one(filter, None).await.unwrap().unwrap();

        Ok(mongodb::bson::from_document(user).unwrap())
    }

    async fn insert_user(&self, email: &str, password: &str, username: &str) -> Result<(), String> {

        let user = User {
            _id: ObjectId::new(), 
            email: email.to_string(),
            password: hash(password, DEFAULT_COST).unwrap(),
            username: username.to_string(),
            created_at: DateTime::<Utc>::from(SystemTime::now()).format("%Y-%m-%d %H:%M:%S").to_string(),
        };

        self.database.collection("users").insert_one(mongodb::bson::to_document(&user).unwrap(), None).await.unwrap();
        Ok(())
    }

    async fn update_user(&self, id: ObjectId, email: &str, password: &str, username: &str) -> mongodb::error::Result<()> {
        let filter = doc! { "id": id };
        let update = doc! { "$set": { "email": email, "password": password, "username": username } };

        self.database.collection::<User>("users").update_one(filter, update, None).await.unwrap();
        Ok(())
    }

    async fn delete_user(&self, id: ObjectId) -> mongodb::error::Result<()> {

        self.database.collection::<User>("users").delete_one(doc! { "id": id }, None).await;
        Ok(())
    }
}

