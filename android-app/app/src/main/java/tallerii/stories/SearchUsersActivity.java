package tallerii.stories;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.controller.ListUsersController;
import tallerii.stories.helpers.UsersAdapter;
import tallerii.stories.network.apimodels.Users;

public class SearchUsersActivity extends StoriesLoggedInActivity {
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private ListUsersController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        setTitle("Friend requests");
        usersRecyclerView = findViewById(R.id.friendsRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(mLayoutManager);
        controller = new ListUsersController(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        controller.getUsers(getProfile().getUserId());
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.getUsers(getProfile().getUserId());
    }

    public void populateUsersRecyclerView(List<Users> friends){
        usersAdapter = new UsersAdapter(
                friends != null && !friends.isEmpty() ? friends : new ArrayList<Users>(),
                this
        );
        usersRecyclerView.setAdapter(usersAdapter);
    }

    @Override
    protected Context getContext() {
        return SearchUsersActivity.this;
    }
}
