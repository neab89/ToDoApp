package com.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.todoapp.adapters.ToDoAdapter;

public class AddNewActivity extends AppCompatActivity {


    private static final int NO_ID = -99;
    private static final String NO_TODO = "";

    private EditText mEditWordView;

    // Unique tag for the intent reply.
    public static final String EXTRA_REPLY = "com.todoapp.REPLY";

    int mId = ToDoActivity.TODO_ADD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        mEditWordView = (EditText) findViewById(R.id.edit_todo);

        // Get data sent from calling activity.
        Bundle extras = getIntent().getExtras();

        // If we are passed content, fill it in for the user to edit.
        if (extras != null) {
            int id = extras.getInt(ToDoAdapter.EXTRA_ID, NO_ID);
            String todo = extras.getString(ToDoAdapter.EXTRA_TODO, NO_TODO);
            if ((id != NO_ID) && (todo != NO_TODO)) {
                mId = id;
                mEditWordView.setText(todo);
            }
        } // Otherwise, start with empty fields
    }


    /**
     * Click handler for the Save button.
     * Creates a new intent for the reply, adds the reply message to it as an extra,
     * sets the intent result, and closes the activity.
     *
     * @param view The view that was clicked.
     */
    public void returnReply(View view) {
        String todo = ((EditText) findViewById(R.id.edit_todo)).getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, todo);
        replyIntent.putExtra(ToDoAdapter.EXTRA_ID, mId);
        setResult(RESULT_OK, replyIntent);
        finish();
    }


}
