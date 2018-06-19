package tallerii.stories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import tallerii.stories.R;
import tallerii.stories.controller.ProfileController;
import tallerii.stories.helpers.MyFirebaseInstanceIdService;
import tallerii.stories.helpers.Store;
import tallerii.stories.network.apimodels.ApplicationProfile;

import static tallerii.stories.activities.ProfileActivity.PROFILE_ID;
import static tallerii.stories.activities.ProfileActivity.PROFILE_OBJECT;

public abstract class StoriesLoggedInActivity extends StoriesAppActivity {
    public static final String TOKEN = "token";
    public static final String TOKEN_STORE = "token";
    private static ApplicationProfile profile;
    private static Store store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login();
    }

    @Override
    protected void onResume() {
        super.onResume();
        login();
    }

    private void login() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getString(TOKEN) != null) {
            store = new Store();
            store.save(TOKEN_STORE, bundle.getString(TOKEN));
            FirebaseInstanceId.getInstance().getToken();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        showMessage("Firebase anon auth failed.");
                    }
                    // Sign in success
                }
            });
            String profileId = bundle.getString(PROFILE_ID);
            if (profileId != null) {
                updateProfile(profileId);
                sendTokenToServer(profileId);
            }
        }
        if (bundle != null && bundle.get(PROFILE_OBJECT) != null) {
            setProfile(new Gson().fromJson(bundle.getString(PROFILE_OBJECT), ApplicationProfile.class));
        }
    }


    private void sendTokenToServer(String currentUserId) {
        final DatabaseReference usersRef = MyFirebaseInstanceIdService.getTokensRef();
        usersRef.child(currentUserId).setValue(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateProfile(String profileId) {
        ProfileController profileController = new ProfileController(StoriesLoggedInActivity.this);
        profileController.getUser(profileId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stories_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                startMainActivity();
                return true;
            case R.id.action_chat:
                startLoggedInActivity(ChatRoomsActivity.class);
                return true;
            case R.id.action_friend_requests:
                startLoggedInActivity(FriendRequestActivity.class);
                return true;
            case R.id.action_profile:
                startLoggedInActivity(UserProfileActivity.class);
                return true;
            case R.id.action_search:
                startLoggedInActivity(SearchUsersActivity.class);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            case R.id.stories_map:
                startLoggedInActivity(MapsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startLoggedInActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(PROFILE_OBJECT, new Gson().toJson(profile));
        startActivity(intent);
        finish();
    }

    private void logout() {
        profile = null;
        new Store().remove(TOKEN_STORE);
        LoginManager.getInstance().logOut();
        startLoginActivity();
    }

    public void initializeProfile(ApplicationProfile applicationProfile) {
        setProfile(applicationProfile);
    }

    public static void setProfile(ApplicationProfile applicationProfile) {
        profile = applicationProfile;
    }

    public static ApplicationProfile getProfile() {
        return profile;
    }

    public static String getToken() {
        return store != null ? store.get(TOKEN_STORE) : null;
    }

    public void updateProfile() {
        updateProfile(getProfile().getUserId());
    }
}
