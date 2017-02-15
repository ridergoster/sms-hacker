package stage.metasploit.com.backdooredapk;

/**
 * Created by Grunt on 15/02/2017.
 */

public class SMS {
    private String body;
    private String type;

    public SMS (String body, String type) {
        this.body = body;
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public String getType() {
        return type;
    }
}
