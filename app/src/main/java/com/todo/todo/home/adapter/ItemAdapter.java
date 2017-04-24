package com.todo.todo.home.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.update.view.UpdateNoteActivity;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bridgeit on 22/3/17.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    Animation mAnimation;
    CardView mCardView;
    private String TAG = "NoteAdapter";
    private Context mContext;
    private List<ToDoItemModel> mdisplayedtoDoItemModels;
    private List<ToDoItemModel> mOriginaltoDoItemModels;


    public ItemAdapter(ToDoActivity toDoActivity, List<ToDoItemModel> toDoItemModels) {
        this.mContext = toDoActivity;
        this.mdisplayedtoDoItemModels = toDoItemModels;
        this.mOriginaltoDoItemModels = toDoItemModels;

    }


    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);


        return new MyViewHolder(itemView);
    }


    public void removeItem(int position) {
        mdisplayedtoDoItemModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mdisplayedtoDoItemModels.size());
    }

    @Override
    public void onBindViewHolder(ItemAdapter.MyViewHolder holder, int position) {
        mAnimation = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.slide_down);
        mCardView.setAnimation(mAnimation);
        mCardView.startAnimation(mAnimation);
        ToDoItemModel toDoItemModel = mdisplayedtoDoItemModels.get(position);
        holder.textViewTitle.setText(toDoItemModel.getTitle());
        holder.textViewnote.setText(toDoItemModel.getNote());
        holder.textViewReminder.setText(toDoItemModel.getReminder());

    }

    @Override
    public int getItemCount() {
        return mdisplayedtoDoItemModels.size();
    }

    public void reduNote(ToDoItemModel todoModelRedu, int position) {
        mdisplayedtoDoItemModels.add(position, todoModelRedu);
        notifyDataSetChanged();
    }

    public void addNote(ToDoItemModel toDoItemModel) {
        mdisplayedtoDoItemModels.add(toDoItemModel);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            public void publishResults(CharSequence constraint, FilterResults results) {

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

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitle, textViewnote, textViewReminder;

        public MyViewHolder(View itemView) {
            super(itemView);
            mAnimation = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.slide_down);
            mCardView = (CardView) itemView.findViewById(R.id.cardview_notes);
            textViewTitle = (TextView) itemView.findViewById(R.id.textview_card_title);
            textViewnote = (TextView) itemView.findViewById(R.id.textview_notes);
            textViewReminder = (TextView) itemView.findViewById(R.id.textView_reminder);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cardview_notes:
                    int position = getAdapterPosition();
                    SharedPreferences pref = mContext.getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    String mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, "null");
                    List<ToDoItemModel> updateModels = ((ToDoActivity) mContext).getUpdateModels();
                    Intent intent = new Intent(mContext, UpdateNoteActivity.class);
                    Bundle bun = new Bundle();
                    bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(updateModels.get(position).getId()));
                    bun.putString(Constants.RequestParam.KEY_NOTE, updateModels.get(position).getNote());
                    bun.putString(Constants.RequestParam.KEY_TITLE, updateModels.get(position).getTitle());
                    bun.putString(Constants.RequestParam.KEY_REMINDER, updateModels.get(position).getReminder());
                    bun.putString(Constants.RequestParam.KEY_STARTDATE, updateModels.get(position).getStartdate());
                    bun.putString(Constants.RequestParam.KEY_ARCHIVE, updateModels.get(position).getArchive());
                    bun.putString(Constants.RequestParam.KEY_SETTIME, updateModels.get(position).getSettime());
                    intent.putExtra(Constants.BundleKey.USER_USER_UID, mUserUID);
                    intent.putExtra(Constants.BundleKey.MEW_NOTE, bun);
                    ((ToDoActivity) mContext).startActivityForResult(intent, 2);
                    ((ToDoActivity) mContext).overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                    // TODO Handle item click
                    break;
            }
        }
    }
}