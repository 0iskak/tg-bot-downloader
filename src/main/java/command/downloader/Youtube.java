package command.downloader;

import com.github.kiulian.downloader.YoutubeDownloader;
import com.github.kiulian.downloader.downloader.request.RequestVideoFileDownload;
import com.github.kiulian.downloader.downloader.request.RequestVideoInfo;
import com.github.kiulian.downloader.model.videos.VideoInfo;
import com.github.kiulian.downloader.model.videos.formats.AudioFormat;
import com.github.kiulian.downloader.model.videos.formats.Format;
import com.github.kiulian.downloader.model.videos.formats.VideoFormat;
import com.github.kiulian.downloader.model.videos.formats.VideoWithAudioFormat;
import command.message.Custom;
import command.message.Downloading;
import command.message.InvalidArgs;
import command.message.InvalidURL;

import java.io.File;
import java.util.UUID;

public class Youtube extends Downloader {
    public static final String command = "/yt";
    public static final String help = String.format("Youtube downloader:\n" +
            "%1$s URL - download best video with sound\n" +
            "%1$s URL %2$s - list available formats\n" +
            "%1$s URL (a/v)number - download specific format", command, format);

    private final YoutubeDownloader downloader = new YoutubeDownloader();
    private final File saveDir = new File(savePath);
    private final String fileName = UUID.randomUUID().toString();
    private VideoInfo info;


    public Youtube() {
        var argsLength = bot.getArgs().length;
        switch (argsLength) {
            case 1:
                new Custom(help);
                break;
            case 2:
            case 3:
                var url = bot.getArgs()[1];
                if (getInfo()) {
                    new InvalidURL(url);
                    return;
                }
                switch (argsLength) {
                    case 2:
                        send(info.bestVideoWithAudioFormat(), Type.VIDEO);
                        break;
                    case 3:
                        var arg = bot.getArgs()[2];
                        if (format.equals(arg)) {
                            new Custom(listFormats());
                        } else {
                            int no;
                            try {
                                no = Integer.parseInt(String.valueOf(arg.charAt(1)));
                            } catch (NumberFormatException | IndexOutOfBoundsException ignored) {
                                new InvalidArgs();
                                return;
                            }
                            switch (arg.charAt(0)) {
                                case 'a':
                                    send(info.audioFormats().get(no), Type.AUDIO);
                                    break;
                                case 'v':
                                    send(info.videoFormats().get(no), Type.VIDEO);
                                    break;
                                default:
                                    new InvalidArgs();
                            }
                        }
                }
        }

    }

    private String listFormats() {
        var formats = new StringBuilder("Audio:\n");
        for (int i = 0; i < info.audioFormats().size(); i++)
            formats.append(String.format("a%d. %s\n", i, toString(info.audioFormats().get(i))));
        formats.append("\nVideo:\n");
        for (int i = 0; i < info.videoFormats().size(); i++)
            formats.append(String.format("v%d. %s\n", i, toString(info.videoFormats().get(i))));
        return formats.toString();
    }

    private String toString(Format format) {
        String quality = " ";

        if (format instanceof AudioFormat) {
            var a = (AudioFormat) format;
            quality += a.audioSampleRate() + "Hz";

            if (a.equals(info.bestAudioFormat()))
                quality += " (best)";
        } else if (format instanceof VideoFormat) {
            var v = (VideoFormat) format;
            quality += v.qualityLabel();

            if (v.equals(info.bestVideoWithAudioFormat()))
                quality += " (best) (default)";
            if (!(v instanceof VideoWithAudioFormat))
                quality += " (no sound)";
        }

        return String.format("%s ", format.extension().value() + quality);
    }

    private void send(Format format, Type type) {
        new Downloading();
        var file = download(format);
        sendMedia(file, type);
    }

    private File download(Format format) {
        return downloader.downloadVideoFile(
                new RequestVideoFileDownload(format)
                        .saveTo(saveDir)
                        .renameTo(fileName)
                        .overwriteIfExists(true)
        ).data();
    }

    private boolean getInfo() {
        var url = parse(bot.getArgs()[1]);
        info = downloader.getVideoInfo(
                new RequestVideoInfo(url)).data();
        return info == null;
    }

    private String parse(String url) {
        String url1 = "https://youtu.be/";
        String url2 = "https://www.youtube.com/watch?v=";
        url = url.replace(url1, "").replace(url2, "");
        int last = url.indexOf('?');
        return last == -1 ? url : url.substring(0, last);
    }

}
