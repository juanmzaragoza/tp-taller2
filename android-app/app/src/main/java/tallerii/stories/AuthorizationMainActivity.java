package tallerii.stories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import tallerii.stories.helpers.Store;
import tallerii.stories.network.ConstantsApplicationApiRest;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.Friend;

import static tallerii.stories.ProfileActivity.PROFILE_ID;
import static tallerii.stories.ProfileActivity.PROFILE_OBJECT;

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
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Intent activityIntent;
        Store store = new Store();

        /* TODO:  we have to validate that token is not expired, for now we will eliminate it */
        store.remove("token");
        // go straight to main if a token is stored
        if ( store.get("token") != null) {
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
        //ConstantsApplicationApiRest.ROOT_URL= "http://192.168.0.8:5858/";
        /*END TESTING SETTINGS*/
        startActivity(activityIntent);
        finish();
    }

}
