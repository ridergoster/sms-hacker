package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

class NewSmsEvent {
    private SMS sms;
    private String phoneNumber;

    NewSmsEvent(SMS sms, String phoneNumber) {
        this.sms = sms;
        this.phoneNumber = phoneNumber;
    }

    SMS getSms() {
        return sms;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }
}
