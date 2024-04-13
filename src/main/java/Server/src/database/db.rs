use std::env;
use std::time::SystemTime;
use dotenv::dotenv;
use mongodb::bson::{doc, document::Document, oid::ObjectId, Bson, extjson::de::Error};
use mongodb::{Client, Collection, Database, options::ClientOptions};
use rocket::fairing::AdHoc;
use crate::entities::data_models::User;
use crate::private_cons::URL_DB;

const DB_NAME: &str = "test";

#[derive(Clone, Debug)]
pub struct DB {
    pub(crate) database: Database,
}


pub async fn init() -> AdHoc {
    AdHoc::on_ignite("Database", |rocket| async {
        match connect().await {
            Ok(database) => rocket.manage(DB::new(database)),
            Err(e) => {
                panic!("Failed to connect to database: {}", e);
            }    
        }
    })    
}

async fn connect() -> mongodb::error::Result<Database> {
    let client_options = ClientOptions::parse(URL_DB).await?;
    let client = Client::with_options(client_options)?;

    client
        .database("urbanoasis")
        .run_command(doc! {"ping": 1}, None)
        .await?;

    println!("Connected to database");

    Ok(client.database("urbanoasis"))
}

impl DB {
    pub fn new(database: Database) -> Self {
        DB { database }
    }
}
