package tallerii.stories.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tallerii.stories.MainActivity;
import tallerii.stories.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // get root view and then access to objects like R.id.usernameView
        View RootView = inflater.inflate(R.layout.fragment_home, container, false);

        String message = getActivity().getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) RootView.findViewById(R.id.usernameView);
        textView.setText(message);

        return RootView;
    }
}
