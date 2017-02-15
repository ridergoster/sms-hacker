package stage.metasploit.com.backdooredapk;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grunt on 15/02/2017.
 */

public class ConversationActivity extends Activity {
    private EditText editSms;
    private ImageView imageSend;
    private ConversationAdapter mAdapter;
    private String phoneNumber;
    int oldsize;
    boolean zero = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        ProgressBar loadingBar = (ProgressBar) findViewById(R.id.progress_conversation);
        loadingBar.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        TextView tv = (TextView) findViewById(R.id.text_dest);
        tv.setText(intent.getStringExtra(getString(R.string.extra_dest)));
        phoneNumber = intent.getStringExtra(getString(R.string.extra_phone));
        RecyclerView rvMessages = (RecyclerView) findViewById(R.id.rv_conversation);
        rvMessages.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(llm);
        imageSend = (ImageView) findViewById(R.id.bt_send);
        imageSend.setImageAlpha(128);
        mAdapter = new ConversationAdapter(getFullConversation(intent.getStringExtra(getString(R.string.extra_threadid))), rvMessages);
        rvMessages.setAdapter(mAdapter);
        loadingBar.setVisibility(View.GONE);
        rvMessages.scrollToPosition(mAdapter.getItemCount() - 1);
        editSms = (EditText) findViewById(R.id.edit_message);
        editSms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                oldsize = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (zero) {
                    imageSend.setImageAlpha(255);
                    imageSend.animate().rotationBy(315).setDuration(300).start();
                    zero = false;
                } else if (s.length() == 0 && !zero) {
                    imageSend.setImageAlpha(128);
                    imageSend.animate().rotationBy(-315).setDuration(300).start();
                    zero = true;
                }
            }
        });
    }

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

    public void onClick(View v) {
        if (imageSend.getImageAlpha() == 255){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, editSms.getText().toString(), null, null);
            mAdapter.addSms(new SMS(editSms.getText().toString(), "2"));
            editSms.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(NewSmsEvent event) {
        mAdapter.addSms(event.getSms());
    }

}
