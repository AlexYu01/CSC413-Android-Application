package com.example.team33.storyfinder;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Stories {

    private static Stories sStories;

    private ArrayList<Story> mStorylist;
    private ArrayList<Story> mFilteredList;

    private Stories(Context context) {
        mStorylist = new ArrayList<>();
        mFilteredList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            Story story = new Story();
            story.setCardNumber("Card #" + i);
            mStorylist.add(story);
        }
    }

    public static Stories get(Context context) {
        if (sStories == null) {
            sStories = new Stories(context);
        }
        return sStories;
    }

    public ArrayList<Story> getFilteredList() {
        return mFilteredList;
    }

    public List<Story> getStoryList() {
        return mStorylist;
    }

    public List<Story> getStoryList(String constraint) {
        mFilteredList.clear();
        if (constraint.length() == 0) {
            mFilteredList.addAll(mStorylist);
        } else {
            final String filterPattern = constraint.toString().toLowerCase().trim();
            String storyName;
            for (final Story mStory : mStorylist) {
                storyName = mStory.getName();
                if(storyName != null) {
                    if (storyName.toLowerCase().contains(filterPattern)) {
                        Story aStory = new Story(mStory.getId(), mStory.getCardNumber(),
                                mStory.getName(), mStory.getDescription(), mStory.getImage());
                        mFilteredList.add(aStory);
                    }
                }
            }
        }
        return mFilteredList;
    }

    public Story getStory(UUID id) {
        for (Story story : mStorylist) {
            if (story.getId().equals(id)) {
                return story;
            }
        }
        return null;
    }
}
