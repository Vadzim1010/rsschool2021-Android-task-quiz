package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.rsschool.quiz.data.DataManager
import com.rsschool.quiz.fragments.QuizFragment
import com.rsschool.quiz.fragments.ResultFragment
import com.rsschool.quiz.listeners.BackButtonListener
import com.rsschool.quiz.listeners.NextButtonListener
import com.rsschool.quiz.listeners.StartOverButtonListener

class MainActivity : AppCompatActivity(), BackButtonListener, NextButtonListener,
    StartOverButtonListener {

    private var dataManager: DataManager = DataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startQuizFragment(dataManager.getRadioButtonNumber(0))
    }

    private fun startQuizFragment(radioButtonNumber: Int) {
        val quizFragment = QuizFragment.newInstance(radioButtonNumber)
        quizFragment.setBackButtonListener(this)
        quizFragment.setNextButtonListener(this)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, quizFragment)
        }
    }

    private fun startResultFragment(result: Int, answers: ArrayList<String>) {
        val resultFragment = ResultFragment.newInstance(result, answers)
        resultFragment.setStartOverButtonListener(this)
        supportFragmentManager.commit {
            replace(R.id.fragment_container, resultFragment)
        }
    }

    override fun onBackButtonListener(pageNumber: Int, radioButtonNumber: Int) {
        dataManager.saveRadioButtonNumber(pageNumber, radioButtonNumber)
        return when (pageNumber) {
            1 -> startQuizFragment(dataManager.getRadioButtonNumber(0))
            2 -> startQuizFragment(dataManager.getRadioButtonNumber(1))
            3 -> startQuizFragment(dataManager.getRadioButtonNumber(2))
            else -> startQuizFragment(dataManager.getRadioButtonNumber(3))
        }
    }

    override fun onNextButtonListener(pageNumber: Int, radioButtonNumber: Int) {
        dataManager.saveRadioButtonNumber(pageNumber, radioButtonNumber)
        return when (pageNumber) {
            0 -> startQuizFragment(dataManager.getRadioButtonNumber(1))
            1 -> startQuizFragment(dataManager.getRadioButtonNumber(2))
            2 -> startQuizFragment(dataManager.getRadioButtonNumber(3))
            3 -> startQuizFragment(dataManager.getRadioButtonNumber(4))
            else -> startResultFragment(dataManager.getResult(), dataManager.getAnswers())
        }
    }

    override fun onStartOverButtonListener() {
        dataManager.clearAll()
        startQuizFragment(dataManager.getRadioButtonNumber(0))
    }
}