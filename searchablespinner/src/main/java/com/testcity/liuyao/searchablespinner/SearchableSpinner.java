package com.testcity.liuyao.searchablespinner;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class SearchableSpinner extends RelativeLayout {
    TextView rob_focus;
    MyAutoCompleteTextView autoCompleteTextView;
    ImageView delete_icon;
    ImageView down_icon;
    public static final String filterMode_Prefix = "Prefix";
    public static final String filterMode_Contains = "Contains";
    String filterMode = filterMode_Contains;
    public SearchableSpinner(Context context) {
        super(context);
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

      private void init(){
          LayoutInflater.from(getContext()).inflate(R.layout.layout_searchable_spinner, this, true);
          rob_focus = findViewById(R.id.rob_focus);
          autoCompleteTextView = findViewById(R.id.spinnerEdit);
          autoCompleteTextView.setThreshold(1);
          delete_icon = findViewById(R.id.delete_icon);
          delete_icon.setOnClickListener(new OnClickListener() {
              @Override
              public void onClick(View v) {
                  autoCompleteTextView.setText("");
              }
          });
          down_icon = findViewById(R.id.down_icon);
          autoCompleteTextView.addTextChangedListener(new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start, int count, int after) {

              }

              @Override
              public void onTextChanged(CharSequence s, int start, int before, int count) {

              }

              @Override
              public void afterTextChanged(Editable s) {
                      if(TextUtils.isEmpty(s)){
                          delete_icon.setVisibility(GONE);
                          down_icon.setVisibility(VISIBLE);
                      }else {
                          delete_icon.setVisibility(VISIBLE);
                          down_icon.setVisibility(GONE);
                      }
              }
          });
      }


        public void init(List<String> allItems, String filterMode){
            this.filterMode = filterMode;
            AutoTextViewAdapter adapter = new AutoTextViewAdapter(getContext(), allItems, this.filterMode);
            autoCompleteTextView.setAdapter(adapter);
        }

        public AutoCompleteTextView getAutoCompleteTextView(){
           return autoCompleteTextView;
        }

    public String getFilterMode() {
        return filterMode;
    }

    public void setFilterMode(String filterMode) {
        this.filterMode = filterMode;
        if(autoCompleteTextView.getAdapter() != null && autoCompleteTextView.getAdapter() instanceof  AutoTextViewAdapter){
            AutoTextViewAdapter adapter = (AutoTextViewAdapter) autoCompleteTextView.getAdapter();
            adapter.setFilterMode(filterMode);
        }
    }
}
