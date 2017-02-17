package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

class NewSmsEvent {
    private SMS sms;
    private String phoneNumber;
    private String person;
    private String threadId;

    NewSmsEvent(SMS sms, String phoneNumber, String person, String threadId) {
        this.sms = sms;
        this.phoneNumber = phoneNumber;
        this.person = person;
        this.threadId = threadId;
    }

    SMS getSms() {
        return sms;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPerson() {
        return person;
    }

    public String getThreadId() {
        return threadId;
    }
}
