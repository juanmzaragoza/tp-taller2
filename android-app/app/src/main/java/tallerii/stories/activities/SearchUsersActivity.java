package tallerii.stories.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.R;
import tallerii.stories.controller.ListUsersController;
import tallerii.stories.helpers.UsersAdapter;
import tallerii.stories.network.apimodels.Users;

public class SearchUsersActivity extends StoriesLoggedInActivity {
    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private ListUsersController controller;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        setTitle("Discover Users");
        usersRecyclerView = findViewById(R.id.friendsRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        usersRecyclerView.setLayoutManager(mLayoutManager);
        controller = new ListUsersController(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = findViewById(R.id.mSearch);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                usersAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                usersAdapter.getFilter().filter(query);
                return false;
            }
        });
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
