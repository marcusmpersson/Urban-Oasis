use std::env;
use std::time::SystemTime;
use dotenv::dotenv;
use mongodb::bson::{doc, document::Document, oid::ObjectId, Bson, extjson::de::Error};
use mongodb::{Client, Collection, Database, options::ClientOptions};
use rocket::fairing::AdHoc;

use crate::entities::data_models::UserCredentials; // Importing the User data model.
use crate::private_cons::URL_DB; // Importing the database URL from a private constants module.

const DB_NAME: &str = "test"; // Defining a constant for the database name.

/// Struct representing the database connection.
#[derive(Clone, Debug)]
pub struct DB {
    pub(crate) database: Database, // MongoDB database instance.
}

/// Initializes the database connection and attaches it to the Rocket instance.
pub async fn init() -> AdHoc {
    AdHoc::on_ignite("Database", |rocket| async {
        match connect().await {
            Ok(database) => rocket.manage(DB::new(database)), // If connection is successful, manage the database instance.
            Err(e) => {
                panic!("Failed to connect to database: {}", e); // If connection fails, panic with an error message.
            }
        }
    })
}

/// Connects to the MongoDB database.
async fn connect() -> mongodb::error::Result<Database> {
    let client_options = ClientOptions::parse(URL_DB).await?; // Parse the database URL to get client options.
    let client = Client::with_options(client_options)?; // Create a MongoDB client with the options.

    // Ping the database to ensure the connection is established.
    client
        .database("urbanoasis")
        .run_command(doc! {"ping": 1}, None)
        .await?;

    println!("Connected to database"); // Print a success message if connected.

    Ok(client.database("urbanoasis")) // Return the database instance.
}

impl DB {
    /// Creates a new instance of the DB struct.
    pub fn new(database: Database) -> Self {
        DB { database } // Initialize the DB struct with the provided database instance.
    }
}
