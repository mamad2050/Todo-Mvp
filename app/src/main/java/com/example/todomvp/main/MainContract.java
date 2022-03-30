package com.example.todomvp.main;

import com.example.todomvp.BasePresenter;
import com.example.todomvp.BaseView;
import com.example.todomvp.model.Task;

import java.util.List;

public interface MainContract {

    interface View extends BaseView {

        void showTasks(List<Task> tasks);

        void clearTasks();

        void updateTask(Task task);

        void setEmptyStateVisibility(boolean visible);

    }

    interface Presenter extends BasePresenter<View> {

        void onDeleteAllButtonClick();

        void onSearch(String q);

        void onTaskItemClick(Task task);

    }

}
