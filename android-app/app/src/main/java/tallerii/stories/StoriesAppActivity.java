package tallerii.stories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import tallerii.stories.network.apimodels.ApplicationProfile;

import static tallerii.stories.ProfileActivity.PROFILE_OBJECT;

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
    }

    private void checkGooglePlayServices() {
        GoogleApiAvailability googleApi = GoogleApiAvailability.getInstance();
        int resultStatus = googleApi.isGooglePlayServicesAvailable(getContext());
        if(resultStatus != ConnectionResult.SUCCESS) {
            googleApi.getErrorDialog(this, resultStatus, 0).show();
        }
    }

    protected String getStringFrom(int resourceId) {
        EditText editText = findViewById(resourceId);
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

    public void startMainActivity(String username) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        startActivity(intent);
        finish();
    }

    public void startMainActivity(String username, String token) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, username);
        intent.putExtra(MainActivity.TOKEN, token);
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

    public void startProfileActivity(String profile) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(PROFILE_OBJECT, profile);
        startActivity(intent);
        finish();
    }

    public void startProfileActivity(ApplicationProfile profile) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra(PROFILE_OBJECT, new Gson().toJson(profile));
        startActivity(intent);
        finish();
    }

    public void startChatRoomsActivity(ApplicationProfile profile) {
        Intent intent = new Intent(this, ChatRoomsActivity.class);
        intent.putExtra(PROFILE_OBJECT, new Gson().toJson(profile));
        startActivity(intent);
        finish();
    }
}
