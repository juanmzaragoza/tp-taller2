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

import tallerii.stories.ChatMessagesActivity;
import tallerii.stories.R;
import tallerii.stories.network.apimodels.Friend;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private final ImageHelper imageHelper;
    private List<Friend> friendsList;
    private Context context;
    private String currentUserId;

    public FriendsAdapter(List<Friend> dataSet, Context context, String userId) {
        this.friendsList = dataSet;
        this.context = context;
        this.currentUserId = userId;
        this.imageHelper = new ImageHelper(context);
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.user_single_row, parent, false);
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Friend user = friendsList.get(position);
        holder.personNameTxtV.setText(user.getFullName());
        imageHelper.setFirebaseImage(user.getPicture(), holder.personImageImgV);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatMessagesActivity(user.getUserId(), user.getFullName());
            }
        });

    }

    private void goToChatMessagesActivity(String personId, String friendName) {
        Intent goToUpdate = new Intent(context, ChatMessagesActivity.class);
        goToUpdate.putExtra(ChatMessagesActivity.FRIEND_ID, personId);
        goToUpdate.putExtra(ChatMessagesActivity.FRIEND_NAME, friendName);
        goToUpdate.putExtra(ChatMessagesActivity.USER_ID, currentUserId);
        context.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
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