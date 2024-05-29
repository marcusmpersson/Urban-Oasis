#[macro_use] 
extern crate rocket;
use rocket::http::Status;
use fairings::cors::{Cors, options};
use mongodb::{
    bson::{Document, doc},
    Client,
    Collection
};
use crate::controllers::{Response, SuccessResponse};

use crate::database::db::init;

mod controllers;
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
            controllers::auth::login,
            controllers::auth::register,
            controllers::auth::logout,
            controllers::auth::me,
            controllers::auth::username,
            controllers::auth::email],
        )
        .mount(
            "/user",
            routes![
                controllers::routes::user::get_inventory,
                controllers::routes::user::add_inventory],
        )
}
