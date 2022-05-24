package com.pyo.quizfrontapp.data

data class quizInfoDto(
    var title:String,
    var example1:String,
    var example2:String,
    var example3:String,
    var example4:String,
    var filePath:String,
    var answer:Int,
    var selectedAnswer:Long,
)
