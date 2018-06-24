package tallerii.stories.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.activities.StoriesLoggedInActivity;
import tallerii.stories.controller.CommentController;
import tallerii.stories.network.apimodels.Comment;
import tallerii.stories.network.apimodels.Storie;

public class CommentsAdapter extends BaseAdapter{

    private final Activity activity;
    private final Context context;
    private final CommentController controller;
    private LayoutInflater inflater;
    private ImageHelper imageHelper;

    private List<Comment> comments;

    public CommentsAdapter(Activity activity, Context context, CommentController controller, List<Comment> storiesToComment) {
        this.activity = activity;
        this.context = context;
        this.controller = controller;
        this.comments = storiesToComment;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int location) {
        return comments.get(location);
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
            convertView = inflater.inflate(R.layout.fragment_storie_comments_item, null);
        if (imageHelper == null)
            imageHelper = new ImageHelper(activity);

        // get views
        View commentView = convertView.findViewById(R.id.commentView);
        TextView userNameComment = convertView.findViewById(R.id.userNameComment);
        ImageView userCommentPic = convertView.findViewById(R.id.userCommentPic);
        TextView messageComment = convertView.findViewById(R.id.messageCommentText);


        // get comment and populate view
        final Comment comment = comments.get(position);
        userNameComment.setText(comment.getUserName());
        userNameComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StoriesLoggedInActivity)activity).startProfileActivity(comment.getUserId());
            }
        });
        messageComment.setText(comment.getMessage());
        imageHelper.setFirebaseImage(comment.getUserPicture(), userCommentPic);

        return convertView;

    }
}
