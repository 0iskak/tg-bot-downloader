package command.downloader;

import com.pengrad.telegrambot.request.SendAudio;
import com.pengrad.telegrambot.request.SendVideo;
import command.Command;
import command.message.Downloading;

import java.io.File;

public abstract class Downloader extends Command {
    public static final String savePath = "src/main/resources/files";
    protected static final String format = "f";

    protected void sendMedia(File file, Type type) {
        Downloading.setDownloaded(true);

        switch (type) {
            case AUDIO -> execute(new SendAudio(bot.getId(), file));
            case VIDEO -> execute(new SendVideo(bot.getId(), file));
        }

        Downloading.setSent(true);
    }

    protected enum Type {
        AUDIO,
        VIDEO
    }
}
