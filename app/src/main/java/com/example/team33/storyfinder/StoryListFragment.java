package com.example.team33.storyfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class StoryListFragment extends Fragment {

    private RecyclerView mStoryRecyclerView;
    private StoryAdapter mAdapter;

    private Button searchButton;
    private EditText searchBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_list, container, false);

        mStoryRecyclerView = (RecyclerView) view
                .findViewById(R.id.story_recycler_view);
        mStoryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        searchBox = (EditText) view.findViewById(R.id.search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateUI(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        searchButton = (Button) view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(searchBox.getText().toString());
            }
        });


        updateUI(searchBox.getText().toString());

        return view;
    }

    private void updateUI(String s) {
        Stories stories = Stories.get(getActivity());
        List<Story> mStories = stories.getStoryList(s);
        if (mAdapter == null) {
            mAdapter = new StoryAdapter(mStories);
            mStoryRecyclerView.setAdapter(mAdapter);
        } else {

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        
        updateUI(searchBox.getText().toString());
    }

    private class StoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mStoryCardNum;
        private TextView mStoryName;
        private TextView mStoryDesc;
        private ImageView mStoryImage;

        private Story mStory;

        public StoryHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            mStoryCardNum = (TextView) itemView.findViewById(R.id.list_item_story_card_number);
            mStoryName = (TextView) itemView.findViewById(R.id.list_item_story_name);
            mStoryDesc = (TextView) itemView.findViewById(R.id.list_item_story_desc);
            //mStoryImage = (ImageView) itemView.findViewById(R.id.story_image_small);
        }

        public void bindStory(Story story) {
            mStory = story;
            mStoryCardNum.setText(mStory.getCardNumber());
            mStoryName.setText(mStory.getName());
            mStoryDesc.setText(mStory.getDescription());
            //mStoryImage.setImageResource(R.drawable.small_mail); // temp
        }

        @Override
        public void onClick(View v) {
            Intent intent = StoryActivity.newIntent(getActivity(), mStory.getId());
            startActivity(intent);
        }
    }

    private class StoryAdapter extends RecyclerView.Adapter<StoryHolder> {
        private List<Story> mStories;

        public StoryAdapter(List<Story> stories) {
            mStories = stories;

        }

            @Override
        public StoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_story, parent, false);
            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(StoryHolder holder, int position) {
            Story story = mStories.get(position);
            holder.bindStory(story);
        }

        @Override
        public int getItemCount() {
            return mStories.size();
        }

    }
}

