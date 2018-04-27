package app.kth.com.groupie.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import app.kth.com.groupie.R;

public class HomeFragment extends Fragment {
    ParentActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        Button signOut = (Button) rootView.findViewById(R.id.sign_out);
        signOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                activity.signOut();
            }
        });

        Button editGroup = (Button) rootView.findViewById(R.id.editgroup_button);
        editGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.goToEditGroup();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (ParentActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
