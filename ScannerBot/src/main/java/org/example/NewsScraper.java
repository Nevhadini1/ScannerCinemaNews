package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewsScraper {

    private static final String NEWS_URL = "https://itc.ua/cinema/";

    // Метод для отримання посилань на новини з головної сторінки
    public List<String> getLatestNewsLinks() throws IOException {
        List<String> newsLinks = new ArrayList<>();
        Document doc = Jsoup.connect(NEWS_URL).get();

        // Знаходимо посилання на новини (селектор може змінюватися залежно від сторінки)
        Elements newsElements = doc.select("h2.entry-title a");
        for (Element newsElement : newsElements) {
            newsLinks.add(newsElement.attr("href"));
        }
        return newsLinks;
    }

    // Метод для парсингу окремої новини
    public String[] getNewsDetails(String newsUrl) throws IOException {
        Document newsDoc = Jsoup.connect(newsUrl).get();

        // Отримання заголовку
        String title = "";
        Element titleElement = newsDoc.selectFirst("h1.entry-title");
        if (titleElement != null) {
            title = titleElement.text();
        }

        // Отримання опису новини
        StringBuilder description = new StringBuilder();
        Elements contentElements = newsDoc.select("div.entry-content p");
        for (Element contentElement : contentElements) {
            if (! contentElement.text().contains("Реклама") && !contentElement.hasClass("intro") && !contentElement.text().contains("LG Home Entertainment")) {
                description.append(contentElement.text()).append("\n");
            }
        }

        // Отримання URL зображення (оновлений селектор)
        String imageUrl = "";
        Element imageElement = newsDoc.selectFirst("meta[property=og:image]");
        if (imageElement != null) {
            imageUrl = imageElement.attr("content"); // Отримання повного URL зображення
            System.out.println("Отримано URL зображення: " + imageUrl);
        } else {
            System.out.println("Зображення не знайдено для цієї новини.");
        }

        // Отримання відео, якщо є
        String videoUrl = "";
        Element videoElement = newsDoc.selectFirst("div.video-wrapper iframe");
        if (videoElement != null) {
            videoUrl = videoElement.attr("src");
        }

        return new String[]{title, description.toString(), imageUrl, videoUrl};
    }

}
