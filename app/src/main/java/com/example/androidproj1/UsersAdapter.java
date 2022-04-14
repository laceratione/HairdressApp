package com.example.androidproj1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

//отвечает за содержимое ListView окна списка пользователей приложения
public class UsersAdapter extends BaseAdapter {
    Activity activity;
    Context context;
    LayoutInflater flater;
    ArrayList<User> objects;

    ImageView btnInfo;
    ImageView btnDelete;

    UsersAdapter(Activity act, Context context, ArrayList<User> users) {
        activity = act;
        this.context = context;
        objects = users;
        flater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = flater.inflate(R.layout.item_user, parent, false);
        }

        User user = getUser(position);

        ((TextView) view.findViewById(R.id.tvUsername)).setText(user.getUsername());

        btnInfo = (ImageView) view.findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(btnInfoListener);
        btnInfo.setTag(position);

        btnDelete = (ImageView) view.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(btnDeleteListener);
        btnDelete.setTag(position);

        return view;
    }

    User getUser(int position) {
        return ((User) getItem(position));
    }

    View.OnClickListener btnInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            User user = objects.get(Integer.parseInt(v.getTag().toString()));
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Дополнительная информация: ");
            String info = String.format("Id: %s \nUsername: %s", user.getId(), user.getUsername());
            builder.setMessage(info);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Закрываем окно
                    dialog.cancel();
                }
            });
            builder.create();
            builder.show();
        }
    };

    View.OnClickListener btnDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                User user = objects.get(Integer.parseInt(v.getTag().toString()));
                String page = "deleteUserMob?id=" + user.getId();

                CallRequest callRequest = (CallRequest) new CallRequest("get", page){
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Toast.makeText(context, "User deleted", Toast.LENGTH_LONG).show();
                    }
                }.execute();

                objects.remove(user);
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(objects);
                refreshActivity(json);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void refreshActivity(String json){
        Intent intent = activity.getIntent();
        intent.putExtra("response", json);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        context.startActivity(intent);
    }

}
