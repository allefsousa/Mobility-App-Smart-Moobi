package developer.allef.smartmobi.smartmobii.View;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import developer.allef.smartmobi.smartmobii.Helper.monitorHora;
import developer.allef.smartmobi.smartmobii.Model.Feed;
import developer.allef.smartmobi.smartmobii.R;

public class NewPostActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File tempFile = null;
    private File cropTempFile = null;
    Feed addPost;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
         addPost = new Feed();
        // TODO: 07/10/2017 trazer data e hora para o post
        Toast.makeText(this, "data"+ monitorHora.retornadaData(), Toast.LENGTH_SHORT).show();






        findViewById(R.id.fotinha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dispatchTakePictureIntent();


            }
        });

        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = ((EditText)findViewById(R.id.comment)).getText().toString();


                final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

                final String postId = db.push().getKey();

                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
               final  String datapost = monitorHora.retornadaData();

                addPost.setLegenda(comment);
                addPost.setUserId(userId);
                addPost.setPhotoperfil(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                addPost.setNomeUserPost(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                addPost.setEmailUserPost(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                addPost.setImagem("");
                addPost.setContadorLikes(0);
                addPost.setDataPost(datapost);
                addPost.setContadorComentarios(0);
                db.child("feed").child(postId).setValue(addPost);
                db.child("postUsuario").child(userId).child(postId).setValue(addPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(!task.isSuccessful()) {

                        }
                        final StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("posts").child(postId + ".jpg");

                        photoRef.putFile(Uri.fromFile(cropTempFile))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String downloadPath = taskSnapshot.getMetadata().getDownloadUrl().toString();
                                        Map<String, Object> postValues = new HashMap<String, Object>();
                                        postValues.put("imagem", downloadPath);
                                        postValues.put("createdAt", ServerValue.TIMESTAMP);
                                        db.child("feed").child(postId).updateChildren(postValues);
                                        db.child("postUsuario").child(userId).child(postId).updateChildren(postValues);
                                        finish();
                                    }
                                });

                    }
                });

            }


//                Map postValues = new HashMap();
//                postValues.put("userId", userId);
//                postValues.put("photoUrl",photoPost);
//                postValues.put("comment", comment);
//                postValues.put("image", null);
//                postValues.put("likesCount", 0);
//                postValues.put("commentsCount", 0);
//                postValues.put("createdAt", ServerValue.TIMESTAMP);


//                Map updateValues = new HashMap();
//                updateValues.put("posts/" + postId, postValues);
//                updateValues.put("userPosts/" + userId + "/" + postId, postValues);
//
//                db.updateChildren(updateValues, new DatabaseReference.CompletionListener() {
//                    @Override
//                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                        if (databaseError != null) {
//                            //TODO: Deu erro e agora?
//                            return;
//                        }
//
//
//           }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


        if (takePictureIntent.resolveActivity(NewPostActivity.this.getPackageManager()) != null) {
            try {
                tempFile = File.createTempFile("temp", ".jpg", NewPostActivity.this.getCacheDir());
            } catch (IOException ex) {
                Snackbar.make(findViewById(android.R.id.content), "Houve um erro ao criar o arquivo temporário...", Snackbar.LENGTH_LONG).show();
            }
            if (tempFile != null) {
                Uri tempFileUri = FileProvider.getUriForFile(NewPostActivity.this, "com.developer.allef.projetomobile.fileprovider", tempFile);
                Log.d("DEBUG", tempFileUri.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            if (tempFile == null) return;
            UCrop.Options options = new UCrop.Options();
            options.setHideBottomControls(false);
            options.setActiveWidgetColor(getResources().getColor(R.color.colorAccent));
            options.setLogoColor(getResources().getColor(R.color.colorAccent));
            options.setCropFrameColor(getResources().getColor(R.color.colorAccent));
            options.setStatusBarColor(getResources().getColor(R.color.colorAccent));
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setCompressionQuality(90);
            try {
                cropTempFile = File.createTempFile("temp_crop", ".jpg", NewPostActivity.this.getCacheDir());
            } catch (IOException e) {
                Snackbar.make(findViewById(android.R.id.content), "Houve um erro ao criar o arquivo temporário...", Snackbar.LENGTH_LONG).show();
            }


            /**
             * cortando a foto armazenada no diretorio temporario
             */
            UCrop.of(Uri.fromFile(tempFile), Uri.fromFile(cropTempFile))
                    .withAspectRatio(900, 600)
                    .withMaxResultSize(900, 600)
                    .withOptions(options)
                    .start(this);
        }

        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            final Uri resultUri = UCrop.getOutput(data);
            ((ImageView)findViewById(R.id.fotinha)).setImageURI(resultUri);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Snackbar.make(findViewById(android.R.id.content), "Houve um erro ao recortar a foto...", Snackbar.LENGTH_LONG).show();
        }
    }



}
