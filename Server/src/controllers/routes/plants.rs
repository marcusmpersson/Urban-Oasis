use super::super::{SuccessResponse, ErrorResponse, Response};
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

use crate::{ database::db::DB, entities::data_models::UserCredentials };
use crate::auth::token::{ AuthenticatedUser, Claims };
use crate::private_cons::{JWT_SECRET, REFRESH_SECRET};
use crate::controllers::validators::{check_valid_login, check_valid_signup};
use crate::entities::data_models::PottedPlant;


#[post("/getplant", data="<req_getplant>")]
pub async fn get_plant(
    db: &State<DB>,
    req_getplant: Json<ReqGetPlant>,
) -> Response<Json<ResGetPlant>> {
    let db: &DB = db as &DB;

    let plant = db.fetch_plant(&req_getplant.name).await.unwrap();

    Ok(SuccessResponse((Status::Ok, Json(ResGetPlant { plant }))))
}

impl DB {
    pub async fn fetch_plant(&self, name: &str) -> Result<PottedPlant, mongodb::error::Error> {
        let collection = self.database.collection("plant");

        let filter = doc! { "name": name };

        let plant = collection.find_one(filter, None).await.unwrap().unwrap();

        Ok(bson::from_document(plant).unwrap())
    }
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqGetPlant {
    pub name: String,
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ResGetPlant {
    pub plant: PottedPlant,
}
