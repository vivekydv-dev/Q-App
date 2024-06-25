package com.ssingh.quizapp

import com.ssingh.quizapp.model.QuizData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("api.php?amount=20&category=21&difficulty=medium&type=multiple")
    fun getQuizData(): Call<QuizData>
}
