package command.message;

import command.downloader.Youtube;

public class Help extends Message {
    public final static String command = "/help";
    public Help() {
        send(String.format("%s\n", Youtube.help));
    }
}
