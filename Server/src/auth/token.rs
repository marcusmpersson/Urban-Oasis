use rocket::error::Error;

use jsonwebtoken::{decode, encode, DecodingKey, EncodingKey, Header, Validation};
use rocket::{
    http::Status,
    request::{self, Request, FromRequest, Outcome},
    serde::{Deserialize, Serialize},
};
use mongodb::bson::oid::ObjectId;

#[derive(Debug, Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct Claims {
    pub sub: ObjectId,
    pub role: String,
    pub exp: u64,
}

pub struct AuthenticatedUser {
    pub id: ObjectId,
}

#[rocket::async_trait]
impl<'r> FromRequest<'r> for AuthenticatedUser {
    type Error = String;

    async fn from_request(request: &'r Request<'_>) -> request::Outcome<Self, Self::Error> {
        if let Some(token) = request.headers().get_one("token") {

            let data = decode::<Claims>(
                token,
                &DecodingKey::from_secret(),
                &Validation::new(jsonwebtoken::Algorithm::HS256),
            );

            let claims = match data {
                Ok(c) => c.claims,
                Err(_) => return Outcome::Failure((Status::Unauthorized, "Invalid token".to_string())),
            };

            Outcome::Success(AuthenticatedUser { id: claims.sub })

        } else {
            Outcome::Failure((Status::Unauthorized, "No token provided".to_string()))
        }
    }
}
