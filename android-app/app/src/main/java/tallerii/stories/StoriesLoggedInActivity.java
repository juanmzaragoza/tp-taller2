package tallerii.stories;

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
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import tallerii.stories.controller.ProfileController;
import tallerii.stories.helpers.Store;
import tallerii.stories.network.apimodels.ApplicationProfile;

import static tallerii.stories.ProfileActivity.PROFILE_ID;
import static tallerii.stories.ProfileActivity.PROFILE_OBJECT;

public abstract class StoriesLoggedInActivity extends StoriesAppActivity {
    public static final String TOKEN = "token";
    public static final String TOKEN_STORE = "token";
    private static ApplicationProfile profile;

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
            final Store store = new Store();
            store.save(TOKEN_STORE, bundle.getString(TOKEN));
            FirebaseInstanceId.getInstance().getToken();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success
                    } else {
                        showMessage("Firebase anon auth failed.");
                    }
                }
            });
            if (bundle.getString(PROFILE_ID) != null) {
                ProfileController profileController = new ProfileController(StoriesLoggedInActivity.this);
                profileController.getUser(bundle.getString(PROFILE_ID));
            }
        }
        if (bundle != null && bundle.get(PROFILE_OBJECT) != null) {
            setProfile(new Gson().fromJson(bundle.getString(PROFILE_OBJECT), ApplicationProfile.class));
        }
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
                startMainActivity(profile.getFullName());
                return true;
            case R.id.action_chat:
                startChatRoomsActivity(profile);
                return true;
            case R.id.action_friend_requests:
                startFriendRequestsActivity(profile);
                return true;
            case R.id.action_profile:
                //startProfileActivity(profile.getId());
                startProfileActivity(profile);
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
}
