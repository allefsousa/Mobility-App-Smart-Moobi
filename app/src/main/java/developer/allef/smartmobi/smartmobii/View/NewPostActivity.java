package developer.allef.smartmobi.smartmobii.View;

import android.app.Activity;
import android.content.Context;
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
import com.google.android.gms.tasks.OnFailureListener;
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
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import developer.allef.smartmobi.smartmobii.Helper.monitorHora;
import developer.allef.smartmobi.smartmobii.Model.Feed;
import developer.allef.smartmobi.smartmobii.R;

public class NewPostActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private File tempFile = null;
    private File cropTempFile = null;
    Feed addPost;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
         addPost = new Feed();
        getSupportActionBar().setTitle("Postar na Linha do Tempo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_voltar);
        context = NewPostActivity.this;








        findViewById(R.id.fotinha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dispatchTakePictureIntent();


            }
        });

        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference db =FirebaseDatabase.getInstance().getReference();
                String postId = db.push().getKey();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                 fazerUploadPhoto(db,postId,userId,view);

               }
        });
    }

    private void fazerUploadPhoto(final DatabaseReference db, final String postId, final String userId, final View view) {
        final String[] uridown = new String[1];
        final String datapost = monitorHora.retornadaData();

        Calendar cal = Calendar.getInstance();
        Date data_atual = cal.getTime();
        final Timestamp ts = new Timestamp(data_atual.getTime());
        final Long a = ts.getTime();

        final String comment = ((EditText) findViewById(R.id.comment)).getText().toString();

        StorageReference photoRef = FirebaseStorage.getInstance().getReference().child("posts").child(postId + ".jpg");

        photoRef.putFile(Uri.fromFile(cropTempFile))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        uridown[0] = downloadUrl.toString();
                        if (uridown[0] == null) return;


                        addPost.setLegenda(comment);
                        addPost.setCreatedAt(a);
                        addPost.setUserId(userId);
                        addPost.setPhotoperfil(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                        addPost.setNomeUserPost(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                        addPost.setEmailUserPost(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        addPost.setImagem(uridown[0]);
                        addPost.setContadorLikes(0);
                        addPost.setDataPost(datapost);
                        addPost.setContadorComentarios(0);
                        db.child("feed").child(postId).setValue(addPost);
                        db.child("postUsuario").child(userId).child(postId).setValue(addPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    finish();
                                }

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(view, "Houve um erro ao salvar o post..." + e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
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
            options.setActiveWidgetColor(getResources().getColor(R.color.secondaryDarkColor));
            options.setLogoColor(getResources().getColor(R.color.secondaryDarkColor));
            options.setCropFrameColor(getResources().getColor(R.color.secondaryDarkColor));
            options.setStatusBarColor(getResources().getColor(R.color.primaryDarkColor));
            options.setCropGridColor(getResources().getColor(R.color.secondaryDarkColor));
            options.setToolbarColor(getResources().getColor(R.color.primaryDarkColor));
            options.setToolbarWidgetColor(getResources().getColor(R.color.Branco));
         //   options.setDimmedLayerColor(getResources().getColor(R.color.Branco));
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
            options.setToolbarTitle("Ajustar Foto");
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
