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
        items.add("张四");
        items.add("张三");
        items.add("张五");
        items.add("张六");
        items.add("张七");
        items.add("李四");
        items.add("王二麻子");
        searchableSpinner.setListItem(items);
    }
}
