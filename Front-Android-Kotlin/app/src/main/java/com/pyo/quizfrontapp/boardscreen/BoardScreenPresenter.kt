package com.pyo.quizfrontapp.boardscreen


class BoardScreenPresenter (val view: BoardScreenContract.View) : BoardScreenContract.Presenter {

    private var model: BoardScreenContract.Model= BoardScreenModel();
    init {
        view.initView();
    }


}