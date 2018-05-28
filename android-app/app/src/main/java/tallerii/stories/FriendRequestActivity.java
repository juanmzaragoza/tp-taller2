package tallerii.stories;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.controller.FriendRequestController;
import tallerii.stories.helpers.FriendRequestAdapter;
import tallerii.stories.network.apimodels.FriendRequest;

public class FriendRequestActivity extends StoriesLoggedInActivity {
    private RecyclerView friendsRecyclerView;
    private FriendRequestAdapter friendRequestAdapter;
    private FriendRequestController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        friendsRecyclerView = findViewById(R.id.friendsRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(mLayoutManager);
        controller = new FriendRequestController(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.getFriendRequests(getProfile().getUserId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.getFriendRequests(getProfile().getUserId());
    }

    public void populateFriendsRecyclerView(List<FriendRequest> friends){
        friendRequestAdapter = new FriendRequestAdapter(
                friends != null && !friends.isEmpty() ? friends : new ArrayList<FriendRequest>(),
                this,
                controller
        );
        friendsRecyclerView.setAdapter(friendRequestAdapter);
    }

    @Override
    protected Context getContext() {
        return FriendRequestActivity.this;
    }
}
