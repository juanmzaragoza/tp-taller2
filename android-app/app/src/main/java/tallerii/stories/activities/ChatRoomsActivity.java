package tallerii.stories.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.R;
import tallerii.stories.helpers.FriendsAdapter;
import tallerii.stories.network.apimodels.Friend;

public class ChatRoomsActivity extends StoriesLoggedInActivity {
    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Conversations");
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

    private void populateFriendsRecyclerView(){
        List<Friend> friends = getProfile().getFriends();
        friendsAdapter = new FriendsAdapter(
                friends != null && !friends.isEmpty() ? friends : new ArrayList<Friend>(),
                getContext(),
                getProfile().getUserId()
        );
        friendsRecyclerView.setAdapter(friendsAdapter);
    }

    @Override
    protected Context getContext() {
        return ChatRoomsActivity.this;
    }
}
