package com.example.newsapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newsapp.R;
import com.example.newsapp.viewmodel.MainViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountSettings extends AppCompatActivity {
    private static final String TAG = "AccountSettings";
    public static final int REQUEST_CODE_PICK_IMAGE = 9001;

    private Button cancel, saveChanges, deleteAccount;
    private EditText name;
    private CircleImageView imageView;
    private ProgressBar progressBar;

    private FirebaseFirestore firebaseFirestore;
    private StorageReference mStorageRef;
    private boolean isUploaded = false;
    private Uri imageUri;
    private MainViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        saveChanges = findViewById(R.id.button_save_changes_acc_settings);
        deleteAccount = findViewById(R.id.button_delete_account_setting);
        //name = findViewById(R.id.);
        imageView = findViewById(R.id.circular_imageView_account_setting);
        progressBar = findViewById(R.id.progressBar_account);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isUploaded){
                    isUploaded = true;
                    uploadData();

                }else {
                    Toast.makeText(getApplicationContext(), "Already uploaded..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog
                        .Builder(AccountSettings.this)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure? \nThis can't be undone!")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // if user clicks ok then it will open network settings
                                Log.d(TAG, "onClick: Clicked on Yes..");
                                deleteUserAccount();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                }).show();
            }
        });

    }
    private void deleteUserAccount(){
        Log.d(TAG, "deleteUserAccount: Deleting User account");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: sucessful ");
                    viewModel.deleteUserDatabase(user.getUid());
                    Intent intent = new Intent(AccountSettings.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d(TAG, "onComplete: error: " + task.getException().getMessage());
                }
            }
        });

    }
    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        isUploaded = false;
    }
    private void uploadData(){


        if (imageUri == null) {
            Toast.makeText(getApplicationContext(), "Select an image for profile", Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.savePhotoAndUpdateDatabase(imageUri);
        progressBar.setVisibility(View.VISIBLE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            Log.d(TAG, "onActivityResult: Pick image successful");
            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: crop request OK : Setting image in imageView");
                imageUri = result.getUri();
                Glide.with(this)
                        .load(imageUri)
                        .into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: error : " + error.getMessage());
            }
        }
    }


}