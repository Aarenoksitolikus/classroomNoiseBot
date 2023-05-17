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
                case "/check" -> checkNoiseLevelToChatRoom(message);
                case "/music" -> playMusicToChatRoom(message);
                case "/info" -> sendInfoToChatRoom(message);
                case "/noise" -> sendText(message.getChatId(), "Пожалуйста, ведите себя потише!");
                default -> sendText(message.getFrom().getId(), "Unavailable command");
            }
        } else if (message.getChat().isUserChat()){
            sendText(message.getFrom().getId(), "Для получения информации о боте введите команду /info");
        }
    }

    private void sendInfoToChatRoom(Message message) {
        sendText(message.getChatId(), """
                Этот бот умеет отдавать ссылку на сайт, где можно проверить текущий уровень шума в помещении, а также ссылку на сайт с разнообразной фоновой расслабляющей музыкой.

                Для получения ссылки на сайт с проверкой шума введите команду /check.

                Для получения ссылки на сайт с фоновой музыкой, введите команду /music""");
    }

    private void checkNoiseLevelToChatRoom(Message message) {
        sendText(message.getChatId(), "Ссылка на сайт для проверки уровня громкости в помещении: https://youlean.co/online-loudness-meter/.\n\nЕсли шкала находится не в зеленой зоне, значит, в комнате слишком шумно.");
    }

    private void playMusicToChatRoom(Message message) {
        sendText(message.getChatId(), "Ссылка на сайт с фоновой музыкой: https://noises.online");
    }
}
