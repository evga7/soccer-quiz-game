package com.pyo.quizfrontapp.mainscreen

class MainScreenPresenter (val view: MainScreenContract.View) : MainScreenContract.Presenter {

    private var model: MainScreenContract.Model= MainScreenModel();
    init {
        view.initView();
    }


}