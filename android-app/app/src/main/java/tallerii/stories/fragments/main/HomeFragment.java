package tallerii.stories.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.activities.ProfileActivity;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.helpers.StoriesAdapter;
import tallerii.stories.interfaces.StoriesAware;
import tallerii.stories.network.apimodels.Storie;

public class HomeFragment extends Fragment implements StoriesAware {

    private StoriesController controller;
    private View rootView;
    private HelperFragment helperFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        helperFragment = new HelperFragment(getContext());
        controller = new StoriesController(this);

        // get root view and then access to objects like R.id.usernameView
        rootView = inflater.inflate(R.layout.fragment_stories, container, false);

        getStories();

        return rootView;
    }

    private void getStories(){
        Bundle arguments = getArguments();
        if (arguments != null) {
            controller.getStories(arguments.getString(ProfileActivity.PROFILE_ID));
            helperFragment.showMessageLoading("Wait while loading stories...");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getStories();
    }

    @Override
    public StoriesLoggedInActivity getLoggedInActivity() {
        return (StoriesLoggedInActivity) getActivity();
    }

    @Override
    public void populateStories(List<Storie> storiesToPopulate) {

        ListView listView = rootView.findViewById(R.id.list);

        StoriesAdapter listAdapter = new StoriesAdapter(getActivity(), getContext(), controller, storiesToPopulate);
        listView.setAdapter(listAdapter);

        // notify data changes to list adapater
        listAdapter.notifyDataSetChanged();

        helperFragment.dismissMessageLoading();

    }
}
