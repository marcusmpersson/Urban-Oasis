use regex::Regex;

const EMAIL_REGEX: &str = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$";
const PASSWORD_REGEX: &str = r"^(?:[^A-Z]*[A-Z])(?:[^a-z]*[a-z])(?:[^0-9]*[0-9])(?:[^\@\$!\*\%\#\?\&]*[@\$!\*\%\#\?\&])[A-Za-z\d@\$!\*\%\#\?\&]{8,}$";
const USERNAME_REGEX: &str = r"^[a-zA-Z0-9_]{3,}$";

struct LenText {
    min: usize,
    max: usize,
}

const LEN_PASSWORD: LenText = LenText { min: 8, max: 50 };
const LEN_USERNAME: LenText = LenText { min: 3, max: 12 };

pub enum LoginError {
    Email,
    Password,
    Username,
}

impl std::fmt::Display for LoginError {
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        match self {
            LoginError::Email => write!(f, "Invalid email"),
            LoginError::Password => write!(f, "Invalid password"),
            LoginError::Username => write!(f, "Invalid username"),
        }
    }
}

pub fn check_valid_text(text: &str, max_size: usize, min_size: usize) -> bool {
    !text.is_empty() && text.len() <= max_size && text.len() >= min_size
}

pub fn check_valid_login(email: &str, password: &str) -> Result<(), LoginError> {
    let re_email = Regex::new(EMAIL_REGEX).unwrap();
    let re_password = Regex::new(PASSWORD_REGEX).unwrap();

    if !re_email.is_match(email) || !check_valid_text(email, 50, 3) {
        return Err(LoginError::Email);
    } 

    if !re_password.is_match(password) || !check_valid_text(password, LEN_PASSWORD.max, LEN_PASSWORD.min) {
        return Err(LoginError::Password);
    }

    Ok(())
} 

pub fn check_valid_signup(email: &str, password: &str, username: &str) -> Result<(), LoginError> {
    let re_username = Regex::new(USERNAME_REGEX).unwrap();

    check_valid_login(email, password)?;    

    if !re_username.is_match(username) || !check_valid_text(username, LEN_USERNAME.max, LEN_USERNAME.min) {
        return Err(LoginError::Username);
    }

    Ok(())
}
