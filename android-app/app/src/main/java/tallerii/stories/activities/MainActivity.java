package tallerii.stories.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import tallerii.stories.R;
import tallerii.stories.fragments.main.AnotherFragment;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.fragments.main.PostStorieFragment;
import tallerii.stories.helpers.Store;

public class MainActivity extends StoriesLoggedInActivity {

    public static final String EXTRA_MESSAGE = "tallerii.stories.loginactivity.MESSAGE";

    private Fragment fragment;
    private FragmentManager fragmentManager;

    @Override
    protected Context getContext() {
        return MainActivity.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();

        // this is the first activity that show, so we get the token from login activity
        Intent intent = getIntent();
        if(intent.getStringExtra(TOKEN) != null) {
            final Store store = new Store();
            store.save("token", intent.getStringExtra(TOKEN));
        }
        if(intent.getStringExtra(EXTRA_MESSAGE) != null) {
            final Store store = new Store();
            showMessage(intent.getStringExtra(EXTRA_MESSAGE), 10);
        }
        // by default show home
        setHomeFragment(ProfileActivity.PROFILE_ID);
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();

        // get bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // on click one, change content
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    return switchContent(item);
                }
            });
    }

    private boolean switchContent(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                setHomeFragment(null); // if null -> take id from getProfile()
                break;
            case R.id.action_account:
                fragment = new AnotherFragment();
                break;
            case R.id.action_new:
                setPostStorieFragment();
                break;
        }
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        return true;
    }

    private void setHomeFragment(String profileId) {
        fragment = new HomeFragment();
        // get profile id and pass it to fragment
        Bundle bundleHomeFragment = getIntent().getExtras();
        bundleHomeFragment.putString(ProfileActivity.PROFILE_ID, profileId != null? profileId:getProfile().getUserId());
        fragment.setArguments(bundleHomeFragment);
    }

    private void setPostStorieFragment() {
        fragment = null;
        fragment = new PostStorieFragment();
    }

    private void commitFragment(Fragment fragment){
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
    }
}