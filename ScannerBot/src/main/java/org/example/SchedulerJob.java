package org.example;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.io.IOException;
import java.util.List;

public class SchedulerJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        NewsScraper scraper = new NewsScraper();
        NewsTracker tracker = new NewsTracker();
        TelegramBotSender botSender = new TelegramBotSender();
        AIProcessor aiProcessor = new AIProcessor();

        try {
            // Отримуємо всі новини з головної сторінки
            List<String> newsLinks = scraper.getLatestNewsLinks();

            // Проходимо по кожній новині і перевіряємо, чи вона вже була відправлена
            for (String newsUrl : newsLinks) {
                System.out.println(newsUrl);

                if (!tracker.isNewsSent(newsUrl)) {
                    // Парсимо деталі новини
                    String[] newsDetails = scraper.getNewsDetails(newsUrl);
                    String title = newsDetails[0];
                    String description = newsDetails[1];
                    String imageUrl = newsDetails[2];
                    String videoUrl = newsDetails[3];

                    // Скорочуємо опис за допомогою AI
                    String shortDescription = aiProcessor.getSummaryFromAI(description);

                    // Формуємо повідомлення
                    String message = "<b>" + title + "</b>\n\n" + shortDescription;
                    if (!videoUrl.isEmpty()) {
                        message += "\nВідео: " + videoUrl;
                    }

                    // Відправляємо фото з підписом
                    if (!imageUrl.isEmpty()) {
                        botSender.sendPhotoWithCaption(imageUrl, title, shortDescription);
                    } else {
                        botSender.sendMessage(message, "HTML");
                    }

                    // Додаємо новину в список відправлених
                    tracker.addSentNews(newsUrl);

                    // Пауза між відправками
                    Thread.sleep(5000); // 5 секунд між відправками
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}