package tallerii.stories;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.helpers.ImageHelper;
import tallerii.stories.network.apimodels.ApplicationProfile;

public abstract class ProfileActivity extends StoriesLoggedInActivity {
    public static final String PROFILE_OBJECT = "profile";
    public static final String PROFILE_ID = "profileId";

    protected ImageView imageView;
    protected ProfileController controller;
    private ImageHelper imageHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWithChildResource();
        controller = getNewController();
        imageHelper = new ImageHelper(getContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(PROFILE_ID) != null) {
            controller.getUser(bundle.getString(PROFILE_ID));
        }
    }

    protected abstract void initWithChildResource();

    public void initializeProfile(ApplicationProfile applicationProfile) {
        String type = applicationProfile.getType();
        if(type == null || type.equals("User")){
            super.initializeProfile(applicationProfile);
        }
        TextView friendsCount = findViewById(R.id.friend_count);
        friendsCount.setText(String.valueOf(applicationProfile.getFriends().size()));
        setUserName(applicationProfile);
        imageHelper.setFirebaseImage(applicationProfile.getProfilePicture(), imageView);
    }

    abstract protected void setUserName(ApplicationProfile applicationProfile);

    abstract protected ProfileController getNewController();
}
