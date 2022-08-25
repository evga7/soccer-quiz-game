package com.pyo.quizfrontapp.network

import com.pyo.quizfrontapp.data.ServerInfo
import com.pyo.quizfrontapp.data.UserRankInfo
import com.pyo.quizfrontapp.data.quizInfoDto
import retrofit2.Call
import retrofit2.http.*

interface QuizService {

    @FormUrlEncoded
    @POST("/front-user")
    fun singUp(
        @Field("nickName") nickName:String,
    ): Call<Void>

    @FormUrlEncoded
    @POST("/front-user/check")
    fun nickNameCheck(
        @Field("nickName") nickName:String,
    ): Call<String>


    @GET("/front-user/random-quiz")
    fun getRandomQuiz(
        @Query("number") number:Long,
    ):Call<List<quizInfoDto>>

    @GET("/front-user/server-info")
    fun getServerInfo(
    ):Call<ServerInfo>

    @FormUrlEncoded
    @PUT("/front-user/user")
    fun userUpdate(
        @Field("nickName") nickName:String,
        @Field("totalQuizCount") totalQuizCount:Long,
        @Field("solvedQuizCount") solvedQuizCount:Long,
    ):Call<Void>

    @GET("/front-user/user-rank")
    fun getUserRank():Call<List<UserRankInfo>>

    @FormUrlEncoded
    @POST("/front-user/rank")
    fun getPlayerRank(
        @Field("nickName")nickName: String,
    ):Call<UserRankInfo>

    @GET("/front-user/version-check")
    fun checkUpdate():Call<String>

    @FormUrlEncoded
    @POST("/front-user/message")
    fun postUserMessage(
        @Field("nickName")nickName: String,
        @Field("message")message:String,
    ):Call<Void>
}