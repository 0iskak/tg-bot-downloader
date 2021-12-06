package command.message;

public class InvalidURL extends Message {
    public InvalidURL(String url) {
        send(url + " is invalid URL");
    }
}
