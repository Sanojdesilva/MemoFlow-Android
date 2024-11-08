package Mobile.memoflow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        Toast.makeText(context, "Reminder: " + title + " - " + content, Toast.LENGTH_LONG).show();

        // Optionally, you can show a notification instead of a Toast
        // You can create and show a notification here
    }
}
