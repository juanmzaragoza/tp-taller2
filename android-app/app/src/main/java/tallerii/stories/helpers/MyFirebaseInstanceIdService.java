package tallerii.stories.helpers;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import tallerii.stories.StoriesLoggedInActivity;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken != null) {
            Log.d(TAG, "Refreshed token: " + refreshedToken);
            sendTokenToServer(refreshedToken);
        }
    }

    public void sendTokenToServer(final String token) {
        final DatabaseReference usersRef = getTokensRef();
        String currentUserId = StoriesLoggedInActivity.getProfile().getUserId();
        usersRef.child(currentUserId).setValue(token);
    }

    public static DatabaseReference getTokensRef() {
        return FirebaseDatabase.getInstance().getReference("firebaseTokens");
    }
}