package com.example.team33.groupfinder.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.model.Group;
import com.example.team33.groupfinder.R;
import com.example.team33.groupfinder.volley.VolleySingleton;

import java.util.List;

/*
 * Created by abhijit on 11/20/16.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Group> mGroupList;
    private OnClickListener listener;

    public RecyclerViewAdapter(List<Group> groupList) {
        this.mGroupList = groupList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_card_layout, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Group group = mGroupList.get(position);

        CardViewHolder cardViewHolder = (CardViewHolder) holder;
        cardViewHolder.setName(group.getName());
        cardViewHolder.setMemberCount(group.getMemberCount(), group.getWho());
        cardViewHolder.setPhotoUrl(group.getGroupPhotoUrl());
        if (listener != null) {
            cardViewHolder.bindClickListener(listener, group);
        }
    }

    @Override
    public int getItemCount() {
        return mGroupList.size();
    }

    public static List<Group> getGroupList() {
        return mGroupList;
    }

    /**
     * Removes older data from mGroupList and update it.
     * Once the data is updated, notifies RecyclerViewAdapter.
     *
     * @param modelList list of groups
     */
    public void updateDataSet(List<Group> modelList) {
        this.mGroupList.clear();
        this.mGroupList.addAll(modelList);
        notifyDataSetChanged();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnClickListener {
        void onCardClick(Group group);

        //void onPosterClick(Group group);
    }

    /**
     * CardViewHolder will hold the layout of the each item in the RecyclerView.
     */
    private class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView name;
        private TextView memberCount;
        private NetworkImageView poster;

        /**
         * Class constructor.
         *
         * @param view layout of each item int the RecyclerView
         */
        CardViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.card_view);
            this.name = (TextView) view.findViewById(R.id.groupName);
            this.memberCount = (TextView) view.findViewById(R.id.groupMemberCount);
            this.poster = (NetworkImageView) view.findViewById(R.id.nivPoster);
        }

        /**
         * append name text to Title:
         *
         * @param name String of Title of movie
         */
        void setName(String name) {
            String t = "Title:\n" + name;
            this.name.setText(t);
        }

        /**
         * append year text to Release Year:
         *
         * @param year String of year of release
         */
        void setMemberCount(int memberCount, String who) {
            String y = memberCount + " " + who + " are in this group";
            this.memberCount.setText(y);
        }

        /**
         * Sends ImageRequest using volley using imageLoader and Cache.
         * This is pre-implemented feature of Volley to cache images for faster responses.
         * Check VolleySingleton class for more details.
         *
         * @param imageUrl URL to poster of the Group
         */
        void setPhotoUrl(String imageUrl) {
            ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
            this.poster.setImageUrl(imageUrl, imageLoader);
        }

        /**
         * @param listener {@link OnClickListener}
         * @param group
         */
        void bindClickListener(final OnClickListener listener, final Group group) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardClick(group);
                }
            });

           /* poster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onPosterClick(group);
                }
            });*/
        }
    }
}
