package tallerii.stories.interfaces;

import java.util.List;

import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.network.apimodels.Storie;

public interface StoriesAware {
    StoriesLoggedInActivity getLoggedInActivity();
    void populateStories(List<Storie> stories);
}
