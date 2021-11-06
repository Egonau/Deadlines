package com.example.deadlines;

import androidx.annotation.NonNull;

import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Single {
    private static final Single INSTANCE = new Single();
    public static String chosenDay = null; //выбранный день в календаре
    public static String chosenMonth = null; //выбранный месяц в календаре
    public static String chosenYear = null; //выбранный год в календаре
    public static List<EventDay> calEvents = new ArrayList<>(); //события в календаре в выбранном месяце
    public static GenericTypeIndicator<ArrayList<HashMap<String, String>>> t = new
            GenericTypeIndicator<ArrayList<HashMap<String, String>>>() {
            }; //обработчик данных из БД
    public static HashMap<String,Object> sortingParams = new HashMap<>(); //параметры сортировки
    public static HashMap<String, String> credentialsOfUser = new HashMap<>(); //данные пользователя
    public static HashMap<String, HashMap<String, String>> schedule = new HashMap<>(); //расписание уроков
    public static HashMap<String,String> tags = new HashMap<>(); //теги
    public static HashMap<String,ArrayList<HashMap<String,Object>>> dayDeadlines = new HashMap<>(); //дедлайны на выбранный день
    public static String chosenLesson = null; //выбранный урок
    public static List<HashMap<String,Object>> deletingDeadlines = new ArrayList<>(); //дедлайн для удаления
    public static HashMap<String,Object> editedDeadline = new HashMap<>(); //дедлайн для редактирования
    public static HashMap<String,Object> sharingDeadline = new HashMap<>(); //дедлайн для раздачи
    public static String[] dateInfo; //строка данных о дне
    public static ArrayList<HashMap<String,Object>> suggestedDeadlines = new ArrayList<>(); //список предложенных дедлайнов
    public static List<HashMap<String,Object>> deletingAcceptedDeadlines = new ArrayList<>(); //дедалайны отправленные на удаление
    public static HashMap<String,HashMap<String,Object>> allUsersCredentials = new HashMap<>(); //данные всех пользователей в краткой форме
    public static ArrayList<HashMap<String,Object>> allCurrentOlympiads = new ArrayList<>(); //список всех текущих олимпиад
    private Single() {
    }

    public static Single getInstance() {
        return INSTANCE;
    }
}
