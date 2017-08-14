package com.example.team33.groupfinder.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.team33.groupfinder.R;
import com.example.team33.groupfinder.app.App;
import com.example.team33.groupfinder.model.Group;
import com.example.team33.groupfinder.volley.VolleySingleton;

import java.util.List;

/**
 * Created by Teng on 12/11/16
 */


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static List<Group> mGroupList;
    private OnClickListener listener;

    public RecyclerViewAdapter(List<Group> groupList) {
        this.mGroupList = groupList;
    }

    public static List<Group> getGroupList() {
        return mGroupList;
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

    }

    /**
     * CardViewHolder will hold the layout of the each item in the RecyclerView.
     */
    private class CardViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;
        private TextView name;
        private TextView memberCount;
        private NetworkImageView groupPhoto;

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
            this.groupPhoto = (NetworkImageView) view.findViewById(R.id.groupPhoto);
        }

        /**
         * @param name String of group name
         */
        void setName(String name) {
            this.name.setText(name);
        }

        /**
         * append memberCount and who
         *
         * @param memberCount int number of members in the group
         * @param who         String of what the group considers themselves
         */
        void setMemberCount(int memberCount, String who) {
            String m = "We're " + memberCount + " " + who;
            this.memberCount.setText(m);
        }

        /**
         * Sends ImageRequest using volley using imageLoader and Cache.
         * This is pre-implemented feature of Volley to cache images for faster responses.
         * Check VolleySingleton class for more details.
         *
         * @param imageUrl URL to groupPhoto of the Group
         */
        void setPhotoUrl(String imageUrl) {
            ImageLoader imageLoader = VolleySingleton.getInstance(App.getContext()).getImageLoader();
            this.groupPhoto.setImageUrl(imageUrl, imageLoader);
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
        }
    }
}
