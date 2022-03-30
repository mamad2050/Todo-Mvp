package com.example.todomvp.detail;

import com.example.todomvp.BasePresenter;
import com.example.todomvp.BaseView;
import com.example.todomvp.model.Task;

public interface TaskDetailContract {

    interface View extends BaseView {

        void showTask(Task task);

        void setDeleteButtonVisibility(boolean visible);

        void showError(String error);

        void returnResult(int resultCode , Task task);


    }

    interface Presenter extends BasePresenter<View> {

        void deleteTask();

        void saveChanges(int importance , String title);

    }

}
