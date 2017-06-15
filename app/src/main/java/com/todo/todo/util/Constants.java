package com.todo.todo.util;

public class Constants {

    public interface BundleKey {
        String USER_EMAIL = "email";
        String USER_NAME = "name";
        String USER_USER_UID = "uid";
        String USER_REGISTER = "register";
        String PROFILE_PIC = "profilepic";
        String MEW_NOTE = "note";
        String GOOGLE_LOGIN = "google";
        String FACEBOOK_LOGIN = "facebook";
        String USER_PROFILE_LOCAL = "localimage";
        String USER_PROFILE_SERVER = "serverimage";
        String USER_PASSWORD = "password";
        String FR_USER_EMAIL = "fbemail";
        String NOTE_ORDER_ID = "noteorderid";
        String MEW_NOTE_TITLE="title";
        String MEW_NOTE_DISK="discription";
    }

    public interface ProfileeKey {
        String SHAREDPREFERANCES_KEY = "testapp";
        String MOBILE_NO = "mobileno";
        String FIRST_NAME = "firstname";
        String LAST_NAME = "lastname";
        String PROFILE_IMAGE_URL = "profil_url";

    }

    public interface RequestParam {
        String DATABASE_NAME = "mylManagekrs";
        String USER_TABLE_NAME = "fellowshipPeriodk";
        String LOCAL_NOTES_TABLE_NAME = "loalpNotkes";
        String NOTES_TABLE_NAME = "TooDokos";
        String KEY_ID = "id";
        String KEY_TITLE = "title";
        String KEY_NOTE = "notes";
        String KEY_REMINDER = "reminder";
        String KEY_STARTDATE = "startdate";
        String KEY_ARCHIVE = "archive";
        String KEY_SETTIME = "settime";
        String TRASH_TABLE_NAME = "trashtable";
        String KEY_COLOR = "color";
        String LOCAL_UPDATE_TABLE_NAME="localupdate";
        String KEY_PIN="keypin";
    }

    public interface NotesType {
        String REMINDER_NOTES = "Reminders";
        String ARCHIVE_NOTES = "Archived";
        String ALL_NOTES = "Notes";
        String DATE_FORMAT = "EEE,MMMd,yy";
        String TRASH_NOTES = "Trash Notes";
        String SHARE_NOTE="Share Note";
    }

    public interface Stringkeys {
        String FLAGT_TRUE = "true";
        String ARCHIVE_UNDO = "UNDO";
        String DEMO_EMAIL = "abcd@gmail.com";
        String FLAG_FALSE = "false";
        String NAME = "Gokul";
        String NULL_VALUIE = "null";
        String MASSEGE_IS_ARCHIVED = " Note is Archived...";
        String FIREBASE_DATABASE_PARENT_CHILD = "usersdata";
        String STR_LINEAR_GRID = "mlinear";
        String LAST_NOTE_COUNT="lastcount";
        String LAST_INDEX="lastindex";
        String FIREBASE_DATABASE_TRASH="trashData";
        String MASSEGE_GET_RESTORE="Note Get Restored";
        String LOCAL_REGISTER="localregister";
    }

    public interface InternateConnnection {
        String CHICK_CONNECTION = "Check Internate Connection...";

    }

}
