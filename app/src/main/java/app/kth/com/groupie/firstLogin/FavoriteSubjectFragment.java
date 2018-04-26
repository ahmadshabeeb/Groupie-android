package app.kth.com.groupie.firstLogin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import app.kth.com.groupie.R;

public class FavoriteSubjectFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_favorite_subject, container, false);

        //SET BUTTON PRESSES
        ImageView language = (ImageView) rootview.findViewById(R.id.language_imageview);
        language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Language");
            }
        });

        ImageView programming = (ImageView) rootview.findViewById(R.id.programming_imageview);
        programming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Programming");
            }
        });

        ImageView math = (ImageView) rootview.findViewById(R.id.math_imageview);
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Math");
            }
        });

        ImageView business = (ImageView) rootview.findViewById(R.id.business_imageview);
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Business and Economics");
            }
        });

        ImageView engineering = (ImageView) rootview.findViewById(R.id.engineering_imageview);
        engineering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Engineering");
            }
        });

        ImageView science = (ImageView) rootview.findViewById(R.id.science_imageview);
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Natural Sciences");
            }
        });

        ImageView law = (ImageView) rootview.findViewById(R.id.law_imageview);
        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Law and Political Science");
            }
        });

        ImageView music = (ImageView) rootview.findViewById(R.id.music_imageview);
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Art and Music");
            }
        });

        ImageView other = (ImageView) rootview.findViewById(R.id.other_imageview);
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFavoriteSubject("Other");
            }
        });

        return rootview;
    }


    private void setFavoriteSubject(String subject){
        FirstLoginActivity activity = (FirstLoginActivity) getActivity();
        activity.addFavoriteSubject(subject);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
