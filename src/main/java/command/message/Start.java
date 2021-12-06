package command.message;

public class Start extends Message {
    public final static String command = "/start";

    public Start() {
        send(String.format("""
                Telegram downloader bot:
                type %s to see commands
                """, Help.command));
    }
}
