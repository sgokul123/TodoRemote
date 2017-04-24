package com.todo.todo.addnote.view;

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
import com.todo.todo.addnote.presenter.AddNotePresenter;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewNoteActivity extends BaseActivity implements View.OnClickListener, NoteInterface {

    AppCompatImageView mImageViewBack, mImageViewPin, mImageViewReminder, mImageViewSave;
    AppCompatTextView mTextViewReminder, mTextViewEditedAt;
    AppCompatEditText mEditTextNote, mEditTextTitle;
    ProgressUtil progressDialog;
    Calendar myCalendar;
    ToDoItemModel mToDoItemModel;
    String formattedDate;

    private String TAG = "NewNoteActivity";
    private AddNotePresenter mAddNotePresenter;
    private String StrTitle, StrReminder, StrNote;
    private DatePickerDialog.OnDateSetListener date;
    private String mUsre_UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_new_note);
        mImageViewBack = (AppCompatImageView) findViewById(R.id.imageView_back_arrow);
        mImageViewPin = (AppCompatImageView) findViewById(R.id.imageView_pin);
        mImageViewReminder = (AppCompatImageView) findViewById(R.id.imageView_reminder);
        mImageViewSave = (AppCompatImageView) findViewById(R.id.imageView_save);
        mTextViewReminder = (AppCompatTextView) findViewById(R.id.textview_reminder_text);
        mEditTextTitle = (AppCompatEditText) findViewById(R.id.edittext_title);
        mEditTextNote = (AppCompatEditText) findViewById(R.id.edittet_note);
        mTextViewEditedAt = (AppCompatTextView) findViewById(R.id.textview_editedat_at);
        progressDialog = new ProgressUtil(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        formattedDate = df.format(c.getTime());
        mTextViewEditedAt.setText(formattedDate);
        Log.i(TAG, "initView: " + getCurrentDate());
        mUsre_UID = getIntent().getStringExtra(Constants.BundleKey.USER_USER_UID);
        Log.i(TAG, "initView: " + mUsre_UID);
        reminderPicker();
        setOnClickListener();
    }

    @Override
    public void setOnClickListener() {

        mImageViewBack.setOnClickListener(this);
        mImageViewPin.setOnClickListener(this);
        mImageViewReminder.setOnClickListener(this);
        mImageViewSave.setOnClickListener(this);
    }

    public void reminderPicker() {
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

        switch (v.getId()) {
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

                mToDoItemModel = new ToDoItemModel();
                StrNote = mEditTextNote.getText().toString();
                mToDoItemModel.setNote(StrNote);
                StrTitle = mEditTextTitle.getText().toString();
                mToDoItemModel.setTitle(StrTitle);
                StrReminder = mTextViewReminder.getText().toString();
                mToDoItemModel.setReminder(StrReminder);
                mToDoItemModel.setSettime(mTextViewEditedAt.getText().toString());
                mToDoItemModel.setStartdate(getCurrentDate());
                mToDoItemModel.setArchive("false");
                mAddNotePresenter = new AddNotePresenter(this, this);

                Log.i(TAG, "onClick: " + mUsre_UID + "  date" + getCurrentDate());
                mAddNotePresenter.loadNotetoFirebase(mUsre_UID, getCurrentDate(), mToDoItemModel);

                break;
            default:

                break;

        }

    }

    private void updateLabel() {

        String myFormat = Constants.NotesType.DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mTextViewReminder.setText(sdf.format(myCalendar.getTime()));

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
        if (flag) {

            Toast.makeText(this, "succcess", Toast.LENGTH_SHORT).show();
            Bundle bun = new Bundle();
            bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mToDoItemModel.getId()));
            bun.putString(Constants.RequestParam.KEY_NOTE, mToDoItemModel.getNote());
            bun.putString(Constants.RequestParam.KEY_TITLE, mToDoItemModel.getTitle());
            bun.putString(Constants.RequestParam.KEY_REMINDER, mToDoItemModel.getReminder());
            bun.putString(Constants.RequestParam.KEY_STARTDATE, mToDoItemModel.getStartdate());
            Intent intent = new Intent();
            intent.putExtra(Constants.BundleKey.MEW_NOTE, bun);
            setResult(2, intent);
            finish();
        } else {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
    }

    public String getCurrentDate() {
        String date = "";
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT);
        date = df.format(c.getTime());
        date = date.trim();
        return date;
    }

}
