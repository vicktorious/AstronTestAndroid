package com.example.vick.astrontest;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
        this.people = people;
    }

    public void orderList() {
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person person1, Person person2) {
                return person1.getLastName().compareToIgnoreCase(person2.getLastName());
            }
        });
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
        ImageView ivFemIcon;
        TextView tvName;
        ImageView ivMalIcon;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            v = inflater.inflate(R.layout.row, null);
            ViewHolder holder = new ViewHolder();
            holder.ivFemIcon = (ImageView) v.findViewById(R.id.ivFemale);
            holder.tvName = (TextView) v.findViewById(R.id.tvName);
            holder.ivMalIcon = (ImageView) v.findViewById(R.id.ivMale);
            v.setTag(holder);
        }

        Person person = people.get(i);
        if (person != null) {
            ViewHolder holder = (ViewHolder) v.getTag();
            holder.ivFemIcon.setImageResource(person.getFemaleIconID());
            holder.tvName.setText(person.getFullName());
            holder.ivMalIcon.setImageResource(person.getMaleIconID());
        }
        return v;
    }
}
