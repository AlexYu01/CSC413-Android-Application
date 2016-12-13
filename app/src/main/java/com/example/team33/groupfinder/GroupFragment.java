package com.example.team33.groupfinder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.team33.groupfinder.adapter.RecyclerViewAdapter;
import com.example.team33.groupfinder.model.Group;



public class GroupFragment extends Fragment {

    private static final String ARG_STORY_ID = "story_id";

    private Group mGroup;
    private EditText mNameField;
    private EditText mDescField;


    public static GroupFragment newInstance(String storyId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_STORY_ID, storyId);

        GroupFragment fragment = new GroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String storyId = (String) getArguments().getSerializable(ARG_STORY_ID);
        for (Group group : RecyclerViewAdapter.getGroupList()) {
            group.getGroupId().equals(storyId);
            mGroup = group;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_story, container, false);

        //mNameField = (EditText) v.findViewById(R.id.story_title);
        //mDescField = (EditText) v.findViewById(R.id.story_desc);

        //mNameField.setText(mStory.getName());
        //mDescField.setText(mStory.getDescription());

        /* TextWatcher generalTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mNameField.getText().hashCode() == s.hashCode()) {
                    mStory.setName(s.toString());
                } else if (mDescField.getText().hashCode() == s.hashCode()) {
                    mStory.setDescription(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mNameField.addTextChangedListener(generalTextWatcher);
        mDescField.addTextChangedListener(generalTextWatcher); */


        return  v;
    }


}
