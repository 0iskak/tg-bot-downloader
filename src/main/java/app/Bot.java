package app;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import command.downloader.Youtube;
import command.message.CommandNotFound;
import command.message.Help;
import command.message.Start;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Getter
public class Bot extends TelegramBot {
    @Getter
    private static Bot bot;

    private Long id;
    private String[] args;
    private String command;

    public Bot() {
        super("API");
        bot = this;
        setUpdatesListener(this::onUpdate);
    }

    private int onUpdate(List<Update> updates) {
        updates.stream().map(Update::message).filter(Objects::nonNull).forEach(this::onUpdate);
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void onUpdate(Message message) {
        var currentTime = Instant.now().getEpochSecond();
        var messageTime = message.date();
        var deltaTime = currentTime - messageTime;

        if (deltaTime > 5) {
            System.out.println("Message is too old");
            return;
        }

        id = message.chat().id();
        args = message.text().split("\s+");
        command = args[0];

        switch (command) {
            case Start.command -> new Start();
            case Help.command -> new Help();
            case Youtube.command -> new Youtube();
            default -> {
                if (command.startsWith("/"))
                    new CommandNotFound();
            }
        }
    }
}
