#[macro_use] 
extern crate rocket;
use rocket::http::Status;
use crate::controller::SuccessResponse;
use crate::controller::Response;
use fairings::cors::{Cors, options};
use mongodb::{
    bson::{Document, doc},
    Client,
    Collection
};

use crate::database::db::init;

mod controller;
mod auth;
mod database;
mod entities;
mod fairings;
mod private_cons;

#[get("/")]
fn index() -> Response<String> {
    Ok(SuccessResponse((Status::Ok, "You shouldn't be reading this".to_string())))
}

#[launch]
async fn rocket() -> _ {

    rocket::build()
        .attach(init().await)
        .attach(Cors)
        .mount("/", routes![options])
        .mount("/", routes![index])
        .mount("/auth", routes![
            controller::auth::login,
            controller::auth::register,
            controller::auth::logout,
            controller::auth::me,
            controller::auth::username,
            controller::auth::email],
        )
}
