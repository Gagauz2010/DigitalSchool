package ru.spsu.fmf.digitalschool.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.spsu.fmf.digitalschool.R;
import ru.spsu.fmf.digitalschool.structures.jsonNews;

public class newsAdapter extends BaseAdapter {
    private ArrayList listData;
    private LayoutInflater layoutInflater;

    public newsAdapter(Context context, ArrayList<jsonNews> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        jsonNews item = (jsonNews) listData.get(position);
        //if (convertView == null) {
            if(item.getImportant() == 0)
                convertView = layoutInflater.inflate(R.layout.news_listitem, null);
            else
                convertView = layoutInflater.inflate(R.layout.news_listitem_attention, null);

            holder = new ViewHolder();
            holder.headlineView = (TextView) convertView.findViewById(R.id.newsHead);
            holder.newslineView = (TextView) convertView.findViewById(R.id.newsBody);
            holder.reportedDateView = (TextView) convertView.findViewById(R.id.postTime);

            convertView.setTag(holder);
        //} else {
        //    holder = (ViewHolder) convertView.getTag();
        //}

        jsonNews newsItem = (jsonNews) listData.get(position);
        holder.headlineView.setText(newsItem.getNewsHead());
        holder.newslineView.setText(newsItem.getNewsText());
        holder.reportedDateView.setText(newsItem.getPublTime());


        return convertView;
    }

    static class ViewHolder {
        TextView headlineView;
        TextView newslineView;
        TextView reportedDateView;
    }
}