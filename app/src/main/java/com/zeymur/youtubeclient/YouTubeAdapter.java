package com.zeymur.youtubeclient;

import com.zeymur.youtubeclient.R;
import com.zeymur.youtubeclient.model.Item;

import com.squareup.picasso.Picasso;

import androidx.paging.PagedListAdapter;
import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;

public class YouTubeAdapter extends PagedListAdapter<Item, YouTubeAdapter.ViewHolder> {
    private final Context context;
    private final YouTubeFragment.Listener listener;
    private final DateFormat dateTimeFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT);

    public static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Item>() {
                @Override
                public boolean areItemsTheSame(Item oldItem, Item newItem) {
                    return oldItem.getContentDetails().getVideoId().equals(newItem.getContentDetails().getVideoId());
                }
                @Override
                public boolean areContentsTheSame(Item oldItem, Item newItem) {
                    return oldItem.getContentDetails().getVideoId().equals(newItem.getContentDetails().getVideoId());
                }
            };

    public YouTubeAdapter(Context context, YouTubeFragment.Listener listener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Item item  = getItem(position);
        holder.item = item;

        if (item.getSnippet().getThumbnails() != null &&
                item.getSnippet().getThumbnails().getMedium() != null &&
                item.getSnippet().getThumbnails().getMedium().getUrl() != null &&
                !item.getSnippet().getThumbnails().getMedium().getUrl().isEmpty())
            Picasso.get().load(item.getSnippet().getThumbnails().getMedium().getUrl())
                    .error(R.drawable.loading_error).fit().centerCrop().into(holder.imgFeatured);
        else
            holder.imgFeatured.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));

        holder.txtTitle.setText(item.getSnippet().getTitle());
        holder.txtDate.setText(dateTimeFormat.format(item.getSnippet().getPublishedAt()));
        holder.view.setOnClickListener((view) -> {
            if (null != listener) listener.onYouTubeItemClicked(holder.item);
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final View view;
        final TextView txtTitle;
        final TextView txtDate;
        final ImageView imgFeatured;
        Item item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;

            txtTitle = view.findViewById(R.id.txtTitle);
            txtDate = view.findViewById(R.id.txtDate);
            imgFeatured = view.findViewById(R.id.imgFeatured);
        }
    }
}
