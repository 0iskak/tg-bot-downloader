package command.message;

import app.Bot;
import com.pengrad.telegrambot.request.SendMessage;
import command.Command;

public abstract class Message extends Command {
    public void send(String message) {
        execute(new SendMessage(Bot.getBot().getId(), message));
    }
}
