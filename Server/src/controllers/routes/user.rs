use super::super::{SuccessResponse, ErrorResponse, Response};
use jsonwebtoken::{encode, Header, EncodingKey};
use rocket::{
    http::Status,
    serde::{json::Json, Serialize, Deserialize},
    State,
    tokio::fs::File,
    data::ToByteUnit,
};
use std::time::SystemTime;
use mongodb::bson::{doc, Document, oid::ObjectId};
use mongodb::{bson, Collection};
use bcrypt::{hash, verify, DEFAULT_COST};
use chrono::{DateTime, offset::Utc};
use std::io::BufReader;
use std::fs::File as StdFile;


use crate::{ database::db::DB, entities::data_models::UserCredentials };
use crate::auth::token::{ AuthenticatedUser, Claims };
use crate::private_cons::{JWT_SECRET, REFRESH_SECRET};
use crate::controllers::validators::{check_valid_login, check_valid_signup};
use crate::entities::data_models::{Inventory, Room, UserInfo};


#[get("/load-user")]
pub async fn get_inventory(
    db: &State<DB>,
    user: AuthenticatedUser,
) -> Response<Json<UserInfo>> {
    let db: &DB = db as &DB;

    let user_info = match db.fetch_user_info(user.id).await {
        Ok(user_info) => user_info,
        Err(_) => {
            UserInfo {
                _id: user.id,
                inventory: Inventory::new(),
                rooms: Vec::new(),
                currency: 500,
                last_updated: Utc::now().to_string(),
            }
        }
    };

    Ok(SuccessResponse((Status::Ok, Json(user_info))))
}


#[post("/save-user", data="<user_upload>")]
pub async fn add_inventory(
    db: &State<DB>,
    user: AuthenticatedUser,
    user_upload: Json<UserUpload>,
) -> Response<String> {
    let db: &DB = db as &DB;

    let user_info = UserInfo {
        _id: user.id,
        inventory: user_upload.inventory.clone(),
        rooms: user_upload.rooms.clone(),
        currency: user_upload.currency,
        last_updated: Utc::now().to_string(),
    };

    db.save_user_info(user_info).await.unwrap();

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
pub struct UserUpload {
    email: String,
    username: String,
    inventory: Inventory,
    rooms: Vec<Room>,
    currency: i32,
}

impl DB {
    pub async fn fetch_user_info(&self, id: ObjectId) -> mongodb::error::Result<UserInfo> {
        let collection: Collection<UserInfo> = self.database.collection("userInfo");

        let filter = doc! { "_id": id };

        Ok(collection.find_one(filter, None).await.unwrap().unwrap())
    }

    pub async fn save_user_info(&self, user_info: UserInfo) -> mongodb::error::Result<()> {
        let collection: Collection<UserInfo> = self.database.collection("userInfo");

        let filter = doc! { "_id": user_info._id };

        let update = doc! { "$set": bson::to_document(&user_info).unwrap() };

        match collection.find_one(filter.clone(), None).await.unwrap() {
            Some(_) => {
                collection.update_one(filter, update, None).await.unwrap();
            },
            None => {
                collection.insert_one(&user_info, None).await.unwrap();
            }
        }
        Ok(())
    }
}


