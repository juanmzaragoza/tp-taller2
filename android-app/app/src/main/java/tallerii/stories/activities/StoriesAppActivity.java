package tallerii.stories.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import static tallerii.stories.activities.ProfileActivity.PROFILE_ID;
import static tallerii.stories.activities.ProfileActivity.PROFILE_OBJECT;

public abstract class StoriesAppActivity extends AppCompatActivity {
    abstract protected Context getContext();

    @Override
    protected void onResume() {
        super.onResume();
        checkGooglePlayServices();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkGooglePlayServices();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultStatus = googleApi.isGooglePlayServicesAvailable(getContext());
        if(resultStatus != ConnectionResult.SUCCESS) {
            googleApi.getErrorDialog(this, resultStatus, 0).show();
        }
    }

    protected String getStringFrom(int resourceId) {
        TextView editText = findViewById(resourceId);
        return editText.getText().toString();
    }

    public void showMessage(String text) {
        log(text);
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void log(String text) {
        Log.i("TOAST","Shown text: " + text);
    }

    public void showMessage(int resId) {
        log("Of resource: " + resId);
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(String text, int length) {
        log(text);
        Toast.makeText(getContext(), text, length).show();
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void startMainActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }

    public void startMainActivity(String username, String token, String profileId) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        intent.putExtra(ProfileActivity.PROFILE_ID, profileId);
        intent.putExtra(StoriesLoggedInActivity.TOKEN, token);
        startActivity(intent);
        finish();
    }

    public void startRegistrationActivity(String username, long id) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra(RegistrationActivity.USERNAME, username);
        intent.putExtra(RegistrationActivity.FBID, id);
        startActivity(intent);
        finish();
    }

    public void startProfileUpdateActivity(String profile) {
        Intent intent = new Intent(this, UserProfileUpdateActivity.class);
        intent.putExtra(PROFILE_OBJECT, profile);
        startActivity(intent);
        finish();
    }

    public void startProfileUpdateActivity(String profileId, String token) {
        Intent intent = new Intent(this, UserProfileUpdateActivity.class);
        intent.putExtra(ProfileActivity.PROFILE_ID, profileId);
        intent.putExtra(StoriesLoggedInActivity.TOKEN, token);
        startActivity(intent);
        finish();
    }

    public void startProfileActivity(String profileId) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        startActivity(intent);
        finish();
    }

    public void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public static String abbreviate(final String text, int length) {
        // Obtained from https://stackoverflow.com/questions/3597550/ideal-method-to-truncate-a-string-with-ellipsis
        // The letters [iIl1] are slim enough to only count as half a character.
        length += Math.ceil(text.replaceAll("[^iIl]", "").length() / 2.0d);
        if (text.length() > length) {
            return text.substring(0, length - 3) + "...";
        }
        return text;
    }

    public void logOutFromFB() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
    }
}
