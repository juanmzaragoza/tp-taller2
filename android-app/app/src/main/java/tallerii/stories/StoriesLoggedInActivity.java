package tallerii.stories;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import tallerii.stories.helpers.Store;
import tallerii.stories.network.apimodels.ApplicationProfile;

import static tallerii.stories.ProfileActivity.PROFILE_OBJECT;

public abstract class StoriesLoggedInActivity extends StoriesAppActivity {
    public static final String TOKEN = "token";
    private static ApplicationProfile profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if(bundle != null && bundle.getString(TOKEN) != null) {
            final Store store = new Store();
            store.save("token", bundle.getString(TOKEN));
        }
        if (bundle != null && bundle.get(PROFILE_OBJECT) != null) {
            setProfile(new Gson().fromJson(bundle.getString(PROFILE_OBJECT), ApplicationProfile.class));
        }
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
            case R.id.friend_requests:
                startFriendRequestsActivity(profile);
                return true;
            case R.id.action_profile:
                //startProfileActivity(profile.getId());
                startProfileActivity(profile);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void setProfile(ApplicationProfile newProfile) {
        profile = newProfile;
    }

    public ApplicationProfile getProfile() {
        return profile;
    }
}
