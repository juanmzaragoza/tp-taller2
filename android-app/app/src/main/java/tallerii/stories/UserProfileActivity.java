package tallerii.stories;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.controller.ProfileUserController;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.FriendRequest;

public class UserProfileActivity extends ProfileActivity {

    private ProfileUserController profileUserController;
    private Button actionButton;

    @Override
    protected void initWithChildResource() {
        setContentView(R.layout.activity_user_profile);
        imageView = findViewById(R.id.profile_picture);
        actionButton = findViewById(R.id.profile_action_button);
        setTitle("Profile");
    }

    @Override
    public void initializeProfile(ApplicationProfile applicationProfile) {
        super.initializeProfile(applicationProfile);
        String type = applicationProfile.getType();
        if (type == null) return;
        switch (type) {
            case "User":
                initAsUser(applicationProfile);
                break;
            case "Friend":
                initAsFriend();
                break;
            case "Stranger":
                initAsStranger(applicationProfile);
                break;
            case "StrangerPendingRequest":
                initAsStrangerPendingRequest();
                break;
        }
    }

    protected void initAsUser(ApplicationProfile applicationProfile) {
        actionButton.setText("Edit");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProfileUpdateActivity(new Gson().toJson(getProfile()));
            }
        });
    }

    private void initAsStrangerPendingRequest() {
        actionButton.setText("Pending");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {/*noop*/}
        });
    }

    private void initAsStranger(final ApplicationProfile applicationProfile) {
        actionButton.setText("Befriend");
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                befriend(applicationProfile);
            }
        });
    }

    private void befriend(ApplicationProfile applicationProfile) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setMessage("Placeholder message");
        ApplicationProfile currentUser = getProfile();
        friendRequest.setFullName(currentUser.getFullName());
        friendRequest.setPicture(currentUser.getProfilePicture());
        friendRequest.setSenderUserId(currentUser.getId());
        friendRequest.setRcvUserId(applicationProfile.getId());
        profileUserController.requestFriendship(friendRequest);
    }

    private void initAsFriend() {
    }

    @Override
    protected ProfileController getNewController() {
        profileUserController = new ProfileUserController(this);
        return profileUserController;
    }

    @Override
    protected Context getContext() {
        return UserProfileActivity.this;
    }

    protected void setUserName(ApplicationProfile applicationProfile) {
        TextView username = findViewById(R.id.user_name);
        username.setText(applicationProfile.getFullName());
    }

    public void deactivateFriendButton() {
    }
}
