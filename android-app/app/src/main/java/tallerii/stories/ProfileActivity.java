package tallerii.stories;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.network.apimodels.ApplicationProfile;

public abstract class ProfileActivity extends StoriesAppActivity {
    public static final String PROFILE_OBJECT = "profile";
    public static final String PROFILE_ID = "profileId";

    protected final int PICK_IMAGE_REQUEST = 71;
    protected ImageView imageView;
    protected StorageReference storageReference;
    protected ProfileController controller;

    protected ApplicationProfile applicationProfile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWithChildResource();
        controller = getController();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //TODO refactor this to parent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.get(PROFILE_OBJECT) != null) {
            ApplicationProfile profile = new Gson().fromJson(bundle.getString(PROFILE_OBJECT), ApplicationProfile.class);
            initializeProfile(profile);
        } else if (bundle != null && bundle.get(PROFILE_ID) != null) {
            controller.getUser(bundle.getString(PROFILE_ID));
        }
    }

    protected abstract void initWithChildResource();

    public void initializeProfile(ApplicationProfile applicationProfile) {
        this.applicationProfile = applicationProfile;
        TextView friendsCount = findViewById(R.id.friend_count);
        friendsCount.setText(String.valueOf(applicationProfile.getFriends().size()));
        setUserName(applicationProfile);

        //try to obtain profile pic from Firebase
        Glide.with(this /* context */)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images/" + applicationProfile.getProfilePicture()))
                .into(imageView)
        ;
    }

    abstract protected void setUserName(ApplicationProfile applicationProfile);

    abstract protected ProfileController getController();
}
