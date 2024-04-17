pub(crate) mod auth;
pub mod validators;

use rocket::http::Status;

#[derive(Responder)]
pub struct SuccessResponse<T> (pub (Status, T));

#[derive(Responder)]
pub struct ErrorResponse(pub (Status, String));

pub type Response<T> = Result<SuccessResponse<T>, ErrorResponse>;

