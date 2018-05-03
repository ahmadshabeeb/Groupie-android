package app.kth.com.groupie.firstLogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import app.kth.com.groupie.R;
import app.kth.com.groupie.parent.ParentActivity;

import static android.app.Activity.RESULT_OK;


public class ProfilePictureFragment extends Fragment {
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView chooseImage;
    private Button uploadImageButton;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 12;
    private  Button skipButton;
    private View view;
    private ImageView testImage;
    private String imageStorageRef;
    private FirebaseAuth mAuth;
    private ParentActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_profile_picture, container, false);
        view = rootView;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth = FirebaseAuth.getInstance();
        uploadImageButton = (Button) rootView.findViewById(R.id.upload_image_button);
        chooseImage = (ImageView) rootView.findViewById(R.id.choose_image_imageView);
        skipButton = (Button) rootView.findViewById(R.id.skip_button);
        testImage = (ImageView) rootView.findViewById(R.id.testimage);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag skip", mAuth.getCurrentUser() + " ...");
                setProfilePicture(null);
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.d("tag on Activity result", mAuth.getCurrentUser() + " ...");
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                chooseImage.setImageBitmap(bitmap);
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    public void uploadImage(){
        if (filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Log.d("tag", mAuth.getCurrentUser() + " ...");


            // create cloud storage ref for image being uploaded

           final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
           ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePictureFragment.this.getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            imageStorageRef = ref.toString();
                            setProfilePicture(imageStorageRef);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfilePictureFragment.this.getContext(), "Upload Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("tag", mAuth.getCurrentUser() + " ...");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }

    public void setProfilePicture(String ref){
        printBar(imageStorageRef, view);
        FirstLoginActivity activity = (FirstLoginActivity) getActivity();
        activity.addProfilePicture(ref);
    }

    public void printBar(String message, View view){
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
