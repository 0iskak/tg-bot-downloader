package command.message;

import com.pengrad.telegrambot.request.EditMessageText;
import command.Command;

public class Edit extends Command {
    public Edit(Integer message_id, String message) {
        execute(new EditMessageText(bot.getId(), message_id, message));
    }
}
