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

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginAct extends AppCompatActivity {

    EditText etEmail,etPassword;
    Button btnLogin;
    ProgressBar progressview;
    TextView SignInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.LoginBtn);
        progressview = findViewById(R.id.progressBar);
        SignInTextView = findViewById(R.id.tv_SignUp);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                if(!validate(email,password)){
                    return;
                }

                LoginUser(email,password);


            }

        });

        SignInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginAct.this,CreateAccountAct.class));
            }
        });
    }

    void LoginUser(String email, String password) {
        changeinProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeinProgress(false);
                if(task.isSuccessful()){
                    if(firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginAct.this,MainActivity.class));
                    }else{
                        Toast.makeText(LoginAct.this,"Email is not veified, please check your inbox",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(LoginAct.this,task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean validate(String email, String Password){
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Email is Invalid");
            return false;
        }
        if(Password.length()>8){
            etPassword.setError("Invalid password Length ");
            return false;
        }

        return true;

    }

    void changeinProgress(boolean inProgress){
        if(inProgress){
            progressview.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
        }else{
            progressview.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
        }
    }
}