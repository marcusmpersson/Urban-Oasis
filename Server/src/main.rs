#[macro_use] 
extern crate rocket;
use rocket::http::Status;
use crate::controllers::SuccessResponse;
use crate::controllers::Response;
use fairings::cors::{Cors, options};
use mongodb::{
    bson::{Document, doc},
    Client,
    Collection
};

use crate::database::db::init;

mod controllers;
mod auth;
mod database;
mod entities;
mod fairings;

#[get("/")]
fn index() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "You shouldn't be reading this".to_string())))
}

#[launch]
async fn rocket() -> _ {

    rocket::build()
        .attach(Cors)
        .manage(init().await)
        .mount("/", routes![options])
        .mount("/", routes![index])
        .mount("/auth", routes![
            controllers::auth::login,
            controllers::auth::register, 
            controllers::auth::logout,
            controllers::auth::me],
        )
}
