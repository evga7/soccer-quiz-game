package com.pyo.quizfrontapp.data

data class ServerInfo(
    var id:Long,
    var totalNumberOfQuiz:Long,
    var totalNumberOfUser:Long,
    var topQuizPlayer:FrontUserInfo,
    var badQuizPlayer:FrontUserInfo,
    var bestQuizPlayer:FrontUserInfo,

)
