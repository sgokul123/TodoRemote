package com.todo.todo.home.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.todo.todo.R;
import com.todo.todo.home.adapter.DataAdapter;
import com.todo.todo.home.model.ToDoModel;
import com.todo.todo.home.presenter.ToDoPresenter;


import java.util.List;

public class ToDoActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, View.OnClickListener ,ToDoActivityInteface {
    private  String TAG ="ToDoActivity";
    ToDoPresenter toDoPresenter;
    ProgressDialog mProgressDialog;
    Toolbar toolbar;
    GridView mGridview;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        floatingActionButton=(FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toDoPresenter =new ToDoPresenter(ToDoActivity.this,getApplicationContext());
       // toDoPresenter.getPresenterNotes();

        mGridview = (GridView) findViewById(R.id.gridview_notes);
     //   mGridview.setAdapter(new DataAdapter(ToDoActivity.this,toDoModels));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        //getSupportFragmentManager().beginTransaction().replace(R.id.layout_todo_main,NewNoteFragment.newInstance()).addToBackStack(null).commit();
        Intent intent =new Intent(ToDoActivity.this,NewNote.class);
        startActivity(intent);
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
        mGridview.setAdapter(new DataAdapter(ToDoActivity.this,toDoModels));
       mGridview.setOnItemClickListener(this);
    }

}
