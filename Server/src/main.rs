#[macro_use] extern crate rocket;
use rocket::http::Status;
use crate::controllers::SuccessResponse;
use crate::controllers::Response;
use mongodb::{
    bson::{Document, doc},
    Client,
    Collection
};

mod controllers;
mod auth;
mod database;

#[get("/")]
fn index() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "You shouldn't be reading this".to_string())))
}

#[launch]
async fn rocket() -> _ {

    let db = database::DB::init().await?;

    rocket::build()
        .mount("/", routes![index])
        .mount("/auth", routes![
            controllers::auth::login,
            controllers::auth::register, 
            controllers::auth::logout,
            controllers::auth::me],
        )
}
