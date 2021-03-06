package com.todo.todo.home.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.todo.todo.R;
import com.todo.todo.addnote.view.NewNoteActivity;
import com.todo.todo.archive.view.ArchiveFragment;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.removenote.view.TrashFragment;
import com.todo.todo.sharenote.ShareNote;
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.util.AsyncTaskLoadImage;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;
import com.todo.todo.util.Constants.ProfileeKey;
import com.todo.todo.util.DownloadImage;
import com.todo.todo.util.DownloadImageInterface;
import com.todo.todo.util.ProgressUtil;
import com.todo.todo.util.Utility;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
public class ToDoActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ToDoActivityInteface {
    private final int SELECT_PHOTO = 3;
    ToDoActivityPresenter mToDoActivityPresenter;
    ProgressUtil mProgressDialog;
    AppCompatTextView mTextView_Email, mTextView_Name, mTextView_Title;
    AppCompatEditText mEditText_Search, mEditTextsearch;
    Toolbar mToolbar, mToolSearch, mToolbardelete;
    AppCompatImageView mImageView_Linear_Grid, mImageView_ProfileImage, mImageViewsearchBack;
    FloatingActionButton mFloatingActionButton;
    AppCompatImageView mImageViewsearch;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Uri mPrfilefilePath;
    List<ToDoItemModel> toDoItemModels;
    List<ToDoItemModel> toDoAllItemModels, mRemindrsToDO, mArchivedNotes, mTrashNotes;
    Utility util;
    UpdateNotePresenter updateNotePresenter;
    private String TAG = "ToDoActivity";
    private String mEmail_id, mUserUID;
    private TrashFragment trashFragment;
    private ArchiveFragment archiveFragment;
    private ToDoNotesFragment notesFragment;
    private ReminderFragment reminderFragment;
    private boolean offAdd = false;
    private int RQS_1 = 1;
    private ShareNote shareNote;
    private String mStrshareNote;
    DatabaseReference databaseReference;
    private boolean draged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_to_do);
        toDoAllItemModels = new ArrayList<>();
        notesFragment = new ToDoNotesFragment(this,toDoAllItemModels);
        getSupportFragmentManager().beginTransaction().replace(R.id.flayout, notesFragment).addToBackStack(null).commit();
        initView();
        updateNotePresenter = new UpdateNotePresenter(ToDoActivity.this, this);
        mRemindrsToDO = new ArrayList<>();
        mArchivedNotes = new ArrayList<>();
        toDoItemModels = new ArrayList<>();
        mTrashNotes = new ArrayList<>();
    }

    @Override
    public void initView() {
        pref = getSharedPreferences(ProfileeKey.SHAREDPREFERANCES_KEY, MODE_PRIVATE);
        editor = pref.edit();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        if(mUserUID.equals("localregister")){
            mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID_LOCAL, Constants.Stringkeys.NULL_VALUIE);
        }

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mImageView_Linear_Grid = (AppCompatImageView) findViewById(R.id.imageView_grid_linear);
        mImageViewsearch = (AppCompatImageView) findViewById(R.id.imageView_search_bar);
        mTextView_Title = (AppCompatTextView) findViewById(R.id.textview_title_toolbar);
        mEditText_Search = (AppCompatEditText) findViewById(R.id.edittext_search_toolbar);
        mEditTextsearch = (AppCompatEditText) findViewById(R.id.edittext_search_toolbar);
        mImageViewsearchBack = (AppCompatImageView) findViewById(R.id.imageView_back_search);
        FacebookSdk.sdkInitialize(getApplicationContext());             //Facebook Social Login sdk initialize
        mProgressDialog = new ProgressUtil(this);
        util = new Utility(this);


        Log.i(TAG, "initView: " + mUserUID);
        // get call to database if User ID is not Null
        if (!mUserUID.equals(Constants.Stringkeys.NULL_VALUIE)) {
            mToDoActivityPresenter = new ToDoActivityPresenter(this, this);
            mToDoActivityPresenter.getPresenterNotes(mUserUID);
        }

        mToolSearch = (Toolbar) findViewById(R.id.toolbarsearch);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbardelete = (Toolbar) findViewById(R.id.toolbar_delete);
        setSupportActionBar(mToolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        mImageView_ProfileImage = (AppCompatImageView) header.findViewById(R.id.imageView_nav_profile);
        mTextView_Email = (AppCompatTextView) header.findViewById(R.id.textView_nav_email);
        mTextView_Name = (AppCompatTextView) header.findViewById(R.id.textview_nave_name);
        setNavigationProfile();
        hideKeyboard();
        setOnClickListener();
    }

    @Override
    public void setOnClickListener() {
        mFloatingActionButton.setOnClickListener(this);
        mImageView_Linear_Grid.setOnClickListener(this);
        mImageViewsearch.setOnClickListener(this);
    }

    @Override
    public void enterFromBottomAnimation() {
        overridePendingTransition(R.anim.activity_no_animation, R.anim.activity_close_translate_to_bottom);
    }

    @Override
    public void exitToBottomAnimation() {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
    }

    public void getUndoArchivedNote(int position) {
        if (mTextView_Title.getText().toString().equals(Constants.NotesType.ARCHIVE_NOTES)) {
            updateNotePresenter.getUndoAchiveNote(mUserUID, mArchivedNotes.get(position).getStartdate(), mArchivedNotes.get(position));
        }
    }

    @Override
    public void hideToolBar(boolean flag) {
        if (flag) {
            mToolbardelete.setVisibility(View.VISIBLE);
        } else {
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    //Set Profile view
    public void setNavigationProfile() {
        String getemail, getName, image_Url = "";
        if (pref.contains(Constants.BundleKey.USER_EMAIL)) {
            getemail = pref.getString(Constants.BundleKey.USER_EMAIL, Constants.Stringkeys.DEMO_EMAIL);
            if (!pref.getString(ProfileeKey.LAST_NAME, "null").equals("null")) {
                getName = pref.getString(ProfileeKey.FIRST_NAME, Constants.Stringkeys.NAME) + " " + pref.getString(ProfileeKey.LAST_NAME, " ");
            } else {
                getName = pref.getString(ProfileeKey.FIRST_NAME, Constants.Stringkeys.NAME);
            }
            mEmail_id = getemail;
            Log.i(TAG, "onCreate:  email" + getemail);
            Connection con = new Connection(getApplicationContext());
            if (con.isNetworkConnected()) {
                if (pref.contains(Constants.BundleKey.USER_PROFILE_SERVER) && pref.getString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_false)).equals(getString(R.string.flag_true))) {
                    mImageView_ProfileImage.setOnClickListener(this);
                    DownloadImage.downloadImage(String.valueOf("myProfiles/" + getemail.substring(0, getemail.indexOf("@")) + ".jpg"), new DownloadImageInterface() {
                        @Override
                        public void getImage(Bitmap bitmap) {
                            mImageView_ProfileImage.setImageBitmap(bitmap);
                            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                            Bitmap conv_bm = getRoundedRectBitmap(resized, 1000);
                            mImageView_ProfileImage.setImageBitmap(conv_bm);
                        }
                    });
                } else if (pref.contains(Constants.BundleKey.PROFILE_PIC) && !pref.getString(Constants.BundleKey.PROFILE_PIC, Constants.Stringkeys.NULL_VALUIE).equals(Constants.Stringkeys.NULL_VALUIE)) {
                    try {
                        URL urls = new URL(pref.getString(Constants.BundleKey.PROFILE_PIC, Constants.Stringkeys.NULL_VALUIE));
                        new AsyncTaskLoadImage(this).execute(String.valueOf(urls));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                mImageView_ProfileImage.setOnClickListener(this);
                if (pref.contains(Constants.BundleKey.USER_PROFILE_LOCAL)) {
                    mPrfilefilePath = Uri.parse(pref.getString(Constants.BundleKey.USER_PROFILE_LOCAL, Constants.Stringkeys.NULL_VALUIE));
                    if (!image_Url.equals(getString(R.string.null_string))) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), mPrfilefilePath);
                            Bitmap conv_bm = getRoundedRectBitmap(bitmap, 1000);
                            mImageView_ProfileImage.setImageBitmap(conv_bm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            mTextView_Email.setText(getemail);
            mTextView_Name.setText(getName);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mToolbar.getVisibility() == View.VISIBLE) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                super.onBackPressed();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
        if (mToolbardelete.getVisibility() == View.VISIBLE) {
            mToolbardelete.setVisibility(View.GONE);
            mToolbar.setVisibility(View.VISIBLE);
            trashFragment.getRefreshNotes();
        } else if (mToolSearch.getVisibility() == View.VISIBLE) {
            mToolSearch.setVisibility(View.GONE);
            mToolbar.setVisibility(View.VISIBLE);
        } else {
        }
        mFloatingActionButton.setVisibility(View.VISIBLE);
    }

    private void logoutUser() {
        if (pref.contains(Constants.BundleKey.USER_REGISTER)) {
            if (pref.contains(Constants.BundleKey.FACEBOOK_LOGIN) && pref.getString(Constants.BundleKey.FACEBOOK_LOGIN, "false").equals("true")) {
                LoginManager.getInstance().logOut();
                editor.putString(Constants.BundleKey.FACEBOOK_LOGIN, Constants.Stringkeys.FLAG_FALSE);
            } else if (pref.contains(Constants.BundleKey.GOOGLE_LOGIN) && pref.getString(Constants.BundleKey.GOOGLE_LOGIN, "false").equals("true")) {
                editor.putString(Constants.BundleKey.GOOGLE_LOGIN, Constants.Stringkeys.FLAG_FALSE);
            }
            editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, Constants.Stringkeys.FLAG_FALSE);
            editor.putString(Constants.BundleKey.USER_REGISTER, Constants.Stringkeys.FLAG_FALSE);
            editor.putString(Constants.BundleKey.PROFILE_PIC, Constants.Stringkeys.NULL_VALUIE);
            editor.putString(Constants.BundleKey.USER_EMAIL, Constants.Stringkeys.NULL_VALUIE);
            editor.putString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
            editor.putString(Constants.BundleKey.USER_NAME, Constants.Stringkeys.NULL_VALUIE);
            editor.putString(ProfileeKey.FIRST_NAME, Constants.Stringkeys.NULL_VALUIE);
            editor.putString(ProfileeKey.LAST_NAME, Constants.Stringkeys.NULL_VALUIE);
            editor.commit();
            Intent intent = new Intent(ToDoActivity.this, LoginActivity.class);
            finish();
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageView_grid_linear:
                //  getAlterRecyclerLayout();
                break;
            case R.id.fab:
                Intent intent = new Intent(ToDoActivity.this, NewNoteActivity.class);
                intent.putExtra(Constants.BundleKey.USER_USER_UID, mUserUID);
                startActivityForResult(intent, 2);
                enterFromBottomAnimation();
                break;
            case R.id.imageView_nav_profile:
                Intent picker = new Intent();
                picker.setType("image/*");
                picker.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(picker, String.valueOf(R.string.select_pick)), SELECT_PHOTO);
                break;
            case R.id.imageView_search_bar:
                mToolbar.setVisibility(View.GONE);
                mToolSearch.setVisibility(View.VISIBLE);
                Animation animate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadeout);
                mToolSearch.setAnimation(animate);
                mToolSearch.startAnimation(animate);
                mImageViewsearchBack.setOnClickListener(this);
                break;
            case R.id.imageView_back_search:
                mToolSearch.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
                mEditText_Search.setText("");
                break;
        }
    }

    public void setVisibilityTollBar(boolean flag) {
        if (flag) {
            mImageViewsearch.setVisibility(View.GONE);
            mTextView_Title.setVisibility(View.GONE);
            mEditText_Search.setVisibility(View.VISIBLE);
        } else {
            mImageViewsearch.setVisibility(View.VISIBLE);
            mTextView_Title.setVisibility(View.VISIBLE);
            mEditText_Search.setVisibility(View.GONE);
        }
    }

    @Override
    public void closeProgressDialog() {
        mProgressDialog.dismissProgress();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.showProgress(getApplicationContext().getString(R.string.load_data));
    }

    //Item selected event
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_notes:
                notesFragment = new ToDoNotesFragment(this,toDoAllItemModels);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, notesFragment).addToBackStack(null).commit();
                mFloatingActionButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_reminders:
                reminderFragment = new ReminderFragment(toDoAllItemModels, this);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, reminderFragment).addToBackStack(null).commit();
                mFloatingActionButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_archive:
                archiveFragment = new ArchiveFragment(toDoAllItemModels, this);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, archiveFragment).addToBackStack(null).commit();
                mFloatingActionButton.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_trash:
                trashFragment = new TrashFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, trashFragment).addToBackStack(null).commit();
                mFloatingActionButton.setVisibility(View.VISIBLE);   //getupdatedView(mTrashNotes, 4);
                break;
            case R.id.nav_share:
                if (mTextView_Title.getText().equals(Constants.NotesType.ALL_NOTES)) {
                    mStrshareNote = Constants.NotesType.ALL_NOTES;
                } else if (mTextView_Title.getText().equals(Constants.NotesType.REMINDER_NOTES)) {
                    mStrshareNote = Constants.NotesType.REMINDER_NOTES;
                } else if (mTextView_Title.getText().equals(Constants.NotesType.ARCHIVE_NOTES)) {
                    mStrshareNote = Constants.NotesType.ARCHIVE_NOTES;
                } else if (mTextView_Title.getText().equals(Constants.NotesType.TRASH_NOTES)) {
                    mStrshareNote = Constants.NotesType.TRASH_NOTES;
                }
                shareNote = new ShareNote(toDoAllItemModels, this,mStrshareNote);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, shareNote).addToBackStack(null).commit();
                break;
            case R.id.nav_logout:
                logoutUser();           //call to Logout Methode
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModelas) {
       if(!draged) {
           this.toDoAllItemModels = toDoItemModelas;
           if (toDoAllItemModels.size() != 0) {
               editor.putInt(Constants.Stringkeys.LAST_NOTE_COUNT, toDoAllItemModels.size());
               editor.commit();
               if (mTextView_Title.getText().equals(Constants.NotesType.ALL_NOTES)){
                   if (offAdd) {

                   }
               }else if (mTextView_Title.getText().equals(Constants.NotesType.REMINDER_NOTES)) {
                   reminderFragment.setUpdatedModel(toDoAllItemModels);
               } else if (mTextView_Title.getText().equals(Constants.NotesType.ARCHIVE_NOTES)) {
                   archiveFragment.setUpdatedModel(toDoAllItemModels);
               }
           }
           else {
           }
       }
    }

    @Override
    public void getResponce(boolean flag) {
        if (flag) {
            Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.success), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Connection con = new Connection(getApplicationContext());
        if (!con.isNetworkConnected()) {
            if (requestCode == 2) {
                if (data != null) {
                    offAdd = true;
                    Bundle ban = data.getBundleExtra(Constants.BundleKey.MEW_NOTE);
                    ToDoItemModel toDoItemModel = new ToDoItemModel();
                    toDoItemModel.setId(Integer.parseInt(ban.getString(Constants.RequestParam.KEY_ID)));
                    toDoItemModel.setTitle(ban.getString(Constants.RequestParam.KEY_TITLE));
                    toDoItemModel.setNote(ban.getString(Constants.RequestParam.KEY_NOTE));
                    toDoItemModel.setReminder(ban.getString(Constants.RequestParam.KEY_REMINDER));
                    toDoItemModel.setStartdate(ban.getString(Constants.RequestParam.KEY_STARTDATE));
                    toDoItemModel.setColor(ban.getString(Constants.RequestParam.KEY_COLOR));
                    toDoItemModel.setSrid(ban.getInt(Constants.RequestParam.KEY_SRID));
                    toDoItemModel.setArchive("false");
                    toDoAllItemModels.add(toDoItemModel);
                    if (mTextView_Title.getText().equals(Constants.NotesType.ALL_NOTES)) {
                        notesFragment.showDataInActivity(toDoAllItemModels);
                    } else if (mTextView_Title.getText().equals(Constants.NotesType.REMINDER_NOTES)) {
                        reminderFragment.setUpdatedModel(toDoAllItemModels);
                    }
                }
            }
        }

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                // Get the url from data
                if (data != null) {
                    mPrfilefilePath = data.getData();
                    if (null != mPrfilefilePath) {
                        Log.i(TAG, "onActivityResult: " + mPrfilefilePath);
                        cropCapturedImage(mPrfilefilePath);
                        editor.putString(Constants.BundleKey.USER_PROFILE_LOCAL, String.valueOf(mPrfilefilePath));
                        editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, getString(R.string.flag_true));
                        editor.commit();
                    }
                }
            }
        }
        //take croped image
        if (requestCode == 4) {
            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap cropedPic = extras.getParcelable("data");
                util.uploadFile(cropedPic, mEmail_id);
                Bitmap scaled = Bitmap.createScaledBitmap(cropedPic, 100, 100, true);
                Bitmap conv_bm = getRoundedRectBitmap(scaled, 3000);
                mImageView_ProfileImage.setImageBitmap(conv_bm);
            }
        }
    }

    //get crop images circular
    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            int color = 0xff424242;
            Paint paint = new Paint();
            Rect rect = new Rect(0, 0, 200, 200);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawCircle(50, 50, 50, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
        } catch (NullPointerException e) {
        } catch (OutOfMemoryError o) {
        }
        return result;
    }


    private void cropCapturedImage(Uri prfilefilePath) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(prfilefilePath, "image/*");
        cropIntent.putExtra("crop", getString(R.string.flag_true));
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 4);
    }

    //hide keyboard
    void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void setImage(Bitmap bitmap) {
        Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
        Bitmap conv_bm = getRoundedRectBitmap(resized, 1000);
        mImageView_ProfileImage.setImageBitmap(conv_bm);
    }

    public void getReminderSet(ToDoItemModel todoItem) {
        Bundle bundle;
        bundle = new Bundle();
        bundle.putString(Constants.BundleKey.MEW_NOTE_TITLE, todoItem.getTitle());
        bundle.putString(Constants.BundleKey.MEW_NOTE_DISK, todoItem.getNote());

    }

    public void setdraged(boolean b) {
        draged=b;
    }
}
