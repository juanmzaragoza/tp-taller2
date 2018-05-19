package tallerii.stories.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.MainActivity;
import tallerii.stories.ProfileActivity;
import tallerii.stories.R;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.helpers.StoriesAdapter;
import tallerii.stories.network.apimodels.Storie;

public class HomeFragment extends Fragment {

    private StoriesController controller;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        controller = new StoriesController(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            controller.getStories(arguments.getString(ProfileActivity.PROFILE_ID));
        }

        // get root view and then access to objects like R.id.usernameView
        rootView = inflater.inflate(R.layout.fragment_stories, container, false);


        return rootView;
    }

    public void populateStories(List<Storie> storiesToPopulate) {

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        StoriesAdapter listAdapter = new StoriesAdapter(getActivity(), storiesToPopulate);
        listView.setAdapter(listAdapter);

        // notify data changes to list adapater
        listAdapter.notifyDataSetChanged();

    }
}
