package tallerii.stories.fragments.main;

import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tallerii.stories.R;
import tallerii.stories.StoriesAppActivity;
import tallerii.stories.controller.CommentController;
import tallerii.stories.helpers.CommentsAdapter;
import tallerii.stories.network.apimodels.Comment;


public class StorieCommentsDialogFragment extends DialogFragment {

    private StoriesAppActivity activity;
    private CommentController controller;

    private View rootView;
    private ImageButton sendMessageCommentButton;
    private TextView messageCommentText;
    private HelperFragment helperFragment;

    private String storieId;


    public StorieCommentsDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static StorieCommentsDialogFragment newInstance(String title) {
        StorieCommentsDialogFragment frag = new StorieCommentsDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_storie_comments, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rootView = view;

        controller = new CommentController(this);
        activity = (StoriesAppActivity) getActivity();
        helperFragment = new HelperFragment(getContext());

        // Fetch arguments from bundle and set title
        storieId = getArguments().getString("storieId");
        if(storieId == null || storieId.isEmpty()){
            activity.showMessage("An error has ocurrer with the resource storie id");
            getDialog().dismiss();
        } else {
            // populate comments on modal show
            getComments();

            final String title = getArguments().getString("title", "Comments");
            getDialog().setTitle(title);

            // Get field from view
            sendMessageCommentButton = (ImageButton) rootView.findViewById(R.id.sendMessageCommentButton);
            sendMessageCommentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (messageCommentText.getText().toString().isEmpty()) {
                        activity.showMessage("Please! Fill the text comment!!!");
                    } else {
                        // do something
                        controller.doComment(storieId, messageCommentText.getText().toString());
                    }
                }
            });
            messageCommentText = (TextView) rootView.findViewById(R.id.messageCommentText);

            // Show soft keyboard automatically and request focus to field
            //mEditText.requestFocus();
            getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

    }

    private void getComments(){
        controller.getCommentsByStorie(storieId);
        helperFragment.showMessageLoading("Wait while loading comments storie...");
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.y * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setLayout((int) (size.x * 0.75), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();

        // call api comments
        getComments();
    }

    public void onSuccessComment(String userId, String message, String storieId) {
        helperFragment.dismissMessageLoading();
        activity.showMessage("Your comment was successfully sended");
        getDialog().dismiss();
    }

    public void onErrorComment(String message) {
        helperFragment.dismissMessageLoading();
        activity.showMessage(message);
    }

    public void populateComments(List<Comment> storiesToComment) {

        ListView listView = (ListView) rootView.findViewById(R.id.commentsList);

        CommentsAdapter listAdapter = new CommentsAdapter(getActivity(), getContext(), controller, storiesToComment);
        listView.setAdapter(listAdapter);

        // notify data changes to list adapater
        listAdapter.notifyDataSetChanged();

        helperFragment.dismissMessageLoading();

    }
}
