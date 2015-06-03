
package com.laquysoft.gdghunt.nichel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.laquysoft.gdghunt.R;
import com.laquysoft.gdghunt.rest.model.HuntModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HuntAdapter extends RecyclerView.Adapter<HuntAdapter.ViewHolder> {
    private final String TAG = HuntAdapter.class.getCanonicalName();

    private Context context;
    private ArrayList<HuntModel> model;
    private OnNewHuntListener listener = null;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName;
        public final ImageView ivImage;
        public final ImageButton btnDown;
        public final ImageButton btnPlay;

        public ViewHolder(View view) {
            super(view);

            tvName = (TextView) view.findViewById(R.id.tvName);
            ivImage = (ImageView) view.findViewById(R.id.ivImage);
            btnDown = (ImageButton) view.findViewById(R.id.btnDownload);
            btnPlay = (ImageButton) view.findViewById(R.id.btnPlay);
        }
    }

    public HuntAdapter(Context context, OnNewHuntListener listener) {
        this.context = context;
        this.model = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public HuntAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_hunt, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;
        final boolean available = (model.get(index).getId().equals("13")); //FIXME - nichel - abilito solo l'hunt di berlino, id = 13
        final boolean downloaded = model.get(index).isDownloaded();
        final String huntid = model.get(index).getId();
        final String name = model.get(index).getDisplayName();
        final String image_url = model.get(index).getImageUrl();

        holder.tvName.setText(name);

        if (!"".equals(image_url)) {
            Picasso.with(context).load(image_url).into(holder.ivImage);
        }

        holder.btnDown.setVisibility((available) ? View.VISIBLE : View.INVISIBLE);
        holder.btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "DOWNLOAD Hunt POS: " + index);

                listener.downloadHunt(index);
            }
        });

        holder.btnPlay.setVisibility((available) ? View.VISIBLE : View.INVISIBLE);
        holder.btnPlay.setEnabled(available && downloaded);
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "CLICK on Hunt ID: " + huntid);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getString(R.string.areuready));

                builder.setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        listener.startHunt(huntid);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(context.getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public void clear() {
        model.clear();
    }

    public void setModel(ArrayList<HuntModel> items) {
        model.addAll(items);
    }

    public interface OnNewHuntListener {
        void downloadHunt(int position);
        void startHunt(String huntid);
    }
}
