package io.hustler.wallzy.constants;

public class ServerConstants {
    public static final String INVALID_AUTH_HEADER = "INVALID AUTH HEADER";
    public static final short USER_UNAVAILABLE = -4000;
    public static final short API_SUCCESS = 2000;
    public static final short API_FAILURE = -2000;
    public static final short TOKEN_ERROR = -500;
    public static final short AUTH_INVALID_G_ACCOUNT = -5000;
    public static final short QUOTES_UPLOADED = 3000;
    public static final short QUOTES_UPLOAD_FAILURE = -3000;
    public static final short PASSWORD_MISMATCH = -5001;

    public static final short USER_ALREADY_EXISTS = -4000;
    public static final short USER_CREATED = 4000;


    /*ROLES BETWEEN 3000 to 3100*/
    public static final short NO_ROLES_FOUND = -3000;
    public static final short RULES_RETURNED_SUCCESSFULLY = 3000;

    /*DEALER CONSTANTS 5500 to 5600*/

    public static final short NO_DELAER_FOUND = -5500;
    public static final short DEALER_RETURNED_SUCCESSFULLY = 5500;
    public static final short PIN_UPDATE_SUCCESS = 5501;
    public static final short NEW_PIN_SENT_SUCCESS = 5502;
    public static final short NEW_USER = 5503;
    public static final short EXISTING_USER = 5504;
    public static final int OTP_SUCCESSFULLY_SENT_TO_USER = 5505;
    public static final int INVALID_CREDENTIALS = 5506;
    public static final int OTP_EXPIRED = 5507;
    public static final int INVALID_OTP = 5508;


    public static final int TOKEN_EXPIRY_HOURS_WEB = 12;
    public static final int TOKEN_EXPIRY_HOURS_MOBILE = 60 * 24; // two months
    public static final int TOKEN_EXPIRY_MINUTE_MOBILE_STORE = 8; // 8 mins
    public static final int OTP_EXPIRY_MINUTES = 5;
    public static final int DATA_UNAVAILABLE = -2004;


    public static final int NO_LATEST_CAT_FOUND = -2005;
    public static final int IMAGE_UNAVAILABLE = -5600;
    public static final int NO_FAVS_FOUND = -5601;


    public static class FlatIconCredits {
        public static String HEART_ICON = "https://www.flaticon.com/authors/kiranshastry/lineal-color";
        public static String DOWNLOAD_ICON = "<div>Icons made by <a href=\"https://www.flaticon.com/authors/good-ware\" title=\"Good Ware\">Good Ware</a> from <a href=\"https://www.flaticon.com/\"title=\"Flaticon\">www.flaticon.com</a></div>";
        public static String IMAGE_ICON = "<div>Icons made by <a href=\"https://www.flaticon.com/authors/xnimrodx\" title=\"xnimrodx\">xnimrodx</a> from <a href=\"https://www.flaticon.com/\"             title=\"Flaticon\">www.flaticon.com</a></div>";
    }
}
