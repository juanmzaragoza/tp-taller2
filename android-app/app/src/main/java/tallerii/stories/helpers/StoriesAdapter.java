package tallerii.stories.helpers;

import android.app.Activity;
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
import java.util.Map;

import tallerii.stories.R;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.fragments.main.StorieCommentsDialogFragment;
import tallerii.stories.network.apimodels.Comment;
import tallerii.stories.network.apimodels.Reactions;
import tallerii.stories.network.apimodels.Storie;

public class StoriesAdapter extends BaseAdapter {

    private static final int REACTION_BUTTON_NOT_PRESSED = 0;
    private static final int REACTION_BUTTON_PRESSED = 1;

    private static final String I_LIKE_REACTION = "LIKE";
    private static final String I_NOTLIKE_REACTION = "NOTLIKE";
    private static final String I_ENJOY_REACTION = "ENJOY";
    private static final String I_GETBORED_REACTION = "GETBORED";

    private Activity activity;
    private final Context context;
    private final StoriesController controller;
    private LayoutInflater inflater;
    private List<Storie> stories;
    private final ImageHelper imageHelper;
    HashMap<Integer, HashMap<ImageButton, Integer>> reactionButtons;

    public StoriesAdapter(Activity activity, Context context, StoriesController controller, List<Storie> stories) {
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
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_stories_item, null);

        TextView name = convertView.findViewById(R.id.name);
        TextView timestamp = convertView.findViewById(R.id.timestamp);
        TextView statusMsg = convertView.findViewById(R.id.txtStatusMsg);
        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        ImageView profilePic = convertView.findViewById(R.id.profilePic);
        ImageView storieImageView = convertView.findViewById(R.id.storieImage1);
        VideoView storieVideoView = convertView.findViewById(R.id.storieVideoView);

        View lastCommentView = convertView.findViewById(R.id.lastCommentView);
        View messageComment = convertView.findViewById(R.id.messageCommentText);
        ImageView userCommentPic = convertView.findViewById(R.id.userCommentPic);
        TextView lastComment = convertView.findViewById(R.id.lastComment);
        TextView usernameLastComment = convertView.findViewById(R.id.usernameLastComment);
        ImageButton sendCommentButton = convertView.findViewById(R.id.sendMessageCommentButton);

        final Storie storie = stories.get(position);

        name.setText(String.format("%s %s", storie.getUserName(), storie.getUserLastName()));

        // Converting timestamp into x ago format
        try {
            DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
            timestamp.setText(DateUtils.printDifference(sdf.parse(storie.getCreatedTime())));
        } catch (ParseException e) {
            timestamp.setText("unknown");
        }

        // Chcek for empty status message
        if (!TextUtils.isEmpty(storie.getDescription())) {
            statusMsg.setText(storie.getDescription());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        /*if (storie.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + storie.getUrl() + "\">"
                    + storie.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }*/

        // user profile pic
        imageHelper.setFirebaseImage(storie.getUserPicture(), profilePic);

        // storie image
        if (storie.getMultimedia() != null && !storie.getMultimedia().isEmpty()) {
            imageHelper.setFirebaseImage(storie.getMultimedia(), storieImageView);
            imageHelper.setFirebaseVideo(storie.getMultimedia(), storieVideoView);
        }

        // prepare reaction buttons -> add each button to a reaction not pressed
        Reactions reactions = storie.getReactions();
        HashMap<ImageButton, Integer> buttons = new HashMap<>();

        int status = reactions.getLike().imVoted()?REACTION_BUTTON_PRESSED:REACTION_BUTTON_NOT_PRESSED;
        buttons.put(changeStatusOnClickBy(convertView,R.id.likeButton, I_LIKE_REACTION,status,position),status);

        status = reactions.getNotLike().imVoted()?REACTION_BUTTON_PRESSED:REACTION_BUTTON_NOT_PRESSED;
        buttons.put(changeStatusOnClickBy(convertView,R.id.dontLikeButton, I_NOTLIKE_REACTION,status,position),status);

        status = reactions.getEnjoy().imVoted()?REACTION_BUTTON_PRESSED:REACTION_BUTTON_NOT_PRESSED;
        buttons.put(changeStatusOnClickBy(convertView,R.id.enjoyButton, I_ENJOY_REACTION,status,position),status);

        status = reactions.getBored().imVoted()?REACTION_BUTTON_PRESSED:REACTION_BUTTON_NOT_PRESSED;
        buttons.put(changeStatusOnClickBy(convertView,R.id.getBoredButton, I_GETBORED_REACTION,status,position),status);
        // save it
        reactionButtons.put(position,buttons);

        // get last comment
        if (!storie.getComments().isEmpty()) {
            Comment comment = storie.getComments().get(0);
            imageHelper.setFirebaseImage(comment.getUserPicture(), userCommentPic);
            usernameLastComment.setText(comment.getUserName());
            lastComment.setText(comment.getMessage());
            lastCommentView.setVisibility(View.VISIBLE);
        } else{
            lastCommentView.setVisibility(View.GONE);
        }

        messageComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    showCommentsDialog(storie.getId());
                }
            }
        });
        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentsDialog(storie.getId());
            }
        });



        return convertView;
    }

    public ImageButton changeStatusOnClickBy(final View convertView, int id, final String reactionName, int status, final int position){
        ImageButton view = convertView.findViewById(id);

        if(status == REACTION_BUTTON_PRESSED){
            view.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeStatus(convertView,(ImageButton)v, reactionName, position);
            }
        });
        return view;
    }

    private void changeStatus(final View convertView, ImageButton v, String reactionName, int positionListView) {
        // For vector drawable
        // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
        if(reactionButtons.get(positionListView).get(v).equals(REACTION_BUTTON_PRESSED)){
            changeToUnpressed(positionListView,v);
        } else{
            changeToPressed(positionListView,v);
            for (Map.Entry<ImageButton, Integer> reactionButton: reactionButtons.get(positionListView).entrySet()) {
                if(!reactionButton.getKey().equals(v)){
                    changeToUnpressed(positionListView, reactionButton.getKey());
                }
            }
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

    private void showCommentsDialog(String storieId) {
        FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
        StorieCommentsDialogFragment commentsDialogFragment = StorieCommentsDialogFragment.newInstance("Comments");

        Bundle args = new Bundle();
        args.putString("storieId", storieId);
        commentsDialogFragment.setArguments(args);

        commentsDialogFragment.showNow(fm, "fragment_storie_comments");
    }




}