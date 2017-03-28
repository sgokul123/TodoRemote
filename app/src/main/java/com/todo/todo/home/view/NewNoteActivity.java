package com.todo.todo.home.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoPresenter;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class NewNoteActivity extends BaseActivity implements View.OnClickListener,NoteInterface{
    private  String TAG="NewNoteActivity";
    AppCompatImageView imageViewBack,imageViewPin,imageViewReminder,imageViewSave;
    AppCompatTextView textViewedited,textViewReminder;
    AppCompatEditText editTextNote,editTextTitle;
    private ToDoPresenter toDoPresenter;
    ProgressUtil progressDialog;
    Calendar myCalendar;
    ToDoItemModel mToDoItemModel;
    private  String StrTitle,StrReminder,StrNote;
    private  DatePickerDialog.OnDateSetListener date;
    private  String mUsre_UID;



    @Override
    public void initialise() {
        setContentView(R.layout.activity_new_note);
        imageViewBack=(AppCompatImageView) findViewById(R.id.imageView_back_arrow);
        imageViewPin=(AppCompatImageView) findViewById(R.id.imageView_pin);
        imageViewReminder=(AppCompatImageView) findViewById(R.id.imageView_reminder);
        imageViewSave=(AppCompatImageView) findViewById(R.id.imageView_save);
        textViewReminder=(AppCompatTextView) findViewById(R.id.textview_reminder_text);
        editTextTitle=(AppCompatEditText) findViewById(R.id.edittext_title);
        editTextNote=(AppCompatEditText) findViewById(R.id.edittet_note);
        progressDialog=new ProgressUtil(this);

        imageViewBack.setOnClickListener(this);
        imageViewPin.setOnClickListener(this);
        imageViewReminder.setOnClickListener(this);
        imageViewSave.setOnClickListener(this);
        Log.i(TAG, "initialise: "+getCurrentDate());
        mUsre_UID=getIntent().getStringExtra(Constants.BundleKey.USER_USER_UID);
        Log.i(TAG, "initialise: "+mUsre_UID);
        myCalender();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialise();
    }

    public  void myCalender(){
        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView_back_arrow:
                finish();
                break;
            case R.id.imageView_pin:

                break;
            case R.id.imageView_reminder:

                new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.imageView_save:
                mToDoItemModel =new ToDoItemModel();
                StrNote=editTextNote.getText().toString();
                mToDoItemModel.set_note(StrNote);
                StrTitle=editTextTitle.getText().toString();
                mToDoItemModel.set_title(StrTitle);
                StrReminder=textViewReminder.getText().toString();
                mToDoItemModel.set_reminder(StrReminder);
                toDoPresenter=new ToDoPresenter(NewNoteActivity.this,this);
                Log.i(TAG, "onClick: "+mUsre_UID+"  date"+getCurrentDate());
                toDoPresenter.loadNotetoFirebase(mUsre_UID,getCurrentDate(),getTimestamp(),mToDoItemModel);
                break;
            default:

                break;

        }

    }

    private void updateLabel() {

        String myFormat = "EEE, MMM d, ''yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textViewReminder.setText(sdf.format(myCalendar.getTime()));

    }

    @Override
    public void closeNoteProgressDialog() {

        progressDialog.dismissProgress();
    }

    @Override
    public void showNoteProgressDialog() {

        progressDialog.showProgress("Adding new Note...");


    }

    @Override
    public void getResponce(boolean flag) {
        if(flag){
            Toast.makeText(this, "succcess", Toast.LENGTH_SHORT).show();
            Bundle bun=new Bundle();
            bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mToDoItemModel.get_id()));
            bun.putString(Constants.RequestParam.KEY_NOTE,mToDoItemModel.get_note());
            bun.putString(Constants.RequestParam.KEY_TITLE,mToDoItemModel.get_title());
            bun.putString(Constants.RequestParam.KEY_REMINDER,mToDoItemModel.get_reminder());
            Intent intent=new Intent();
            intent.putExtra(Constants.BundleKey.MEW_NOTE,bun);
            setResult(2,intent);
            finish();
        }
        else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }

    }

        public  String  getCurrentDate(){
            String date="";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("EEE,MMMd,yy");
            date= df.format(c.getTime());
            date=date.trim();
            return date;
        }

        public  String getTimestamp(){
            String timestamp="";
             try {
                int day, second, minute, hour;
                GregorianCalendar calendar = new GregorianCalendar();
                 day = calendar.get(Calendar.DAY_OF_MONTH);
                second = calendar.get(Calendar.SECOND);
                minute = calendar.get(Calendar.MINUTE);
                hour = calendar.get(Calendar.HOUR);

                 timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
                        .parse(calendar.get(Calendar.MONTH)+"/"+day+"/"+calendar.get(Calendar.YEAR)+" "+hour+":"+minute+":"+second)
                        .getTime()+"";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return timestamp;
        }
}
