package Mobile.memoflow;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {

    static void showToast(Context context,String Maasage){
        Toast.makeText(context,Maasage,Toast.LENGTH_SHORT).show();
    }

    static CollectionReference getCollectionRefferenceForNotes(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return FirebaseFirestore.getInstance().collection("Notes")
                .document(firebaseAuth.getCurrentUser().getUid()).collection("my_notes");

    }

    static  String TimeStampToString(Timestamp timestamp){
        return new SimpleDateFormat("MM/dd/yyyy").format(timestamp.toDate());

    }
}
