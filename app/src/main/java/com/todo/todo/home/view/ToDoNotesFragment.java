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
import com.todo.todo.util.Connection;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ToDoNotesFragment extends Fragment implements ToDoActivityInteface, View.OnClickListener {

    private static final String TAG = "TrashFragment";
    List<ToDoItemModel> mAllToDONotes;
    List<ToDoItemModel> mTodoNotes,mPinnedNotes;
    AppCompatImageView mImageView_Linear_Grid;
    AppCompatTextView titleTextView,textViewPinned;
    private ItemAdapter mToDoNotesAdapter;
    private RecyclerView mToDoRecyclerView,mPinnedRecyclerView;
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
    private  boolean draged=false;
    private ItemAdapter mToDoPinnedAdapter;
    int start=-1,end;
    ToDoActivity mToDoActivity;

    public ToDoNotesFragment(ToDoActivity toDoActivity, List<ToDoItemModel> toDoAllItemModels) {
        this.mAllToDONotes = toDoAllItemModels;
        this.mToDoActivity=toDoActivity;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
        mToDoRecyclerView = (RecyclerView) view.findViewById(R.id.gridview_fragment_notes);
        mPinnedRecyclerView=(RecyclerView) view.findViewById(R.id.pinned_fragment_notes);
        mTextView_blank_recycler = (AppCompatTextView) view.findViewById(R.id.textview_blank_fragment_recyclerview);
        // mArchiveNotePresenter = new ArchiveNotePresenter(getActivity().getBaseContext(), ToDoNotesFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mTextView_blank_recycler.setText(getString(R.string.data_is_null));
        textViewPinned=(AppCompatTextView)view.findViewById(R.id.textview_pinned);
        titleTextView = (AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        mEditText_Search = (AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid = (AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        //getActivity().s(mToolbar);
        // mArchiveNotePresenter.getTrashNotes();
        titleTextView.setText(Constants.NotesType.ALL_NOTES);
        addTextListener();
        mImageView_Linear_Grid.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mLinear = pref.getBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        mTodoNotes=new ArrayList<>();
        mPinnedNotes=new ArrayList<>();
        if (!mUserUID.equals(Constants.Stringkeys.NULL_VALUIE)) {
            mToDoActivityPresenter = new ToDoActivityPresenter(this, getActivity().getBaseContext());
            mToDoActivityPresenter.getPresenterNotes(mUserUID);
        }

        trashNotePresenter = new TrashNotePresenter(getActivity().getBaseContext());
        updateNotePresenter = new UpdateNotePresenter(getActivity().getBaseContext(), this);
        initSwipe();
        return view;
    }

    public void addTextListener() {

        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (mToDoNotesAdapter != null) {
                    Filter filter = mToDoNotesAdapter.getFilter();
                    filter.filter(query);
                }if (mToDoPinnedAdapter != null) {
                    Filter filter2= mToDoPinnedAdapter.getFilter();
                    filter2.filter(query);
                }

            }
        });
    }

    //swipe view delete / Archive
    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.i(TAG, "onMove: " + viewHolder.getAdapterPosition() + "  target" + target.getAdapterPosition());
                if(start==-1){
                    start=viewHolder.getAdapterPosition();
                }
                 end= target.getAdapterPosition();
                mToDoNotesAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
               if(start>=0){
                   trashNotePresenter.dragNotes(mUserUID,mTodoNotes,end,start);
                   draged=true;
                   mToDoActivity.setdraged(true);
               }
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                start=-1;
                end=-1;
                final int position = viewHolder.getAdapterPosition();
                final List<ToDoItemModel> forArchiveAlldataModels = mAllToDONotes;
                Log.i(TAG, "onSwiped: ");
                final ToDoItemModel toDoItem = mTodoNotes.get(position);
                if (direction == ItemTouchHelper.LEFT) {
                    trashNotePresenter.removeFirebaseData(toDoItem, forArchiveAlldataModels, mUserUID);
                    Snackbar snackbar = Snackbar
                            .make(getActivity().getCurrentFocus(), Constants.Stringkeys.MASSEGE_IS_DELETED, Snackbar.LENGTH_LONG)
                            .setAction(Constants.Stringkeys.ARCHIVE_UNDO, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    trashNotePresenter.undoRemoveFirebaseData(toDoItem, forArchiveAlldataModels, mUserUID);
                                }
                            });
                    snackbar.setDuration(2000);     //5 sec duration if want to Undo else it will Archive note
                    snackbar.show();
                    if (mTodoNotes.size() == 1) {
                        mTodoNotes.remove(0);
                    }
                } else {
                    getArchive(mToDoNotesAdapter, position, mTodoNotes.get(position));
                }
            }

            //Archive Note Methode  And do Undo if required
            public void getArchive(final ItemAdapter archiveitemAdapter, final int position, final ToDoItemModel toDoItemModel) {
                final String date = toDoItemModel.getStartdate();
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
    public void onClick(View v) {
        editor = pref.edit();
        switch (v.getId()) {
            case R.id.imageView_grid_linear:
                getAlterRecyclerLayout();
                break;
        }
    }

    private void getRecyclerLayout() {
        if (mLinear) {
            mPinnedRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mPinnedRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout() {
        if (!mLinear) {
            mLinear = true;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, true);
            editor.commit();
            mPinnedRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mLinear = false;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
            editor.commit();
            mPinnedRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

    //get All notes
    public void getAllToDo() {
        mPinnedNotes.clear();
        mTodoNotes.clear();
        if (mAllToDONotes != null) {
            for (ToDoItemModel todoItem : mAllToDONotes) {
                if (todoItem.getArchive().equals("false")) {
                    if(todoItem.isPin()){
                        mPinnedNotes.add(todoItem);
                        textViewPinned.setVisibility(View.VISIBLE);
                    }else {
                        mTodoNotes.add(todoItem);
                    }
                }
            }
        }
    }

    @Override
    public void closeProgressDialog() {
    }

    @Override
    public void showProgressDialog() {
    }

    @Override
    public void showDataInActivity(List<ToDoItemModel> toDoItemModels) {
        if(!draged){
            mToDoActivity.showDataInActivity(toDoItemModels);
            getsortList(toDoItemModels);
        }
    }

    private void getsortList(List<ToDoItemModel> toDoItemModels) {
          int size=toDoItemModels.size();
        List<ToDoItemModel> toDoItemModels1=toDoItemModels;
        mAllToDONotes.clear();
        for(int i=0;i<size-1;i++){
                    for(int j=i+1;j<size;j++){
                        if(toDoItemModels1.get(i).getSrid()>toDoItemModels1.get(j).getSrid()){
                            Collections.swap(toDoItemModels1, i, j);
                        }
                    }
            }

        editor = pref.edit();
        mAllToDONotes=toDoItemModels1;
        editor.putInt(Constants.Stringkeys.LAST_SRNO,mAllToDONotes.get(size-1).getSrid()+1);
        editor.commit();
        getAllToDo();
        if (mTodoNotes.size() != 0||mPinnedNotes.size()!=0) {
            getupdatedView(mTodoNotes,mPinnedNotes, 1);
        } else {
            getupdatedView(mTodoNotes,mPinnedNotes, 1);
        }
    }

    private void getupdatedView(List<ToDoItemModel> mTodoNotes, List<ToDoItemModel> pinnedNotes, int i) {
        switch (i) {
            case 1:
                if (pinnedNotes.size() == 0 && mTodoNotes.size()==0) {
                    mTextView_blank_recycler.setVisibility(View.VISIBLE);
                    mTextView_blank_recycler.setText(getString(R.string.data_is_null));
                    mToDoRecyclerView.setVisibility(View.INVISIBLE);
                    mPinnedRecyclerView.setVisibility(View.INVISIBLE);
                } else {
                    mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                    mToDoRecyclerView.setVisibility(View.VISIBLE);
                    getRecyclerLayout();
                    if(pinnedNotes.size()!=0){
                        textViewPinned.setVisibility(View.VISIBLE);
                        mPinnedRecyclerView.setVisibility(View.VISIBLE);
                        mToDoPinnedAdapter = new ItemAdapter(getActivity(), pinnedNotes);
                        mPinnedRecyclerView.setAdapter(mToDoPinnedAdapter);
                    }
                    if(mTodoNotes.size()!=0){
                        mToDoNotesAdapter = new ItemAdapter(getActivity(), mTodoNotes);
                        mToDoRecyclerView.setAdapter(mToDoNotesAdapter);
                        if(pinnedNotes.size()==0){
                            textViewPinned.setVisibility(View.GONE);
                        }
                    }
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
        showDataInActivity(mAllToDONotes);
    }
}
