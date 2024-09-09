package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TelegramBotSender {

    private static final String BOT_TOKEN = "6760216739:AAFGSgdz5hpkICIHGqBOBosTDrOgujboisE";
    private static final String CHAT_ID = "-1002084059113";

    public void sendMessage(String message, String parseMode) throws IOException {
        String urlString = String.format(
                "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s&parse_mode=%s",
                BOT_TOKEN, CHAT_ID, URLEncoder.encode(message, "UTF-8"), parseMode);

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Помилка при відправці повідомлення: " + response.toString());
        }
    }

    public void sendPhoto(String photoUrl) throws IOException {
        System.out.println(photoUrl);
        String urlString = String.format(
                "https://api.telegram.org/bot%s/sendPhoto?chat_id=%s&photo=%s",
                BOT_TOKEN, CHAT_ID, URLEncoder.encode(photoUrl, "UTF-8"));

        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Помилка при відправці зображення: " + response.toString());
        }
    }
public void sendPhotoWithCaption(String photoUrl, String title, String description) throws IOException {
    // Форматування підпису (заголовок + опис)
    String caption = "<b>" + title + "</b>\n\n" + description;

    // URL для запиту до Telegram API
    String urlString = String.format(
            "https://api.telegram.org/bot%s/sendPhoto?chat_id=%s&photo=%s&caption=%s&parse_mode=HTML",
            BOT_TOKEN, CHAT_ID, URLEncoder.encode(photoUrl, "UTF-8"), URLEncoder.encode(caption, "UTF-8"));

    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
        System.out.println("Помилка при відправці фото");
    } else {
        System.out.println("Фото з підписом успішно надіслано");
    }
}

}
