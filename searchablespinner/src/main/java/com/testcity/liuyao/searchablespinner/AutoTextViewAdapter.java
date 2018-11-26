package com.testcity.liuyao.searchablespinner;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AutoTextViewAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<String> allItem;
    private List<String> filterItems;

    public AutoTextViewAdapter(Context context, List<String> allItem){
        this.context = context;
        this.allItem = allItem;
        filterItems = new ArrayList<String>();
    }
    @Override
    public int getCount() {
        return filterItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_drop, parent, false);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv.setText(filterItems.get(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return new MyFilter();
    }

    public static class  ViewHolder{
        public TextView tv;
    }
    public static class TextViewStyle{
        private int textSize;
        private String textColor;

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }
    }




    private class MyFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            List<String> filterItems = new ArrayList<String>();
            if(TextUtils.isEmpty(constraint)){
                filterItems.addAll(allItem);
            }else{
                for(String str : allItem){
                    if(str.contains(constraint)){
                        filterItems.add(str);
                    }
                }
            }
            filterResults.values = filterItems;
            filterResults.count = filterItems.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                filterItems.clear();
                filterItems.addAll((List<String>) results.values);
                notifyDataSetChanged();
        }


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            if(resultValue instanceof Integer){
               int index = (Integer) resultValue;
               return filterItems.get(index);
            }else{
                return super.convertResultToString(resultValue);
            }

        }

    }
}
