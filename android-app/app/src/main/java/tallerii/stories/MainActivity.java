package tallerii.stories;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import tallerii.stories.fragments.main.AnotherFragment;
import tallerii.stories.fragments.main.HomeFragment;
import tallerii.stories.fragments.main.PostStorieFragment;
import tallerii.stories.helpers.Store;

public class MainActivity extends StoriesLoggedInActivity {

    public static final String EXTRA_MESSAGE = "tallerii.stories.loginactivity.MESSAGE";
    public static final String TOKEN = "token";

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

        // by default show home
        setHomeFragment();
        commitFragment(fragment);

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
                setHomeFragment();
                break;
            case R.id.action_account:
                fragment = new AnotherFragment();
                break;
            case R.id.action_new:
                setPostStorieFragment();
                break;
        }
        commitFragment(fragment);
        return true;
    }

    private void setHomeFragment() {
        // get profile id and pass it to fragment
        Bundle bundleHomeFragment = getIntent().getExtras();
        bundleHomeFragment.putString(ProfileActivity.PROFILE_ID,getProfile().getUserId());

        fragment = null;
        fragment = new HomeFragment();
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
