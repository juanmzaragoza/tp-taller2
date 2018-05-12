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
import tallerii.stories.helpers.Store;
import tallerii.stories.StoriesLoggedInActivity;

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

        //TODO: why we do this
        Intent intent = getIntent();
        if(intent.getStringExtra(TOKEN) != null) {
            final Store store = new Store();
            store.save("token", intent.getStringExtra(TOKEN));
        }

        // by default show home
        fragment = new HomeFragment();
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
                setHomeFragment();
                break;
            case R.id.action_account:
                fragment = new AnotherFragment();
            case R.id.action_new:
                fragment = new AnotherFragment();
        }
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_container, fragment).commit();
        return true;
    }

    private void setHomeFragment() {
        fragment = new HomeFragment();
        fragment.setArguments(getIntent().getExtras());
    }
}
