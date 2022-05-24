package com.pyo.quizfrontapp.rankscreen

import android.content.Context
import com.pyo.quizfrontapp.data.UserRankInfo

interface RankScreenContract {
    interface View {
        fun initView()
        fun showRank()
        fun showPlayerRank(userRankInfo: UserRankInfo)
        fun showAdFit()

        fun showPopup(msg: String)
    }
    interface Presenter {
        fun getRankAndSave()
        // onCreate 화면 초기화시에
        // 저장된 데이터가 있는지 Model 에서 확인하고
        // 확인한 결과에 따라 View 에 어떤 내용을 보일지 지시한다
        fun getRankTable():MutableList<UserRankInfo>
        fun getPlayerRank(context: Context)


    }
    interface Model
    {
        fun saveRankTable(rankTable: List<UserRankInfo>)
        fun getRankList():MutableList<UserRankInfo>

    }
}