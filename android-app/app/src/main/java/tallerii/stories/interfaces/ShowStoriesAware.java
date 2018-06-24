package tallerii.stories.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.network.apimodels.Storie;

public interface ShowStoriesAware {
    void bindVideoViewAction();
    void bindToggleMediaAction();
    View getRootView(LayoutInflater inflater, ViewGroup container);
    void updateViewOnChangeMediaType();
}
