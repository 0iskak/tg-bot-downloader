package command;

import app.Bot;
import com.pengrad.telegrambot.request.AbstractSendRequest;
import com.pengrad.telegrambot.request.EditMessageText;

public abstract class Command {
    protected Bot bot = Bot.getBot();
    protected Integer message_id;

    public void execute(AbstractSendRequest<?> request) {
        message_id = bot.execute(request).message().messageId();
    }

    protected void execute(EditMessageText editMessageText) {
        bot.execute(editMessageText);
    }
}
