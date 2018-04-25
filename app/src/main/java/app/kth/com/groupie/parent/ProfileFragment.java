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

        Button toGroup = (Button) rootView.findViewById(R.id.sign_out);
        toGroup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toGroupMessagingActivity();
            }
        });

        Button toEditProfile = (Button) rootView.findViewById(R.id.to_edit_profile);
        toEditProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                //go to group activity
                activity.toEditProfileActivity();
            }
        });

        Button toSetting = (Button) rootView.findViewById(R.id.to_setting);
        toSetting.setOnClickListener(new View.OnClickListener() {
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
