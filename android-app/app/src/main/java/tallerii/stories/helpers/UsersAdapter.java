package tallerii.stories.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tallerii.stories.activities.ProfileActivity;
import tallerii.stories.R;
import tallerii.stories.activities.UserProfileActivity;
import tallerii.stories.network.apimodels.Users;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private final ImageHelper imageHelper;
    private List<Users> usersList;
    private Context context;

    public UsersAdapter(List<Users> dataSet, Context context) {
        this.usersList = dataSet;
        this.context = context;
        this.imageHelper = new ImageHelper(context);
    }

    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.user_single_row, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Users user = usersList.get(position);
        holder.personNameTxtV.setText(user.getFullName());
        imageHelper.setFirebaseImage(user.getPicture(), holder.personImageImgV);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfile(user.getId());
            }
        });

    }

    private void goToProfile(String personId) {
        Intent goToUpdate = new Intent(context, UserProfileActivity.class);
        goToUpdate.putExtra(ProfileActivity.PROFILE_ID, personId);
        context.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTxtV;
        public ImageView personImageImgV;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personNameTxtV = v.findViewById(R.id.userName);
            personImageImgV = v.findViewById(R.id.userImage);
        }
    }
}