package com.todoapp.adapters;

/**
 * Created by neerajaabraham on 29/11/18.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.todoapp.AddNewActivity;
import com.todoapp.R;
import com.todoapp.ToDoActivity;
import com.todoapp.data.ToDoDBHelper;
import com.todoapp.data.ToDoItem;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {

    private static final String TAG = ToDoAdapter.class.getSimpleName();

    Context mContext;
    LayoutInflater mInflater;
    ToDoDBHelper mDB;
    ToDoItem entry = new ToDoItem();

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_TODO = "TODO";
    public static final String EXTRA_POSITION = "POSITION";
    public static final String EXTRA_CHECKED = "ISCHECKED";

    public ToDoAdapter(Context context, ToDoDBHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public ToDoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.single_todo_layout, parent, false);
        return new ToDoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ToDoViewHolder holder, int position) {

        final ToDoItem current = mDB.query(position);


        holder.tododesc.setText(current.getmToDo());


        if (current.getChecked() == 1) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }

        final ToDoViewHolder h = holder;

        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id = current.getmId();
                int deleted = mDB.delete(id);
                if (deleted >= 0) {
                    notifyItemRemoved(h.getAdapterPosition());
                }

            }
        });

        holder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, AddNewActivity.class);

                intent.putExtra(EXTRA_ID, current.getmId());
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_TODO, current.getmToDo());
                intent.putExtra(EXTRA_CHECKED, current.getChecked());

                // Start an empty edit activity.
                ((Activity) mContext).startActivityForResult(intent, ToDoActivity.TODO_EDIT);

            }
        });

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkBox.isChecked()) {

                    entry.setChecked(1); //setting the checkbox is checked
                    mDB.update(current.getmId(), 1);

                } else {

                    entry.setChecked(0);  //not checked
                    mDB.update(current.getmId(), 0);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }

    class ToDoViewHolder extends RecyclerView.ViewHolder {

        public TextView tododesc;
        ImageButton delete_btn;
        ImageButton edit_btn;
        CheckBox checkBox;

        public ToDoViewHolder(View itemView) {
            super(itemView);
            tododesc = itemView.findViewById(R.id.tododescription);
            delete_btn = itemView.findViewById(R.id.delete);
            edit_btn = itemView.findViewById(R.id.edit);
            checkBox = itemView.findViewById(R.id.checkbox_completed);
        }
    }
}
