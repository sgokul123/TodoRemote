package com.todo.todo.addnote.view;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.todo.todo.R;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.home.view.ToDoActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewNoteFragment extends Fragment implements View.OnClickListener,NoteInterface {
    private  String TAG ="NewNoteFragment";
    AppCompatImageView imageViewBack,imageViewPin,imageViewReminder,imageViewSave;
    AppCompatTextView textViewedited,textViewReminder;
    AppCompatEditText editTextNote,editTextTitle;
    ToDoActivityPresenter toDoActivityPresenter;
    ProgressDialog progressDialog;
    Calendar myCalendar;
    private  String StrTitle,StrReminder,StrNote;
    private  DatePickerDialog.OnDateSetListener date;

    public static NewNoteFragment newInstance() {
        NewNoteFragment fragment = new NewNoteFragment();
        Bundle args = new Bundle();
       fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);

        imageViewBack=(AppCompatImageView) view.findViewById(R.id.imageView_back_arrow);
        imageViewPin=(AppCompatImageView)view. findViewById(R.id.imageView_pin);
        imageViewReminder=(AppCompatImageView)view. findViewById(R.id.imageView_reminder);
        imageViewSave=(AppCompatImageView)view. findViewById(R.id.imageView_save);
        textViewReminder=(AppCompatTextView)view. findViewById(R.id.textview_reminder_text);
        editTextTitle=(AppCompatEditText)view. findViewById(R.id.edittext_title);
        editTextNote=(AppCompatEditText)view. findViewById(R.id.edittet_note);
        progressDialog=new ProgressDialog(getActivity());


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


        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageView_back_arrow:

                break;
            case R.id.imageView_pin:

                break;
            case R.id.imageView_reminder:

                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.imageView_save:

                ToDoItemModel toDoItemModel =new ToDoItemModel();

                StrNote=editTextNote.getText().toString();
                toDoItemModel.setNote(StrNote);
                editTextNote.setText("");
                StrTitle=editTextTitle.getText().toString();
                toDoItemModel.setTitle(StrTitle);
                editTextTitle.setText("");
                StrReminder=textViewReminder.getText().toString();
                toDoItemModel.setReminder(StrReminder);
                textViewReminder.setText("");
                Toast.makeText(getActivity(), toDoItemModel +"", Toast.LENGTH_SHORT).show();
                toDoActivityPresenter =new ToDoActivityPresenter(new ToDoActivity(),getActivity());

               break;
            default:
                break;
        }

    }

    private void updateLabel() {

        String myFormat = "dd/MM/yy";            //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        textViewReminder.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void closeNoteProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void showNoteProgressDialog() {

        progressDialog.setMessage("Wait while adding Note...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void getResponce(boolean flag) {
        if(flag){
            Toast.makeText(getActivity(), "succcess", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
        }
    }
}
