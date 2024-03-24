use super::{SuccessResponse, ErrorResponse, Response};
use jsonwebtoken::{encode, Header, EncodingKey, Claims};
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
};

use crate::{ database::db::DB, entities::data_models::User };

#[post("/login", data="<req_sign_in>")]
pub async fn login(
    db: &State<DB>,
    req_sign_in: Json<ReqSignIn>,
) -> Response<Json<ResSignIn>> {
    let db: &DB = db as &DB;
    let user = db.fetch_user(&req_sign_in.email).await.unwrap();
    if user.password == req_sign_in.password {
       let token = encode(
           &Header::default(),
           &Claims {
               sub: user.id.to_string(),
               exp: (SystemTime::now() + Duration::from_secs(60)).duration_since(UNIX_EPOCH).unwrap().as_secs() as usize,
               role: "user".to_string(),
           },
           &EncodingKey::from_secret("secret".as_ref()),
       ); 
    } else {
        Ok(ErrorResponse((Status::Unauthorized, "Unauthorized".to_string())))
    }
}

#[post("/register", data="<req_sign_up>")]
pub async fn register(
    db: &State<DB>,
    req_sign_up: Json<ReqSignUp>,
) -> Response<String> {
    let user = db.create_user(&req_sign_up.email, &req_sign_up.password).await.unwrap();
    Ok(SuccessResponse((Status::Ok, "Registered".to_string())))
}

#[post("/logout")]
pub async fn logout() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "Logout".to_string())))
}

#[get("/me")]
pub async fn me(db: &DB, user: AuthenticatedUser) -> Response<String> {
    let db = db as &DB;

    let user: data_models::User = db.fetch_user(&req_sign_in.email).await.unwrap();

    Ok(SuccessResponse((Status::Ok, ResMe {
        id: user._id,
        email: user.email,
        username: user.username,
    })))
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

