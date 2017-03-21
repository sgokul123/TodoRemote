package com.todo.todo.home.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.todo.todo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewNote extends AppCompatActivity implements View.OnClickListener {
    AppCompatImageView imageViewBack,imageViewPin,imageViewReminder,imageViewSave;
    AppCompatTextView textViewedited,textViewReminder;
    AppCompatEditText editTextNote,editTextTitle;
    Calendar myCalendar;
    private  String StrTitle,StrReminder,StrNote;
    private  DatePickerDialog.OnDateSetListener date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        imageViewBack=(AppCompatImageView) findViewById(R.id.imageView_back_arrow);
        imageViewPin=(AppCompatImageView) findViewById(R.id.imageView_pin);
        imageViewReminder=(AppCompatImageView) findViewById(R.id.imageView_reminder);
        imageViewSave=(AppCompatImageView) findViewById(R.id.imageView_save);
        textViewReminder=(AppCompatTextView) findViewById(R.id.textview_reminder_text);
        editTextTitle=(AppCompatEditText) findViewById(R.id.edittext_title);
        editTextNote=(AppCompatEditText) findViewById(R.id.edittet_note);
        imageViewBack.setOnClickListener(this);
                imageViewPin.setOnClickListener(this);
        imageViewReminder.setOnClickListener(this);
                imageViewSave.setOnClickListener(this);
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

                StrNote=editTextNote.getText().toString();
                StrTitle=editTextTitle.getText().toString();
                StrReminder=textViewReminder.getText().toString();
                Toast.makeText(this, ""+StrTitle+"  "+StrNote+"   "+StrReminder, Toast.LENGTH_SHORT).show();

                break;
            default:

                break;

        }

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textViewReminder.setText(sdf.format(myCalendar.getTime()));
    }
}
