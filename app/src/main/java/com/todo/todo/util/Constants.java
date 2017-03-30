package com.todo.todo.util;

/**
 * Created by bridgeit on 11/2/17.
 */
public class Constants {

    public interface BundleKey{
        public static final String USER_EMAIL = "email";
        public static final String USER_NAME = "name";
        public static final String USER_USER_UID = "uid";
        public static final String USER_REGISTER = "register";
        public static final String MEW_NOTE = "note";
        public static final String GOOGLE_LOGIN = "google";
        public static final String FACEBOOK_LOGIN = "facebook";

    }
    public interface ProfileeKey {
        public static final String MOBILE_NO = "mobileno";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String PROFILE_IMAGE_URL = "profil_url";

    }
        public  interface  RequestParam{
        public static final String DATABASE_NAME ="ToDosManager";
        public static final String USER_TABLE_NAME= "fellowshipPeriod";
        public static final String LOCAL_NOTES_TABLE_NAME ="localNotes";
            public static final String NOTES_TABLE_NAME ="ToDos";
        public static final String KEY_ID ="id";
        public static final String KEY_TITLE ="title";
        public static final String KEY_NOTE ="notes";
        public static final String KEY_REMINDER ="reminder";
        public static final String KEY_STARTDATE ="startdate";


    }


}
