package tallerii.stories.helpers;

import android.text.TextUtils;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

import tallerii.stories.network.apimodels.Users;

class UsersFilter extends Filter {

    private UsersAdapter adapter;
    private List<Users> filterList;

    public UsersFilter(List<Users> usersList, UsersAdapter usersAdapter) {
        this.adapter = usersAdapter;
        this.filterList = usersList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (!TextUtils.isEmpty(constraint)) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<Users> filteredPlayers = new ArrayList<>();
            for (Users user : filterList) {
                if (user.getFullName().toUpperCase().contains(constraint)) {
                    filteredPlayers.add(user);
                }
            }

            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.setUsersList((List<Users>)results.values);
        adapter.notifyDataSetChanged();
    }
}
