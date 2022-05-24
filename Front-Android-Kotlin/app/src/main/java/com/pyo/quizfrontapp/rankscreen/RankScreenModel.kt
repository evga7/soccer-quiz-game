package com.pyo.quizfrontapp.rankscreen

import android.util.Log
import com.pyo.quizfrontapp.data.UserRankInfo

class RankScreenModel: RankScreenContract.Model {
    private var rankList= mutableListOf<UserRankInfo>()
    private lateinit var playRankInfo:UserRankInfo


    override fun saveRankTable(rankTable: List<UserRankInfo>) {
        Log.e("최종들오옴",rankTable.toString())
        for (frontUserInfo in rankTable) {
            rankList.add(frontUserInfo)
        }
        Log.e("최종드감",rankList.toString())
    }

    override fun getRankList(): MutableList<UserRankInfo> {
        Log.e("리턴맨",rankList.toString())
        return rankList
    }



}