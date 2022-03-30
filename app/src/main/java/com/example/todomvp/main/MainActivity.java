package com.example.todomvp.main;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.todomvp.R;
import com.example.todomvp.detail.TaskDetailActivity;
import com.example.todomvp.model.AppDatabase;
import com.example.todomvp.model.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContract.View, TaskAdapter.TaskItemEventListener {

    public static final String EXTRA_KEY_TASK = "task";
    private static final int REQ_CODE = 980;
    public static final int RESULT_CODE_ADD_TASK = 1001;
    public static final int RESULT_CODE_UPDATE_TASK = 2002;
    public static final int RESULT_CODE_DELETE_TASK = 3003;
    private MainContract.Presenter presenter;
    private TaskAdapter taskAdapter;
    private View emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(AppDatabase.getAppDatabase(this).getTaskDao());
        taskAdapter = new TaskAdapter(this, this);

        emptyState = findViewById(R.id.emptyState);

        Button addNewTaskBtn = findViewById(R.id.addNewTaskBtn);
        addNewTaskBtn.setOnClickListener(e -> {
            Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
            startActivityForResult(intent, REQ_CODE);
        });

        RecyclerView recyclerView = findViewById(R.id.taskListRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        Button deleteAllBtn = findViewById(R.id.deleteAllBtn);
        deleteAllBtn.setOnClickListener(e -> presenter.onDeleteAllButtonClick());

        EditText searchEt = findViewById(R.id.searchEt);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                presenter.onSearch(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        presenter.onAttach(this);

    }

    @Override
    public void showTasks(List<Task> tasks) {

        taskAdapter.setTasks(tasks);

    }

    @Override
    public void clearTasks() {

        taskAdapter.clearItems();

    }

    @Override
    public void updateTask(Task task) {
        taskAdapter.updateItem(task);
    }


    @Override
    public void setEmptyStateVisibility(boolean visible) {

        emptyState.setVisibility(visible ? View.VISIBLE : View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_CODE_ADD_TASK || resultCode == RESULT_CODE_UPDATE_TASK ||
                    resultCode == RESULT_CODE_DELETE_TASK && data != null) {

                Task task = data.getParcelableExtra(EXTRA_KEY_TASK);

                if (task != null) {

                    if (resultCode == RESULT_CODE_ADD_TASK) {
                        taskAdapter.addItem(task);

                    } else if (resultCode == RESULT_CODE_UPDATE_TASK) {

                        taskAdapter.updateItem(task);
                    } else
                        taskAdapter.deleteItem(task);

                    setEmptyStateVisibility(taskAdapter.getItemCount() == 0);

                }
            }
        }
    }

    @Override
    public void onClick(Task task) {

        presenter.onTaskItemClick(task);

    }

    @Override
    public void onLongClick(Task task) {

        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(EXTRA_KEY_TASK, task);
        startActivityForResult(intent, REQ_CODE);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDetach();
    }
}