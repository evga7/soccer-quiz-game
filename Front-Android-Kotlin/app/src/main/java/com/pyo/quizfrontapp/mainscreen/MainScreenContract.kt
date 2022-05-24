package com.pyo.quizfrontapp.mainscreen

interface MainScreenContract {
    interface View {
        fun initView()

        fun onGame(number:Int)
        fun showPopup( msg: String)
        fun selectGameProblemsPopup()
        fun goRank()
        fun showAdFit()
        fun goMore()
        fun goBoard()
    }
    interface Presenter {
        // onCreate 화면 초기화시에
        // 저장된 데이터가 있는지 Model 에서 확인하고
        // 확인한 결과에 따라 View 에 어떤 내용을 보일지 지시한다

    }
    interface Model
    {
    }
}