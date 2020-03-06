package com.hiresight.hiresightclient;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.ByteArrayOutputStream;

import android.os.Bundle;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    private static int PICK_IMAGE = 1;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private StorageReference storageReference;
    private Button registerBtn;
    private ImageView photoBtn;
    private EditText nameText, regText, icText, contactText, emailText, passText;
    private Uri downloadUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        nameText = (EditText) findViewById(R.id.nameText);
        regText = (EditText) findViewById(R.id.regText);
        contactText = (EditText) findViewById(R.id.contactText);
        icText = (EditText) findViewById(R.id.icText);
        emailText = (EditText) findViewById(R.id.emailText);
        passText = (EditText) findViewById(R.id.passText);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        AddData();
    }
/*
    public void selectImage() {
        photoBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("image/*");
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                        startActivityForResult(chooserIntent, PICK_IMAGE);

                    }
                }
        );

    }
*/
    public void AddData() {
        registerBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        auth.createUserWithEmailAndPassword(emailText.getText().toString(), passText.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String clientID = auth.getCurrentUser().getUid();
                                            Client client = new Client(nameText.getText().toString(), regText.getText().toString(), icText.getText().toString(), contactText.getText().toString(), emailText.getText().toString(), passText.getText().toString(), null);
                                            DocumentReference documentReference = db.collection("Clients").document(clientID);
                                            documentReference.set(client)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Log.d("Firestore", "User profile stored!");
                                                            } else
                                                                Log.d("Firestore", task.getException().getMessage());
                                                        }
                                                    });
                                            Intent startIntent = new Intent(getApplicationContext(), UploadClientImageActivity.class);
                                            startActivity(startIntent);
                                        } else {
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });

                    }
                }

        );
    }


/*
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                photoBtn.setImageURI(uri);
                android.view.ViewGroup.LayoutParams layoutParams = photoBtn.getLayoutParams();
                layoutParams.width = 200;
                layoutParams.height = 200;
                photoBtn.setLayoutParams(layoutParams);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String uploadImage (String userID){

        photoBtn.setDrawingCacheEnabled(true);
        photoBtn.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) photoBtn.getDrawable()).getBitmap();
        UploadTask uploadTask = storageReference.child("client_image").child(userID + ".jpg").putBytes(imageViewToByte(bitmap));
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("Image", "Image not uploaded");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("Image", "Image uploaded");
            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    System.out.println(downloadUri = task.getResult());
                } else {
                    Log.d("image", "upload image error.");
                }
            }
        });
        return urlTask.toString();
    }


    public byte[] imageViewToByte (Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

 */
}
