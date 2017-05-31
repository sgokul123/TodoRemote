package com.todo.todo.addnote.view;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jrummyapps.android.colorpicker.ColorPickerDialog;
import com.jrummyapps.android.colorpicker.ColorPickerDialogListener;
import com.todo.todo.R;
import com.todo.todo.addnote.presenter.AddNotePresenter;
import com.todo.todo.base.BaseActivity;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.util.Constants;
import com.todo.todo.util.ProgressUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewNoteActivity extends BaseActivity implements View.OnClickListener, NoteInterface,ColorPickerDialogListener {

    final static int RQS_1 = 1;
    AppCompatImageView mImageViewBack, imageView_Color_Picker, mImageViewReminder, mImageViewSave;
    AppCompatTextView mTextViewReminder, mTextViewEditedAt;
    AppCompatEditText mEditTextNote, mEditTextTitle;
    ProgressUtil progressDialog;
    Calendar myCalendar;
    ToDoItemModel mToDoItemModel;
    String formattedDate;
    RelativeLayout relativeLayout;
    private String TAG = "NewNoteActivity";
    private AddNotePresenter mAddNotePresenter;
    private String StrTitle, StrReminder, StrNote;
    private DatePickerDialog.OnDateSetListener date;
    private String mUsre_UID;
    private int mNote_Order_Id;
    private String noteColor;
    private int DIALOG_ID=9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enterFromBottomAnimation();
        initView();

    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_new_note);
        mImageViewBack = (AppCompatImageView) findViewById(R.id.imageView_back_arrow);
        imageView_Color_Picker= (AppCompatImageView) findViewById(R.id.imageView_color_picker);
        mImageViewReminder = (AppCompatImageView) findViewById(R.id.imageView_reminder);
        mImageViewSave = (AppCompatImageView) findViewById(R.id.imageView_save);
        mTextViewReminder = (AppCompatTextView) findViewById(R.id.textview_reminder_text);
        mEditTextTitle = (AppCompatEditText) findViewById(R.id.edittext_title);
        mEditTextNote = (AppCompatEditText) findViewById(R.id.edittet_note);
        mTextViewEditedAt = (AppCompatTextView) findViewById(R.id.textview_editedat_at);
        relativeLayout= (RelativeLayout) findViewById(R.id.layout_add_new_card);

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
        imageView_Color_Picker.setOnClickListener(this);
        mImageViewReminder.setOnClickListener(this);
        mImageViewSave.setOnClickListener(this);
    }

    @Override
    public void enterFromBottomAnimation() {

    }

    @Override
    public void exitToBottomAnimation() {
        overridePendingTransition(R.anim.activity_open_translate_from_bottom, R.anim.activity_no_animation);
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
                if(!mEditTextTitle.getText().toString().isEmpty()){
                    saveNote();
                }
                finish();
                break;
            case R.id.imageView_color_picker:
                getColorPicker();
                break;
            case R.id.imageView_reminder:
                DatePickerDialog datePickerDialog=new DatePickerDialog(this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                break;
            case R.id.imageView_save:
                if(!mEditTextTitle.getText().toString().isEmpty()){
                    saveNote();
                }else{
                    finish();
                }
                break;
        }

    }

    private void saveNote() {
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
        mToDoItemModel.setColor(noteColor);
        mAddNotePresenter = new AddNotePresenter(this, this);
        Log.i(TAG, "onClick: " + mUsre_UID + "  date" + getCurrentDate());
        mAddNotePresenter.loadNotetoFirebase(mUsre_UID, getCurrentDate(), mToDoItemModel);
    }
    private void getColorPicker() {
        ColorPickerDialog.newBuilder()
                .setDialogType(ColorPickerDialog.TYPE_PRESETS)
                .setAllowPresets(true)
                .setDialogId(DIALOG_ID)
                .setColor(Color.BLACK)
                .setShowAlphaSlider(true)
                .show(this);
    }

 /*   private void setAlarm(String targetCal) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = dateFormat.parse("12:01:32");
        System.out.println(date.getTime());
        Intent intent = new Intent(getBaseContext(), MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }*/

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
          ///  Toast.makeText(this, "succcess", Toast.LENGTH_SHORT).show();
            Bundle bun = new Bundle();
            bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mToDoItemModel.getId()));
            bun.putString(Constants.RequestParam.KEY_NOTE, mToDoItemModel.getNote());
            bun.putString(Constants.RequestParam.KEY_TITLE, mToDoItemModel.getTitle());
            bun.putString(Constants.RequestParam.KEY_REMINDER, mToDoItemModel.getReminder());
            bun.putString(Constants.RequestParam.KEY_STARTDATE, mToDoItemModel.getStartdate());
            bun.putString(Constants.RequestParam.KEY_COLOR,noteColor);
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

    @Override
    public void onColorSelected(int dialogId, @ColorInt int color) {
        noteColor= String.valueOf(color);
        relativeLayout.setBackgroundColor(color);
    }

    @Override
    public void onDialogDismissed(int dialogId) {

    }
}
