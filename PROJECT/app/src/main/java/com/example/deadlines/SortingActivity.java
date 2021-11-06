package com.example.deadlines;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.HashMap;

public class SortingActivity extends AppCompatActivity {

    private Button sortingButton;
    private Button resetSortingButton;
    private Spinner themeSortingSpinner;
    private Spinner prioritySortingSpinner;
    private Spinner sharingSortingSpinner;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_SELECTED_THEME = "selected_theme";
    public static final String APP_PREFERENCES_SELECTED_PRIORITY = "selected_priority";
    public static final String APP_PREFERENCES_SELECTED_SHARING = "selected_sharing";
    String[] theme = {"Выбрать","Уроки","Внеурочные мероприятия", "Внешкольные события"};
    String[] priority = {"Выбрать","Важный","Неважный"};
    String[] sharing  ={"Выбрать","Общелицейский дедлайн","Дедлайн группы","Личный дедлайн"};
    private SharedPreferences mSettings;
    private HashMap<String,Object> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        sortingButton = findViewById(R.id.sortingButton);
        resetSortingButton = findViewById(R.id.resetSortingButton);
        themeSortingSpinner = findViewById(R.id.themeSortingSpinner);
        prioritySortingSpinner = findViewById(R.id.prioritySortingSpinner);
        sharingSortingSpinner = findViewById(R.id.sharingSortingSpinner);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        //наполнение списков данными
        //1
        ArrayAdapter<String> themeSortingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, theme);
        themeSortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSortingSpinner.setAdapter(themeSortingAdapter);
        themeSortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put(APP_PREFERENCES_SELECTED_THEME, adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //2
        ArrayAdapter<String> prioritySortingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priority);
        prioritySortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prioritySortingSpinner.setAdapter(prioritySortingAdapter);
        prioritySortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put(APP_PREFERENCES_SELECTED_PRIORITY, adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //3
        ArrayAdapter<String> sharingSortingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sharing);
        sharingSortingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sharingSortingSpinner.setAdapter(sharingSortingAdapter);
        sharingSortingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                map.put(APP_PREFERENCES_SELECTED_SHARING, adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //установка текущих значений
        if (mSettings.contains(APP_PREFERENCES_SELECTED_THEME)&&mSettings.contains(APP_PREFERENCES_SELECTED_PRIORITY)&&mSettings.contains(APP_PREFERENCES_SELECTED_SHARING)) {
            try {
                themeSortingSpinner.setSelection(themeSortingAdapter.getPosition(mSettings.getString(APP_PREFERENCES_SELECTED_THEME,"")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                prioritySortingSpinner.setSelection(prioritySortingAdapter.getPosition(mSettings.getString(APP_PREFERENCES_SELECTED_PRIORITY,"")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                sharingSortingSpinner.setSelection(sharingSortingAdapter.getPosition(mSettings.getString(APP_PREFERENCES_SELECTED_SHARING,"")));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //добавление параметров сортировки в App_preferences
    public void Sorting(View view) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_SELECTED_THEME,String.valueOf(map.get(APP_PREFERENCES_SELECTED_THEME)));
        editor.putString(APP_PREFERENCES_SELECTED_PRIORITY,String.valueOf(map.get(APP_PREFERENCES_SELECTED_PRIORITY)));
        editor.putString(APP_PREFERENCES_SELECTED_SHARING,String.valueOf(map.get(APP_PREFERENCES_SELECTED_SHARING)));
        map.clear();
        editor.apply();
        Intent intent=new Intent(SortingActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    //очищение параметров сортировки
    public void Resetting(View view) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_SELECTED_THEME,"");
        editor.putString(APP_PREFERENCES_SELECTED_PRIORITY,"");
        editor.putString(APP_PREFERENCES_SELECTED_SHARING,"");
        editor.apply();
        Intent intent=new Intent(SortingActivity.this,MenuActivity.class);
        startActivity(intent);
        finish();
    }
}