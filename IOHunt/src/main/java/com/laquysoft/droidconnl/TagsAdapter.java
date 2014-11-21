package com.laquysoft.droidconnl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by joaobiriba on 23/10/14.
 */
public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    private final Context mContext;

    private Hunt mHunt;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public ImageView mImageFounded;

        public ViewHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.imageView);
            mImageFounded = (ImageView) v.findViewById(R.id.foundedIcon);
            mTextView = (TextView) v.findViewById(R.id.textView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TagsAdapter(Context context, Hunt hunt)
    {
        mHunt = hunt;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        AHTag theTag = mHunt.tagList.get(position);
        holder.mTextView.setText("Tag " +  position);
        if (mHunt.isTagFound(theTag.id)) {
            holder.mImageFounded.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_black_18dp));
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mHunt.tagList.size();
    }
}