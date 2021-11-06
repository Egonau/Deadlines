package com.example.deadlines;
import android.content.Intent;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class ParserOlymps {
    public ParserOlymps() throws IOException, InterruptedException {
        NewThreadProcesses.launch(runParsing);
    }
    //парсинг списка олимпиад
    Runnable runParsing = new Runnable() {
        @Override
        public void run() {
            Single.getInstance().allCurrentOlympiads.clear();
            Document doc = null;
            String type;
            String classes = "any";
            String[] groupOfUser = Single.getInstance().credentialsOfUser.get("Group").toString().split("");
            if (groupOfUser.length>1) {
                if (groupOfUser[0].equals("")) {
                    classes = groupOfUser[1].toString() + groupOfUser[2].toString();
                } else {
                    try {
                        classes = groupOfUser[1].toString();
                    } finally {
                        classes = "any";
                    }

                }
            }
            switch(Single.getInstance().credentialsOfUser.get("Olymps")){
                case ("Биология"):{
                    type = "any&subject%5B11%5D=on";
                    break;
                }
                case ("География"):{
                    type = "any&subject%5B10%5D=on";
                    break;
                }
                case ("Информатика"):{
                    type = "any&subject%5B7%5D=on";
                    break;
                }
                case ("Математика"):{
                    type = "any&subject%5B6%5D=on";
                    break;
                }
                case ("Физика"):{
                    type = "any&subject%5B12%5D=on";
                    break;
                }
                case ("Химия"):{
                    type = "any&subject%5B13%5D=on";
                    break;
                }
                case ("Астрономия"):{
                    type = "any&subject%5B20%5D=on";
                    break;
                }
                case ("ИЗО"):{
                    type = "any&subject%5B22%5D=on";
                    break;
                }
                case ("Искусство"):{
                    type = "any&subject%5B18%5D=on";
                    break;
                }
                case ("История"):{
                    type = "any&subject%5B8%5D=on";
                    break;
                }
                case ("Лингвистика"):{
                    type = "any&subject%5B24%5D=on";
                    break;
                }
                case ("Литература"):{
                    type = "any&subject%5B2%5D=on";
                    break;
                }
                case ("ОБЖ"):{
                    type = "any&subject%5B16%5D=on";
                    break;
                }case ("Обществознание"):{
                    type = "any&subject%5B9%5D=on";
                    break;
                }case ("Предпринимательство"):{
                    type = "any&subject%5B23%5D=on";
                    break;
                }case ("Право"):{
                    type = "any&subject%5B15%5D=on";
                    break;
                }case ("Психология"):{
                    type = "any&subject%5B28%5D=on";
                    break;
                }case ("Робототехника"):{
                    type = "any&subject%5B27%5D=on";
                    break;
                }case ("Русский язык"):{
                    type = "any&subject%5B1%5D=on";
                    break;
                }case ("Технология"):{
                    type = "any&subject%5B17%5D=on";
                    break;
                }case ("Физкультура"):{
                    type = "any&subject%5B19%5D=on";
                    break;
                }case ("Черчение"):{
                    type = "any&subject%5B31%5D=on";
                    break;
                }case ("Ин. языки"):{
                    type = "any&subject%5B3%5D=on";
                    break;
                }
                case ("Экология"):{
                    type = "any&subject%5B21%5D=on";
                    break;
                }
                case ("Экономика"):{
                    type = "any&subject%5B14%5D=on";
                    break;
                }
                default:
                    type = "any";
            }
            String str = "https://olimpiada.ru/activities?type="+ type+"&class="+classes+"&period=week";
            try {
                doc = Jsoup.connect(str).userAgent("Chrome/4.0.249.0 Safari/532.5").get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements listNews = doc.select("#megalist");
            for (Element element : listNews) {
                for (Element thing : element.select("div.o-block")) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("Name", String.valueOf(thing.select("div.o-info > a:nth-child(1) > span").text()));
                    item.put("Status", String.valueOf(thing.select("div.o-info > a:nth-child(2) > span").text()));
                    item.put("Link", "https://olimpiada.ru" + String.valueOf(thing.select("div.o-info > a.none_a.black.olimp_desc").attr("href")));
                    Single.getInstance().allCurrentOlympiads.add(item);
                }
            }
        }
    };


}
