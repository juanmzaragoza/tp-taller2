package tallerii.stories.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tallerii.stories.R;
import tallerii.stories.controller.ProfileController;
import tallerii.stories.controller.ProfileUserController;
import tallerii.stories.helpers.DateUtils;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.Friend;
import tallerii.stories.network.apimodels.FriendRequest;

public class UserProfileActivity extends ProfileActivity {

    private ProfileUserController profileUserController;
    private Button actionButton;
    private String requestMessage;
    private TextView birthday;
    private TextView gender;

    @Override
    protected void initWithChildResource() {
        setContentView(R.layout.activity_user_profile);
        imageView = findViewById(R.id.profile_picture);
        actionButton = findViewById(R.id.profile_action_button);
        birthday = findViewById(R.id.birthday);
        gender = findViewById(R.id.gender);
        setTitle("Profile");
    }

    @Override
    public void initializeProfile(ApplicationProfile applicationProfile) {
        super.initializeProfile(applicationProfile);
        String birthdayText = applicationProfile.getBirthday();
        this.birthday.setText(birthdayText != null ? birthdayText : "Unknown");
        String gender = applicationProfile.getGender();
        this.gender.setText(gender  != null ? gender : "Unknown");
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
        fillFriends(applicationProfile);
    }

    private void fillFriends(ApplicationProfile applicationProfile) {
        LinearLayout friendsLayout = findViewById(R.id.friends_holder);
        for (Friend friend :applicationProfile.getFriends()) {
            addFriend(friendsLayout, friend);
        }
    }

    private void addFriend(LinearLayout friendsLayout, final Friend friend) {
        LinearLayout friendLayout = new LinearLayout(this);
        friendLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(85, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getPx(3), getPx(0), getPx(3), getPx(0));
        friendLayout.setLayoutParams(layoutParams);
        ImageView pictureView = getFriendPicture(friend);
        friendLayout.addView(pictureView);
        TextView nameView = getFriendName(friend);
        friendLayout.addView(nameView);

        friendsLayout.addView(friendLayout);
    }

    @NonNull
    private TextView getFriendName(Friend friend) {
        TextView nameView = new TextView(this);
        nameView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        nameView.setText(friend.getName());
        return nameView;
    }

    @NonNull
    private ImageView getFriendPicture(final Friend friend) {
        ImageView pictureView = new ImageView((this));
        pictureView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,85));
        imageHelper.setFirebaseImage(friend.getPicture(), pictureView);
        pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUpdate = new Intent(getContext(), UserProfileActivity.class);
                goToUpdate.putExtra(ProfileActivity.PROFILE_ID, friend.getUserId());
                getContext().startActivity(goToUpdate);
                finish();
            }
        });
        pictureView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return pictureView;
    }

    private int getPx(int value) {
        Resources r = getContext().getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
    }

    protected void initAsUser(ApplicationProfile applicationProfile) {
        actionButton.setText(R.string.edit_button);
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
        actionButton.setText(R.string.unfriend_button);
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
        actionButton.setText(R.string.befriend_button);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                befriend(applicationProfile);
            }
        });
    }

    private void befriend(final ApplicationProfile applicationProfile) {
        requestMessage = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Request message");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestMessage = input.getText().toString();
                createFriendRequest(applicationProfile, requestMessage);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void createFriendRequest(ApplicationProfile applicationProfile, String message) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setMessage(message);
        ApplicationProfile currentUser = getProfile();
        friendRequest.setFirstName(currentUser.getName());
        friendRequest.setLastName(currentUser.getLastName());
        friendRequest.setPicture(currentUser.getProfilePicture());
        friendRequest.setSenderUserId(currentUser.getUserId());
        friendRequest.setRcvUserId(applicationProfile.getUserId());
        friendRequest.setCreatedTime(DateUtils.getTimeFromTimestamp(DateUtils.getNowTime()));
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

    protected void setUserInfo(ApplicationProfile applicationProfile) {
        TextView username = findViewById(R.id.user_name);
        username.setText(applicationProfile.getFullName());
    }

    public void deactivateFriendButton() {
    }
}
