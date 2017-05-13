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
        public static final String PROFILE_PIC = "profilepic";
        public static final String MEW_NOTE = "note";
        public static final String GOOGLE_LOGIN = "google";
        public static final String FACEBOOK_LOGIN = "facebook";
        public static final String USER_PROFILE_LOCAL = "localimage";
        public static final String USER_PROFILE_SERVER = "serverimage";
        String USER_PASSWORD = "password";
        String FR_USER_EMAIL = "fbemail";
        String NOTE_ORDER_ID = "noteorderid";
    }
    public interface ProfileeKey {
        public static final String SHAREDPREFERANCES_KEY = "testapp";
        public static final String MOBILE_NO = "mobileno";
        public static final String FIRST_NAME = "firstname";
        public static final String LAST_NAME = "lastname";
        public static final String PROFILE_IMAGE_URL = "profil_url";

    }
        public  interface  RequestParam{
        public static final String DATABASE_NAME ="mylManagekrs";
        public static final String USER_TABLE_NAME= "fellowshipPeriodk";
        public static final String LOCAL_NOTES_TABLE_NAME ="loalpNotkes";
            public static final String NOTES_TABLE_NAME ="TooDokos";
        public static final String KEY_ID ="id";
        public static final String KEY_TITLE ="title";
        public static final String KEY_NOTE ="notes";
        public static final String KEY_REMINDER ="reminder";
        public static final String KEY_STARTDATE ="startdate";

            public static final String KEY_ARCHIVE ="archive";
       public static final String KEY_SETTIME = "settime";
            String TRASH_TABLE_NAME = "trashtable";
        }
    public interface NotesType {
        public static final String REMINDER_NOTES = "Reminders";
        public static final String ARCHIVE_NOTES = "Archived";
        public static final String  ALL_NOTES= "Notes";
        public static final String DATE_FORMAT = "EEE,MMMd,yy";


        String TRASH_NOTES = "Trash Notes";
    }
    public interface Stringkeys {
        public static final String FLAGT_TRUE = "true";
        public static final String ARCHIVE_UNDO = "UNDO";
        public static final String DEMO_EMAIL = "abcd@gmail.com";
        public static final String FLAG_FALSE = "false";
        public static final String NAME = "Gokul";
        public static final String NULL_VALUIE = "null";
        public static final String MASSEGE_IS_ARCHIVED = " Note is Archived...";
        public static final String FIREBASE_DATABASE_PARENT_CHILD = "usersdata";
    }

    public interface InternateConnnection {
        public static final String CHICK_CONNECTION = "Check Internate Connection...";

    }

}
