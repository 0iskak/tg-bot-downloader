package app;

import lombok.SneakyThrows;

public class App {

    @SneakyThrows
    public static void main(String[] args) {
        new Bot();
        new Cleaner();
        System.out.println("Started!");
    }
}
