package com.pyo.quizfrontapp.gamescreen

import android.util.Log
import com.pyo.quizfrontapp.data.quizInfoDto

class GameScreenModel:GameScreenContract.Model {


    private var quizList= listOf<quizInfoDto>()
    private var quizIndex=-1
    private var selectedNumberOfQuiz = 0
    private var totalNumberOfQuiz=0
    private var answerScore=0

    override fun saveQuiz(quizInfoDtos:List<quizInfoDto>) {
        Log.e("저장되나요",quizInfoDtos.toString())
        quizList=quizInfoDtos
    }


    //중복없이 랜덤 문제번호 만듬


    override fun increaseQuizIndex() = ++quizIndex

    override fun decreaseQuizIndex()=--quizIndex
    override fun setAnswer(answer: Int) {
        if (quizList[quizIndex].answer==answer)
            answerScore++
        quizList[quizIndex].selectedAnswer=answer.toLong()
    }

    override fun getQuizIndex()=quizIndex;

    override fun getQuiz(index: Int): quizInfoDto {
        return quizList[index]
    }

    override fun getQuizAll(): List<quizInfoDto> {
        return quizList
    }


    override fun getSelectedNumberOfQuiz()=selectedNumberOfQuiz

    override fun setSelectedNumberOfQuiz(number: Int) {
        selectedNumberOfQuiz=number
    }

    override fun increaseAnswerScore() {
        answerScore++
    }

    override fun getAnswerScore()=answerScore
}