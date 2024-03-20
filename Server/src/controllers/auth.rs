use super::Response;
use super::SuccessResponse;
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
};

#[post("/login", data="req_sign_in")]
pub async fn login(
    db: &State<DB>,
    req_sign_in: Json<ReqSignIn>,
) -> Response<ReqSignIn> {
    let user = db.fetch_user(&req_sign_in.email).await.unwrap();
    if user.password == req_sign_in.password {
        Ok(SuccessResponse((Status::Ok, ResSignIn {
            token: "token".to_string(),
        })))
    } else {
        Ok(ErrorResponse((Status::Unauthorized, "Unauthorized".to_string())))
    }
}

#[post("/register", data="req_sign_up")]
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
pub async fn me() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "Id: 00 \nUsername: Test \nPassword: test123".to_string())))
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
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResMe {
    id: i32,
    email: String,
    username: String,
}

