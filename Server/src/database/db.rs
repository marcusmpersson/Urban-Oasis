use std::env;
use dotenv::dotenv;
use mongodb::bson::{doc, document::Document, oid::ObjectId, Bson};
use mongodb::{Client, options::ClientOptions, Collection};

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
        let client = Client::with_uri_str(uri).unwrap();
        let db = client.database("rustDB");
    }

    pub async fn fetch_user(&self) -> Result<User, >
}