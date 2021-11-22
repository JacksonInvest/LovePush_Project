package com.lovepushapp.core.utils;


public class GlobalsVariables {

    public static final String user_id="user_id";
    public static final String message = "message";


    public static final String COMMA_SEPERATOR = ",";
    public static final String ARRAY_START = "[";
    public static final String ARRAY_END = "]";
//    public static String quickblox_password="12345678CARD_APP";
public static String quickblox_password="12345678LOVEPUSH_APP";

    public interface DAGGERS {
        String AUTH = "AUTH";
        String NON_AUTH = "NON_AUTH";
    }

    public interface STORAGE {
        String NAME = "LOVE_PUSH_STORAGE";


        // Fixed Type
        String TYPE_STRING = "TYPE_STRING";

        public interface STORAGE_KEYS {
            String UserId = "UserId";
            String SessionId = "SessionId";
            String LoginResponse = "LoginResponse";
            String Email = "Email";

            String GET_PROFILE = "GET_PROFILE";
            String Gender = "GENDER";

            String verification_code = "verification_code";
            String phone_code = "phone_code";
            String phone = "phone";
            String name = "name";
            String QuickbloxId = "qquickblox_id";
            String profile_image = "profile_image";
            String referal_code = "referal_code";
            String CURRENT_LANGUAGE = "current_language";


            String username = "username";
            String token = "token";
        }
    }


    public interface BUNDLE {

        String FROM = "FROM";
        String FROM_FORGOT = "FROM_FORGOT";
        String FROM_LOGIN_REGISTER = "FROM_LOGIN_REGISTER";
    }

    public interface ANIM_TYPE {
        int RIGHT_TO_LEFT = 0;
        int LEFT_TO_RIGHT = 1;
        int DOWN_TO_TOP = 2;
        int TOP_TO_DOWN = 3;
        int ONLY_TOP = 4;
        int ONLY_DOWN = 5;
        int NONE = 6;
    }

    public interface REQUEST_CODE {
        int PLACE_PICKER_REQUEST = 9001;
    }

    public interface GENDER {
        String Male = "1";
        String female = "2";
        String homo = "3";
    }

    public interface SEXUAL {
        String Heter = "1";
        String Bi = "2";
        String Homos = "3";
    }

    public interface POST_TYPE {
        String LOVE = "1";
        String CONNECT = "2";
    }

    public interface DEVICE_TYPE {
        String android = "1";
    }

    public interface LOGIN_TYPE {
        String MANNUAL = "1";
    }

    public interface ANNONY_TYPE {
        String ANNONY_IS = "1";
        String ANNONY_NOT = "0";
    }
}
