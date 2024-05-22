use rocket::error::Error;

use jsonwebtoken::{decode, encode, DecodingKey, EncodingKey, Header, Validation};
use rocket::{
    http::Status,
    request::{self, Request, FromRequest, Outcome},
    serde::{Deserialize, Serialize},
};
use mongodb::bson::oid::ObjectId; // Importing ObjectId type from MongoDB BSON.
use crate::private_cons::JWT_SECRET; // Importing the JWT secret from a private constants module.

/// Struct representing the JWT claims.
#[derive(Debug, Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct Claims {
    pub sub: ObjectId, // Subject (user ID) of the token.
    pub role: String,  // Role of the user.
    pub exp: u64,      // Expiration time of the token.
}

/// Struct representing an authenticated user.
pub struct AuthenticatedUser {
    pub id: ObjectId, // ID of the authenticated user.
}

/// Implementation of the FromRequest trait for AuthenticatedUser.
#[rocket::async_trait]
impl<'r> FromRequest<'r> for AuthenticatedUser {
    type Error = String;

    /// Asynchronous method to extract an AuthenticatedUser from a request.
    async fn from_request(request: &'r Request<'_>) -> request::Outcome<Self, Self::Error> {
        // Check if the request has a "token" header.
        if let Some(token) = request.headers().get_one("token") {

            // Decode the token using the JWT secret and validation settings.
            let data = decode::<Claims>(
                token,
                &DecodingKey::from_secret(JWT_SECRET.as_ref()),
                &Validation::new(jsonwebtoken::Algorithm::HS256),
            );

            // Match on the result of the token decoding.
            let claims = match data {
                // If successful, extract the claims.
                Ok(c) => c.claims,
                // If there's an error, return an unauthorized error outcome.
                Err(_) => return Outcome::Failure((Status::Unauthorized, "Invalid token".to_string())),
            };

            // Return a successful outcome with the authenticated user's ID.
            Outcome::Success(AuthenticatedUser { id: claims.sub })

        } else {
            // If no token is provided, return an unauthorized error outcome.
            Outcome::Failure((Status::Unauthorized, "No token provided".to_string()))
        }
    }
}
