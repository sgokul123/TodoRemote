package com.todo.todo.home.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.List;

/**
 * Created by bridgeit on 18/3/17.
 */

public class NoteAdapter extends BaseAdapter {
        private  String TAG ="NoteAdapter";
        private Context mContext;
        private List<ToDoItemModel> toDoItemModels;

        public NoteAdapter(Context c, List<ToDoItemModel> toDoItemModels) {
            Log.i(TAG, "NoteAdapter: ");
            mContext = c;
            this.toDoItemModels = toDoItemModels;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            Log.i(TAG, "getCount: ");
            return toDoItemModels.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            Log.i(TAG, "getView: start");
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Log.i(TAG, "getView: ");
            if (convertView == null) {
                
                ToDoItemModel toDoItemModel = toDoItemModels.get(position);

                grid = new View(mContext);
                Log.i(TAG, "getView: "+toDoItemModel.get_title());
                grid = inflater.inflate(R.layout.itemview_notes, null);
                TextView textViewTitle = (TextView) grid.findViewById(R.id.textview_card_title);
                TextView textViewnote = (TextView) grid.findViewById(R.id.textview_notes);
                TextView textViewReminder = (TextView) grid.findViewById(R.id.textView_reminder);
                textViewTitle.setText(toDoItemModel.get_title());
                textViewnote.setText(toDoItemModel.get_note());
                textViewReminder.setText(toDoItemModel.get_reminder());
            } else {
                grid = (View) convertView;
            }

            return grid;
        }

    }

