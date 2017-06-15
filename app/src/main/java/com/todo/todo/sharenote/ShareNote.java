package com.todo.todo.sharenote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.todo.todo.R;
import com.todo.todo.database.DatabaseHandler;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoActivityInteface;
import com.todo.todo.removenote.view.TrashFragmentInterface;
import com.todo.todo.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by bridgeit on 8/6/17.
 */

public class ShareNote  extends Fragment implements  View.OnClickListener ,ShareNoteInteface{

    private static final String TAG = "TrashFragment";
    private ItemAdapter mSharingAdapter;
    List<ToDoItemModel> mAllToDONotes;
    List<ToDoItemModel> mShareingNote;
    private RecyclerView mToDoRecyclerView;
    private AppCompatTextView mTextView_blank_recycler;
    private AppCompatEditText mEditText_Search;
    private boolean mLinear = false;
    AppCompatImageView mImageView_Linear_Grid;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    AppCompatTextView titleTextView;
    Toolbar mToolbar;
    String mStrshareNote;
    private int count=0;
    ArrayList<Integer> arrayList=new ArrayList<>();
    ToDoActivityInteface mToDoActivityInteface;
    private FloatingActionButton mFloatingActionButton;

    public ShareNote(List<ToDoItemModel> toDoAllItemModels, ToDoActivityInteface toDoActivityInteface, String strshareNote) {
        this.mAllToDONotes=toDoAllItemModels;
        this.mToDoActivityInteface=toDoActivityInteface;
        mStrshareNote=strshareNote;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        View view =inflater.inflate(R.layout.fragment_trash,container,false);
        mToDoRecyclerView = (RecyclerView)view. findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler=(AppCompatTextView)view.findViewById(R.id.textview_blank_fragment_recyclerview);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mEditText_Search=(AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid=(AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        titleTextView =(AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        titleTextView.setText(Constants.NotesType.SHARE_NOTE);
        addTextListener();
        pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mLinear=pref.getBoolean(Constants.Stringkeys.STR_LINEAR_GRID,false);
        mImageView_Linear_Grid.setOnClickListener(this);
        displaySharingNotes();
        mFloatingActionButton.setVisibility(View.INVISIBLE);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        return view;
    }

    public void displaySharingNotes() {
        mShareingNote =getSharingToDos();
        if(mShareingNote.size()==0){
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        }else {
            mTextView_blank_recycler.setVisibility(View.INVISIBLE);
            mSharingAdapter =new ItemAdapter(getActivity(),mShareingNote,this);
            getRecyclerLayout();
            mToDoRecyclerView.setAdapter(mSharingAdapter);
        }

    }

    public void addTextListener() {

        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (mSharingAdapter != null) {
                    Filter filter = mSharingAdapter.getFilter();
                    filter.filter(query);
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        editor = pref.edit();
        switch (v.getId()){
            case R.id.imageView_grid_linear:
                getAlterRecyclerLayout();
                break;
        }

    }


    private void getRecyclerLayout() {
        if (mLinear) {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout() {
        if (!mLinear) {
            mLinear = true;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID,true);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mLinear = false;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID,false);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    //get Archived todo notes
    public List<ToDoItemModel> getSharingToDos() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mStrshareNote.equals(Constants.NotesType.ALL_NOTES)) {
            tempToDoModels=getAllToDo();
        } else if (mStrshareNote.equals(Constants.NotesType.REMINDER_NOTES)) {
            tempToDoModels=getTodaysReminder();
        } else if (mStrshareNote.equals(Constants.NotesType.ARCHIVE_NOTES)) {
            tempToDoModels=getArchivedToDos();
        } else if (mStrshareNote.equals(Constants.NotesType.TRASH_NOTES)) {
            DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
            tempToDoModels=databaseHandler.getAllTrashToDos();
        }
        return tempToDoModels;
    }

    //get Archived todo notes
    public List<ToDoItemModel> getArchivedToDos() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mAllToDONotes != null) {
            for (ToDoItemModel todoItem : mAllToDONotes) {
                if (todoItem.getArchive().equals(getString(R.string.flag_true))) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get Reminders todo
    public List<ToDoItemModel> getTodaysReminder()
    {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(Constants.NotesType.DATE_FORMAT);
        String date = df.format(c.getTime());
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mAllToDONotes != null)
        {
            for (ToDoItemModel todoItem : mAllToDONotes)
            {
                if (todoItem.getReminder().equals(date) && todoItem.getArchive().equals(getString(R.string.flag_false)))
                {
                    //  toDoActivity.getReminderSet(todoItem);
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    //get All notes
    public List<ToDoItemModel> getAllToDo() {
        List<ToDoItemModel> tempToDoModels = new ArrayList<>();
        if (mAllToDONotes != null) {
            for (ToDoItemModel todoItem : mAllToDONotes) {
                if (todoItem.getArchive().equals("false")) {
                    tempToDoModels.add(todoItem);
                }
            }
        }
        return tempToDoModels;
    }

    @Override
    public void shareNote(int position) {
        ToDoItemModel toDoItemModel = mShareingNote.get(position);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getString(R.string.gettitle) + toDoItemModel.getTitle() + getActivity().getString(R.string.getdiscription) + toDoItemModel.getNote());
        sendIntent.setType("text/plain");
        getActivity().startActivity(sendIntent);

    }
   /* public void setUpdatedModel(List<ToDoItemModel> updatedModel) {
        this.mAllToDONotes = updatedModel;
        mShareingNote = getSharingToDos();
        displaySharingNotes(mShareingNote);
    }*/
}
