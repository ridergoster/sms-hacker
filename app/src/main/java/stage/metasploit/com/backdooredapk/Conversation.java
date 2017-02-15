package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

public class Conversation {
    private String person;
    private String lastMessage;
    private String threadId;

    Conversation(String person, String lastMessage, String threadId) {
        this.person = person;
        this.lastMessage = lastMessage;
        this.threadId = threadId;
    }

    public String getPerson() {
        return person;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public String getThreadId() {
        return threadId;
    }
}
