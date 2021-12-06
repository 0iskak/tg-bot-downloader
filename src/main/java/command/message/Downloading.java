package command.message;

import lombok.Setter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Downloading extends Message {
    @Setter
    private static boolean downloaded;
    @Setter
    private static boolean sent;

    private int count = 0;
    private final ScheduledExecutorService executor;
    private final String stage = "Stage[x/2]:";

    public Downloading() {
        send(downloadingMessage());
        downloaded = false;
        sent = false;

        var period = 200L;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::downloading, period, period,
                TimeUnit.MILLISECONDS);
    }

    private void downloading() {
        if (sent) {
            new Edit(message_id, doneMessage());
            executor.shutdown();
            return;
        }
        if (downloaded)
            new Edit(message_id, sendingMessage());
        else
            new Edit(message_id, downloadingMessage());

        if (count++ >= 4)
            count = 0;
    }

    private String sendingMessage() {
        return stage.replace("x", "2") + " Sending"
                + ".".repeat(count);
    }

    private String doneMessage() {
        return stage.replace("x", "2") + " Done";
    }

    private String downloadingMessage() {
        return stage.replace("x", "1") +
                " Downloading" + ".".repeat(count);
    }
}
