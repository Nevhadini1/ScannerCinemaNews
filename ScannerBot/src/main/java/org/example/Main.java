package org.example;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            // Створення планувальника Quartz
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

            // Налаштування JobDetail (наш SchedulerJob, який сканує сайт)
            JobDetail job = JobBuilder.newJob(SchedulerJob.class)
                    .withIdentity("newsJob", "group1")
                    .build();

            // Налаштування тригера для запуску задачі кожні 3 години
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("newsTrigger", "group1")
                    .startNow() // Почати відразу після запуску
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInHours(4)  // Інтервал 4 години
                            .repeatForever())         // Повторювати завжди
                    .build();

            // Запуск планувальника
            scheduler.scheduleJob(job, trigger);
            scheduler.start();

            System.out.println("Планувальник запущено, сканування кожні 3 години.");

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
