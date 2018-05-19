package tallerii.stories;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class UserProfileActivity extends ProfileActivity {

    @Override
    protected void initWithChildResource() {
        setContentView(R.layout.activity_user_profile);
        imageView = findViewById(R.id.profile_picture);
    }

    @Override
    protected ProfileController getController() {
        return new ProfileController(this);
    }

    @Override
    protected Context getContext() {
        return UserProfileActivity.this;
    }

    protected void setUserName(ApplicationProfile applicationProfile) {
        TextView username = findViewById(R.id.user_name);
        username.setText(applicationProfile.getFullName());
    }

    public void editProfile(View dummy) {
        startProfileUpdateActivity(new Gson().toJson(getProfile()));
    }
}
