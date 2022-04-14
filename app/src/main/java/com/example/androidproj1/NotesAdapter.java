package com.example.androidproj1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

//отвечает за интерфейс ListView заявок
public class NotesAdapter extends BaseAdapter {
    Activity activity;
    Context context;
    LayoutInflater flater;
    ArrayList<Note> objects;
    ImageView btnInfo;
    ImageView btnDelete;

    public NotesAdapter(Activity act, Context context, ArrayList<Note> notes) {
        activity = act;
        this.context = context;
        objects = notes;
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
            view = flater.inflate(R.layout.item_note, parent, false);
        }

        Note note = getNote(position);
        ((TextView) view.findViewById(R.id.tvFullName)).setText(note.getName());

        btnInfo = (ImageView) view.findViewById(R.id.btnInfoNote);
        btnInfo.setOnClickListener(btnInfoListener);
        btnInfo.setTag(position);

        btnDelete = (ImageView) view.findViewById(R.id.btnDeleteNote);
        btnDelete.setOnClickListener(btnDeleteListener);
        btnDelete.setTag(position);

        return view;
    }

    Note getNote(int position) {
        return ((Note) getItem(position));
    }

    View.OnClickListener btnInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                Note note = objects.get(Integer.parseInt(v.getTag().toString()));
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Дополнительная информация: ");
                String info = String.format("ФИО: %s \nДата посещения: %s \nМобильный: %s \nТип услуги: %s",
                        note.getName(), note.getDateVisit(), note.getPhone(), note.getServiceType());
                builder.setMessage(info);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем окно
                        dialog.cancel();
                    }
                });
                builder.create();
                builder.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener btnDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                Note note = objects.get(Integer.parseInt(v.getTag().toString()));
                String page = "deleteNoteMob?id=" + note.getId();

                CallRequest callRequest = (CallRequest) new CallRequest("get", page){
                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        Toast.makeText(context, "Note deleted", Toast.LENGTH_LONG).show();
                    }
                }.execute();

                objects.remove(note);
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