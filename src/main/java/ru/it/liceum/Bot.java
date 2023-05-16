package ru.it.liceum;

import be.tarsos.dsp.io.TarsosDSPAudioInputStream;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

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
            if ("/music".equals(message.getText())) {
                playMusic(message);
            } else {
                sendText(message.getFrom().getId(), "Unavailable command");
            }
        } else if (message.hasVoice()) {
            checkNoiseLevel(message);
        }
    }

    private void checkNoiseLevel(Message message) {
        sendText(message.getFrom().getId(), "noise check started");
        Voice voice = message.getVoice();
        try {
            GetFile getFile = new GetFile();
            getFile.setFileId(voice.getFileId());
            String filePath = execute(getFile).getFilePath();
            File file = new File("src\\main\\resources\\voice\\voice" + message.getFrom().getId() + ".wav");
            downloadFile(filePath, file);
//            AudioInputStream in = AudioSystem.getAudioInputStream(new File(file.getPath()));
            /*AudioFormat fromFormat = in.getFormat();
            AudioFormat toFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    fromFormat.getSampleRate(),
                    16,
                    fromFormat.getChannels(),
                    fromFormat.getChannels() * 2,
                    fromFormat.getSampleRate(),
                    false);

            AudioInputStream converted = AudioSystem.getAudioInputStream(toFormat, in);

            File out = new File("track.wav");
            AudioSystem.write(converted, AudioFileFormat.Type.WAVE, out);

            in.close();
            converted.close();*/
            TarsosDSPAudioInputStream audioStream = new AudioInputStreamAdapter(audioInputStream);
            double dbValue = Math.abs(20 * Math.log10(calculateRMS(Files.readAllBytes(file.toPath())) / 32768));
            sendText(message.getFrom().getId(), "Среднее значение громкости: " + dbValue + " децибел");
        } catch (IOException | TelegramApiException e) {
            e.printStackTrace();
            // ignore
        }
    }

    private static double getAverageDBValue(File file) throws UnsupportedAudioFileException, IOException, NullPointerException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        byte[] bytes = new byte[(int) (frames * format.getFrameSize())];
        audioInputStream.read(bytes);
        double rms = calculateRMS(bytes);
        return 20 * Math.log10(rms / Math.pow(2, 15));
    }

    private static double calculateRMS(byte[] bytes) {
        int[] samples = new int[bytes.length / 2];
        for (int i = 0; i < samples.length; i++) {
            short sample = (short) (((bytes[i * 2] & 0xFF) << 8) | (bytes[i * 2 + 1] & 0xFF));
            samples[i] = sample;
        }
        long sum = 0;
        for (int sample : samples) {
            sum += (long) sample * sample;
        }
        double mean = (double) sum / samples.length;
        return Math.sqrt(mean);
    }

    private void playMusic(Message message) {
        sendText(message.getFrom().getId(), "music playing");
    }
}
