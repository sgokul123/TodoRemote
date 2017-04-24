package com.todo.todo.home.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.home.model.ToDoItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 18/3/17.
 */

public class NoteAdapter extends BaseAdapter implements Filterable {
        private  String TAG ="NoteAdapter";
        private Context mContext;
         Animation mAnimation;
        CardView mCardView;
        private List<ToDoItemModel> mdisplayedtoDoItemModels;
          private List<ToDoItemModel> mOriginaltoDoItemModels;
        public NoteAdapter(Context c, List<ToDoItemModel> toDoItemModels) {
            Log.i(TAG, "NoteAdapter: ");
            mContext = c;
            this.mdisplayedtoDoItemModels = toDoItemModels;
            this.mOriginaltoDoItemModels = toDoItemModels;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            Log.i(TAG, "getCount: ");
            return mdisplayedtoDoItemModels.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mdisplayedtoDoItemModels.get(position);
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
                
                ToDoItemModel toDoItemModel = mdisplayedtoDoItemModels.get(position);

                grid = new View(mContext);
                Log.i(TAG, "getView: "+toDoItemModel.getTitle());
                mCardView=(CardView) grid.findViewById(R.id.cardview_notes);

                grid = inflater.inflate(R.layout.item_card, null);
                TextView textViewTitle = (TextView) grid.findViewById(R.id.textview_card_title);
                TextView textViewnote = (TextView) grid.findViewById(R.id.textview_notes);
                TextView textViewReminder = (TextView) grid.findViewById(R.id.textView_reminder);
                textViewTitle.setText(toDoItemModel.getTitle());
                textViewnote.setText(toDoItemModel.getNote());
                textViewReminder.setText(toDoItemModel.getReminder());
            } else {
                grid = (View) convertView;
            }

            return grid;
        }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            public void publishResults(CharSequence constraint,FilterResults results) {

                mdisplayedtoDoItemModels = (ArrayList<ToDoItemModel>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new Filter.FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<ToDoItemModel> FilteredArrList = new ArrayList<ToDoItemModel>();

                if (mOriginaltoDoItemModels == null) {
                    mOriginaltoDoItemModels = new ArrayList<ToDoItemModel>(mdisplayedtoDoItemModels); // saves the original data in mOriginalValues
                }



                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginaltoDoItemModels.size();
                    results.values = mOriginaltoDoItemModels;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginaltoDoItemModels.size(); i++) {
                        String data = mOriginaltoDoItemModels.get(i).getTitle();
                        if (data.toLowerCase().startsWith(constraint.toString())) {
                            FilteredArrList.add(mOriginaltoDoItemModels.get(i));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }

        };
        return filter;
    }
}

