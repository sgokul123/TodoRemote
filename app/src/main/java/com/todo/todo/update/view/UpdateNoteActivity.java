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
import com.todo.todo.home.view.ToDoActivityInteface;
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateNoteActivity extends BaseActivity implements View.OnClickListener, ToDoActivityInteface {
    AppCompatImageView imageViewBack, imageViewPin, imageViewReminder, imageViewSave;
    AppCompatTextView textViewReminder;
    AppCompatEditText editTextNote, editTextTitle;
    UpdateNotePresenter updateNotePresenter;
    ProgressUtil progressDialog;
    Calendar remiderPick;
    ToDoItemModel mToDoItemModel;
    private String TAG = "NewNoteActivity";
    private String StrTitle, StrReminder, StrNote, StrStartDate, StrSetTime;
    private DatePickerDialog.OnDateSetListener date;
    private String mUsre_UID, Note_id, mIsArchive;
    private AppCompatTextView mTextViewEditedAt;
    private String mNote_Order_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        updateNotePresenter = new UpdateNotePresenter(UpdateNoteActivity.this, this);

        initView();
    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_new_note);
        imageViewBack = (AppCompatImageView) findViewById(R.id.imageView_back_arrow);
        imageViewPin = (AppCompatImageView) findViewById(R.id.imageView_pin);
        imageViewReminder = (AppCompatImageView) findViewById(R.id.imageView_reminder);
        imageViewSave = (AppCompatImageView) findViewById(R.id.imageView_save);
        textViewReminder = (AppCompatTextView) findViewById(R.id.textview_reminder_text);
        editTextTitle = (AppCompatEditText) findViewById(R.id.edittext_title);
        editTextNote = (AppCompatEditText) findViewById(R.id.edittet_note);
        mTextViewEditedAt = (AppCompatTextView) findViewById(R.id.textview_editedat_at);
        progressDialog = new ProgressUtil(this);

        mUsre_UID = getIntent().getStringExtra(Constants.BundleKey.USER_USER_UID);
        Bundle ban = getIntent().getBundleExtra(Constants.BundleKey.MEW_NOTE);
        setData(ban);
        Log.i(TAG, "initView: " + mUsre_UID);
        reminderPicker();
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
        StrStartDate = ban.getString(Constants.RequestParam.KEY_STARTDATE);
        Note_id = ban.getString(Constants.RequestParam.KEY_ID);
        mIsArchive = ban.getString(Constants.RequestParam.KEY_ARCHIVE);
        StrSetTime = ban.getString(Constants.RequestParam.KEY_SETTIME);

        if (mIsArchive.equals(R.string.flag_true)) {
        }
    }

    public void reminderPicker() {


        remiderPick = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                remiderPick.set(Calendar.YEAR, year);
                remiderPick.set(Calendar.MONTH, monthOfYear);
                remiderPick.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
                new DatePickerDialog(this, date, remiderPick
                        .get(Calendar.YEAR), remiderPick.get(Calendar.MONTH),
                        remiderPick.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.imageView_save:
                mToDoItemModel = new ToDoItemModel();
                StrNote = editTextNote.getText().toString();
                mToDoItemModel.setNote(StrNote);
                StrTitle = editTextTitle.getText().toString();
                mToDoItemModel.setTitle(StrTitle);
                StrReminder = textViewReminder.getText().toString();
                mToDoItemModel.setReminder(StrReminder);
                mToDoItemModel.setStartdate(StrStartDate);
                mToDoItemModel.setId(Integer.parseInt(Note_id));
                mToDoItemModel.setArchive(mIsArchive);
                mToDoItemModel.setSettime(StrSetTime);
                updateNotePresenter.updateNote(mUsre_UID, StrStartDate, mToDoItemModel);
                break;
        }

    }

    private void updateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT, Locale.US);
        textViewReminder.setText(sdf.format(remiderPick.getTime()));

    }


    @Override
    public void closeProgressDialog() {
        progressDialog.dismissProgress();
    }

    @Override
    public void showProgressDialog() {
        progressDialog.showProgress(getString(R.string.loading));
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {

    }

    @Override
    public void getResponce(boolean flag) {
        if (flag) {
            Toast.makeText(this, getString(R.string.updated), Toast.LENGTH_SHORT).show();
            Bundle bun = new Bundle();
            bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mToDoItemModel.getId()));
            bun.putString(Constants.RequestParam.KEY_NOTE, mToDoItemModel.getNote());
            bun.putString(Constants.RequestParam.KEY_TITLE, mToDoItemModel.getTitle());
            bun.putString(Constants.RequestParam.KEY_REMINDER, mToDoItemModel.getReminder());
            bun.putString(Constants.RequestParam.KEY_SETTIME, mToDoItemModel.getSettime());
            Intent intent = new Intent();
            intent.putExtra(Constants.BundleKey.MEW_NOTE, bun);
            setResult(2, intent);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.fail_update), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void getUndoArchivedNote(int position) {

    }

}
