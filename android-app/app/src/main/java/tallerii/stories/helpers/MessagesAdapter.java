package tallerii.stories.helpers;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.network.apimodels.ChatMessage;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private static final int ITEM_TYPE_SENT = 0;
    private static final int ITEM_TYPE_RECEIVED = 1;
    private final String currentUserId;
    private List<ChatMessage> messagesList;
    private Context mContext;

    public MessagesAdapter(List<ChatMessage> dataset, Context context, String currentUserId) {
        this.messagesList = dataset;
        this.mContext = context;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (messagesList.get(position).getSenderId().equals(currentUserId)) {
            return ITEM_TYPE_SENT;
        } else {
            return ITEM_TYPE_RECEIVED;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        if (viewType == ITEM_TYPE_SENT) {
            v = LayoutInflater.from(mContext).inflate(R.layout.sent_message_row, null);
        } else if (viewType == ITEM_TYPE_RECEIVED) {
            v = LayoutInflater.from(mContext).inflate(R.layout.received_message_row, null);
        }
        return new ViewHolder(v); // view holder for header items
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatMessage msg = messagesList.get(position);
        holder.messageTextView.setText(msg.getMessage());
//        holder.dateTextView.setText(msg.getDate());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageTextView;
        public TextView dateTextView;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            messageTextView = v.findViewById(R.id.chatMsgTextView);
            dateTextView = v.findViewById(R.id.chatMsgDateTextView);
        }
    }
}
