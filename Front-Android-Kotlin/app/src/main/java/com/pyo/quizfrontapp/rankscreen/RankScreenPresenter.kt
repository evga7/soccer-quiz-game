package com.pyo.quizfrontapp.rankscreen

import android.content.Context
import android.util.Log
import com.pyo.quizfrontapp.data.UserRankInfo
import com.pyo.quizfrontapp.network.UserClient
import com.pyo.quizfrontapp.preference.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankScreenPresenter (val view: RankScreenContract.View) : RankScreenContract.Presenter {

    private var model: RankScreenContract.Model= RankScreenModel();
    init {
        view.initView();
    }

    override fun getRankAndSave() {
        UserClient.retrofitService.getUserRank().enqueue(object: Callback<List<UserRankInfo>> {
            override fun onResponse(call: Call<List<UserRankInfo>>,response: Response<List<UserRankInfo>>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        model.saveRankTable(body)
                        view.showRank()
                    }
                }
            }
            override fun onFailure(call: Call<List<UserRankInfo>>, t: Throwable) {
                view.showPopup("랭킹을 가져오는데 실패하였습니다.")
                return
            }
        })
    }

    override fun getRankTable(): MutableList<UserRankInfo> {
        return model.getRankList()
    }

    override fun getPlayerRank(context: Context) {
        val userId = MySharedPreferences.getUserId(context)
        UserClient.retrofitService.getPlayerRank(userId).enqueue(object: Callback<UserRankInfo>{
            override fun onResponse(call: Call<UserRankInfo>, response: Response<UserRankInfo>) {
                if (response.isSuccessful)
                {
                    val body = response.body()
                    if (body != null) {
                        Log.e("플레이어랭킹",body.toString())
                        view.showPlayerRank(body)
                    }
                }
            }

            override fun onFailure(call: Call<UserRankInfo>, t: Throwable) {
                view.showPopup("랭킹을 가져오는데 실패하였습니다.")
                return
            }

        })
    }



}