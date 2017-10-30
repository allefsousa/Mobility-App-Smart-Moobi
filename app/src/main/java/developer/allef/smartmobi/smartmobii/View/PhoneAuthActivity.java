package developer.allef.smartmobi.smartmobii.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.KeyboardShortcutInfo;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import developer.allef.smartmobi.smartmobii.R;


public class PhoneAuthActivity extends AppCompatActivity  {

    EditText mPhoneNumberField, mVerificationField;
    Button mStartButton, mVerifyButton;
    TextView mResendButton, titulo;

    @BindView(R.id.progresauth)
    ProgressBar bar;


    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    AlertDialog alert11;
     boolean resposta;



    private static final String TAG = "PhoneAuthActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        getSupportActionBar();
        getSupportActionBar().setTitle("Login com Telefone");
        ButterKnife.bind(this);
        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
        titulo = (TextView)findViewById(R.id.title_text);
        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (TextView) findViewById(R.id.button_resend);



        mPhoneNumberField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        mVerificationField.setVisibility(View.INVISIBLE);
        mVerifyButton.setVisibility(View.INVISIBLE);
        mResendButton.setVisibility(View.INVISIBLE);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePhoneNumber()) {
                    return;
                }
              //  exemplo_alerta();
                dialogNumeroCorreto();



            }
        });
        mPhoneNumberField.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                    InputMethodManager imm = (InputMethodManager) PhoneAuthActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPhoneNumberField.getWindowToken(), 0);

                    dialogNumeroCorreto();
                }
                return false;
            }
        });

//        mStartButton.setOnClickListener(this);
//        mVerifyButton.setOnClickListener(this);
//        mResendButton.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "Verificação Completa:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "Falha Na Verificação", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    mPhoneNumberField.setError("Invalid phone number.");
                    Snackbar.make(findViewById(android.R.id.content), "Codigo de verificação não confere !!",
                            Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quantidade de Solicitações excedida !!",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

            }



        };

      mPhoneNumberField.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View view) {
              bar.setVisibility(View.INVISIBLE);
              mPhoneNumberField.setCursorVisible(true);
          }
      });
    }

    private void verificarNumeroCorreto() {
        mPhoneNumberField.setInputType(InputType.TYPE_NULL);
        mPhoneNumberField.clearFocus();
        mPhoneNumberField.setFocusableInTouchMode(false);
        mPhoneNumberField.setCursorVisible(false);
        startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        bar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorAccent));
        bar.setVisibility(View.VISIBLE);
        bar.setIndeterminate(true);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = task.getResult().getUser();

                            Intent intent = new Intent(PhoneAuthActivity.this,IntroActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                           bar.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                            finish();
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Snackbar.make(findViewById(android.R.id.content), "Codigo Invalido.",
                                        Snackbar.LENGTH_SHORT).show();
                                bar.setVisibility(View.INVISIBLE);

                            }



                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Numero informado não é valido.");
            return false;
        }
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
//
//        }


    }

    public void dialogNumeroCorreto(){

        String titulo = "Confirmação de Numero.";
        String Mensagem = "O Numero de verificação esta correto ?" + mPhoneNumberField.getText();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setIcon(R.drawable.compass);
        builder.setMessage(Mensagem);


        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                resposta = true;
                verificarNumeroCorreto();


                dialogInterface.dismiss();


            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                resposta = false;

                dialogInterface.dismiss();

            }
        });

         alert11 = builder.create();

         alert11.show();

    }

}
