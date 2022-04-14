package com.example.androidproj1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//отвечает за содержимое ListView страницы пользователя
public class UserPageAdapter extends BaseAdapter {
    Context context;
    LayoutInflater flater;
    ArrayList<String> objects;
    List<Integer> icons = new ArrayList<Integer>();


    public UserPageAdapter(Context context, ArrayList<String> strings) {
        this.context = context;
        objects = strings;
        flater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        icons.add(R.drawable.ic_action_do_note);
        icons.add(R.drawable.ic_action_notes);
        icons.add(R.drawable.ic_action_logout);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = flater.inflate(R.layout.item_user_page, parent, false);
        }

        String text = getText(position);
        ((TextView) view.findViewById(R.id.tvTextUserPage)).setText(text);
        ((TextView) view.findViewById(R.id.tvTextUserPage)).setCompoundDrawablesWithIntrinsicBounds(
                icons.get(position), 0, R.drawable.ic_arrow_right, 0);

        return view;
    }

    String getText(int position) {
        return ((String) getItem(position));
    }
}
