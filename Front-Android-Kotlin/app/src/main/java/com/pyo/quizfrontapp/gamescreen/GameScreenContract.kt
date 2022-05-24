package com.pyo.quizfrontapp.gamescreen

import com.pyo.quizfrontapp.data.quizInfoDto

interface GameScreenContract {
    interface View {
        fun initView()
        fun viewQuiz(quizInfoDto: quizInfoDto)
        fun getQuizImage(quizInfoDto: quizInfoDto)
        fun setAnswerButtonImpl()
        fun getNextQuiz(answer: Int)
        fun showProgress(isShow:Boolean)
        fun showQuizUI(isShow:Boolean)
        fun endGameGoScoreScreen()
        fun goMainScreen()
        fun showAdFit()
        fun showPopup(msg: String)
        fun showQuizIndex()
        fun showTimer(index:Int)
    }
    interface Presenter {
        // onCreate 화면 초기화시에
        // 저장된 데이터가 있는지 Model 에서 확인하고
        // 확인한 결과에 따라 View 에 어떤 내용을 보일지 지시한다
        fun nextQuizIndex():Int
        fun prevQuizIndex():Int
        fun getQuizFromIndex(index:Int):quizInfoDto
        fun getQuizIndex():Int
        fun getQuizFromServer(selectedNumberOfQuiz: Long)
        fun getQuizAll(): List<quizInfoDto>
        fun selectAnswer(answer: Int)
        fun getSelectedNumberOfQuiz():Int
        fun setSelectedNumberOfQuiz(number: Int)
        fun upScore()
        fun getAnswerScore():Int

    }
    interface Model
    {
        fun saveQuiz(quizInfoDtos:List<quizInfoDto>)
        fun increaseQuizIndex():Int
        fun decreaseQuizIndex():Int
        fun setAnswer(answer:Int)
        fun getQuizIndex():Int
        fun getQuiz(index:Int):quizInfoDto
        fun getQuizAll(): List<quizInfoDto>
        fun getSelectedNumberOfQuiz():Int
        fun setSelectedNumberOfQuiz(number: Int)
        fun increaseAnswerScore()
        fun getAnswerScore():Int
    }
}