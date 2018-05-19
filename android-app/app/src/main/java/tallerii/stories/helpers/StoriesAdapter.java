package tallerii.stories.helpers;

/*import info.androidhive.listviewfeed.storieImageView;
import info.androidhive.listviewfeed.R;
import info.androidhive.listviewfeed.app.AppController;
import info.androidhive.listviewfeed.data.FeedItem;*/
import tallerii.stories.R;
import tallerii.stories.network.apimodels.Story;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StoriesAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Story> stories;
    private StorageReference storageReference;
    private final ImageHelper imageHelper;

    public StoriesAdapter(Activity activity, List<Story> stories) {
        this.activity = activity;
        this.stories = stories;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imageHelper = new ImageHelper(activity);
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
        ImageView storieImageView = (ImageView) convertView.findViewById(R.id.storyImage1);

        Story story = stories.get(position);

        name.setText(story.getUserName() +" "+story.getUserLastName());

        // Converting timestamp into x ago format
        //Integer timeAgo = story.getCreatedTime();
        timestamp.setText("22 hours ago");

        // Chcek for empty status message
        if (!TextUtils.isEmpty(story.getDescription())) {
            statusMsg.setText(story.getDescription());
            statusMsg.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
            statusMsg.setVisibility(View.GONE);
        }

        // Checking for null feed url
        /*if (story.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + story.getUrl() + "\">"
                    + story.getUrl() + "</a> "));

            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }*/

        // user profile pic
        imageHelper.setFirebaseImage(story.getUserPicture(), profilePic);

        // story image
        if (story.getMultimedia() != null && !story.getMultimedia().isEmpty()) {
            storieImageView.setVisibility(View.VISIBLE);
            imageHelper.setFirebaseImage(story.getMultimedia(), storieImageView);
        } else {
            storieImageView.setVisibility(View.GONE);
        }

        return convertView;
    }

}