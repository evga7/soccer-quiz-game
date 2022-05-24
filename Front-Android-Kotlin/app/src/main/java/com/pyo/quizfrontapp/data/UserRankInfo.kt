package com.pyo.quizfrontapp.data


data class UserRankInfo(
    var id:Long,
    var nickName:String,
    var totalQuizCount:Long,
    var solvedQuizCount:Long,
    var percentageOfAnswer:Double,
)
