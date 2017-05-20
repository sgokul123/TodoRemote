package com.todo.todo.home.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.home.adapter.ItemAdapter;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.presenter.RemoveNotePresenter;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;
public class TrashFragment extends Fragment implements  TrashFragmentInterface, View.OnClickListener {

    private static final String TAG = "TrashFragment";
    private ItemAdapter mTrashAdapter;
    List<ToDoItemModel> mTrashNotes;
    private RecyclerView mToDoRecyclerView;
    private AppCompatTextView mTextView_blank_recycler;
    private RemoveNotePresenter removeNotePresenter;
    private AppCompatEditText mEditText_Search;
    private boolean mLinear = false;
    AppCompatImageView mImageView_Linear_Grid;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String mUserUID;
    private AppCompatTextView titleTextView;
    private List<ToDoItemModel> updateModel;

    public TrashFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View view =inflater.inflate(R.layout.fragment_trash,container,false);
        mToDoRecyclerView = (RecyclerView)view. findViewById(R.id.gridview_fragment_notes);
        mTextView_blank_recycler=(AppCompatTextView)view.findViewById(R.id.textview_blank_fragment_recyclerview);
        removeNotePresenter = new RemoveNotePresenter(getActivity().getBaseContext(),TrashFragment.this);
        mTextView_blank_recycler.setVisibility(View.VISIBLE);
        mEditText_Search= (AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
        mImageView_Linear_Grid= (AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
        titleTextView =(AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        titleTextView.setText(Constants.NotesType.TRASH_NOTES);
        addTextListener();
        initSwipe();
        mImageView_Linear_Grid.setOnClickListener(this);
        pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        mLinear=pref.getBoolean("mlinear",false);
        removeNotePresenter.getTrashNotes();
        mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
        return view;
    }
    @Override
    public void displayTrashNotes(List<ToDoItemModel> todoItemModel) {
        mTrashNotes=todoItemModel;
        if(mTrashNotes.size()==0){
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        }else {
            mTextView_blank_recycler.setVisibility(View.INVISIBLE);
            mTrashAdapter=new ItemAdapter(getActivity(),todoItemModel);
            getRecyclerLayout();
            mToDoRecyclerView.setAdapter(mTrashAdapter);
        }

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
                    removeNotePresenter.getDeleteTrashNote(mTrashNotes.get(position));
                        mTrashAdapter.removeItem(position);
                        mTrashAdapter.notifyDataSetChanged();
                        if(mTrashAdapter.getItemCount()==0){
                            mTextView_blank_recycler.setVisibility(View.VISIBLE);
                            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
                        }
                    }else {
                        removeNotePresenter.getRestoreNote(mUserUID,mTrashNotes.get(position));
                    }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mToDoRecyclerView);

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

}
