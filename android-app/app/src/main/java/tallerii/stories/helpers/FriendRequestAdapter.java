package tallerii.stories.helpers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

import tallerii.stories.R;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.controller.FriendRequestController;
import tallerii.stories.network.apimodels.FriendRequest;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {
    private final ImageHelper imageHelper;
    private List<FriendRequest> friendsList;
    private final StoriesAppActivity appActivity;
    private FriendRequestController controller;

    public FriendRequestAdapter(List<FriendRequest> dataSet, StoriesAppActivity appActivity, FriendRequestController controller) {
        this.friendsList = dataSet;
        this.appActivity = appActivity;
        this.controller = controller;
        this.imageHelper = new ImageHelper(this.appActivity);
    }

    @Override
    public FriendRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.friend_request, parent, false);
        return new ViewHolder(v);
    }

    public void removeRequest(String requestId) {
        Iterator<FriendRequest> it = friendsList.iterator();
        while (it.hasNext()) {
            FriendRequest request = it.next();
            if (requestId.equals(request.getId())) {
                it.remove();
            }
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final FriendRequest friend = friendsList.get(holder.getAdapterPosition());
        holder.personNameTxtV.setText(friend.getFullName());
        imageHelper.setFirebaseImage(friend.getPicture(), holder.personImageImgV);
        holder.dateTxtV.setText(friend.getCreatedTime());
        holder.messageTxtV.setText(friend.getMessage());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appActivity.startProfileActivity(friend.getSenderUserId());
            }
        });
        setAcceptListener(holder, friend);
        setDeclineListener(holder, friend);
    }

    private void setAcceptListener(@NonNull final ViewHolder holder, final FriendRequest friend) {
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.acceptFriendRequest(friend.getId(), FriendRequestAdapter.this);
                removeRequest(holder);
            }
        });
    }

    private void setDeclineListener(@NonNull final ViewHolder holder, final FriendRequest friend) {
        holder.declineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.declineFriendRequest(friend.getId(), FriendRequestAdapter.this);
                removeRequest(holder);
            }
        });
    }

    private void removeRequest(@NonNull ViewHolder holder) {
        int position = holder.getAdapterPosition();
        friendsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, friendsList.size());
    }

    @Override
    public int getItemCount() {
        return friendsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView personNameTxtV;
        public TextView messageTxtV;
        public TextView dateTxtV;
        public ImageView personImageImgV;
        public Button acceptBtn;
        public Button declineBtn;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            personNameTxtV = v.findViewById(R.id.userName);
            messageTxtV = v.findViewById(R.id.message);
            dateTxtV = v.findViewById(R.id.date);
            personImageImgV = v.findViewById(R.id.userImage);
            acceptBtn = v.findViewById(R.id.accept);
            declineBtn = v.findViewById(R.id.decline);
        }
    }
}