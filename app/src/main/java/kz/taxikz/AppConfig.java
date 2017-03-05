package kz.taxikz;

import kz.taxikz.taxi4.R;

public interface AppConfig {
    public static final int API_ANDROID_DOMAIN_SSL_CERTIFICATE = R.raw.android_cert;
    public static final int API_DEV2_DOMAIN_SSL_CERTIFICATE = R.raw.dev2_cert;
    public static final int API_DEV_DOMAIN_SSL_CERTIFICATE = R.raw.dev_cert;

    public static final String API_DOMAIN = "https://android.taxi5.kz:443/";
    public static final String[] API_SERVER_IP_LIST = new String[]{"37.17.177.170", "85.29.156.82"};
    public static final int APP_VERSION = 52;
    public static final String PRIVACY_POLICY_URL = "http://taxi5.kz/main/privacy";


    public interface FabricEvents {
        public static final String ADD_ADDRESS = "Add address";
        public static final String CANCEL_ORDER_OK = "Cancel order";
        public static final String CREATE_ORDER_NO_ADDRESSES = "Create order error (no addresses)";
        public static final String CREATE_ORDER_NO_PRICE = "Create order error (no price)";
        public static final String CREDITS_CLICK = "Credits order";
        public static final String PASSWORD_CHECK = "Check password";
        public static final String PASSWORD_CHECK_FAILED = "Check password (FAILED)";
        public static final String PASSWORD_CHECK_OK = "Check password (OK)";
        public static final String REMOVE_ADDRESS_BUTTON = "Remove address (button)";
        public static final String REMOVE_ADDRESS_SWIPE = "Remove address (swipe)";
        public static final String REMOVE_FAVORITE_ADDRESS = "Remove favorite address (swipe)";
        public static final String SHOW_ORDER_DETAILS = "Show order details";
        public static final String SIDE_MENU_ABOUT_CLICK = "Side menu about click";
        public static final String SIDE_MENU_CITY_CLICK = "Side menu city click";
        public static final String SIDE_MENU_NEWS_CLICK = "Side menu news click";
        public static final String SIDE_MENU_REVIEW_CLICK = "Side menu review click";
        public static final String SIDE_MENU_SHARE_CLICK = "Side menu share click";
        public static final String SMS_CODE_GET = "Get SMS";
        public static final String SMS_CODE_GET_FAILED = "Get SMS (FAILED)";
        public static final String SMS_CODE_GET_OK = "Get SMS (OK)";
    }
}
