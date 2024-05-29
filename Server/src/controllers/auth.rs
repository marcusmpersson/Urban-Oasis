use super::{SuccessResponse, ErrorResponse, Response};
use jsonwebtoken::{encode, Header, EncodingKey};
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
};
use std::time::{SystemTime, UNIX_EPOCH};
use mongodb::bson::{doc, oid::ObjectId};
use bcrypt::{hash, verify, DEFAULT_COST};
use chrono::{DateTime, offset::Utc};
use mongodb::bson;

use crate::{database::db::DB, entities::data_models::{UserCredentials, Inventory}}; // Importing local crate modules.
use crate::auth::token::{AuthenticatedUser, Claims}; // Importing local authentication token module.
use crate::private_cons::{JWT_SECRET, REFRESH_SECRET}; // Importing secret keys.
use crate::controllers::validators::{check_valid_login, check_valid_signup}; // Importing local validator functions.


/// Handler for the login route.
#[post("/login", data="<req_sign_in>")]
pub async fn login(
    db: &State<DB>, // State for database access.
    req_sign_in: Json<ReqSignIn>, // Request payload for sign-in.
) -> Response<Json<ResSignIn>> {
    let db: &DB = db as &DB;

    // Fetch user by email.
    let u: UserCredentials = match db.fetch_user(&req_sign_in.email).await.unwrap() {
        Some(u) => u,
        None => return Err(ErrorResponse((Status::Unauthorized, "Invalid credentials".to_string()))),
    };

    // Validate login credentials.
    match check_valid_login(&req_sign_in.email, &req_sign_in.password) {
        Ok(_) => {
            // Verify password.
            if !verify(&req_sign_in.password, &u.password).unwrap() {
                return Err(ErrorResponse((Status::Unauthorized, "Invalid credentials".to_string())));
            }

            let exp_duration = 4 * 60 * 60; // Token expiration duration in seconds (4 hours)
            let expiration = SystemTime::now()
                .duration_since(UNIX_EPOCH)
                .expect("Time went backwards")
                .as_secs() + exp_duration;

            // Create JWT claims.
            let claims = Claims {
                sub: u._id,
                role: "user".to_string(),
                exp: expiration,
            };

            // Encode JWT token.
            let token = encode(
                &Header::default(),
                &claims,
                &EncodingKey::from_secret(JWT_SECRET.as_bytes()),
            ).unwrap();

            // Return success response with token.
            Ok(SuccessResponse((Status::Ok, Json(ResSignIn { token }))))
        },
        Err(e) => Err(ErrorResponse((Status::UnprocessableEntity, e.to_string()))),
    }
}

/// Handler for the register route.
#[post("/register", data="<req_sign_up>")]
pub async fn register(
    db: &State<DB>, // State for database access.
    req_sign_up: Json<ReqSignUp>, // Request payload for sign-up.
) -> Response<String> {
    let db = db as &DB;

    // Validate sign-up details.
    match check_valid_signup(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username) {
        Ok(_) => {
            // Check if email already exists.
            if db.fetch_user(&req_sign_up.email).await.unwrap().is_some() {
                return Err(ErrorResponse((Status::Conflict, "Email already exists".to_string())));
            }

            // Insert new user into the database.
            db.insert_user_credentials(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username).await.unwrap();

            // Return success response.
            Ok(SuccessResponse((Status::Created, "Registered".to_string())))
        },
        Err(e) => Err(ErrorResponse((Status::UnprocessableEntity, e.to_string()))),
    }
}

/// Handler for the logout route.
#[post("/logout")]
pub async fn logout() -> Response<String> {
    // Simply return success response as logout logic may be handled on the client side.
    Ok(SuccessResponse((Status::Ok, "Logout".to_string())))
}

/// Handler to get the authenticated user's details.
#[get("/me")]
pub async fn me(
    db: &State<DB>,
    user: AuthenticatedUser // Authenticated user data.
) -> Response<Json<ResMe>> {
    let db = db as &DB;

    // Fetch user by ID.
    let u: UserCredentials = db.fetch_user_by_id(user.id).await.unwrap();

    // Return user's email and username.
    Ok(SuccessResponse((
        Status::Ok,
        Json(ResMe {
            email: u.email,
            username: u.username,
        }),
    )))
}

/// Handler for deleting the authenticated user.
#[post("/delete")]
pub async fn delete(
    db: &State<DB>,
    user: AuthenticatedUser // Authenticated user data.
) -> Response<String> {
    let db = db as &DB;

    // Delete user by ID.
    db.delete_user(user.id).await.unwrap();

    // Return success response.
    Ok(SuccessResponse((Status::Ok, "Deleted".to_string())))
}

/// Handler for updating the authenticated user's details.
#[post("/update", data="<req_sign_up>")]
pub async fn update(
    db: &State<DB>,
    req_sign_up: Json<ReqSignUp>, // Request payload for updating user details.
    user: AuthenticatedUser // Authenticated user data.
) -> Response<String> {
    let db = db as &DB;

    // Validate updated user details.
    match check_valid_signup(&req_sign_up.email, &req_sign_up.password, &req_sign_up.username) {
        Ok(_) => {
            // Update user details in the database.
            db.update_user(user.id, &req_sign_up.email, &req_sign_up.password, &req_sign_up.username).await.unwrap();
            // Return success response.
            Ok(SuccessResponse((Status::Ok, "Updated".to_string())))
        },
        Err(e) => Err(ErrorResponse((Status::UnprocessableEntity, e.to_string()))),
    }
}

/// Handler to check if an email is available for registration.
#[post("/email", data="<req_email>")]
pub async fn email(
    db: &State<DB>,
    req_email: Json<ReqEmail>, // Request payload for email check.
) -> Response<String> {
    let db = db as &DB;

    // Check if email already exists.
    return match db.fetch_user(&req_email.email).await.unwrap() {
        Some(_) => Err(ErrorResponse((Status::Conflict, "Email already exists".to_string()))),
        None => Ok(SuccessResponse((Status::Ok, "Email available".to_string()))),
    }
}

/// Handler to check if a username is available for registration.
#[post("/username", data="<req_username>")]
pub async fn username(
    db: &State<DB>,
    req_username: Json<ReqUsername>, // Request payload for username check.
) -> Response<String> {
    let db = db as &DB;

    // Check if username already exists.
    return match db.fetch_user(&req_username.username).await.unwrap() {
        Some(_) => Err(ErrorResponse((Status::Conflict, "Username already exists".to_string()))),
        None => Ok(SuccessResponse((Status::Ok, "Username available".to_string()))),
    }
}

/// Struct representing the sign-in request payload.
#[derive(Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqSignIn {
    email: String,
    password: String,
}

/// Struct representing the sign-in response payload.
#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResSignIn {
    token: String,
}

/// Struct representing the sign-up request payload.
#[derive(Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqSignUp {
    email: String,
    password: String,
    username: String,
}

/// Struct representing the authenticated user's response payload.
#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResMe {
    email: String,
    username: String,
}

/// Struct representing the email check request payload.
#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqEmail {
    email: String,
}

/// Struct representing the username check request payload.
#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqUsername {
    username: String,
}

impl DB {
    /// Fetch user by email from the database.
    async fn fetch_user(&self, email: &str) -> mongodb::error::Result<Option<UserCredentials>> {
        let collection_user = self.database.collection::<UserCredentials>("userCredentials");

        Ok(collection_user.find_one(bson::doc! { "email": email }, None).await.unwrap())
    }

    /// Fetch user by ID from the database.
    async fn fetch_user_by_id(&self, id: ObjectId) -> Result<UserCredentials, String> {
        let filter = doc! { "id": id };
        let user = self.database.collection("userCredentials").find_one(filter, None).await.unwrap().unwrap();

        Ok(mongodb::bson::from_document(user).unwrap())
    }

    /// Insert a new user into the database.
    async fn insert_user_credentials(&self, email: &str, password: &str, username: &str) -> Result<(), String> {
        let user = UserCredentials {
            _id: ObjectId::new(),
            email: email.to_string(),
            password: hash(password, DEFAULT_COST).unwrap(),
            username: username

                .to_string(),
            created_at: DateTime::<Utc>::from(SystemTime::now()).format("%Y-%m-%d %H:%M:%S").to_string(),
        };

        self.database.collection("userCredentials").insert_one(mongodb::bson::to_document(&user).unwrap(), None).await.unwrap();
        Ok(())
    }

    /// Update user details in the database.
    async fn update_user(&self, id: ObjectId, email: &str, password: &str, username: &str) -> mongodb::error::Result<()> {
        let filter = doc! { "id": id };
        let update = doc! { "$set": { "email": email, "password": password, "username": username } };

        self.database.collection::<UserCredentials>("userCredentials").update_one(filter, update, None).await.unwrap();
        Ok(())
    }

    /// Delete user from the database.
    async fn delete_user(&self, id: ObjectId) -> mongodb::error::Result<()> {
        self.database.collection::<UserCredentials>("userCredentials").delete_one(doc! { "id": id }, None).await;
        Ok(())
    }
}

/// Struct for email verification details.
#[derive(Serialize, Deserialize, Debug, Clone)]
pub struct EmailVerification {
    pub _id: ObjectId,
    pub six_digit_code: String,
}
