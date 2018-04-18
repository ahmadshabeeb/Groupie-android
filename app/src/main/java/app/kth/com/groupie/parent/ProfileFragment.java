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

public class ProfileFragment extends Fragment {
    ParentActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInsatnceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);

        Button ToYourGroupButton = (Button) rootView.findViewById(R.id.ToYourGroupFromProfile);
        ToYourGroupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toGroupMessagingActivity();
            }
        });

        Button ToEditProfileButton = (Button) rootView.findViewById(R.id.ToEditprofile);
        ToEditProfileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toEditProfileActivity();
            }
        });

        Button ToSettingButton = (Button) rootView.findViewById(R.id.ToSetting);
        ToSettingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toSettingActivity();
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
