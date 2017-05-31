package com.todo.todo.archive.view;
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
import com.todo.todo.archive.presenter.ArchiveNotePresenter;
import com.todo.todo.home.view.ToDoActivityInteface;
import com.todo.todo.removenote.view.TrashFragmentInterface;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;
    public class ArchiveFragment extends Fragment implements  View.OnClickListener,TrashFragmentInterface {

        private static final String TAG = "TrashFragment";
        private ItemAdapter mArchiveAdapter;
        List<ToDoItemModel> mAllToDONotes;
        List<ToDoItemModel> mArchiveNotes;
        private RecyclerView mToDoRecyclerView;
        private AppCompatTextView mTextView_blank_recycler;
        private ArchiveNotePresenter mArchiveNotePresenter;
        private AppCompatEditText mEditText_Search;
        private boolean mLinear = false;
        AppCompatImageView mImageView_Linear_Grid,imageView_Delete;
        private SharedPreferences pref;
        private SharedPreferences.Editor editor;
        private String mUserUID;
        AppCompatTextView titleTextView,mTextViewCount;
        Toolbar mToolbar;
        private int count=0;
        ArrayList<Integer> arrayList=new ArrayList<>();
        private Toolbar mToolbardelete;
        ToDoActivityInteface mToDoActivityInteface;
        public ArchiveFragment(List<ToDoItemModel> toDoAllItemModels, ToDoActivityInteface toDoActivityInteface) {
            this.mAllToDONotes=toDoAllItemModels;
            this.mToDoActivityInteface=toDoActivityInteface;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater,container,savedInstanceState);
            View view =inflater.inflate(R.layout.fragment_trash,container,false);

          mToDoRecyclerView = (RecyclerView)view. findViewById(R.id.gridview_fragment_notes);
            mTextView_blank_recycler=(AppCompatTextView)view.findViewById(R.id.textview_blank_fragment_recyclerview);
            mArchiveNotePresenter = new ArchiveNotePresenter(getActivity().getBaseContext(), ArchiveFragment.this);
            mTextView_blank_recycler.setVisibility(View.VISIBLE);
            mEditText_Search=(AppCompatEditText) getActivity().findViewById(R.id.edittext_search_toolbar);
            mImageView_Linear_Grid=(AppCompatImageView) getActivity().findViewById(R.id.imageView_grid_linear);
            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
            titleTextView =(AppCompatTextView) getActivity().findViewById(R.id.textview_title_toolbar);

           // mArchiveNotePresenter.getTrashNotes();
            titleTextView.setText(Constants.NotesType.ARCHIVE_NOTES);
            addTextListener();
            initSwipe();

            pref =getActivity(). getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, getActivity().MODE_PRIVATE);
            mLinear=pref.getBoolean(Constants.Stringkeys.STR_LINEAR_GRID,false);
            mImageView_Linear_Grid.setOnClickListener(this);
            mArchiveNotes=getArchivedToDos();
            displayArchiveNotes(mArchiveNotes);
            // editor = pref.edit();
            mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, Constants.Stringkeys.NULL_VALUIE);
            mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            mToolbardelete =(Toolbar)getActivity().findViewById(R.id.toolbar_delete);
            imageView_Delete= (AppCompatImageView) getActivity().findViewById(R.id.imageView_delete);
            mTextViewCount= (AppCompatTextView) getActivity().findViewById(R.id.textview_count);
            imageView_Delete.setOnClickListener(this);
            return view;
        }

        public void displayArchiveNotes(List<ToDoItemModel> todoItemModel) {
            mArchiveNotes =todoItemModel;
            if(mArchiveNotes.size()==0){
                mTextView_blank_recycler.setVisibility(View.VISIBLE);
                mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
            }else {
                mTextView_blank_recycler.setVisibility(View.INVISIBLE);
                mArchiveAdapter =new ItemAdapter(getActivity(),todoItemModel,this);
                getRecyclerLayout();
                mToDoRecyclerView.setAdapter(mArchiveAdapter);
            }

        }



        public void addTextListener() {

            mEditText_Search.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence query, int start, int before, int count) {
                    if (mArchiveAdapter != null) {
                        Filter filter = mArchiveAdapter.getFilter();
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
                    mArchiveAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();

                    Log.i(TAG, "onSwiped: ");
                    if (direction == ItemTouchHelper.RIGHT) {
                        mArchiveNotePresenter.getRestoreArchiveNote(mUserUID,mArchiveNotes.get(position));
                        mArchiveAdapter.removeItem(position);
                        mArchiveAdapter.notifyDataSetChanged();
                        if(mArchiveAdapter.getItemCount()==0){
                            mTextView_blank_recycler.setVisibility(View.VISIBLE);
                            mTextView_blank_recycler.setText(getString(R.string.get_trash_null));
                        }
                    }else {
                        mArchiveNotePresenter.removeFirebaseData(mArchiveNotes.get(position),mAllToDONotes,mUserUID);
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
                case R.id.imageView_delete:

                    getHideToolBar(false);
                    mArchiveAdapter =new ItemAdapter(getActivity(),mArchiveNotes);
                    getRecyclerLayout();
                    mToDoRecyclerView.setAdapter(mArchiveAdapter);
                    count=0;
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

        public void setUpdatedModel(List<ToDoItemModel> updatedModel) {
            this.mAllToDONotes = updatedModel;
            mArchiveNotes=getArchivedToDos();
            displayArchiveNotes(mArchiveNotes);
        }

        @Override
        public void getHideToolBar(boolean flag) {
            if (flag) {
                mToolbar.setVisibility(View.GONE);
                mToolbardelete.setVisibility(View.VISIBLE);
            } else {
                mToolbardelete.setVisibility(View.GONE);
                mToolbar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void getCountIncreament(int position) {
            count=count+1;
            arrayList.add(position);
            mTextViewCount.setText(count+"  Selected");
        }

        @Override
        public void displayTrashNotes(List<ToDoItemModel> todoItemModel) {

        }

        @Override
        public void getRefreshNotes() {

        }

        @Override
        public void getCountDecreament(Integer position) {
            count=count-1;
            arrayList.remove(position);
            mTextViewCount.setText(count+"  Selected");
        }

    }