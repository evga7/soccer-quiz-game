package com.pyo.quizfrontapp.morescreen

import android.content.Context
import com.pyo.quizfrontapp.data.ServerInfo
import com.pyo.quizfrontapp.network.UserClient
import com.pyo.quizfrontapp.preference.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoreScreenPresenter (val view: MoreScreenContract.View) : MoreScreenContract.Presenter {

    private var model: MoreScreenContract.Model= MoreScreenModel();
    init {
        view.initView();
    }

    override fun getServerInfo() {
        UserClient.retrofitService.getServerInfo().enqueue(object: Callback<ServerInfo>{
            override fun onResponse(call: Call<ServerInfo>, response: Response<ServerInfo>) {
                val body = response.body()
                if (body != null) {
                    view.showServerInfo(body)
                }
            }

            override fun onFailure(call: Call<ServerInfo>, t: Throwable) {
                return
            }

        })
    }
    override fun postMessage(context:Context,message:String)
    {
        UserClient.retrofitService.postUserMessage(MySharedPreferences.getUserId(context),message).enqueue(object :Callback<Void>{
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                view.showPopup2("감사합니다! \n메시지 전송 성공")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                view.showPopup2("메시지 전송 실패")
            }

        })
    }
}