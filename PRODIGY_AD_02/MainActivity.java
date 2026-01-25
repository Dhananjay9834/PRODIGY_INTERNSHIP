package com.example.to_dolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter adapter;
    private ArrayList<Task> taskList;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        fabAdd = findViewById(R.id.fabAdd);

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(taskList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Add divider
        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        // Add new task
        fabAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        EditText etTask = new EditText(this);

        new AlertDialog.Builder(this)
                .setTitle("Add Task")
                .setView(etTask)
                .setPositiveButton("Add", (dialog, which) -> {
                    String taskName = etTask.getText().toString().trim();
                    if (!taskName.isEmpty()) {
                        taskList.add(new Task(taskName));
                        adapter.notifyItemInserted(taskList.size() - 1);
                    } else {
                        Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
