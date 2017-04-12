package com.todo.todo.home.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.todo.todo.R;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.listener.RecyclerItemClickListener;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.RemoveNotePresenter;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.update.view.UpdateNoteActivity;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;
import com.todo.todo.util.Constants.ProfileeKey;
import com.todo.todo.util.DownloadImage;
import com.todo.todo.util.DownloadImageInterface;
import com.todo.todo.util.ProgressUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ToDoActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener ,ToDoActivityInteface {
    private final int SELECT_PHOTO = 3;
    ToDoActivityPresenter mToDoActivityPresenter;
    ProgressUtil mProgressDialog;
    AppCompatTextView mTextView_Email,mTextView_Name, mTextView_Title;
    AppCompatEditText mEditText_Search;
    Toolbar mToolbar;
    boolean isReminderAdapter=false;
    boolean isArchivedAdapter=false;
    RecyclerView mToDoRecyclerView;
    AppCompatImageView mImageView_Linear_Grid, mImageView_ProfileImage,mImageViewsearch;
    FloatingActionButton mFloatingActionButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Uri mPrfilefilePath;
    ItemAdapter itemAdapter,mReminderAdapter,mArchivedAdapter;
    List<ToDoItemModel> toDoItemModels;
    List<ToDoItemModel> toDoAllItemModels, mRemindrsToDO, mArchivedNotes;
    private Paint paint = new Paint();
    private  String TAG ="ToDoActivity";
    private  String mEmail_id,mUserUID;
    private  boolean issearch=false;
    private  boolean mLinear=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialise();

    }

    @Override
    public void initialise() {

        setContentView(R.layout.activity_to_do);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //LinearLayout layoutToolbar = (LinearLayout)  mToolbar.findViewById(R.id.layoutToolbar);

        mFloatingActionButton =(FloatingActionButton) findViewById(R.id.fab);
        mImageView_Linear_Grid =(AppCompatImageView) findViewById(R.id.imageView_grid_linear);
        mImageViewsearch=(AppCompatImageView) findViewById(R.id.imageView_search_bar);
        mToDoRecyclerView = (RecyclerView) findViewById(R.id.gridview_notes);
        mTextView_Title =(AppCompatTextView) findViewById(R.id.textview_title_toolbar);
        mEditText_Search =(AppCompatEditText) findViewById(R.id.edittext_search_toolbar);
        FacebookSdk.sdkInitialize(getApplicationContext());
        mProgressDialog=new ProgressUtil(this);
        setSupportActionBar(mToolbar);

        pref = getSharedPreferences(ProfileeKey.SHAREDPREFERANCES_KEY, MODE_PRIVATE);
        editor = pref.edit();
        mUserUID=pref.getString(Constants.BundleKey.USER_USER_UID,"null");
        Log.i(TAG, "initialise: "+mUserUID);
        if(!mUserUID.equals("null")){
            mToDoActivityPresenter =new ToDoActivityPresenter(this,this);
            mToDoActivityPresenter.getPresenterNotes(mUserUID);
        }
        mToDoRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mToDoRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mToDoRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        setVisibilityTollBar(false);
                        String typeOfNotes=mTextView_Title.getText().toString();
                        List<ToDoItemModel> updateModels=getUpdateModels(typeOfNotes);
                        Intent intent =new Intent(ToDoActivity.this,UpdateNoteActivity.class);
                        Bundle bun=new Bundle();
                        bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(updateModels.get(position).get_id()));
                        bun.putString(Constants.RequestParam.KEY_NOTE, updateModels.get(position).get_note());
                        bun.putString(Constants.RequestParam.KEY_TITLE, updateModels.get(position).get_title());
                        bun.putString(Constants.RequestParam.KEY_REMINDER, updateModels.get(position).get_reminder());
                        bun.putString(Constants.RequestParam.KEY_STARTDATE, updateModels.get(position).get_startdate());
                        bun.putString(Constants.RequestParam.KEY_ARCHIVE, updateModels.get(position).get_Archive());
                        intent.putExtra(Constants.BundleKey.USER_USER_UID,mUserUID);
                        intent.putExtra(Constants.BundleKey.MEW_NOTE,bun);
                        startActivityForResult(intent,2);
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                        //  Toast.makeText(this, "selected"+position, Toast.LENGTH_SHORT).show();
                        // TODO Handle item click
                    }
                }));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        mImageView_ProfileImage =(AppCompatImageView) header.findViewById(R.id.imageView_nav_profile);
        mTextView_Email =(AppCompatTextView) header.findViewById(R.id.textView_nav_email);
        mTextView_Name=(AppCompatTextView)header. findViewById(R.id.textview_nave_name);

        setNavigationProfile();
        addTextListener();
        initSwipe();
        hideKeyboard();
        setOnClickListener();
    }

    private List<ToDoItemModel> getUpdateModels(String typeOfNotes) {
        if(typeOfNotes.equals(Constants.NotesType.ALL_NOTES)){
            return toDoItemModels;
        }else if (typeOfNotes.equals(Constants.NotesType.REMINDER_NOTES)){
            return mRemindrsToDO;
        }else {
            return mArchivedNotes;
        }
    }

    @Override
    public void setOnClickListener() {
        mImageView_ProfileImage.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        mImageView_Linear_Grid.setOnClickListener(this);
        mImageViewsearch.setOnClickListener(this);
    }

    /* public Bitmap getBitmap(URL url){
         final Bitmap[] bitmaps = {null};
         Picasso.with(getApplicationContext())
                 .load(pref.getString(Constants.BundleKey.PROFILE_PIC,"ghfgh"))
                 .into(new Target() {
                     @Override
                     public void onBitmapLoaded (Bitmap bitmap, Picasso.LoadedFrom from){
                         bitmaps[0] =bitmap;
                     }
                     @Override
                     public void onBitmapFailed(Drawable errorDrawable) {
                     }
                     @Override
                     public void onPrepareLoad(Drawable placeHolderDrawable) {

                     }
                 });


         Target target = new Target() {
             @Override
             public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
             }

             @Override
             public void onBitmapFailed(Drawable errorDrawable) {
             }

             @Override
             public void onPrepareLoad(Drawable placeHolderDrawable) {
             }
         };
         return bitmaps[0];
     }*/
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                RemoveNotePresenter removeNotePresenter=new RemoveNotePresenter(getApplicationContext());
                String typeOfNotes=mTextView_Title.getText().toString();
                Log.i(TAG, "onSwiped: "+typeOfNotes);
                if(typeOfNotes.equals(Constants.NotesType.ALL_NOTES)){
                    Log.i(TAG, "onSwiped: ");
                    if (direction == ItemTouchHelper.LEFT){
                        removeNotePresenter.removeFirebaseData(toDoItemModels,mUserUID,position);
                    } else {
                        getArchive(itemAdapter,position,toDoItemModels.get(position));
                    }
                }else if (typeOfNotes.equals(Constants.NotesType.REMINDER_NOTES)){
                    if (direction == ItemTouchHelper.LEFT){
                        removeNotePresenter.removeFirebaseData(mRemindrsToDO,mUserUID,position);
                    } else {
                        getArchive(mReminderAdapter,position,mRemindrsToDO.get(position));
                    }
                }else {
                    if (direction == ItemTouchHelper.LEFT){
                        removeNotePresenter.removeFirebaseData(mArchivedNotes,mUserUID,position);
                    } else {
                        getArchive(mArchivedAdapter,position,mArchivedNotes.get(position));
                    }
                }


            }

            public  void getArchive(final ItemAdapter archiveitemAdapter, final int position, final ToDoItemModel toDoItemModel){
                final UpdateNotePresenter updateNotePresenter=new UpdateNotePresenter(getApplicationContext(),this);
                final String date= toDoItemModel.get_startdate();
                archiveitemAdapter.removeItem(position);
                updateNotePresenter.getAchiveNote(mUserUID,date, toDoItemModel);
                Snackbar snackbar = Snackbar
                        .make(getCurrentFocus(), "Message is deleted", Snackbar.LENGTH_LONG)
                        .setAction("UNDO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateNotePresenter.getUndoAchiveNote(mUserUID,date,toDoItemModel);
                                toDoItemModel.set_Archive("false");
                                archiveitemAdapter.reduNote(toDoItemModel,position);
                            }
                        });
                snackbar.setDuration(5000);
                snackbar.show();
            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);
    }

    public  void setNavigationProfile(){
        String  getemail,getName,image_Url="";
        if(pref.contains(Constants.BundleKey.USER_EMAIL))
        {
            getemail =pref.getString(Constants.BundleKey.USER_EMAIL, "abcd@gmail.com");
            getName=pref.getString(ProfileeKey.FIRST_NAME, "Gokul")+" "+pref.getString(ProfileeKey.LAST_NAME,"Sonawane");
            mEmail_id=getemail;
            Log.i(TAG, "onCreate:  email"+getemail);

            Connection con=new Connection(getApplicationContext());
            if(con.isNetworkConnected()) {
                if (pref.contains(Constants.BundleKey.PROFILE_PIC) && !pref.getString(Constants.BundleKey.PROFILE_PIC, "null").equals("null")) {
                      /* try {
                            URL url = new URL(pref.getString(Constants.BundleKey.PROFILE_PIC, "null"));
                           //Bitmap resized = Bitmap.createScaledBitmap(getBitmap(url), 100, 100, true);
                           Bitmap conv_bm = getRoundedRectBitmap(getBitmap(url), 1000);
                           mImageView_ProfileImage.setImageBitmap(conv_bm);
                          } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                         e.printStackTrace();
                       }*/
                } else if (pref.contains(Constants.BundleKey.USER_PROFILE_SERVER)) {
                    DownloadImage.downloadImage(pref.getString(Constants.BundleKey.USER_PROFILE_SERVER, "myProfiles/sample.jpg"), new DownloadImageInterface() {
                        @Override
                        public void getImage(Bitmap bitmap) {
                            mImageView_ProfileImage.setImageBitmap(bitmap);
                            Bitmap resized = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                            Bitmap conv_bm = getRoundedRectBitmap(resized, 1000);
                            mImageView_ProfileImage.setImageBitmap(conv_bm);
                        }
                    });
                }
            }else
            {
                if(pref.contains(Constants.BundleKey.USER_PROFILE_LOCAL)){
                    mPrfilefilePath = Uri.parse(pref.getString(Constants.BundleKey.USER_PROFILE_LOCAL,"null"));
                    if(!image_Url.equals("null")){
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),mPrfilefilePath);
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
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        setVisibilityTollBar(false);
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            mToDoRecyclerView.setAdapter(itemAdapter);
            isReminderAdapter=false;
            isArchivedAdapter=false;
            mTextView_Title.setText(Constants.NotesType.ALL_NOTES);
            // Handle the camera action
        } else if (id == R.id.nav_reminders) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT);
            String date= df.format(c.getTime());
            mRemindrsToDO =getTodaysReminder(date);
            mReminderAdapter =new ItemAdapter(ToDoActivity.this, mRemindrsToDO) ;
            mToDoRecyclerView.setAdapter(mReminderAdapter);
            isReminderAdapter=true;
            isArchivedAdapter=false;
            mTextView_Title.setText(Constants.NotesType.REMINDER_NOTES);

        } else if (id == R.id.nav_create_new_label) {

        } else if (id == R.id.nav_archive) {
            mArchivedNotes =getArchivedToDos();
            mArchivedAdapter =new ItemAdapter(ToDoActivity.this, mArchivedNotes) ;
            mToDoRecyclerView.setAdapter(mArchivedAdapter);
            isArchivedAdapter=true;
            isReminderAdapter=false;
            mTextView_Title.setText(Constants.NotesType.ARCHIVE_NOTES);
        } else if (id == R.id.nav_deleted) {

        }else if(id==R.id.nav_logout){
            if( pref.contains(Constants.BundleKey.USER_REGISTER)) {

                if (pref.contains(Constants.BundleKey.FACEBOOK_LOGIN)&&pref.getString(Constants.BundleKey.FACEBOOK_LOGIN, "false").equals("true")) {
                    LoginManager.getInstance().logOut();
                    editor.putString(Constants.BundleKey.FACEBOOK_LOGIN, "false");
                }
                else if (pref.contains(Constants.BundleKey.GOOGLE_LOGIN)&&pref.getString(Constants.BundleKey.GOOGLE_LOGIN, "false").equals("true")) {
                    editor.putString(Constants.BundleKey.GOOGLE_LOGIN, "false");
                }
                editor.putString(Constants.BundleKey.USER_REGISTER, "false");
                editor.putString(Constants.BundleKey.PROFILE_PIC, "null");
                editor.putString(Constants.BundleKey.USER_EMAIL, "null");
                editor.putString(Constants.BundleKey.USER_USER_UID, "null");
                editor.putString(Constants.BundleKey.USER_NAME, "null");
                editor.putString(ProfileeKey.FIRST_NAME, "null");
                editor.putString(ProfileeKey.LAST_NAME, "null");
                editor.commit();
                Intent intent = new Intent(ToDoActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView_grid_linear:
                //Convert Grid view to linear
                if(!mLinear){
                    mLinear=true;
                    mToDoRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    mToDoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
                }
                else{
                    mLinear=false;
                    mToDoRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mToDoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
                }
                Toast.makeText(this, "Convert view ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab:
                Intent intent =new Intent(ToDoActivity.this,NewNoteActivity.class);
                intent.putExtra(Constants.BundleKey.USER_USER_UID,mUserUID);
                startActivityForResult(intent,2);
                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                break;
            case R.id.imageView_nav_profile:
                Intent picker = new Intent();
                picker.setType("image/*");
                picker.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(picker, "Select Picture"), SELECT_PHOTO);
                break;
            case R.id.imageView_search_bar:
                setVisibilityTollBar(true);
                break;
            default:
                //Default
                break;
        }
    }



    public  void setVisibilityTollBar(boolean flag){
        if(flag){
            mImageViewsearch.setVisibility(View.GONE);
            mTextView_Title.setVisibility(View.GONE);
            mEditText_Search.setVisibility(View.VISIBLE);
        }
        else{
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
        mProgressDialog.showProgress("Please Wait while loading data");
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModelas) {
        this.toDoAllItemModels =toDoItemModelas;
        Log.i(TAG, "showDataInActivity: ");
        if(toDoAllItemModels.size()!=0){
            Log.i(TAG, "showDataInActivity: ");
            toDoItemModels=getAllToDo();
            if(toDoItemModels.size()!=0){
                itemAdapter =new ItemAdapter(ToDoActivity.this,toDoItemModels) ;
                mToDoRecyclerView.setAdapter(itemAdapter);
            }
        }else {
            Toast.makeText(this, "No data Present", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getResponce(boolean flag) {

        if(flag){
            Toast.makeText(getApplicationContext(), "succcess", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if(requestCode==2){
                Bundle ban=data.getBundleExtra(Constants.BundleKey.MEW_NOTE);
                ToDoItemModel toDoItemModel=new ToDoItemModel();
            toDoItemModel.set_id(Integer.parseInt(ban.getString(Constants.RequestParam.KEY_ID)));
            toDoItemModel.set_title(ban.getString(Constants.RequestParam.KEY_TITLE));
            toDoItemModel.set_note(ban.getString(Constants.RequestParam.KEY_NOTE));
            toDoItemModel.set_reminder(ban.getString(Constants.RequestParam.KEY_REMINDER));
            toDoItemModel.set_startdate(ban.getString(Constants.RequestParam.KEY_STARTDATE));
            toDoAllItemModels.add(toDoItemModel);
            NoteAdapter itemAdapter=new NoteAdapter(ToDoActivity.this, toDoAllItemModels);
            Log.i(TAG, "onActivityResult: "+toDoItemModel.get_title());
            mToDoRecyclerView.setAdapter(itemAdapter);

        }*/

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                // Get the url from data
                mPrfilefilePath = data.getData();
                if (null != mPrfilefilePath) {
                    Log.i(TAG, "onActivityResult: "+ mPrfilefilePath);
                    Bitmap bitmap = null;
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),mPrfilefilePath);
                        Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
                        Bitmap conv_bm = getRoundedRectBitmap(scaled, 3000);
                        mImageView_ProfileImage.setImageBitmap(conv_bm);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.putString(Constants.BundleKey.USER_PROFILE_LOCAL, String.valueOf(mPrfilefilePath));
                    editor.putString(Constants.BundleKey.USER_PROFILE_SERVER, String.valueOf("myProfiles/"+mEmail_id.substring(0,mEmail_id.indexOf("@"))+".jpg"));
                    editor.commit();
                    uploadFile();
                }
            }
        }

    }

    private void uploadFile(){
        //if there is a file to upload
        if (mPrfilefilePath != null) {
            //displaying a progress dialog while upload is going on
            mProgressDialog.showProgress("Uploading");
            StorageReference riversRef = FirebaseStorage.getInstance().getReference();
            StorageReference ref=riversRef.child("myProfiles/"+mEmail_id.substring(0,mEmail_id.indexOf("@"))+".jpg");
            ref.putFile(mPrfilefilePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            mProgressDialog.dismissProgress();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            mProgressDialog.dismissProgress();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            //displaying percentage in progress dialog
                            mProgressDialog.showProgress("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            Log.i(TAG, "uploadFile: ");  //you can display an error toast
        }
    }


    public void addTextListener(){

        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if(isReminderAdapter&&mReminderAdapter!=null){
                    Filter filter = mReminderAdapter.getFilter();
                    filter.filter(query);
                }else if(isArchivedAdapter&&mArchivedAdapter!=null){
                    Filter filter = mArchivedAdapter.getFilter();
                    filter.filter(query);
                }else {
                    if(itemAdapter!=null){
                        Filter filter = itemAdapter.getFilter();
                        filter.filter(query);
                    }
                }


            }
        });
    }


    //get Reminders todo
    public  List<ToDoItemModel> getTodaysReminder(String date){
        List<ToDoItemModel> tempToDoModels=new ArrayList<>();
        if(toDoAllItemModels!=null) {
            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if (todoItem.get_reminder().equals(date) && todoItem.get_Archive().equals("false")) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get All notes
    public  List<ToDoItemModel> getAllToDo(){
        List<ToDoItemModel> tempToDoModels=new ArrayList<>();
        if(toDoAllItemModels!=null){
            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if(todoItem.get_Archive().equals("false")){
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get Archived todo notes
    public  List<ToDoItemModel> getArchivedToDos(){
        List<ToDoItemModel> tempToDoModels=new ArrayList<>();
        if(toDoAllItemModels!=null) {

            for (ToDoItemModel todoItem : toDoAllItemModels) {
                if (todoItem.get_Archive().equals("true")) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //hide keyboard
    void hideKeyboard(){

        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
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

}
