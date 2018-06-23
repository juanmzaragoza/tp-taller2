package tallerii.stories.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.network.apimodels.Storie;

public interface StoriesAware {
    StoriesLoggedInActivity getLoggedInActivity();
    void populateStories(List<Storie> stories);
    View getRootView(LayoutInflater inflater, ViewGroup container);
}
