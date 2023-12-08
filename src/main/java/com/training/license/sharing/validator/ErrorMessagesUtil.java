package com.training.license.sharing.validator;

public class ErrorMessagesUtil {

    public static String USER_NOT_EXIST_MESSAGE = "User with this data does not exist";

    public static String ID_NOT_EXIST_MESSAGE = "ID list is empty or contains non-existent id";

    public static String LICENSE_NOT_EXIST_MESSAGE = "License does not exist";

    public static String LICENSE_DO_NOT_HAVE_SEATS_MESSAGE = "the training license exceeded the number of users";

    public static String LICENSE_EXPIRATION_MESSAGE = "License is expired";

    public static String LICENSE_NONEXISTENT_MESSAGE = "License does not exist";

    public static String REJECTED_REQUEST_CAN_NOT_BE_CHANGED_TEMPLATE = "Rejected request can't be changed. Requests:";

    public static String IMAGE_INCORRECT_TYPE_MESSAGE = "Image must have JPEG/JPG/PNG type";

    public static String IMAGE_INCORRECT_SIZE_MESSAGE = "Logo Image Size must be be between 2 and 10 MB";

    public static String LICENSE_WITH_NAME_NON_EXISTENT_MESSAGE = "License with this name does not exist!";

    public static String LICENSE_WITH_NAME_ALREADY_EXIST_MESSAGE = "License with this name is already exists";

    public static final String LICENSE_WITH_ID_NON_EXISTENT_MESSAGE = "License with this id does not exist";
}
