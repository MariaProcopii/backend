package com.training.license.sharing.validator;

public class ErrorMessagesUtil {
    public static final String USER_NOT_EXIST_MESSAGE = "User with this data does not exist";
    public static final String USER_NOT_EXIST_FOR_CREDENTIAL = "User %s with this credentials does not exist";
    public static final String ID_NOT_EXIST_MESSAGE = "ID list is empty or contains non-existent id";
    public static final String LICENSE_NOT_EXIST_MESSAGE = "License does not exist";
    public static final String LICENSE_DO_NOT_HAVE_SEATS_MESSAGE = "the training license exceeded the number of users";
    public static final String LICENSE_EXPIRATION_MESSAGE = "License is expired";
    public static final String REJECTED_REQUEST_CAN_NOT_BE_CHANGED_TEMPLATE = "Rejected request can't be changed. Requests:";
    public static final String IMAGE_INCORRECT_TYPE_MESSAGE = "Image must have JPEG/JPG/PNG type";
    public static final String IMAGE_INCORRECT_SIZE_MESSAGE = "Logo Image Size must be be between 2 and 10 MB";
    public static final String ROLE_NOT_VALID = "Role type is not valid";
    public static final String PAGE_NOT_VALID = "Page number is not valid. Should be bigger or equal to 0";
    public static final String SIZE_NOT_VALID = "Size is not valid. Should be bigger than 0";
    public static final String EMAIL_NOT_UNIQE = "Email is already used";
    public static final String INVALID_OBJECT_FIELD = "Invalid property field for type Request";
    public static final String ARGUMENT_TYPE_MISMATCH = "Argument type has invalid format";
    public static final String LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE = "License with this name does not exist!";
    public static final String LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE = "License with this name is already exists";
    public static final String LICENSE_WITH_ID_NON_EXISTENT_MESSAGE = "License with this id does not exist";
    public static final String COST_NOT_AVAILABLE = "No cost data available";
}
