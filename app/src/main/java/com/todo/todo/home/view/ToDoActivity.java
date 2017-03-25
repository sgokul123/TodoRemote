package com.todo.todo.home.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.home.adapter.CustomGrid;
import com.todo.todo.home.adapter.DataAdapter;
import com.todo.todo.home.model.ToDoModel;
import com.todo.todo.home.presenter.ToDoPresenter;
import com.todo.todo.login.view.LoginActivity;


import java.util.List;

public class ToDoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener ,ToDoActivityInteface {
    private  String TAG ="ToDoActivity";
    ToDoPresenter mTtoDoPresenter;
    ProgressDialog mProgressDialog;
    AppCompatTextView mTextViewEmail,mTextView_Name;
    Toolbar mToolbar;
    GridView mGridview;
    AppCompatImageView mImageViewsearch, mImageViewGrid;
    FloatingActionButton mFloatingActionButton;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private  String mEmail_id;
    private  boolean mLinear=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mFloatingActionButton =(FloatingActionButton) findViewById(R.id.fab);
        mImageViewsearch =(AppCompatImageView) findViewById(R.id.imageView_search);
        mImageViewGrid =(AppCompatImageView) findViewById(R.id.imageView_grid_linear);

        mGridview = (GridView) findViewById(R.id.gridview_notes);

        mFloatingActionButton.setOnClickListener(this);
        mImageViewsearch.setOnClickListener(this);
        mImageViewGrid.setOnClickListener(this);

        pref = getSharedPreferences("testapp", MODE_PRIVATE);
        editor = pref.edit();



        setSupportActionBar(mToolbar);
        mTtoDoPresenter =new ToDoPresenter(ToDoActivity.this,this);
        mTtoDoPresenter.getPresenterNotes();

        //   mGridview.setAdapter(new DataAdapter(ToDoActivity.this,toDoModels));
        mGridview.setOnItemClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        mTextViewEmail=(AppCompatTextView) header.findViewById(R.id.textView_nav_email);
        mTextView_Name=(AppCompatTextView)header. findViewById(R.id.textview_nave_name);
        if(pref.contains("email"))
        {
            String  getemail=pref.getString("user_id", "abcd@gmail.com");
            String  getName=pref.getString("name", "xyz");
            Log.i(TAG, "onCreate:  email"+getemail);
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {

            // Handle the camera action
        } else if (id == R.id.nav_reminders) {


        } else if (id == R.id.nav_create_new_label) {


        } else if (id == R.id.nav_archive) {


        } else if (id == R.id.nav_deleted) {


        }else if(id==R.id.nav_logout){

            editor.putString("register","false");
                    editor.commit();
            Intent intent =new Intent(ToDoActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView_search:
                //Search Note
                Toast.makeText(this, "Search View", Toast.LENGTH_SHORT).show();
                break;
            case R.id.imageView_grid_linear:
                //Convert Grid view to linear
                if(!mLinear){
                    mLinear=true;
                    mGridview.setNumColumns(1);
                    mImageViewGrid.setImageResource(R.drawable.ic_grid);
                }
                else{
                    mLinear=false;
                    mGridview.setNumColumns(2);
                    mImageViewGrid.setImageResource(R.drawable.ic_list);
                }
                Toast.makeText(this, "Convert view ", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fab:

                /*getSupportFragmentManager().beginTransaction().replace(R.id.layout_todo_main,NewNoteFragment.newInstance()).addToBackStack(null).commit();
                mToolbar.setVisibility(View.INVISIBLE);
                getSupportActionBar().hide();
                mFloatingActionButton.setVisibility(View.INVISIBLE);*/
                Intent intent =new Intent(ToDoActivity.this,NewNote.class);
                startActivity(intent);
                break;
            default:
                //Default


                break;

        }

    }

    @Override
    public void closeProgressDialog() {
        mProgressDialog.dismiss();
    }

    @Override
    public void showProgressDialog() {
        mProgressDialog.setMessage("Please Wait while loading data");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    public void showDataInActivity(List<ToDoModel> toDoModels) {
        Log.i(TAG, "showDataInActivity: "+toDoModels.get(1).get_reminder());
        DataAdapter adapter=new DataAdapter(ToDoActivity.this,toDoModels);
        mGridview.setAdapter(adapter);

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

    }

}
