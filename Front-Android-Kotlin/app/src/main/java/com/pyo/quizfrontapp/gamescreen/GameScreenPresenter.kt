package com.pyo.quizfrontapp.gamescreen

import com.pyo.quizfrontapp.data.quizInfoDto
import com.pyo.quizfrontapp.network.UserClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameScreenPresenter (val view: GameScreenContract.View) : GameScreenContract.Presenter {

    private var model: GameScreenContract.Model= GameScreenModel();

    init {
        view.initView();
    }

    override fun
            nextQuizIndex()=model.increaseQuizIndex()


    override fun prevQuizIndex()=model.decreaseQuizIndex()


    override fun getQuizFromIndex(index: Int)=model.getQuiz(index)


    override fun getQuizIndex()=model.getQuizIndex()

    override fun getQuizFromServer(selectedNumberOfQuiz: Long) {
        UserClient.retrofitService.getRandomQuiz(selectedNumberOfQuiz).enqueue(object:
            Callback<List<quizInfoDto>> {
            override fun onResponse(
                call: Call<List<quizInfoDto>>,
                response: Response<List<quizInfoDto>>
            ) {
                if (response.isSuccessful)
                {
                    val body = response.body()
                    if (body != null) {
                        model.saveQuiz(body)
                    }
                    view.getNextQuiz(-1)
                    view.showQuizUI(true)
                    view.showProgress(false)
                }
            }

            override fun onFailure(call: Call<List<quizInfoDto>>, t: Throwable) {
                return
            }

        })
    }


    override fun getQuizAll() = model.getQuizAll()

    override fun selectAnswer(answer:Int) {
        model.setAnswer(answer)
    }


    override fun getSelectedNumberOfQuiz()=model.getSelectedNumberOfQuiz()

    override fun setSelectedNumberOfQuiz(number: Int) {
        model.setSelectedNumberOfQuiz(number)
    }

    override fun upScore() {
        model.increaseAnswerScore()
    }

    override fun getAnswerScore()=model.getAnswerScore()
}