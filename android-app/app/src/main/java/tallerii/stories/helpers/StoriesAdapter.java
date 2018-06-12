package tallerii.stories.helpers;

/*import info.androidhive.listviewfeed.storieImageView;
import info.androidhive.listviewfeed.R;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;*/
import tallerii.stories.R;
import tallerii.stories.network.apimodels.Storie;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StoriesAdapter extends BaseAdapter {

    private static final int REACTION_BUTTON_NOT_PRESSED = 0;
    private static final int REACTION_BUTTON_PRESSED = 1;

    private Activity activity;
    private LayoutInflater inflater;
    private List<Storie> stories;
    private final ImageHelper imageHelper;
    HashMap<View, HashMap<ImageButton, Integer>> reactionButtons;

    public StoriesAdapter(Activity activity, List<Storie> stories) {
        this.activity = activity;
        this.stories = stories;
        imageHelper = new ImageHelper(activity);
        reactionButtons = new HashMap();
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
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fragment_stories_item, null);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
        //TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        ImageView profilePic = convertView.findViewById(R.id.profilePic);
        ImageView storieImageView = (ImageView) convertView.findViewById(R.id.storieImage1);

        Storie storie = stories.get(position);

        name.setText(storie.getUserName() +" "+storie.getUserLastName());

        // Converting timestamp into x ago format
        //Integer timeAgo = storie.getCreatedTime();
        timestamp.setText("22 hours ago");

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
            storieImageView.setVisibility(View.VISIBLE);
            imageHelper.setFirebaseImage(storie.getMultimedia(), storieImageView);
        } else {
            storieImageView.setVisibility(View.GONE);
        }

        // prepare reaction buttons
        HashMap<ImageButton, Integer> buttons = new HashMap<>();
        buttons.put(changeStatusOnClickBy(convertView,R.id.likeButton),REACTION_BUTTON_NOT_PRESSED);
        buttons.put(changeStatusOnClickBy(convertView,R.id.dontLikeButton),REACTION_BUTTON_NOT_PRESSED);
        buttons.put(changeStatusOnClickBy(convertView,R.id.enjoyButton),REACTION_BUTTON_NOT_PRESSED);
        buttons.put(changeStatusOnClickBy(convertView,R.id.getBoredButton),REACTION_BUTTON_NOT_PRESSED);

        reactionButtons.put(convertView,buttons);

        return convertView;
    }

    public ImageButton changeStatusOnClickBy(final View convertView, int id){
        ImageButton view = (ImageButton) convertView.findViewById(id);
        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeStatus(convertView,(ImageButton)v);
            }
        });
        return view;
    }

    private void changeStatus(final View convertView, ImageButton v) {
        // For vector drawable
        // https://stackoverflow.com/questions/20121938/how-to-set-tint-for-an-image-view-programmatically-in-android
        if(reactionButtons.get(convertView).get(v).equals(REACTION_BUTTON_PRESSED)){
            changeToUnpressed(convertView,v);
        } else{
            changeToPressed(convertView,v);
            for (Map.Entry<ImageButton, Integer> pair: reactionButtons.get(convertView).entrySet()) {
                if(!pair.getKey().equals(v)){
                    changeToUnpressed(convertView, pair.getKey());
                }
            }
        }

    }

    private void changeToPressed(final View convertView, ImageButton v){
        v.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
        reactionButtons.get(convertView).put(v,REACTION_BUTTON_PRESSED);
    }

    private void changeToUnpressed(final View convertView, ImageButton v){
        v.setColorFilter(ContextCompat.getColor(activity, R.color.reaction_button_not_pressed), android.graphics.PorterDuff.Mode.SRC_IN);
        reactionButtons.get(convertView).put(v,REACTION_BUTTON_NOT_PRESSED);
    }



}