package com.liuyao.searchablespinnerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.testcity.liuyao.searchablespinner.SearchableSpinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SearchableSpinner searchableSpinner = findViewById(R.id.searchableSpinner);
        List<String> items = new ArrayList<String>();
        items.add("张飞");
        items.add("关羽");
        items.add("赵云");
        items.add("张翼德");
        items.add("关云长");
        items.add("赵子龙");
        items.add("刘备");
        items.add("刘玄德");
        searchableSpinner.init(items, SearchableSpinner.filterMode_Prefix);
    }
}
