package com.todo.todo.removenote.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;

import com.todo.todo.R;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.removenote.presenter.TrashNotePresenter;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TrashFragment extends Fragment implements TrashFragmentInterface, View.OnClickListener {

    private static final String TAG = "TrashFragment";
    List<ToDoItemModel> mTrashNotes;
    AppCompatImageView mImageView_Linear_Grid, imageView_Delete;
    private ItemAdapter mTrashAdapter;
    private RecyclerView mToDoRecyclerView;
    private AppCompatTextView mTextView_blank_recycler;
    private TrashNotePresenter trashNotePresenter;
    private AppCompatEditText mEditText_Search;
    private boolean mLinear = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mUserUID;
    private AppCompatTextView titleTextView, mTextViewCount;
    private List<ToDoItemModel> updateModel;
    private Toolbar mToolbardelete, mToolbar,mToolSearch;
    private int count;
    private ArrayList<Integer> arrayList;

    public TrashFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_trash, container, false);
      //  mToDoRecyclerView = (RecyclerView) view.findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler = (AppCompatTextView) view.findViewById(R.id.textview_blank_fragment_recyclerview);
        trashNotePresenter = new TrashNotePresenter(getActivity().getBaseContext(), TrashFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mEditText_Search = (AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid = (AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        titleTextView = (AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbardelete = (Toolbar) getActivity().findViewById(R.id.toolbar_delete);
        mToolSearch = (Toolbar) getActivity().findViewById(R.id.toolbarsearch);
        imageView_Delete = (AppCompatImageView) getActivity().findViewById(R.id.imageView_delete);
        mTextViewCount = (AppCompatTextView) getActivity().findViewById(R.id.textview_count);
        titleTextView.setText(Constants.NotesType.TRASH_NOTES);
        addTextListener();
        initSwipe();
        mImageView_Linear_Grid.setOnClickListener(this);
        imageView_Delete.setOnClickListener(this);
        pref = getActivity().getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mLinear = pref.getBoolean("mlinear", false);
        trashNotePresenter.getTrashNotes();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        arrayList = new ArrayList<>();
        return view;
    }

    @Override
    public void getHideToolBar(boolean flag) {
        if (flag) {
            mToolSearch.setVisibility(View.GONE);
            mToolbar.setVisibility(View.GONE);
            mToolbardelete.setVisibility(View.VISIBLE);
        } else {
            mToolbardelete.setVisibility(View.GONE);
            mToolbar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getCountDecreament(Integer position) {
        count = count - 1;
        Log.i(TAG, "getCountDecreament: "+arrayList.remove(position));//arrayList.remove(position);
        mTextViewCount.setText(count + "  Selected");

    }

    @Override
    public void getCountIncreament(int position) {
        count = count + 1;
        arrayList.add(position);
        mTextViewCount.setText(count + "  Selected");
    }

    @Override
    public void displayTrashNotes(List<ToDoItemModel> todoItemModel) {
        mTrashNotes = todoItemModel;
        if (mTrashNotes.size() == 0) {
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
            List<ToDoItemModel> trashNotes = new ArrayList<>();
            mTrashAdapter = new ItemAdapter(getActivity(), trashNotes, this);
            getRecyclerLayout();
            mToDoRecyclerView.setAdapter(mTrashAdapter);

        } else {
            mTextView_blank_recycler.setVisibility(View.INVISIBLE);
            mTrashAdapter = new ItemAdapter(getActivity(), todoItemModel, this);
            getRecyclerLayout();
            mToDoRecyclerView.setAdapter(mTrashAdapter);
        }

    }

    @Override
    public void getRefreshNotes() {
        count=0;
        arrayList.clear();
        mTrashAdapter = new ItemAdapter(getActivity(), mTrashNotes, this);
        getRecyclerLayout();
        mToDoRecyclerView.setAdapter(mTrashAdapter);
    }


    public void addTextListener() {
        mEditText_Search.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence query, int start, int before, int count) {
                if (mTrashAdapter != null) {
                    Filter filter = mTrashAdapter.getFilter();
                    filter.filter(query);
                }

            }
        });
    }

    //swipe view delete / Archive
    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                mTrashAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                Log.i(TAG, "onSwiped: ");
                if (direction == ItemTouchHelper.LEFT) {
                    trashNotePresenter.getDeleteTrashNote(mTrashNotes.get(position));
                    mTrashAdapter.removeItem(position);
                    mTrashAdapter.notifyDataSetChanged();
                    if (mTrashAdapter.getItemCount() == 0) {
                        mTextView_blank_recycler.setVisibility(View.VISIBLE);
                        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
                    }
                } else {
                    trashNotePresenter.getRestoreNote(mUserUID, mTrashNotes.get(position));
                }
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
            case R.id.imageView_delete:
                getHideToolBar(false);
                getDeleteNotes();
                count = 0;
                arrayList.clear();

                break;
        }
    }

    private void getDeleteNotes() {
        List<ToDoItemModel> trashNotes = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            trashNotes.add(mTrashNotes.get(arrayList.get(i)));
        }
        if (trashNotes.size() != 0) {
            trashNotePresenter.getDeleteMultipleTrashNotes(trashNotes);
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
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, true);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mLinear = false;
            editor.putBoolean(Constants.Stringkeys.STR_LINEAR_GRID, false);
            editor.commit();
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }


}
