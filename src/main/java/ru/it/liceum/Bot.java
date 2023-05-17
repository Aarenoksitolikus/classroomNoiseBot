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
                case "/info", "/info@ClassroomNoiseBot" -> getInfo(message);
                case "/theory", "/theory@ClassroomNoiseBot" -> getTheory(message);
                case "/options", "/options@ClassroomNoiseBot" -> getCheckOptions(message);
                case "/checkers", "/checkers@ClassroomNoiseBot" -> getCheckers(message);
                case "/checker1", "/checker1@ClassroomNoiseBot" -> getFirstChecker(message);
                case "/checker2", "/checker2@ClassroomNoiseBot" -> getSecondChecker(message);
                case "/music", "/music@ClassroomNoiseBot" -> playMusicToChatRoom(message);
                case "/noise", "/noise@ClassroomNoiseBot" ->
                        sendText(message.getChatId(), "Пожалуйста, ведите себя потише!");
                default -> sendText(message.getFrom().getId(), "Unavailable command");
            }
        } else if (message.getChat().isUserChat()) {
            sendText(message.getFrom().getId(), "Для получения информации о боте введите команду /info");
        }
    }

    private void getInfo(Message message) {
        sendText(message.getChatId(), "Этот бот поможет вам быстро получить доступ " +
                "к интернет-ресурсам по влиянию постороннего шума на организм и работоспособность, " +
                "существующим методам борьбы с лишним шумом и способам измерения уровня шума в помещении, " +
                "а также содержит ссылки на полезные для борьбы с шумом сайты");
    }

    private void getTheory(Message message) {
        sendText(message.getChatId(), """
                Здесь вы можете найти ссылки на несколько сайтов, содержащих теоретическую информацию о том, как уровень шума влияет на человека.

                https://web.snauka.ru/issues/2017/06/83679

                https://eduherald.ru/ru/article/view?id=12026

                http://ecologylib.ru/books/item/f00/s00/z0000044/st063.shtml""");
    }

    private void getCheckOptions(Message message) {
        sendText(message.getChatId(), """
                Здесь вы можете найти ссылки на несколько сайтов, содержащих информацию о современных способах борьбы с шумом.

                https://www.protrud.com/обучение/учебный-курс/защита-от-повышенного-уровня-шума/

                https://skomplekt.com/kak-snizit-shum-v-ofise/
                
                https://cge28.ru/noise/""");
    }

    private void getCheckers(Message message) {
        sendText(message.getChatId(), """
                Здесь вы можете найти ссылки на несколько сайтов, на которых можно найти информацию о способах изменения уровня шума в помещении, а также о калибровке измерительных приборов.

                https://tion.ru/blog/uroven-shuma/

                https://androidinsider.ru/polezno-znat/izmerit-shum-telefonom-vsyo-chto-nuzhno-znat-ob-etom.html#:~:text=Огромное%20количество%20пользователей%20Android%20знают,смартфон%20в%20сторону%20источника%20шума
                
                https://www.metronx.ru/articles/kalibrovka-shumomera/""");
    }

    private void getFirstChecker(Message message) {
        sendText(message.getChatId(), "Ссылка на первый сайт для проверки уровня громкости в помещении: https://youlean.co/online-loudness-meter/.\n\nЕсли шкала находится не в зеленой зоне, значит, в комнате слишком шумно.");
    }

    private void getSecondChecker(Message message) {
        sendText(message.getChatId(), "Ссылка на второй сайт для проверки уровня громкости в помещении: https://www.checkhearing.org/soundmeter.php.\n\nЕсли шкала находится выше голубой зоны, значит, в комнате слишком шумно.");

    }

    private void playMusicToChatRoom(Message message) {
        sendText(message.getChatId(), "Ссылка на сайт с фоновой музыкой: https://noises.online");
    }
}
