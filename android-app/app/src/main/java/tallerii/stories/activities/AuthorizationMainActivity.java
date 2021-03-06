package tallerii.stories.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.database.FirebaseDatabase;

import tallerii.stories.helpers.Store;
import tallerii.stories.network.ConstantsApplicationApiRest;

/*
 * -----------------
 * Troubleshooting
 * -----------------
 * To fix The key hash … does not match any stored key hashes
 * See https://stackoverflow.com/questions/29554338/the-key-hash-does-not-match-any-stored-key-hashes
 *
 */
public class AuthorizationMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;
        Store store = new Store();

        if ( StoriesLoggedInActivity.getProfile() != null) {
            activityIntent = new Intent(this, MainActivity.class);
        } else {// else not logged in
            // clean fb access token because we handle the access to the application

            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut();
            }
            //show login form
            activityIntent = new Intent(this, LoginActivity.class);
        }
        /*START TESTING SETTINGS*/
//        ConstantsApplicationApiRest.ROOT_URL= "http://192.168.0.76:5858/";
        /*END TESTING SETTINGS*/
        startActivity(activityIntent);
        finish();
    }

}
