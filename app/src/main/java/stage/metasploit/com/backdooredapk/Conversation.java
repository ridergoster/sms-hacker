package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

public class Conversation {
    private String person;
    private String lastMessage;
    private String threadId;
    private String phoneNumber;

    Conversation(String person, String lastMessage, String threadId, String phoneNumber) {
        this.person = person;
        this.lastMessage = lastMessage;
        this.threadId = threadId;
        this.phoneNumber = phoneNumber;
    }

    public String getPerson() {
        return person;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
