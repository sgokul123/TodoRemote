package com.todo.todo.home.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.todo.todo.R;
import com.todo.todo.archive.presenter.ArchiveNotePresenter;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.ToDoActivityPresenter;
import com.todo.todo.removenote.presenter.TrashNotePresenter;
import com.todo.todo.update.presenter.UpdateNotePresenter;
import com.todo.todo.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderFragment extends Fragment implements ToDoActivityInteface, View.OnClickListener {

    private static final String TAG = "TrashFragment";
    List<ToDoItemModel> mAllToDONotes;
    List<ToDoItemModel> mReminderNotes;
    AppCompatImageView mImageView_Linear_Grid;
    AppCompatTextView titleTextView;
    private ItemAdapter mReminderNotesAdapter;
    private RecyclerView mToDoRecyclerView;
    private AppCompatTextView mTextView_blank_recycler;
    private ArchiveNotePresenter mArchiveNotePresenter;
    private AppCompatEditText mEditText_Search;
    private boolean mLinear = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mUserUID;
    private TrashNotePresenter trashNotePresenter;
    private UpdateNotePresenter updateNotePresenter;
    private ToDoActivityPresenter mToDoActivityPresenter;
    private int RQS_1=1;
    ToDoActivity toDoActivity;
    public ReminderFragment(List<ToDoItemModel> toDoAllItemModels, ToDoActivity toDoActivity) {
        this.mAllToDONotes = toDoAllItemModels;
        this.toDoActivity=toDoActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
        mToDoRecyclerView = (RecyclerView) view.findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler = (AppCompatTextView) view.findViewById(R.id.textview_blank_fragment_recyclerview);
        // mArchiveNotePresenter = new ArchiveNotePresenter(getActivity().getBaseContext(), ToDoNotesFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        titleTextView = (AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        mEditText_Search = (AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid = (AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        // mArchiveNotePresenter.getTrashNotes();
        titleTextView.setText(Constants.NotesType.REMINDER_NOTES);
        addTextListener();
        mImageView_Linear_Grid.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mLinear = pref.getBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);

        trashNotePresenter = new TrashNotePresenter(getActivity().getBaseContext());
        updateNotePresenter = new UpdateNotePresenter(getActivity().getBaseContext(), this);
       // initSwipe();
        mReminderNotes = getTodaysReminder();
        showDataInActivity(mReminderNotes);
        return view;
    }

    public void addTextListener()
    {

        mEditText_Search.addTextChangedListener(new TextWatcher()
        {

            public void afterTextChanged(Editable s)
            {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count)
            {
                if (mReminderNotesAdapter != null)
                {
                    Filter filter = mReminderNotesAdapter.getFilter();
                    filter.filter(query);
                }

            }
        });
    }

    //swipe view delete / Archive
    private void initSwipe()
    {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // Collections.swap(toDoItemModels,viewHolder.getAdapterPosition(), target.getAdapterPosition());
                Log.i(TAG, "onMove: " + viewHolder.getAdapterPosition() + "  target" + target.getAdapterPosition());
                int from, destination;
                from = viewHolder.getAdapterPosition();
                destination = target.getAdapterPosition();
                mReminderNotesAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                /*updateNotePresenter.getMoveNotes(mUserUID, toDoItemModels.get(from), toDoItemModels.get(destination));*/
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                final int position = viewHolder.getAdapterPosition();
                //    String typeOfNotes = mTextView_Title.getText().toString();
                final List<ToDoItemModel> forArchiveAlldataModels = mAllToDONotes;
                Log.i(TAG, "onSwiped: ");
                final ToDoItemModel toDoItem = mReminderNotes.get(position);
                if (direction == ItemTouchHelper.LEFT) {
                    trashNotePresenter.removeFirebaseData(toDoItem, forArchiveAlldataModels, mUserUID);
                    Snackbar snackbar = Snackbar
                            .make(getActivity().getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                            .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    trashNotePresenter.undoRemoveFirebaseData(toDoItem, forArchiveAlldataModels, mUserUID);
                                }
                            });
                    snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                    snackbar.show();
                    if (mReminderNotes.size() == 1)
                    {
                        mReminderNotes.remove(0);
                    }
                }
                else
                    {
                    getArchive(mReminderNotesAdapter, position, mReminderNotes.get(position));
                }
            }

            //Archive Note Methode  And do Undo if required
            public void getArchive(final ItemAdapter archiveitemAdapter, final int position, final ToDoItemModel toDoItemModel) {
                final String date = toDoItemModel.getStartdate();
                // archiveitemAdapter.removeItem(position);
                updateNotePresenter.getAchiveNote(mUserUID, date, toDoItemModel);
                Snackbar snackbar = Snackbar
                        .make(getActivity().getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_ARCHIVED, Snackbar.LENGTH_LONG)
                        .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateNotePresenter.getUndoAchiveNote(mUserUID, date, toDoItemModel);
                                toDoItemModel.setArchive(Constants.Stringkeys.FLAG_FALSE);
                                archiveitemAdapter.reduNote(toDoItemModel, position);
                            }
                        });
                snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                snackbar.show();

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);
    }

    @Override
    public void onClick(View v)
    {
        editor = pref.edit();
        switch (v.getId())
        {
            case R.id.imageView_grid_linear:
                getAlterRecyclerLayout();
                break;
        }
    }

    private void getRecyclerLayout()
    {
        if (mLinear)
        {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        }
        else
        {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout()
    {
        if (!mLinear)
        {
            mLinear = true;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, true);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        }
        else
            {
            mLinear = false;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    @Override
    public void closeProgressDialog()
    {
    }

    @Override
    public void showProgressDialog()
    {

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

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {
        if (mReminderNotes.size() != 0) {
            getupdatedView(mReminderNotes, 1);
        } else {
            getupdatedView(mReminderNotes, 1);
        }
    }

    private void getupdatedView(List<ToDoItemModel> toDoItemModels, int i) {

        switch (i) {
            case 1:
                if (toDoItemModels.size() == 0) {
                    mTextView_blank_recycler.setVisibility(View.VISIBLE);
                    mTextView_blank_recycler.setText(getString(R.string.data_is_null));
                    mToDoRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                    mToDoRecyclerView.setVisibility(View.VISIBLE);
                    getRecyclerLayout();
                    mReminderNotesAdapter = new ItemAdapter(getActivity(), toDoItemModels);
                    mToDoRecyclerView.setAdapter(mReminderNotesAdapter);
                }
                break;
        }
    }

    @Override
    public void getResponce(boolean flag) {

    }

    @Override
    public void getUndoArchivedNote(int position) {

    }

    @Override
    public void hideToolBar(boolean flag) {

    }

    public void setUpdatedModel(List<ToDoItemModel> toDoAllItemModels) {

        this.mAllToDONotes = toDoAllItemModels;
        mReminderNotes = getTodaysReminder();
        showDataInActivity(mReminderNotes);
    }
}


