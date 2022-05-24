package com.pyo.quizfrontapp.morescreen

import android.content.Context
import com.pyo.quizfrontapp.data.ServerInfo

interface MoreScreenContract {
    interface View {
        fun initView()
        fun showServerInfo(serverInfo: ServerInfo)

        fun showPopup(msg: String)
        fun showAdFit()
        fun showPopupMessage()
        fun showPopup2(msg: String)
    }
    interface Presenter {
        // onCreate 화면 초기화시에
        // 저장된 데이터가 있는지 Model 에서 확인하고
        // 확인한 결과에 따라 View 에 어떤 내용을 보일지 지시한다
        fun getServerInfo()

        fun postMessage(context: Context, message: String)
    }
    interface Model
    {

    }
}