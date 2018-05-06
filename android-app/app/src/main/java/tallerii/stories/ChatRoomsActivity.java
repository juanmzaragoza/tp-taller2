package tallerii.stories;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.helpers.FriendsAdapter;
import tallerii.stories.network.apimodels.ApplicationProfile;
import tallerii.stories.network.apimodels.Friend;

import static tallerii.stories.ProfileActivity.PROFILE_OBJECT;

public class ChatRoomsActivity extends StoriesAppActivity {

    private ApplicationProfile profile;
    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // TODO refactor code to get profile like profile activities maybe?
        Bundle bundle = getIntent().getExtras();
        if (bundle == null && bundle.get(PROFILE_OBJECT) == null) {
            throw new IllegalArgumentException("Missing profile");
        }
        profile = new Gson().fromJson(bundle.getString(PROFILE_OBJECT), ApplicationProfile.class);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        populateFriendsRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFriendsRecyclerView();
    }

    private boolean switchContent(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                // change content
            case R.id.action_account:
                // change content
            case R.id.action_new:
                // change content
        }
        return true;
    }

    private void populateFriendsRecyclerView(){
        List<Friend> friends = profile.getFriends();
        friendsAdapter = new FriendsAdapter(
                friends != null && !friends.isEmpty() ? friends : new ArrayList<Friend>(),
                getContext(),
                profile.getUserId()
        );
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    @Override
    protected Context getContext() {
        return ChatRoomsActivity.this;
    }
}
