package tallerii.stories.helpers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import tallerii.stories.ChatMessagesActivity;
import tallerii.stories.R;
import tallerii.stories.network.apimodels.Friend;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private final StorageReference storageReference;
    private List<Friend> friendsList;
    private Context context;
    private String currentUserId;

    public FriendsAdapter(List<Friend> dataSet, Context context, String userId) {
        this.friendsList = dataSet;
        this.context = context;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        this.storageReference = storage.getReference();
        this.currentUserId = userId;
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
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageReference.child("images/" + user.getPicture()))
                .into(holder.personImageImgV)
        ;
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatMessagesActivity(user.getId());
            }
        });
    }

    private void goToChatMessagesActivity(String personId) {
        Intent goToUpdate = new Intent(context, ChatMessagesActivity.class);
        goToUpdate.putExtra(ChatMessagesActivity.FRIEND_ID, personId);
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