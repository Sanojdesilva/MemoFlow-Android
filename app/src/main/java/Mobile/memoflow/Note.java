package Mobile.memoflow;

import com.google.firebase.Timestamp;

public class Note {
    String title;
    String Content;
    com.google.firebase.Timestamp timestamp;

    public Note() {
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return Content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        Content = content;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
