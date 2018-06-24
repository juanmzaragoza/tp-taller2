package tallerii.stories.helpers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tallerii.stories.R;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.fragments.main.StorieCommentsDialogFragment;
import tallerii.stories.network.apimodels.Comment;
import tallerii.stories.network.apimodels.ReactionResume;
import tallerii.stories.network.apimodels.Storie;

public class StoriesAdapter extends BaseAdapter {

    private static final int REACTION_BUTTON_NOT_PRESSED = 0;
    private static final int REACTION_BUTTON_PRESSED = 1;

    private StoriesLoggedInActivity activity;
    private final Context context;
    private final StoriesController controller;
    private LayoutInflater inflater;
    private List<Storie> stories;
    private final ImageHelper imageHelper;
    private HashMap<Integer, HashMap<ImageButton, Integer>> reactionButtons;

    public StoriesAdapter(StoriesLoggedInActivity activity, Context context, StoriesController controller, List<Storie> stories) {
        this.activity = activity;
        this.context = context;
        this.controller = controller;
        this.stories = stories;
        imageHelper = new ImageHelper(activity);
        reactionButtons = new HashMap<>();
    }

    @Override
    public int getCount() {
        return stories.size();
    }

    @Override
    public Object getItem(int location) {
        return stories.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = LayoutInflater.from(parent.getContext());

        ViewHolder holder;
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.fragment_stories_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Storie storie = stories.get(position);

        holder.name.setText(String.format("%s %s", storie.getUserName(), storie.getUserLastName()));
        setGoToProfileClickListener(storie.getUserId(), holder.name);

        // Converting timestamp into x ago format
        try {
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
            holder.timestamp.setText(DateUtils.printDifference(sdf.parse(storie.getCreatedTime())));
        } catch (ParseException e) {
            holder.timestamp.setText("unknown");
        }

        // Check for empty status message
        if (!TextUtils.isEmpty(storie.getDescription())) {
            holder.statusMsg.setText(storie.getDescription());
            holder.statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            holder.statusMsg.setVisibility(View.GONE);
        }

        // user profile pic
        imageHelper.setFirebaseImage(storie.getUserPicture(), holder.profilePic);
        // storie image
        if (storie.getMultimedia() != null && !storie.getMultimedia().isEmpty()) {
            imageHelper.setFirebaseImage(storie.getMultimedia(), holder.storieImageView);
            imageHelper.setFirebaseVideo(storie.getMultimedia(), holder.storieVideoView);
        }

        HashMap<ImageButton, Integer> buttons = new HashMap<>();
        prepareReactionButtons(holder.layout, buttons, storie.getReactions().obtainReactions(), storie.getId(), position);
        reactionButtons.put(position,buttons);

        setUpComment(holder, storie);

        return holder.layout;
    }

    private void setUpComment(ViewHolder holder, final Storie storie) {
        // get last comment
        if (!storie.getComments().isEmpty()) {
            Comment comment = storie.getComments().get(0);
            imageHelper.setFirebaseImage(comment.getUserPicture(), holder.userCommentPic);
            holder.usernameLastComment.setText(comment.getUserName());
            setGoToProfileClickListener(comment.getUserId(), holder.usernameLastComment);
            holder.lastComment.setText(comment.getMessage());
            holder.lastCommentView.setVisibility(View.VISIBLE);
        } else{
            holder.lastCommentView.setVisibility(View.GONE);
        }

        holder.messageComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    showCommentsDialog(storie.getId());
                }
            }
        });
        holder.sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsDialog(storie.getId());
            }
        });
    }

    private void setGoToProfileClickListener(final String userId, TextView usernameLastComment) {
        usernameLastComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startProfileActivity(userId);
            }
        });
    }

    private void showCommentsDialog(String storieId) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        StorieCommentsDialogFragment commentsDialogFragment = StorieCommentsDialogFragment.newInstance("Comments");

        Bundle args = new Bundle();
        args.putString("storieId", storieId);
        commentsDialogFragment.setArguments(args);

        commentsDialogFragment.showNow(fm, "fragment_storie_comments");
    }

    private void prepareReactionButtons(final View layout, HashMap<ImageButton, Integer> buttons,
                                        List<ReactionResume> reactionResumes, final String storieId, final int positionListView) {
        for (final ReactionResume reactionResume : reactionResumes) {
            final TextView countView = layout.findViewById(reactionResume.getCountId());
            countView.setText(String.valueOf(reactionResume.getCount()));
            ImageButton button = layout.findViewById(reactionResume.getViewId());
            Integer status;
            if (reactionResume.getReact() != null) {
                status = REACTION_BUTTON_PRESSED;
                button.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
            } else {
                status = REACTION_BUTTON_NOT_PRESSED;
                button.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_not_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    changeStatus((ImageButton)v, reactionResume.getReactionType(), positionListView, storieId, countView);
                }
            });
            buttons.put(button, status);
        }
    }

    private void changeStatus(ImageButton button, String reactionName, int positionListView, String storieId, TextView countView) {
        // For vector drawable
        // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
        int reactionCount = Integer.parseInt(countView.getText().toString());
        if(reactionButtons.get(positionListView).get(button).equals(REACTION_BUTTON_PRESSED)){
            changeToUnpressed(positionListView, button);
            countView.setText(String.valueOf(reactionCount - 1));
            // call to api - empty for unset
            controller.changeReaction(storieId,"");
        } else{
            changeToPressed(positionListView, button);
            for (Map.Entry<ImageButton, Integer> reactionButton: reactionButtons.get(positionListView).entrySet()) {
                if(!reactionButton.getKey().equals(button)){
                    changeToUnpressed(positionListView, reactionButton.getKey());
                }
            }
            countView.setText(String.valueOf(reactionCount + 1));
            // call to api
            controller.changeReaction(storieId,reactionName);
        }
    }

    private void changeToPressed(final int positionListView, ImageButton v){
        v.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
        reactionButtons.get(positionListView).put(v,REACTION_BUTTON_PRESSED);
    }

    private void changeToUnpressed(final int positionListView, ImageButton v){
        v.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_not_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
        reactionButtons.get(positionListView).put(v,REACTION_BUTTON_NOT_PRESSED);
    }

    public StoriesLoggedInActivity getActivity() {
        return activity;
    }

    public static class ViewHolder {
        public final TextView name;
        public final TextView timestamp;
        public final TextView statusMsg;
        public final ImageView profilePic;
        public final ImageView storieImageView;
        public final VideoView storieVideoView;
        public View layout;

        public View lastCommentView;
        public View messageComment;
        public ImageView userCommentPic;
        public TextView lastComment;
        public TextView usernameLastComment;
        public ImageButton sendCommentButton;

        public ViewHolder(View v) {
            layout = v;
            name = v.findViewById(R.id.name);
            timestamp = v.findViewById(R.id.timestamp);
            statusMsg = v.findViewById(R.id.txtStatusMsg);
            profilePic = v.findViewById(R.id.profilePic);
            storieImageView = v.findViewById(R.id.storieImage1);
            storieVideoView = v.findViewById(R.id.storieVideoView);

            lastCommentView = layout.findViewById(R.id.lastCommentView);
            messageComment = layout.findViewById(R.id.messageCommentText);
            userCommentPic = layout.findViewById(R.id.userCommentPic);
            lastComment = layout.findViewById(R.id.lastComment);
            usernameLastComment = layout.findViewById(R.id.usernameLastComment);
            sendCommentButton = layout.findViewById(R.id.sendMessageCommentButton);
        }
    }
}