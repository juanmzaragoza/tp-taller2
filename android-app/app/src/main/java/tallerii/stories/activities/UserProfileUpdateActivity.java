package tallerii.stories.activities;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.util.UUID;

import tallerii.stories.R;
import tallerii.stories.controller.ProfileController;
import tallerii.stories.controller.ProfileUpdateController;
import tallerii.stories.fragments.profile.DatePickerFragment;
import tallerii.stories.helpers.DateUtils;
import tallerii.stories.helpers.ImageHelper;
import tallerii.stories.network.apimodels.ApplicationProfile;

public class UserProfileUpdateActivity extends ProfileActivity {

    private Uri filePath = null;
    private EditText firstName;
    private EditText lastName;
    private TextView birthday;
    private EditText gender;

    private Runnable onSuccess = new Runnable() {
        @Override
        public void run() {
            ((ProfileUpdateController)controller).putApplicationProfile(getProfile());
        }
    };

    private Runnable onError = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    protected void initWithChildResource() {
        setContentView(R.layout.activity_user_update_profile);
        imageView = findViewById(R.id.profile_picture);
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        birthday = findViewById(R.id.birthday);
        gender = findViewById(R.id.gender);
        setTitle("Update Profile");
    }

    @Override
    public void initializeProfile(ApplicationProfile applicationProfile) {
        super.initializeProfile(applicationProfile);
        String birthdayText = applicationProfile.getBirthday();
        String birthday = !TextUtils.isEmpty(birthdayText) ? birthdayText :
                DateUtils.getTimeFromTimestamp(DateUtils.getNowTime());
        this.birthday.setText(birthday);
        String gender = applicationProfile.getGender();
        if (gender != null){
            this.gender.setText(gender);
        }
    }

    @Override
    protected void setUserInfo(ApplicationProfile applicationProfile) {
        firstName.setText(applicationProfile.getName());
        lastName.setText(applicationProfile.getLastName());
    }


    public void chooseImage(View view) {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , 1);
    }

    public void takePhoto(View view) {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
            case 1:
                if(resultCode == RESULT_OK){
                    filePath = imageReturnedIntent.getData();
                    imageView.setImageURI(null);
                    imageView.setImageURI(filePath);
                }
                break;
        }
    }

    @Override
    protected Context getContext() {
        return UserProfileUpdateActivity.this;
    }

    @Override
    protected ProfileController getNewController() {
        return new ProfileUpdateController(this);
    }

    public void upload(View view) {

        ImageHelper imageHelper = new ImageHelper(getContext());

        ApplicationProfile applicationProfile = getProfile();
        applicationProfile.setFirstName(getStringFrom(R.id.first_name));
        applicationProfile.setLastName(getStringFrom(R.id.last_name));
        applicationProfile.setGender(getStringFrom(R.id.gender));
        applicationProfile.setBirthday(getStringFrom(R.id.birthday));
        if (filePath != null) {
            applicationProfile.setProfilePicture(UUID.randomUUID().toString().replace("-", ""));
            imageHelper.uploadMedia(applicationProfile.getProfilePicture(), filePath, onSuccess, onError);//updates profile only if picture loaded
            filePath = null;
        } else {
            ((ProfileUpdateController)controller).putApplicationProfile(getProfile());
        }
    }

    public void chooseDate(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putString("date", getStringFrom(R.id.birthday));
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datePicker");
    }
}
