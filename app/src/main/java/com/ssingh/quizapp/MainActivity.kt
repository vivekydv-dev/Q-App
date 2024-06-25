package com.ssingh.quizapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ssingh.quizapp.databinding.ActivityMainBinding
import com.ssingh.quizapp.model.MDQuizData
import com.ssingh.quizapp.model.QuizData
import com.ssingh.quizapp.model.mQuizData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var apiInterface: ApiInterface
    var quizData: ArrayList<MDQuizData> = arrayListOf()
    private var questionIndex = 0
    private var userAnswer = " "
    private var userAnswerList: ArrayList<String> = arrayListOf()
    var quizAnswers: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = RetrofitInstance.getInstance().create(ApiInterface::class.java)
        getQuizData()

        binding.op1.setOnClickListener {
            userAnswer = binding.op1.text.toString()
            binding.uAnswer.text = userAnswer
        }
        binding.op2.setOnClickListener {
            userAnswer = binding.op2.text.toString()
            binding.uAnswer.text = userAnswer
        }
        binding.op3.setOnClickListener {
            userAnswer = binding.op3.text.toString()
            binding.uAnswer.text = userAnswer
        }
        binding.op4.setOnClickListener {
            userAnswer = binding.op4.text.toString()
            binding.uAnswer.text = userAnswer
        }

        binding.next.setOnClickListener {
            userAnswerList.add(userAnswer)
            binding.uAnswer.text = " "
            userAnswer = " "
            questionIndex++
            if (questionIndex < quizData.size) {
                setData()
            } else {
                var score = 0
                val num = quizAnswers.size - 1
                for (i in 0..num) {
                    if (quizAnswers[i] == userAnswerList[i]) {
                        score++
                    }
                }
                Toast.makeText(this, " your score is $score", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getQuizData() {
        val call = apiInterface.getQuizData()
        call.enqueue(object : Callback<QuizData?> {
            override fun onResponse(p0: Call<QuizData?>, p1: Response<QuizData?>) {
                if (p1.isSuccessful && p1.body() != null) {
                    val result = p1.body()!!.results
                    result.forEach {
                        val option = it.incorrect_answers as ArrayList<String>
                        val randomIndex = Random.nextInt(0, 3)
                        option.add(randomIndex, it.correct_answer)
                        quizData.add(MDQuizData(it.question, option))
                        quizAnswers.add(it.correct_answer)
                    }
                    setData()
                }
            }

            override fun onFailure(p0: Call<QuizData?>, p1: Throwable) {
                p1.printStackTrace()
                Toast.makeText(this@MainActivity, "Failed to fetch Data", Toast.LENGTH_LONG).show()
            }
        })
    }

    fun setData() {
        val data = quizData[questionIndex]
        binding.question.text = data.question
        binding.op1.text = data.option[0]
        binding.op2.text = data.option[1]
        binding.op3.text = data.option[2]
        binding.op4.text = data.option[3]

        if (questionIndex == quizData.size - 1) {
            binding.next.text = "Submit"
        }
    }
}