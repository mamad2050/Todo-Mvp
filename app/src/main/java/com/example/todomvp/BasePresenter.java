package com.example.todomvp;

public interface BasePresenter<T extends BaseView> {

    void onAttach(T view);

    void onDetach();

}
