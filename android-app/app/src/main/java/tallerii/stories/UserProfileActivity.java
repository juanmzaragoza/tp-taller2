package tallerii.stories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.controller.ProfileUserController;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.Friend;
import tallerii.stories.network.apimodels.FriendRequest;

public class UserProfileActivity extends ProfileActivity {

    private ProfileUserController profileUserController;
    private Button actionButton;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

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
        ApplicationProfile.ProfileType type = applicationProfile.getType();
        if (type == null) return;
        switch (type) {
            case USER:
                initAsUser(applicationProfile);
                break;
            case FRIEND:
                initAsFriend(applicationProfile);
                break;
            case STRANGER:
                initAsStranger(applicationProfile);
                break;
            case STRANGER_PENDING_REQUEST:
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

    private void initAsFriend(ApplicationProfile applicationProfile) {
        String friendshipId = "";
        for (Friend friends: getProfile().getFriends()) {
            if (friends.getUserId().equals(applicationProfile.getUserId())) {
                friendshipId = friends.getId();
            }
        }
        actionButton.setText("Unfriend");
        actionButton.setOnClickListener(createOnClickListener(friendshipId, applicationProfile.getUserId()));
    }

    @NonNull//had to in order to declare final
    private View.OnClickListener createOnClickListener(final String friendshipId, final String friendUserId) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfriend(friendshipId, friendUserId);
            }
        };
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
        friendRequest.setFirstName(currentUser.getName());
        friendRequest.setLastName(currentUser.getLastName());
        friendRequest.setPicture(currentUser.getProfilePicture());
        friendRequest.setSenderUserId(currentUser.getUserId());
        friendRequest.setRcvUserId(applicationProfile.getUserId());
        friendRequest.setCreatedTime(SIMPLE_DATE_FORMAT.format(Calendar.getInstance().getTime()));
        profileUserController.requestFriendship(friendRequest);
    }

    private void unfriend(String friendshipId, String friendUserId) {
        profileUserController.unfriend(friendshipId, friendUserId);
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
