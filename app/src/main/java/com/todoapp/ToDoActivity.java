package com.todoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.todoapp.adapters.ToDoAdapter;
import com.todoapp.data.ToDoDBHelper;

import static com.todoapp.MainActivity.SHARED_PREFS;

public class ToDoActivity extends AppCompatActivity {

    private static final String TAG = ToDoActivity.class.getSimpleName();

    public static final int TODO_EDIT = 1;
    public static final int TODO_ADD = -1;

    TextView username_tv;

    private ToDoDBHelper dbHelper;
    private ToDoAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);


        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String username = prefs.getString("USER_NAME", "user");

        username_tv = findViewById(R.id.username_text);
        username_tv.setText(username);

        dbHelper = new ToDoDBHelper(this);
        mAdapter = new ToDoAdapter(this, dbHelper);
        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add:

                addTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void addTask() {

        Intent intent = new Intent(getBaseContext(), AddNewActivity.class);
        startActivityForResult(intent, TODO_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TODO_EDIT) {
            if (resultCode == RESULT_OK) {
                String word = data.getStringExtra(AddNewActivity.EXTRA_REPLY);

                // Update the database.
                if (!TextUtils.isEmpty(word)) {
                    int id = data.getIntExtra(ToDoAdapter.EXTRA_ID, -99);

                    if (id == TODO_ADD) {
                        dbHelper.insert(word);
                    } else if (id >= 0) {
                        dbHelper.update(id, word);
                    }
                    // Update the UI.
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            R.string.empty_not_saved,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
