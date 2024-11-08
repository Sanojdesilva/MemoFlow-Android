package Mobile.memoflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;

public class AddNoteAct extends AppCompatActivity {

    EditText title,content;
    ImageButton saveBtn;
    TextView pageTitle;

    ImageButton setReminderBtn;


    String Stitle , Scontent , SdocId;
    boolean isEditMode = false;
    ImageButton deleteBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = findViewById(R.id.notesTitleText);
        content = findViewById(R.id.contenttext);
        saveBtn = findViewById(R.id.saveNoteBtn);
        pageTitle = findViewById(R.id.Pagetitle);
        deleteBtn = findViewById(R.id.deleteBtn);
        setReminderBtn = findViewById(R.id.ReminderButton);

        //receive data
        Stitle = getIntent().getStringExtra("title");
        Scontent= getIntent().getStringExtra("content");
        SdocId = getIntent().getStringExtra("docId");

        if(SdocId!=null && !SdocId.isEmpty()){
            isEditMode = true;
        }

        title.setText(Stitle);
        content.setText(Scontent);
        if(isEditMode){
            pageTitle.setText("Edit Your Note");
            deleteBtn.setVisibility(View.VISIBLE);
        }


        saveBtn.setOnClickListener((v)->SaveNote());
        deleteBtn.setOnClickListener((v)->DeleteNotes());
        setReminderBtn.setOnClickListener((v) -> showDateTimePickerDialog());

    }

    private void showDateTimePickerDialog() {

        final Calendar currentDate = Calendar.getInstance();
        final Calendar date = Calendar.getInstance();
        new DatePickerDialog(AddNoteAct.this, (view, year, monthOfYear, dayOfMonth) -> {
            date.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(AddNoteAct.this, (view1, hourOfDay, minute) -> {
                date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                date.set(Calendar.MINUTE, minute);
                setReminder(date.getTimeInMillis());
            }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    void setReminder(long timeInMillis) {
        Intent intent = new Intent(AddNoteAct.this, ReminderReceiver.class);
        intent.putExtra("title", title.getText().toString());
        intent.putExtra("content", content.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNoteAct.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
            Utility.showToast(AddNoteAct.this, "Reminder set successfully!");
        }
    }


    void DeleteNotes() {
        // for delete notes from firebase
        DocumentReference documentReference;
        documentReference = Utility.getCollectionRefferenceForNotes().document(SdocId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(AddNoteAct.this,"Notes deleted Successfully!");
                    finish();
                }else {
                    Utility.showToast(AddNoteAct.this,"Failed While deleting Notes!");
                }
            }
        });
    }



    void SaveNote(){
        String noteTitle = title.getText().toString();
        String noteContent = content.getText().toString();
        if(noteTitle.isEmpty()){
            title.setError("Title is Missing");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNoteinFireBase(note);

    }

    void saveNoteinFireBase(Note note){
        DocumentReference documentReference;
        if(isEditMode){
            //update the note
            documentReference = Utility.getCollectionRefferenceForNotes().document(SdocId);
        }else{
            //create a new note
            documentReference = Utility.getCollectionRefferenceForNotes().document();
        }


        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Utility.showToast(AddNoteAct.this,"Notes added Successfully!");
                    finish();
                }else {
                    Utility.showToast(AddNoteAct.this,"Failed While Adding Notes!");
                }
            }
        });
    }
}