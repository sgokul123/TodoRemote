package com.todo.todo.home.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.GridView;
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
import com.todo.todo.home.adapter.CustomGrid;
import com.todo.todo.home.listener.RecyclerItemClickListener;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.RemoveNotePresenter;
import com.todo.todo.home.presenter.ToDoPresenter;
import com.todo.todo.login.view.LoginActivity;
import com.todo.todo.update.view.UpdateNoteActivity;
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;
import com.todo.todo.util.Constants.ProfileeKey;
import com.todo.todo.util.DownloadImage;
import com.todo.todo.util.DownloadImageInterface;
import com.todo.todo.util.ProgressUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;

public class ToDoActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener ,ToDoActivityInteface {

    private final int SELECT_PHOTO = 3;
    ToDoPresenter mTtoDoPresenter;
    ProgressUtil mProgressDialog;
    AppCompatTextView mTextViewEmail,mTextView_Name,mTextViewTitle;
    AppCompatEditText mEditTextSearch;
    Toolbar mToolbar;
    RecyclerView mRecyclerView;
    AppCompatImageView  mImageViewGrid,mImageViewNavProfile;//mImageViewsearch
    FloatingActionButton mFloatingActionButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private Paint p = new Paint();
    Uri mPrfilefilePath;

    CustomGrid adapter;
    List<ToDoItemModel> toDoItemModels;
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
        mFloatingActionButton =(FloatingActionButton) findViewById(R.id.fab);
        mImageViewGrid =(AppCompatImageView) findViewById(R.id.imageView_grid_linear);
        mRecyclerView = (RecyclerView) findViewById(R.id.gridview_notes);
        mTextViewTitle=(AppCompatTextView) findViewById(R.id.textview_title_toolbar);
        mEditTextSearch=(AppCompatEditText) findViewById(R.id.edittext_title_toolbar);

        mFloatingActionButton.setOnClickListener(this);
        mImageViewGrid.setOnClickListener(this);
        mProgressDialog=new ProgressUtil(this);

        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();
        setSupportActionBar(mToolbar);


        mUserUID=pref.getString(Constants.BundleKey.USER_USER_UID,"null");
        mTtoDoPresenter =new ToDoPresenter(ToDoActivity.this,this);
        mTtoDoPresenter.getPresenterNotes(mUserUID);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {


                        Intent intent =new Intent(ToDoActivity.this,UpdateNoteActivity.class);
                        Bundle bun=new Bundle();
                        bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(toDoItemModels.get(position).get_id()));
                        bun.putString(Constants.RequestParam.KEY_NOTE,toDoItemModels.get(position).get_note());
                        bun.putString(Constants.RequestParam.KEY_TITLE,toDoItemModels.get(position).get_title());
                        bun.putString(Constants.RequestParam.KEY_REMINDER,toDoItemModels.get(position).get_reminder());
                        bun.putString(Constants.RequestParam.KEY_STARTDATE,toDoItemModels.get(position).get_startdate());
                        intent.putExtra(Constants.BundleKey.USER_USER_UID,mUserUID);
                        intent.putExtra(Constants.BundleKey.MEW_NOTE,bun);
                        startActivityForResult(intent,2);
                        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
                      //  Toast.makeText(this, "selected"+position, Toast.LENGTH_SHORT).show();
                        // TODO Handle item click
                    }
                }));
        FacebookSdk.sdkInitialize(getApplicationContext());
       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        mImageViewNavProfile=(AppCompatImageView) header.findViewById(R.id.imageView_nav_profile);
        mTextViewEmail=(AppCompatTextView) header.findViewById(R.id.textView_nav_email);
        mTextView_Name=(AppCompatTextView)header. findViewById(R.id.textview_nave_name);
        mImageViewNavProfile.setOnClickListener(this);
        setNavigationProfile();
        addTextListener();
        initSwipe();
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                RemoveNotePresenter removeNotePresenter=new RemoveNotePresenter(getApplicationContext());
                if (direction == ItemTouchHelper.LEFT){
                    removeNotePresenter.removeFirebaseData(toDoItemModels,mUserUID,position);

                } else {
                   /* removeNotePresenter.getArchiveData(toDoItemModels.get(position),mUserUID,position);
                    adapter.removeItem(position);
                  */  //removeView();
                     }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                          // icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                      //   c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                       //   icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_archive );
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                      //  c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }
    public  void setNavigationProfile(){
        String  getemail,getName,image_Url="";
        if(pref.contains("email"))
        {
            getemail =pref.getString(Constants.BundleKey.USER_EMAIL, "abcd@gmail.com");
            getName=pref.getString(ProfileeKey.FIRST_NAME, "Gokul")+" "+pref.getString(ProfileeKey.LAST_NAME,"Sonawane");
            mEmail_id=getemail;

            Log.i(TAG, "onCreate:  email"+getemail);
            Connection con=new Connection(getApplicationContext());
            if(con.isNetworkConnected()){
                if(pref.contains(Constants.BundleKey.USER_PROFILE_SERVER))
                    DownloadImage.downloadImage(pref.getString(Constants.BundleKey.USER_PROFILE_SERVER, "myProfiles/sample.jpg"), new DownloadImageInterface() {
                        @Override
                        public void getImage(Bitmap bitmap) {
                            mImageViewNavProfile.setImageBitmap(bitmap);
                        }
                    });
            }else {
                if(pref.contains(Constants.BundleKey.USER_PROFILE_LOCAL)){

                    mPrfilefilePath = Uri.parse(pref.getString(Constants.BundleKey.USER_PROFILE_LOCAL,"null"));

                    if(!image_Url.equals("null")){
                        mImageViewNavProfile.setImageURI(mPrfilefilePath);
                    }
                }

            }
            mTextViewEmail.setText(getemail);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            mRecyclerView.setAdapter(adapter);

            // Handle the camera action
        } else if (id == R.id.nav_reminders) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("EEE,MMMd,yy");
           String date= df.format(c.getTime());
            List<ToDoItemModel> remindrsToDO =getTodaysReminder(date);
            CustomGrid  adapter2 =new CustomGrid(ToDoActivity.this,remindrsToDO) ;
            mRecyclerView.setAdapter(adapter2);

        } else if (id == R.id.nav_create_new_label) {



        } else if (id == R.id.nav_archive) {



        } else if (id == R.id.nav_deleted) {


        }else if(id==R.id.nav_logout){
           if( pref.contains(Constants.BundleKey.FACEBOOK_LOGIN)){
               if (pref.getString(Constants.BundleKey.FACEBOOK_LOGIN, "false").equals("true")) {
                   LoginManager.getInstance().logOut();
                   editor.putString(Constants.BundleKey.FACEBOOK_LOGIN,"false");
                   editor.commit();
                   Intent intent =new Intent(ToDoActivity.this, LoginActivity.class);
                   startActivity(intent);
               }else{
                   editor.putString("register","false");
                   editor.commit();
                   Intent intent =new Intent(ToDoActivity.this, LoginActivity.class);
                   startActivity(intent);
               }
           }else{
               editor.putString("register","false");
               editor.commit();
               Intent intent =new Intent(ToDoActivity.this, LoginActivity.class);
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
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mImageViewGrid.setImageResource(R.drawable.grid_view);
                }
                else{
                    mLinear=false;
                            mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    mImageViewGrid.setImageResource(R.drawable.list_view);
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
            default:
                //Default
                break;
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
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {
        this. toDoItemModels=toDoItemModels;

        if(toDoItemModels.size()!=0){

            adapter =new CustomGrid(ToDoActivity.this,toDoItemModels) ;

            mRecyclerView.setAdapter(adapter);


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
            toDoItemModels.add(toDoItemModel);
            NoteAdapter adapter=new NoteAdapter(ToDoActivity.this, toDoItemModels);
            Log.i(TAG, "onActivityResult: "+toDoItemModel.get_title());
            mRecyclerView.setAdapter(adapter);

        }*/

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PHOTO) {
                // Get the url from data
                mPrfilefilePath = data.getData();
                if (null != mPrfilefilePath) {
                    Log.i(TAG, "onActivityResult: "+ mPrfilefilePath);
                    mImageViewNavProfile.setImageURI(mPrfilefilePath);
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

        mEditTextSearch.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                Filter filter = adapter.getFilter();
                filter.filter(query);

            }
        });
    }


    private void onSwipeLeft() {
        Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
    }

    private void onSwipeRight() {
        Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
    }


    public  List<ToDoItemModel> getTodaysReminder(String date){
        List<ToDoItemModel> tempToDoModels=new ArrayList<>();
        for (ToDoItemModel todoItem :toDoItemModels) {
            if(todoItem.get_reminder().equals(date)){
                tempToDoModels.add(todoItem);
            }
        }

        return tempToDoModels;
    }

}
