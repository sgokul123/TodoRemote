package com.todo.todo.update.view;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bridgeit on 29/3/17.
 */

    public class  UpdateNoteActivity extends BaseActivity implements View.OnClickListener,UpdateNoteActivityInterface {
        private  String TAG="NewNoteActivity";
        AppCompatImageView imageViewBack,imageViewPin,imageViewReminder,imageViewSave;
        AppCompatTextView textViewedited,textViewReminder;
        AppCompatEditText editTextNote,editTextTitle;
        UpdateNotePresenter updateNotePresenter;
        ProgressUtil progressDialog;
        Calendar myCalendar;
        ToDoItemModel mToDoItemModel;
        private  String StrTitle,StrReminder,StrNote,StrStartDate,StrSetTime;
        private  DatePickerDialog.OnDateSetListener date;
        private  String mUsre_UID,Note_id,mIsArchive;
    private AppCompatTextView mTextViewEditedAt;
    private String formattedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initialise();
    }
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
            mTextViewEditedAt=(AppCompatTextView) findViewById(R.id.textview_editedat_at);
            progressDialog=new ProgressUtil(this);

            progressDialog=new ProgressUtil(this);

            mUsre_UID=getIntent().getStringExtra(Constants.BundleKey.USER_USER_UID);
            Bundle ban=getIntent().getBundleExtra(Constants.BundleKey.MEW_NOTE);
            setData(ban);
            Log.i(TAG, "initialise: "+mUsre_UID);
            myCalender();
            setOnClickListener();
        }

    @Override
    public void setOnClickListener() {

        imageViewBack.setOnClickListener(this);
        imageViewPin.setOnClickListener(this);
        imageViewReminder.setOnClickListener(this);
        imageViewSave.setOnClickListener(this);

    }

    private void setData(Bundle ban) {

        textViewReminder.setText(ban.getString(Constants.RequestParam.KEY_REMINDER));
        editTextTitle.setText(ban.getString(Constants.RequestParam.KEY_TITLE));
        editTextNote.setText(ban.getString(Constants.RequestParam.KEY_NOTE));
        mTextViewEditedAt.setText(ban.getString(Constants.RequestParam.KEY_SETTIME));
        StrStartDate=ban.getString(Constants.RequestParam.KEY_STARTDATE);
        Note_id=ban.getString(Constants.RequestParam.KEY_ID);
        mIsArchive=ban.getString(Constants.RequestParam.KEY_ARCHIVE);
        StrSetTime=ban.getString(Constants.RequestParam.KEY_SETTIME);

        if(mIsArchive.equals("true")){

        }
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
                    mToDoItemModel.set_startdate(StrStartDate);
                    mToDoItemModel.set_id(Integer.parseInt(Note_id));
                    mToDoItemModel.set_Archive(mIsArchive);
                    mToDoItemModel.set_Settime(StrSetTime);
                    updateNotePresenter=new UpdateNotePresenter(getApplicationContext(),this);
                    Log.i(TAG, "onClick: ");
                    updateNotePresenter.updateNote(mUsre_UID,StrStartDate,mToDoItemModel);
                    break;
                default:

                    break;

            }

        }

        private void updateLabel() {

            SimpleDateFormat sdf = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT, Locale.US);
            textViewReminder.setText(sdf.format(myCalendar.getTime()));

        }

    @Override
    public void closeProgress() {
        progressDialog.dismissProgress();
    }

    @Override
    public void showProgress() {
        progressDialog.showProgress("Please Wait while Updating...");
    }

    @Override
        public void getResponce(boolean flag) {
            if(flag){
                Toast.makeText(this, "succcessfully Updated...", Toast.LENGTH_SHORT).show();
                Bundle bun=new Bundle();
                bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mToDoItemModel.get_id()));
                bun.putString(Constants.RequestParam.KEY_NOTE,mToDoItemModel.get_note());
                bun.putString(Constants.RequestParam.KEY_TITLE,mToDoItemModel.get_title());
                bun.putString(Constants.RequestParam.KEY_REMINDER,mToDoItemModel.get_reminder());
                bun.putString(Constants.RequestParam.KEY_SETTIME,mToDoItemModel.get_Settime());
                Intent intent=new Intent();
                intent.putExtra(Constants.BundleKey.MEW_NOTE,bun);
                setResult(2,intent);
                finish();
            }
            else {
                Toast.makeText(this, "fail to Update...", Toast.LENGTH_SHORT).show();
            }

        }

    }
