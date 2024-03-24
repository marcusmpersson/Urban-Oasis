use std::env;
use std::time::SystemTime;
use dotenv::dotenv;
use mongodb::bson::{doc, document::Document, oid::ObjectId, Bson};
use mongodb::{Client, options::ClientOptions, Collection};
<<<<<<< HEAD
use crate::entities::data_models::User;
=======
>>>>>>> parent of 4c9e574 (Test Commit)

const DB_NAME: &str = "test";

#[derive(Clone, Debug)]
pub struct DB {
    pub(crate) client: Client,
}

impl DB {
    pub async fn init() -> Self {
        dotenv().ok();
        let uri = match env::var("mongodb+srv://admin:igXWJEOYuEgN7zJj@urbanoasis.iga2ujs.mongodb.net/?retryWrites=true&w=majority&appName=UrbanOasis") {
            Ok(v) => v.to_string(),
            Err(_) => format!("Error loading env variable"),
        };
<<<<<<< HEAD
        DB {
            client: Client::with_uri_str(uri).unwrap().await,
        }
    }

    pub async fn fetch_user(&self, email: &str) -> Result<User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "email": email };
        let user = collection.find_one(filter, None).await.unwrap().unwrap();
        Ok(User {
            _id: user.get_object_id("_id").unwrap().to_hex(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
        })
    }

    pub async fn create_user(&self, username: &str, email: &str, password: &str) -> Result<User, Error> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let user = doc! { "email": email, "password": password };
        collection.insert_one(user, None).await.unwrap();
        Ok(User {
            _id: user.get_object_id("_id").unwrap().to_hex(),
            username: user.get_str("username").unwrap().to_string(),
            email: user.get_str("email").unwrap().to_string(),
            password: user.get_str("password").unwrap().to_string(),
            created_at: SystemTime::now().to_string(), 
        })
    }

    pub async fn update_user(&self, id: &str, email: &str, password: &str) -> mongodb::error::Result<()> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "_id": ObjectId::(id).unwrap() };
        let update = doc! { "$set": { "email": email, "password": password } };
        collection.find_one_and_update(filter, update, None).await.unwrap().unwrap();
        Ok(())
    }

    pub async fn delete_user(&self, id: &str) -> mongodb::error::Result<()> {
        let db = self.client.database(DB_NAME);
        let collection = db.collection("users");
        let filter = doc! { "_id": ObjectId::(id).unwrap() };
        let user = collection.find_one_and_delete(filter, None).await.unwrap().unwrap();
        Ok(())
    }
}
=======
        let client = Client::with_uri_str(uri).unwrap();
        let db = client.database("rustDB");
    }

    pub async fn fetch_user(&self) -> Result<User, >
}
>>>>>>> parent of 4c9e574 (Test Commit)
