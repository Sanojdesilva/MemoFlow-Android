package Mobile.memoflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class BuffringAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buffring);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseAuth currentUser = FirebaseAuth.getInstance();
                if(currentUser.getCurrentUser()==null){
                    startActivity(new Intent(BuffringAct.this,LoginAct.class));
                }else{
                    startActivity(new Intent(BuffringAct.this,MainActivity.class));
                }
                finish();
            }
        },2200);

    }
}