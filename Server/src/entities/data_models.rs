#[derive(Debug, Serialize, Deserialize)]
pub struct User {
    id: i32,
    email: String,
    username: String,
    password: String,
    created_at: String,
}


