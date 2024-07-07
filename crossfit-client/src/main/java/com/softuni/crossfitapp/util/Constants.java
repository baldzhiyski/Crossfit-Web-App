package com.softuni.crossfitapp.util;
import java.util.regex.Pattern;

public enum Constants {
    ;

    // Controllers constants
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    public static final String DOT = ".";

    // Paths
    public static final String PATH_TO_COACHES = "src/main/resources/inputs/coaches.json";
    public static final String PATH_TO_MEMBERSHIPS = "src/main/resources/inputs/memberships.json";
    public static final String PATH_TO_ROLES = "src/main/resources/inputs/roles.json";
    public static final String PATH_TO_COACHES_USERS = "src/main/resources/inputs/coaches_user-profiles.json";
    public static final String PATH_TO_ADMINS = "src/main/resources/inputs/admins.json";

    // Validation messages
    public static final String FIELD_REQUIRED = "This field is required";
    public static final String FIELD_ACCEPTED = "You must accept the terms and conditions";

    // Email validation messages
    public static final String INVALID_EMAIL_FORMAT = "Invalid email format or domain";
    public static final String EMPTY_EMAIL = "Email cannot be empty";
    public static final String INVALID_DOMAIN_OPTION = "Invalid domain option. Please choose abv, gmail, or yahoo";
    public static final String EMAIL_ALREADY_IN_USE = "The current email has been already used! Pick another one!";
    public static final String USERNAME_ALREADY_TAKEN = "Username has already been taken!";

    // Born date validation messages
    public static final String INVALID_BORN_DATE = "Invalid date of birth format";
    public static final String NOT_OLD_ENOUGH = "You must be at least 18 years old";
    public static final String EMPTY_BORN_DATE = "Please provide information about your birthday ! ";

    // Password validation messages
    public static final String INVALID_PASS = "Invalid password ! Try again ...";
    public static final String PASSWORD_CANNOT_BE_NULL = "Password cannot be null";
    public static final String PASSWORD_TOO_SHORT = "Password is too short";
    public static final String PASSWORD_TOO_LONG = "Password is too long";
    public static final String PASSWORD_SHOULD_CONTAIN_LOWERCASE_LETTER = "Password should contain at least one lowercase letter";
    public static final String PASSWORD_SHOULD_CONTAIN_UPPERCASE_LETTER = "Password should contain at least one uppercase letter";
    public static final String PASSWORD_SHOULD_CONTAIN_DIGIT = "Password should contain at least one digit";
    public static final String PASSWORD_SHOULD_CONTAIN_SPECIAL_SYMBOL = "Password should contain at least one special symbol";

    public static final String NOT_MATCHING_PASSWORDS = "Passwords do not match !";

    // Phone number validation messages
    public static final String INVALID_PHONE_NUMBER_FORMAT = "Invalid phone number format";
    public static final String EMPTY_PHONE = "Please provide your phone number !";
    public static final String UNIQUE_PHONE_REQUIRED = "An account with this number is already registered !";

    // File validation messages
    public static final String FILE_INVALID = "Invalid file";
    public static final String FILE_EMPTY= "Invalid file";
    public static final String FILE_SIZE_EXCEEDED = "File size exceeds the maximum allowed";
    public static final String FILE_TYPE_NOT_ALLOWED = "File type not allowed. Only JPEG and JPG are allowed";

    // File validation constants
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB in bytes

    // Regex patterns
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(abv\\.bg|yahoo\\.com|gmail\\.com)$";

    // Phone number regex pattern
    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+(?:[0-9]●?){6,14}[0-9]$");
    public static final Pattern PATTERN_LOWER = Pattern.compile("[a-z]");
    public static final Pattern PATTERN_UPPER = Pattern.compile("[A-Z]");
    public static final Pattern PATTERN_DIGIT = Pattern.compile("[0-9]");
    public static final Pattern PATTERN_SYMBOL = Pattern.compile("[!@#$%^&*()_+<>?]");
    public static final String EVENT_CACHE = "Cache eviction initiated.";
}
