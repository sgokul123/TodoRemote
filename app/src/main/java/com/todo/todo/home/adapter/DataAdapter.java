package com.todo.todo.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.home.model.ToDoModel;

import java.util.List;

/**
 * Created by bridgeit on 18/3/17.
 */

public class DataAdapter extends BaseAdapter {

        private Context mContext;
        private List<ToDoModel> toDoModels;

        public DataAdapter(Context c,List<ToDoModel> toDoModels ) {
            mContext = c;
            this.toDoModels=toDoModels;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return toDoModels.size();
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
            View grid;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                ToDoModel toDoModel=toDoModels.get(position);

                grid = new View(mContext);
                grid = inflater.inflate(R.layout.card_notes, null);
                TextView textViewTitle = (TextView) grid.findViewById(R.id.textview_card_title);
                TextView textViewnote = (TextView) grid.findViewById(R.id.textview_notes);
                TextView textViewReminder = (TextView) grid.findViewById(R.id.textview_notes);
                textViewTitle.setText(toDoModel.get_title());
                textViewnote.setText(toDoModel.get_note());
                textViewReminder.setText(toDoModel.get_reminder());
            } else {
                grid = (View) convertView;
            }

            return grid;
        }

    }

