package com.example.deadlines;

import android.content.Intent;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Parser {
    public Parser() throws IOException, InterruptedException {
        NewThreadProcesses.launch(runParsing);
    }
    //парсинг распиния уроков
    Runnable runParsing = new Runnable() {
        @Override
        public void run() {
            Document doc = null;
            HashMap<String, HashMap<String, String>> schedule = new HashMap<>();
            String str = "https://hselyceum.eljur.ru/journal-schedule-action/class."+Single.getInstance().credentialsOfUser.get("Group")+"?&week=both";
            try {
                doc = Jsoup.connect(str).userAgent("Chrome/4.0.249.0 Safari/532.5").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements listNews = doc.select("#schedule > div");
            for (Element element : listNews) {
                Elements lessons = element.select("div > div.schedule__day__content__column > div");
                HashMap<String, String> day = new HashMap<>();
                for (Element lesson : lessons.not(".schedule__day__content__lesson--extra")) {
                    day.put(String.valueOf(lesson.select("div.columns > div.column-75 > div.schedule__day__content__lesson__num").text()), String.valueOf(lesson.select("div.columns > div.column-75 > div.schedule__day__content__lesson__data > span").text()));
                }
                schedule.put(String.valueOf(element.select("div > div.column-40 > p:nth-child(2)").text()), day);

            }
            Single.getInstance().schedule = schedule;
        }
    };


}
