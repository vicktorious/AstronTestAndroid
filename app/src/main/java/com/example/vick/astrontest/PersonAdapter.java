package com.example.vick.astrontest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Vick on 1/23/17.
 */

public class PersonAdapter extends BaseAdapter {

    private Context context;
    private List<Person> people;

    public PersonAdapter(Context context, List<Person> people) {
        this.context = context;
        this.people = people;
    }

    public void setList(List<Person> people) {
        this.people.clear();
        this.people = people;
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public Object getItem(int i) {
        return people.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class ViewHolder {
        ImageView ivIcon;
        TextView tvName;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row, null);
            ViewHolder holder = new ViewHolder();
            holder.ivIcon = (ImageView) v.findViewById(R.id.ivAgeGroup);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            v.setTag(holder);
        }

        Person person = people.get(i);
        if (person != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.ivIcon.setImageResource(person.getAgeGroup().getIconID());
            holder.tvName.setText(person.getFullName());
        }
        return v;
    }
}
