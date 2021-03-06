package com.todo.todo.home.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.todo.todo.R;
import com.todo.todo.archive.view.ArchiveFragmentInterface;
import com.todo.todo.home.model.ToDoItemModel;
import com.todo.todo.home.view.ToDoActivity;
import com.todo.todo.removenote.view.TrashFragmentInterface;
import com.todo.todo.sharenote.ShareNote;
import com.todo.todo.update.view.UpdateNoteActivity;
import com.todo.todo.util.Constants;

import java.util.ArrayList;
import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> implements Filterable {

    Animation mAnimation;
    ToDoActivity mToDoActivity;
    int count = 0;
    TrashFragmentInterface mTrashFragmentInterface;
    ArchiveFragmentInterface archiveFragmentInterface ;
    private String TAG = "NoteAdapter";
    private Activity mContext;
    private List<ToDoItemModel> mdisplayedtoDoItemModels;
    private List<ToDoItemModel> mOriginaltoDoItemModels;
    private ToDoItemModel toDoIte;
    private String shareSub;
    private String shareBody;
    private MyViewHolder holders;
    private boolean onclick=false;
    ShareNote shareNote;
    public ItemAdapter(Activity activity, List<ToDoItemModel> todoItemModel) {
        this.mContext = activity;
        this.mdisplayedtoDoItemModels = todoItemModel;
        this.mOriginaltoDoItemModels = todoItemModel;
    }

    public ItemAdapter(Activity context, List<ToDoItemModel> todoItemModel, TrashFragmentInterface trashFragmentInterface) {
        this.mContext = context;
        this.mdisplayedtoDoItemModels = todoItemModel;
        this.mOriginaltoDoItemModels = todoItemModel;
        this.mTrashFragmentInterface = trashFragmentInterface;
    }

    public ItemAdapter(Activity activity, List<ToDoItemModel> mShareingNote, ShareNote shareNote) {
        this.mContext=activity;
        this.mdisplayedtoDoItemModels = mShareingNote;
        this.mOriginaltoDoItemModels = mShareingNote;
        this.shareNote=shareNote;

    }

    public ItemAdapter(Activity context, List<ToDoItemModel> todoItemModel, ArchiveFragmentInterface archiveFragmentInterface) {
        this.mContext=context;
        this.mdisplayedtoDoItemModels = todoItemModel;
        this.mOriginaltoDoItemModels = todoItemModel;
      this.archiveFragmentInterface=archiveFragmentInterface;
    }

    @Override
    public ItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public void removeItem(int position) {
        mdisplayedtoDoItemModels.remove(position);
        notifyItemRangeChanged(position, mdisplayedtoDoItemModels.size());
    }

    @Override
    public void onBindViewHolder(final ItemAdapter.MyViewHolder holder, final int position) {
        mAnimation = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.slide_down);

        holder.mCardView.setAnimation(mAnimation);
        holder.mCardView.startAnimation(mAnimation);
        ToDoItemModel toDoItemModel = mdisplayedtoDoItemModels.get(position);
        holder.textViewTitle.setText(toDoItemModel.getTitle());
        holder.textViewnote.setText(toDoItemModel.getNote());
        holder.textViewReminder.setText(toDoItemModel.getReminder());
        holder.textViewtime.setText(toDoItemModel.getSettime());
        if(toDoItemModel.getColor()!=null){
            holder.layout_card.setBackgroundColor(Integer.parseInt(toDoItemModel.getColor()));
        }
        if(toDoItemModel.isPin()){
            holder.imageViewPoint.setVisibility(View.VISIBLE);
        }

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



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewTitle, textViewnote;
        CardView mCardView;
        RelativeLayout layout_card;
        AppCompatTextView textViewReminder,textViewtime;
        AppCompatImageView imageViewPinm,imageViewPoint;

        public MyViewHolder(View itemView) {
            super(itemView);
            mAnimation = AnimationUtils.loadAnimation(mContext.getApplicationContext(), R.anim.slide_down);
            mCardView = (CardView) itemView.findViewById(R.id.cardview_notes);
            layout_card = (RelativeLayout) itemView.findViewById(R.id.layout_card);
            textViewTitle = (TextView) itemView.findViewById(R.id.textview_card_title);
            textViewnote = (TextView) itemView.findViewById(R.id.textview_notes);
            textViewReminder = (AppCompatTextView) itemView.findViewById(R.id.textView_reminder);
            imageViewPinm=(AppCompatImageView)itemView.findViewById(R.id.imageView_pin);
            textViewtime=(AppCompatTextView) itemView.findViewById(R.id.textView_time);
            imageViewPoint=(AppCompatImageView)itemView.findViewById(R.id.imageView_cardpoint);
            mCardView.setOnClickListener(this);
            imageViewPinm.setOnClickListener(this);

            if (mTrashFragmentInterface != null) {
                mCardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        layout_card.setBackgroundColor(R.color.back_front_color);
                        mTrashFragmentInterface.getHideToolBar(true);
                        mTrashFragmentInterface.getCountIncreament(getAdapterPosition());
                        onclick=true;
                        return false;
                    }
                });

            }else {
            }

        }


        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.cardview_notes:
                    int position = getAdapterPosition();
                    if (mTrashFragmentInterface != null) {
                        if(onclick){
                            int color = Color.TRANSPARENT;
                            Drawable background = layout_card.getBackground();
                            if (background instanceof ColorDrawable)
                                color = ((ColorDrawable) background).getColor();
                            if(color==R.color.back_front_color){
                                if(mdisplayedtoDoItemModels.get(position).getColor()!=null) {
                                    layout_card.setBackgroundColor(Integer.parseInt(mdisplayedtoDoItemModels.get(position).getColor()));
                                }else {
                                    layout_card.setBackgroundColor(R.color.text_color_white);
                                }
                                mTrashFragmentInterface.getHideToolBar(true);
                                mTrashFragmentInterface.getCountDecreament(position);
                            }else{
                                layout_card.setBackgroundColor(R.color.back_front_color);
                                mTrashFragmentInterface.getHideToolBar(true);
                                mTrashFragmentInterface.getCountIncreament(position);
                            }
                        }
                    }else if(shareNote!=null) {
                        shareNote.shareNote(position);
                    }
                    else if(archiveFragmentInterface==null)
                     {
                        SharedPreferences pref = mContext.getSharedPreferences(Constants.ProfileeKey.SHAREDPREFERANCES_KEY, mContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        String mUserUID = pref.getString(Constants.BundleKey.USER_USER_UID, "null");
                        //List<ToDoItemModel> updateModels =  mContext).getUpdateModels();
                        Intent intent = new Intent(mContext, UpdateNoteActivity.class);
                        Bundle bun = new Bundle();
                        bun.putString(Constants.RequestParam.KEY_ID, String.valueOf(mdisplayedtoDoItemModels.get(position).getId()));
                        bun.putString(Constants.RequestParam.KEY_NOTE, mdisplayedtoDoItemModels.get(position).getNote());
                        bun.putString(Constants.RequestParam.KEY_TITLE, mdisplayedtoDoItemModels.get(position).getTitle());
                        bun.putString(Constants.RequestParam.KEY_REMINDER, mdisplayedtoDoItemModels.get(position).getReminder());
                        bun.putString(Constants.RequestParam.KEY_STARTDATE, mdisplayedtoDoItemModels.get(position).getStartdate());
                        bun.putString(Constants.RequestParam.KEY_ARCHIVE, mdisplayedtoDoItemModels.get(position).getArchive());
                        bun.putString(Constants.RequestParam.KEY_SETTIME, mdisplayedtoDoItemModels.get(position).getSettime());
                        bun.putString(Constants.RequestParam.KEY_COLOR, mdisplayedtoDoItemModels.get(position).getColor());
                         bun.putBoolean(Constants.RequestParam.KEY_PIN, mdisplayedtoDoItemModels.get(position).isPin());
                         bun.putInt(Constants.RequestParam.KEY_SRID, mdisplayedtoDoItemModels.get(position).getSrid());
                         intent.putExtra(Constants.BundleKey.USER_USER_UID, mUserUID);
                        intent.putExtra(Constants.BundleKey.MEW_NOTE, bun);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String transitionName =mContext.getString(R.string.transition_title_image);
                        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext, mCardView,transitionName);
                        ActivityCompat.startActivity((Activity)mContext, intent, options.toBundle());

                    }
                    break;
                case R.id.imageView_pin:
                    if(mTrashFragmentInterface==null && shareNote==null){

                    }
                    break;
            }
        }

        public void setOnclickListener() {
            mCardView.setOnClickListener(this);
        }
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

}