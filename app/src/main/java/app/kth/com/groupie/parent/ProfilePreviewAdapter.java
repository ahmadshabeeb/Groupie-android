package app.kth.com.groupie.parent;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.kth.com.groupie.R;
import app.kth.com.groupie.data.structure.Profile;

public class ProfilePreviewAdapter extends RecyclerView.Adapter<ProfilePreviewAdapter.ProfileViewHolder> {
    private ArrayList<Profile> profileList;
    private Context context;

    public ProfilePreviewAdapter (ArrayList<Profile> profileList, Context context){
        this.profileList = profileList;
        this.context = context;
    }


    public class ProfileViewHolder extends RecyclerView.ViewHolder{
        public TextView firstName;
//        public TextView lastName;
        public ImageView profilePic;
        public ProfileViewHolder(View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.preview_first_name);
//            lastName = itemView.findViewById(R.id.preview_last_name);
            profilePic = itemView.findViewById(R.id.preview_profile_picture);
        }
    }

    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("tag", "WE REAch HERE 1");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_profile_preview, parent, false);
        return new ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        Log.d("tag", "WE REAch HERE 2");
        Profile profile = profileList.get(position);
        Log.d("tag", "first name: " + profile.getFirstName());
        holder.firstName.setText(profile.getFirstName());
//        holder.lastName.setText(profile.getLastName());
        Log.d("tag", "last name: " + profile.getLastName());
        if(profile.getProfilePicture() != null ){
            String urlImage = profile.getProfilePicture();
            Glide.with(context)
                    .load(urlImage)
                    .into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(R.mipmap.ic_profile);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("tag", "getItemCount: " + profileList.size());
        return profileList.size();
    }
}