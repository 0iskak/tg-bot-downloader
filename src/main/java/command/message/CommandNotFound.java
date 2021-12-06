package command.message;

public class CommandNotFound extends Message {
    public CommandNotFound() {
        send(bot.getCommand() + ": command not found");
    }
}
