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
import android.support.v4.app.FragmentManager;
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

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.todo.todo.R;
import com.todo.todo.addnote.view.NewNoteActivity;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.RemoveNotePresenter;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.login.view.LoginActivity;
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

public class ToDoActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, ToDoActivityInteface {
    private final int SELECT_PHOTO = 3;
    ToDoActivityPresenter mToDoActivityPresenter;
    ProgressUtil mProgressDialog;
    AppCompatTextView mTextView_Email, mTextView_Name, mTextView_Title;
    AppCompatEditText mEditText_Search, mEditTextsearch;
    Toolbar mToolbar, mToolSearch, mToolbardelete;
    boolean isReminderAdapter = false;
    boolean isArchivedAdapter = false;
    // RecyclerView mToDoRecyclerView;
    AppCompatImageView mImageView_Linear_Grid, mImageView_ProfileImage, mImageViewsearchBack;
    FloatingActionButton mFloatingActionButton;
    AppCompatImageView mImageViewsearch;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Uri mPrfilefilePath;
    ItemAdapter itemAdapter, mReminderAdapter, mArchivedAdapter;
    List<ToDoItemModel> toDoItemModels;
    List<ToDoItemModel> toDoAllItemModels, mRemindrsToDO, mArchivedNotes, mTrashNotes;
    Utility util;
    UpdateNotePresenter updateNotePresenter;
    RemoveNotePresenter removeNotePresenter;
    private String isUpdateUI = "";
    private String TAG = "ToDoActivity";
    private String mEmail_id, mUserUID;
    private boolean issearch = false;
    private boolean mLinear = false;
    private boolean isTrashAdapter;
    private ItemAdapter mTrashAdapter;
    private TrashFragment trashFragment;
    private ArchiveFragment archiveFragment;
    private ToDoNotesFragment notesFragment;
    private ReminderFragment reminderFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        toDoAllItemModels = new ArrayList<>();
        notesFragment = new ToDoNotesFragment(toDoAllItemModels);
        getSupportFragmentManager().beginTransaction().replace(R.id.flayout, notesFragment).addToBackStack(null).commit();
        initView();
        updateNotePresenter = new UpdateNotePresenter(ToDoActivity.this, this);
        mRemindrsToDO = new ArrayList<>();
        mArchivedNotes = new ArrayList<>();
        toDoItemModels = new ArrayList<>();
        mTrashNotes = new ArrayList<>();


        ///  removeNotePresenter = new RemoveNotePresenter(getApplicationContext(),ToDoActivity.this);
    }

    @Override
    public void initView() {


        // mTextView_blank_recycler=(AppCompatTextView)findViewById(R.id.textview_blank_recyclerview);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mImageView_Linear_Grid = (AppCompatImageView) findViewById(R.id.imageView_grid_linear);
        mImageViewsearch = (AppCompatImageView) findViewById(R.id.imageView_search_bar);
        //  mToDoRecyclerView = (RecyclerView) findViewById(R.id.gridview_notes);
        mTextView_Title = (AppCompatTextView) findViewById(R.id.textview_title_toolbar);
        mEditText_Search = (AppCompatEditText) findViewById(R.id.edittext_search_toolbar);
        mEditTextsearch = (AppCompatEditText) findViewById(R.id.edittext_search_toolbar);
        mImageViewsearchBack = (AppCompatImageView) findViewById(R.id.imageView_back_search);

        FacebookSdk.sdkInitialize(getApplicationContext());             //Facebook Social Login sdk initialize
        mProgressDialog = new ProgressUtil(this);

        util = new Utility(this);
        pref = getSharedPreferences(ProfileeKey.SHAREDPREFERANCES_KEY, MODE_PRIVATE);
        editor = pref.edit();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        Log.i(TAG, "initView: " + mUserUID);

        // get call to database if User ID is not Null
        if (!mUserUID.equals(Constants.Stringkeys.NULL_VALUIE)) {
            mToDoActivityPresenter = new ToDoActivityPresenter(this, this);
            mToDoActivityPresenter.getPresenterNotes(mUserUID);
        }
       /* mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mTextView_blank_recycler.setText(getString(R.string.data_is_null));
        mToDoRecyclerView.setVisibility(View.INVISIBLE);
        mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        // mToDoRecyclerView.setItemAnimator(new DefaultItemAnimator());
*/
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
        isUpdateUI = mTextView_Title.getText().toString();

        setNavigationProfile();
        //  addTextListener();
        //initSwipe();
        hideKeyboard();
        setOnClickListener();
    }

    public List<ToDoItemModel> getUpdateModels() {
        List<ToDoItemModel> dataModels = new ArrayList<>();
        String typeOfNotes = mTextView_Title.getText().toString();
        if (typeOfNotes.equals(Constants.NotesType.ALL_NOTES)) {
            dataModels = toDoItemModels;
        } else if (typeOfNotes.equals(Constants.NotesType.REMINDER_NOTES)) {
            dataModels = mRemindrsToDO;
        } /*else if (typeOfNotes.equals(Constants.NotesType.ARCHIVE_NOTES)) {
            dataModels = mArchivedNotes;
        }else if(typeOfNotes.equals(Constants.NotesType.TRASH_NOTES)) {
            dataModels = mTrashNotes;
        }*/
        return dataModels;
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
/*

    //swipe view delete / Archive
    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // Collections.swap(toDoItemModels,viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.i(TAG, "onMove: " + viewHolder.getAdapterPosition() + "  target" + target.getAdapterPosition());
                int from, destination;
                from = viewHolder.getAdapterPosition();
                destination = target.getAdapterPosition();
                itemAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                */
/*updateNotePresenter.getMoveNotes(mUserUID, toDoItemModels.get(from), toDoItemModels.get(destination));*//*

                return true;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                String typeOfNotes = mTextView_Title.getText().toString();
                final List<ToDoItemModel> forArchiveAlldataModels = toDoAllItemModels;
                Log.i(TAG, "onSwiped: " + typeOfNotes);
                if (typeOfNotes.equals(Constants.NotesType.ALL_NOTES)) {
                    Log.i(TAG, "onSwiped: ");
                    final ToDoItemModel toDoItem=toDoItemModels.get(position);
                    if (direction == ItemTouchHelper.LEFT) {
                       removeNotePresenter.removeFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                        Snackbar snackbar = Snackbar
                                .make(getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                                .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeNotePresenter.undoRemoveFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                                    }
                                });
                        snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                        snackbar.show();

                        if(toDoItemModels.size()==1){
                           toDoItemModels.remove(0);
                           if(mRemindrsToDO.size()>0) mRemindrsToDO.remove(0);
                           showDataInActivity(new ArrayList<ToDoItemModel>());
                       }
                    } else {
                        getArchive(itemAdapter, position, toDoItemModels.get(position));
                    }
                } else if (typeOfNotes.equals(Constants.NotesType.REMINDER_NOTES)) {
                    if (direction == ItemTouchHelper.LEFT) {
                        final ToDoItemModel toDoItem=mRemindrsToDO.get(position);
                        removeNotePresenter.removeFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                        Snackbar snackbar = Snackbar
                                .make(getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                                .setAction(Constants.Stringkeys.ARCHIVE_UNDO,new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        removeNotePresenter.undoRemoveFirebaseData(toDoItem,forArchiveAlldataModels, mUserUID);
                                    }
                                });
                        snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                        snackbar.show();

                        if(mRemindrsToDO.size()==1){
                            mRemindrsToDO.remove(0);
                            showDataInActivity(new ArrayList<ToDoItemModel>());
                        }
                    } else {
                        getArchive(mReminderAdapter, position, mRemindrsToDO.get(position));
                    }
                } else {
                    getRecyclerLayout();
                  //  mToDoRecyclerView.setAdapter(mArchivedAdapter);
                   */
/* if (direction == ItemTouchHelper.LEFT) {
                        removeNotePresenter.removeFirebaseData(mArchivedNotes.get(position),toDoAllItemModels, mUserUID);
                    } else {
                        getArchive(mArchivedAdapter, position, mArchivedNotes.get(position));
                    }*//*

                }
            }

            //Archive Note Methode  And do Undo if required
            public void getArchive(final ItemAdapter archiveitemAdapter, final int position, final ToDoItemModel toDoItemModel) {
                //UpdateNotePresenter updateNotePresenter = new UpdateNotePresenter(ToDoActivity.this, this);
                final String date = toDoItemModel.getStartdate();
               // archiveitemAdapter.removeItem(position);
                updateNotePresenter.getAchiveNote(mUserUID, date, toDoItemModel);
                Snackbar snackbar = Snackbar
                        .make(getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                        .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateNotePresenter.getUndoAchiveNote(mUserUID, date, toDoItemModel);
                                toDoItemModel.setArchive(Constants.Stringkeys.FLAG_FALSE);
                                archiveitemAdapter.reduNote(toDoItemModel, position);
                            }
                        });
                snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                snackbar.show();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);

    }
*/

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            super.onBackPressed();
            /*editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
            editor.commit();*/
            finish();
        } else {
            fragmentManager.popBackStack();
        }
        // getSupportFragmentManager().popBackStackImmediate();
        //  getupdatedView(toDoItemModels, 1);
        isReminderAdapter = false;
        isArchivedAdapter = false;
//            mTextView_Title.setText(Constants.NotesType.ALL_NOTES);

    }

    /*
    * Logout User From ToDo App
    * */
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
                // overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                break;
            case R.id.imageView_nav_profile:
                Intent picker = new Intent();
                picker.setType("image/*");
                picker.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(picker, String.valueOf(R.string.select_pick)), SELECT_PHOTO);
                break;
            case R.id.imageView_search_bar:
                mToolSearch = (Toolbar) findViewById(R.id.toolbarsearch);
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
/*
    private void getRecyclerLayout() {
        if (mLinear) {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }*/

    /*   //set Linear or grid Layout
       private void getAlterRecyclerLayout() {
           if (!mLinear) {
               mLinear = true;
               mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
               mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
           } else {
               mLinear = false;
               mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
               mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
           }
       }
   */
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
                //  getupdatedView(toDoItemModels, 1);
                isReminderAdapter = false;
                isArchivedAdapter = false;
                notesFragment = new ToDoNotesFragment(toDoAllItemModels);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, notesFragment).addToBackStack(null).commit();

                //   getSupportFragmentManager().popBackStackImmediate();
                // mTextView_Title.setText(Constants.NotesType.ALL_NOTES);
                break;
            case R.id.nav_reminders:
              /*  getRecyclerLayout();
                getupdatedView(mRemindrsToDO, 2);*/
                isReminderAdapter = true;
                isArchivedAdapter = false;
                isTrashAdapter = false;
                reminderFragment = new ReminderFragment(toDoAllItemModels);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, reminderFragment).addToBackStack(null).commit();
/*
                mTextView_Title.setText(Constants.NotesType.REMINDER_NOTES);
                getSupportFragmentManager().popBackStackImmediate();*/
                break;
            case R.id.nav_archive:
                // getSupportFragmentManager().popBackStackImmediate();
                archiveFragment = new ArchiveFragment(toDoAllItemModels, this);
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, archiveFragment).addToBackStack(null).commit();
                //getupdatedView(mArchivedNotes, 3);
                isArchivedAdapter = true;
                isReminderAdapter = false;
                isTrashAdapter = false;
                // mTextView_Title.setText(Constants.NotesType.ARCHIVE_NOTES);
                break;
            case R.id.nav_trash:
                trashFragment = new TrashFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flayout, trashFragment).addToBackStack(null).commit();
                //getupdatedView(mTrashNotes, 4);
                isTrashAdapter = true;
                isReminderAdapter = false;
                isArchivedAdapter = false;
                //  mTextView_Title.setText(Constants.NotesType.TRASH_NOTES);
                break;

            case R.id.nav_logout:
                logoutUser();           //call to Logout Methode
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void shareTextUrl() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "http://www.codeofaninja.com");

        startActivity(Intent.createChooser(share, "Share link!"));
    }
/*
    public void getupdatedView(List<ToDoItemModel> allNotes, int i) {
            switch (i) {
                case 1:
                    if(allNotes.size()==0){
                        mTextView_blank_recycler.setVisibility(View.VISIBLE);
                        mTextView_blank_recycler.setText(getString(R.string.data_is_null));
                        mToDoRecyclerView.setVisibility(View.INVISIBLE);
                    }else {
                        mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                        mToDoRecyclerView.setVisibility(View.VISIBLE);
                        getRecyclerLayout();
                        itemAdapter = new ItemAdapter(ToDoActivity.this, allNotes);
                        mToDoRecyclerView.setAdapter(itemAdapter);
                    }
                    break;
                case 2:
                    if(allNotes.size()==0){
                        mTextView_blank_recycler.setVisibility(View.VISIBLE);
                        mTextView_blank_recycler.setText(getString(R.string.get_reminder_null));
                        mToDoRecyclerView.setVisibility(View.INVISIBLE);
                    }else {
                        mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                        mToDoRecyclerView.setVisibility(View.VISIBLE);
                        getRecyclerLayout();
                        mReminderAdapter = new ItemAdapter(ToDoActivity.this, allNotes);
                        mToDoRecyclerView.setAdapter(mReminderAdapter);
                    }
                    break;
                case 3:
                    if(allNotes.size()==0){
                        mTextView_blank_recycler.setVisibility(View.VISIBLE);
                        mTextView_blank_recycler.setText(getString(R.string.get_archive_null));
                        mToDoRecyclerView.setVisibility(View.INVISIBLE);
                    }else {
                        mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                        mToDoRecyclerView.setVisibility(View.VISIBLE);
                        getRecyclerLayout();
                        mArchivedAdapter = new ItemAdapter(ToDoActivity.this, allNotes);
                        mToDoRecyclerView.setAdapter(mArchivedAdapter);
                    }
                    break;

            }
    }*/

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModelas) {
        this.toDoAllItemModels = toDoItemModelas;
        if (toDoAllItemModels.size() != 0) {

            if (mTextView_Title.getText().equals(Constants.NotesType.ALL_NOTES)) {
                notesFragment.setUpdatedModel(toDoAllItemModels);
            } else if (mTextView_Title.getText().equals(Constants.NotesType.REMINDER_NOTES)) {
                reminderFragment.setUpdatedModel(toDoAllItemModels);
            } else if (mTextView_Title.getText().equals(Constants.NotesType.ARCHIVE_NOTES)) {
                archiveFragment.setUpdatedModel(toDoAllItemModels);
            }
        } else {
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
                    Bundle ban = data.getBundleExtra(Constants.BundleKey.MEW_NOTE);
                    ToDoItemModel toDoItemModel = new ToDoItemModel();
                    toDoItemModel.setId(Integer.parseInt(ban.getString(Constants.RequestParam.KEY_ID)));
                    toDoItemModel.setTitle(ban.getString(Constants.RequestParam.KEY_TITLE));
                    toDoItemModel.setNote(ban.getString(Constants.RequestParam.KEY_NOTE));
                    toDoItemModel.setReminder(ban.getString(Constants.RequestParam.KEY_REMINDER));
                    toDoItemModel.setStartdate(ban.getString(Constants.RequestParam.KEY_STARTDATE));
                    toDoAllItemModels.add(toDoItemModel);
                    showDataInActivity(toDoAllItemModels);
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
        if (requestCode == 3) {
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

    private void cropCapturedImage(Uri prfilefilePath) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(prfilefilePath, "image/*");
        cropIntent.putExtra("crop", getString(R.string.flag_true));
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 256);
        cropIntent.putExtra("outputY", 256);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, 3);
    }

/*

    public void addTextListener() {

        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (isReminderAdapter && mReminderAdapter != null) {
                    Filter filter = mReminderAdapter.getFilter();
                    filter.filter(query);
                } else if (isArchivedAdapter && mArchivedAdapter != null) {
                    Filter filter = mArchivedAdapter.getFilter();
                    filter.filter(query);
                }
                else if (isTrashAdapter && mTrashAdapter != null) {
                    Filter filter = mTrashAdapter.getFilter();
                    filter.filter(query);
                }else {
                    if (itemAdapter != null) {
                        Filter filter = itemAdapter.getFilter();
                        filter.filter(query);
                    }
                }


            }
        });
    }
*/

    //get Reminders todo
    public List<ToDoItemModel> getTodaysReminder(String date) {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (toDoAllItemModels != null) {
            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if (todoItem.getReminder().equals(date) && todoItem.getArchive().equals(getString(R.string.flag_false))) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get All notes
    public List<ToDoItemModel> getAllToDo() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (toDoAllItemModels != null) {
            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if (todoItem.getArchive().equals(getString(R.string.flag_false))) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get Archived todo notes
    public List<ToDoItemModel> getArchivedToDos() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (toDoAllItemModels != null) {

            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if (todoItem.getArchive().equals(getString(R.string.flag_true))) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
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


}
