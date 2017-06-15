package com.todo.todo.alarmmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.RelativeLayout;
import com.todo.todo.R;
import com.todo.todo.util.Constants;

public class ReminderActivity extends AppCompatActivity {
    Bundle bundle;
    private AppCompatTextView textViewReminder;
    private AppCompatTextView textViewTitle;
    private AppCompatTextView textViewNote;
    private AppCompatTextView mTextViewEditedAt;
    private RelativeLayout relativeLayout;
    private String noteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_layout);
        bundle=getIntent().getExtras();
        textViewReminder = (AppCompatTextView) findViewById(R.id.textview_reminder_text1);
        textViewTitle = (AppCompatTextView) findViewById(R.id.edittext_title1);
        textViewNote = (AppCompatTextView) findViewById(R.id.edittet_note1);
        mTextViewEditedAt = (AppCompatTextView) findViewById(R.id.textview_editedat_at1);
        relativeLayout= (RelativeLayout) findViewById(R.id.layout_add_new_card1);
        setData(bundle);
    }

    private void setData(Bundle ban) {
        textViewReminder.setText(ban.getString(Constants.RequestParam.KEY_REMINDER));
        textViewTitle.setText(ban.getString(Constants.RequestParam.KEY_TITLE));
        textViewNote.setText(ban.getString(Constants.RequestParam.KEY_NOTE));
        mTextViewEditedAt.setText(ban.getString(Constants.RequestParam.KEY_SETTIME));

        if(ban.getString(Constants.RequestParam.KEY_COLOR)!=null){
            noteColor=ban.getString(Constants.RequestParam.KEY_COLOR);
            relativeLayout.setBackgroundColor(Integer.parseInt(ban.getString(Constants.RequestParam.KEY_COLOR)));
        }

    }
}
