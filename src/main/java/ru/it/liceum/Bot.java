package ru.it.liceum;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {


    @Override
    public String getBotToken() {
        return "6148529989:AAGcSNWt4eFSgQz8dftlmVkUZWC73_CQTaU";
    }

    @Override
    public String getBotUsername() {
        return "ClassroomNoiseBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();

        checkIfMsgIsCommand(update);
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString())
                .text(what)
                .build();
        try {
            execute(sm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void checkIfMsgIsCommand(Update update) {
        Message message = update.getMessage();
        if (message.isCommand()) {
            switch (message.getText()) {
                case "/check@ClassroomNoiseBot" -> checkNoiseLevelToChatRoom(message);
                case "/music@ClassroomNoiseBot" -> playMusicToChatRoom(message);
                case "/info@ClassroomNoiseBot" -> sendInfoToChatRoom(message);
                case "/info" -> sendInfoToUser(message);
                case "/music" -> playMusicToUser(message);
                case "/check" -> checkNoiseLevelToUser(message);
                case "/noise" -> sendText(message.getFrom().getId(), "В комнате слишком шумно. Сообщите об этом в чате класса");
                case "/noise@ClassroomNoiseBot" -> sendText(message.getChatId(), "Пожалуйста, ведите себя потише!");
                default -> sendText(message.getFrom().getId(), "Unavailable command");
            }
        } else if (message.getChat().isUserChat()){
            sendText(message.getFrom().getId(), "Для получения информации о боте введите команду /info");
        }
    }

    private void checkNoiseLevelToUser(Message message) {
        sendText(message.getFrom().getId(), "Ссылка на сайт для проверки уровня громкости в помещении: https://youlean.co/online-loudness-meter/\n\nЕсли шкала находится не в зеленой зоне, значит, в комнате слишком шумно.");
    }

    private void playMusicToUser(Message message) {
        sendText(message.getFrom().getId(), "Ссылка на сайт с фоновой музыкой: https://noises.online");
    }

    private void sendInfoToUser(Message message) {
        sendText(message.getFrom().getId(), """
                Этот бот умеет отдавать ссылку на сайт, где можно проверить текущий уровень шума в помещении, а также ссылку на сайт с разнообразной фоновой расслабляющей музыкой.

                Для получения ссылки на сайт с проверкой шума введите команду /check.

                Для получения ссылки на сайт с фоновой музыкой, введите команду /music.""");
    }

    private void sendInfoToChatRoom(Message message) {
        sendText(message.getChatId(), """
                Этот бот умеет отдавать ссылку на сайт, где можно проверить текущий уровень шума в помещении, а также ссылку на сайт с разнообразной фоновой расслабляющей музыкой.

                 Для получения ссылки на сайт с проверкой шума введите команду /check

                 для получения ссылки на сайт с фоновой музыкой, введите команду /music""");
    }

    private void checkNoiseLevelToChatRoom(Message message) {
        sendText(message.getChatId(), "Ссылка на сайт для проверки уровня громкости в помещении: https://youlean.co/online-loudness-meter/.\n\nЕсли шкала находится не в зеленой зоне, значит, в комнате слишком шумно.");
    }

    private void playMusicToChatRoom(Message message) {
        sendText(message.getChatId(), "Ссылка на сайт с фоновой музыкой: https://noises.online");
    }
}
