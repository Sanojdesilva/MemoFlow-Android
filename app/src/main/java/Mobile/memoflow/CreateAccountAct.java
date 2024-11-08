package Mobile.memoflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class CreateAccountAct extends AppCompatActivity {

    EditText etEmail,etPassword,etConfirmPassword;
    Button btnCreateAccount;
    ProgressBar progressview;
    TextView LoginTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_Confirmpassword);
        btnCreateAccount = findViewById(R.id.createAccountBtn);
        progressview = findViewById(R.id.progressBar_CreateAccount);
        LoginTextView = findViewById(R.id.tv_login);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if(!validateData(email,password,confirmPassword)){
                    return ;
                }

                createAccountInFirebase(email,password);


            }
        });

        LoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateAccountAct.this,LoginAct.class));
            }
        });
    }

    void createAccountInFirebase(String Email, String Password){
        changeinProgress(true);

        FirebaseAuth fireBaseAuth = FirebaseAuth.getInstance();

        fireBaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(CreateAccountAct.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgress(false);
                if(task.isSuccessful()){
                    Toast.makeText(CreateAccountAct.this,"Account Successfully Created , Check Email to Verify",Toast.LENGTH_SHORT).show();
                    fireBaseAuth.getCurrentUser().sendEmailVerification();
                    fireBaseAuth.signOut();
                    finish();
                }else{
                    Toast.makeText(CreateAccountAct.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    void changeinProgress(boolean inProgress){
        if(inProgress){
            progressview.setVisibility(View.VISIBLE);
            btnCreateAccount.setVisibility(View.GONE);
        }else{
            progressview.setVisibility(View.GONE);
            btnCreateAccount.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email,String Password,String ConfirmPassword){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is Invalid");
            return false;
        }

        if(Password.length()>8){
            etPassword.setError("Passwords Must be Maximum 8 Characters");
            return false;
        }

        if(!Password.equals(ConfirmPassword)){
            etConfirmPassword.setError("Password Dosen't Match");
            return false;
        }

        return true;
    }
}