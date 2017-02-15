package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

public class Conversation {
    private String person;
    private String lastMessage;

    Conversation(String person, String lastMessage) {
        this.person = person;
        this.lastMessage = lastMessage;
    }

    public String getPerson() {
        return person;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
