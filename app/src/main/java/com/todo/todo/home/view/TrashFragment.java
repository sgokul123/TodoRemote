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

    public TrashFragment(AppCompatEditText editText_search, AppCompatImageView mImageView_Linear_Grid) {
        this.mEditText_Search=editText_search;
        this.mImageView_Linear_Grid=mImageView_Linear_Grid;
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
        mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
        removeNotePresenter.getTrashNotes();
        addTextListener();
        initSwipe();
        mImageView_Linear_Grid.setOnClickListener(this);

        pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
        editor = pref.edit();
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
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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
                    Filter filter = mTrashAdapter.getFilter();
                    filter.filter(query);
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

        getAlterRecyclerLayout();
    }

    private void getRecyclerLayout() {
        if (mLinear) {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        }
    }

    //set Linear or grid Layout
    private void getAlterRecyclerLayout() {
        if (!mLinear) {
            mLinear = true;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.grid_view);
        } else {
            mLinear = false;
            mToDoRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            mImageView_Linear_Grid.setImageResource(R.drawable.list_view);
        }
    }

}
