use std::env;
use dotenv::dotenv;
use mongodb::bson::{doc, document::Document, oid::ObjectId, Bson, extjson::de::Error};
use mongodb::{Client, options::ClientOptions, Collection};
use crate::data_models::entities.User;

const DB_NAME: &str = "test";

#[derive(Clone, Debug)]
pub struct DB {
    pub client: Client,
}

impl DB {
    pub async fn init() -> Self {
        dotenv().ok();
        let uri = match env::var("MONGOURI") {
            Ok(v) => v.to_string(),
            Err(_) => format!("Error loading env variable"),
        };
        DB {
            client: Client::with_uri_str(uri).unwrap(),
        }
    }

    pub async fn fetch_user(&self, email: &str) -> Result<entities.User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "email": email };
        let user = collection.find_one(filter, None).await.unwrap().unwrap();
        Ok(entities.User {
            id: user.get_object_id("_id").unwrap().to_hex(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
        })
    }

    pub async fn create_user(&self, email: &str, password: &str) -> Result<entities.User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let user = doc! { "email": email, "password": password };
        collection.insert_one(user, None).await.unwrap();
        Ok(entities.User {
            id: user.get_object_id("_id").unwrap().to_hex(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
        })
    }

    pub async fn update_user(&self, id: &str, email: &str, password: &str) -> Result<entities.User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "_id": ObjectId::with_string(id).unwrap() };
        let update = doc! { "$set": { "email": email, "password": password } };
        collection.update_one(filter, update, None).await.unwrap();
        Ok(entities.User {
            id: user.get_object_id("_id").unwrap().to_hex(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
        })
    }

    pub async fn delete_user(&self, id: &str) -> Result<entities.User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "_id": ObjectId::with_string(id).unwrap() };
        let user = collection.find_one_and_delete(filter, None).await.unwrap().unwrap();
        Ok(entities.User {
            id: user.get_object_id("_id").unwrap().to_hex(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
        })
    }
}
