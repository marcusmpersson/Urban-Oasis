use regex::Regex;

const EMAIL_REGEX: &str = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"; // Regular expression for validating email addresses.
const USERNAME_REGEX: &str = r"^[a-zA-Z0-9_]{3,}$"; // Regular expression for validating usernames.

/// Struct to define minimum and maximum length constraints for text fields.
struct LenText {
    min: usize, // Minimum length of the text.
    max: usize, // Maximum length of the text.
}

const LEN_PASSWORD: LenText = LenText { min: 8, max: 50 }; // Length constraints for passwords.
const LEN_USERNAME: LenText = LenText { min: 3, max: 12 }; // Length constraints for usernames.

/// Enum representing possible login errors.
pub enum LoginError {
    Email, // Error related to email validation.
    Password, // Error related to password validation.
    Username, // Error related to username validation.
}

impl std::fmt::Display for LoginError {
    /// Implements the Display trait for LoginError to provide user-friendly error messages.
    fn fmt(&self, f: &mut std::fmt::Formatter) -> std::fmt::Result {
        match self {
            LoginError::Email => write!(f, "Invalid email"),
            LoginError::Password => write!(f, "Invalid password"),
            LoginError::Username => write!(f, "Invalid username"),
        }
    }
}

/// Checks if a given text is valid based on its length constraints.
pub fn check_valid_text(text: &str, max_size: usize, min_size: usize) -> bool {
    !text.is_empty() && text.len() <= max_size && text.len() >= min_size // Returns true if the text is non-empty and within the specified length range.
}

/// Validates email and password for login.
pub fn check_valid_login(email: &str, password: &str) -> Result<(), LoginError> {
    let re_email = Regex::new(EMAIL_REGEX).unwrap(); // Compiles the email regular expression.

    // Validates the email format and length.
    if !re_email.is_match(email) || !check_valid_text(email, 50, 3) {
        return Err(LoginError::Email); // Returns an Email error if validation fails.
    }

    // Validates the password format and length.
    if !is_password_valid(password) || !check_valid_text(password, LEN_PASSWORD.max, LEN_PASSWORD.min) {
        return Err(LoginError::Password); // Returns a Password error if validation fails.
    }

    Ok(()) // Returns Ok if both email and password are valid.
}

/// Validates email, password, and username for signup.
pub fn check_valid_signup(email: &str, password: &str, username: &str) -> Result<(), LoginError> {
    let re_username = Regex::new(USERNAME_REGEX).unwrap(); // Compiles the username regular expression.

    check_valid_login(email, password)?; // Validates email and password using the login function.

    // Validates the username format and length.
    if !re_username.is_match(username) || !check_valid_text(username, LEN_USERNAME.max, LEN_USERNAME.min) {
        return Err(LoginError::Username); // Returns a Username error if validation fails.
    }

    Ok(()) // Returns Ok if email, password, and username are all valid.
}

/// Checks if a password is valid based on various criteria.
fn is_password_valid(password: &str) -> bool {
    let mut checks: [bool; 5] = [false; 5]; // Array to track various validation checks.

    // Iterates over each character in the password to perform validation checks.
    for c in password.chars() {
        checks[0] |= c.is_uppercase(); // Check for at least one uppercase letter.
        checks[1] |= c.is_lowercase(); // Check for at least one lowercase letter.
        checks[2] |= c.is_whitespace(); // Check for the absence of whitespace.
        checks[3] |= c.is_ascii_digit(); // Check for at least one digit.
    }

    // Ensures all necessary checks are true, no whitespace, and password length is within the specified range.
    checks[0] && checks[1] && !checks[2] && checks[3] && password.len() >= 8 && password.len() <= 50
}
