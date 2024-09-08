package org.example;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class NewsTracker {

    private Set<String> sentNews = new HashSet<>();
    private static final String FILE_PATH = "sent_news.txt";

    // Додає новину до списку відправлених
    public void addSentNews(String newsUrl) {
        sentNews.add(newsUrl);
        saveSentNewsToFile();
    }

    // Перевіряє, чи була новина вже відправлена
    public boolean isNewsSent(String newsUrl) {
        loadSentNewsFromFile();
        return sentNews.contains(newsUrl);
    }

    // Зберігає новини до файлу
    private void saveSentNewsToFile() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (String newsUrl : sentNews) {
                writer.write(newsUrl + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Завантажує новини з файлу
    private void loadSentNewsFromFile() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sentNews.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
