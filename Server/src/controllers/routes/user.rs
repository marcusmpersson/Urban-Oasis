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

use crate::{ database::db::DB, entities::data_models::User };
use crate::auth::token::{ AuthenticatedUser, Claims };
use crate::private_cons::{JWT_SECRET, REFRESH_SECRET};
use crate::controllers::validators::{check_valid_login, check_valid_signup};

#[get("/inventory")]
pub async fn get_inventory(
    db: &State<DB>,
    user: AuthenticatedUser,
) -> Response<Json<Vec<Inventory>>> {
    let db: &DB = db as &DB;

    let inventory = db.fetch_inventory().await.unwrap();

    Ok(SuccessResponse((Status::Ok, Json(inventory))))
}

#[post("/inventory", data="<req_inventory>")]
pub async fn add_inventory(
    db: &State<DB>,
    user: AuthenticatedUser,
    req_inventory: Json<ReqInventory>,
) -> Response<String> {
    let db: &DB = db as &DB;

    let inventory = Inventory {
        _id: ObjectId::new(),
        name: req_inventory.name.clone(),
        quantity: req_inventory.quantity,
        user_id: user.sub.clone(),
    };

    db.add_inventory(inventory).await.unwrap();

    Ok(SuccessResponse((Status::Ok, "Inventory added".to_string())))
}


#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct ReqInventory {
    pub name: String,
    pub quantity: i32,
}

#[derive(Serialize, Deserialize)]
#[serde(crate = "rocket::serde")]
pub struct Inventory {
    pub _id: ObjectId,
    pub name: String,
    pub quantity: i32,
    pub user_id: ObjectId,
}

impl DB {

    pub async fn fetch_inventory(&self) -> mongodb::error::Result<Inventory> {
        let collection = self.database.collection("inventory");

        let cursor = collection.find(None, None).await?;

        let mut inventory: Vec<Inventory> = Vec::new();

        while let Some(result) = cursor.next().await {
            match result {
                Ok(doc) => {
                    let i = bson::from_bson(bson::Bson::Document(doc)).unwrap();
                    inventory.push(i);
                },
                Err(e) => {
                    return Err(e);
                }
            }
        }

        Ok(inventory)
    }

    pub async fn add_inventory(&self, inventory: Inventory) -> mongodb::error::Result<()> {
        let collection = self.database.collection("inventory");

        collection.insert_one(bson::to_document(&inventory).unwrap(), None).await?;

        Ok(())
    }

}


