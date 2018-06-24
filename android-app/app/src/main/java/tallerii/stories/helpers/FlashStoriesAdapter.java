package tallerii.stories.helpers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.controller.StoriesController;
import tallerii.stories.interfaces.ThumbnailCallback;
import tallerii.stories.network.apimodels.Storie;

public class FlashStoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "ThumbnailsAdapter";

    private final List<Storie> dataSet;
    private final ImageHelper imageHelper;

    public FlashStoriesAdapter(Activity activity, List<Storie> dataSet) {
        Log.v(TAG, "Thumbnails Adapter has " + dataSet.size() + " items");
        this.dataSet = dataSet;
        imageHelper = new ImageHelper(activity);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.v(TAG, "On Create View Holder Called");
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.fragment_flash_stories_item, viewGroup, false);
        return new FlashStoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Storie storie = dataSet.get(position);
        Log.v(TAG, "On Bind View Called");
        FlashStoriesViewHolder thumbnailsViewHolder = (FlashStoriesViewHolder) holder;
        if (storie.getMultimedia() != null && !storie.getMultimedia().isEmpty()) {
            imageHelper.setFirebaseImage(storie.getMultimedia(), thumbnailsViewHolder.thumbnail);
        }
        thumbnailsViewHolder.userName.setText(storie.getUserName());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class FlashStoriesViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        public ImageView thumbnail;

        public FlashStoriesViewHolder(View v) {
            super(v);
            this.thumbnail = (ImageView) v.findViewById(R.id.storieImage);
            this.userName = (TextView) v.findViewById(R.id.usernameView);
        }
    }
}
