package com.softuni.crossfitapp.util;
import java.util.regex.Pattern;

public enum Constants {
    ;

    // Controllers constants
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult";
    public static final String DOT = ".";

    // Paths
    public static final String PATH_TO_COACHES = "inputs/coaches.json";
    public static final String PATH_TO_MEMBERSHIPS = "inputs/memberships.json";
    public static final String PATH_TO_ROLES = "inputs/roles.json";
    public static final String PATH_TO_COACHES_USERS = "inputs/coaches_user-profiles.json";
    public static final String PATH_TO_ADMINS = "inputs/admins.json";
    // File validation constants
    public static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB in bytes

    // Regex patterns
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@(abv\\.bg|yahoo\\.com|gmail\\.com)$";
    public static final String YOUTUBE_URL_REGEX = "^(http(s)??\\:\\/\\/)?(www\\.)?((youtube\\.com\\/watch\\?v=)|(youtu.be\\/))([a-zA-Z0-9\\-_])+";

    // Phone number regex pattern
    public static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+(?:[0-9]●?){6,14}[0-9]$");
    public static final Pattern PATTERN_LOWER = Pattern.compile("[a-z]");
    public static final Pattern PATTERN_UPPER = Pattern.compile("[A-Z]");
    public static final Pattern PATTERN_DIGIT = Pattern.compile("[0-9]");
    public static final Pattern PATTERN_SYMBOL = Pattern.compile("[!@#$%^&*()_+<>?]");
    public static final String EVENT_CACHE = "Cache eviction initiated.";
}
