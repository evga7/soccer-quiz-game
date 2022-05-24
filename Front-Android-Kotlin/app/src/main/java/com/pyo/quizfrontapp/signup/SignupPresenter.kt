package com.pyo.quizfrontapp.signup

import android.content.Context
import com.pyo.quizfrontapp.network.UserClient
import com.pyo.quizfrontapp.preference.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupPresenter(val view: SignupContract.View) : SignupContract.Presenter {

    private var model:SignupContract.Model=SignupModel();
    init {
        view.initView();
    }

    override fun updateCheck(context: Context) {
        UserClient.retrofitService.checkUpdate().enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (!response.body().equals("0.0.5-SNAPSHOT"))
                {
                    view.showPopup("앱을 업데이트 해주세요. \n 혹은 서버가 점검 중 입니다.")
                }
                else if (!MySharedPreferences.getUserId(context).isNullOrEmpty())
                {
                    view.onLogin()
                }
                else
                    view.showMain()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                view.showPopup("네트워크를 연결할수 없습니다.")
                return
            }

        })
    }


}