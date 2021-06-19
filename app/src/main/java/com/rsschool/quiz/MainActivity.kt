package com.rsschool.quiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rsschool.quiz.data.DataManager
import com.rsschool.quiz.fragments.FragmentQuestions
import com.rsschool.quiz.fragments.FragmentResult
import com.rsschool.quiz.listeners.BackButtonListener
import com.rsschool.quiz.listeners.NextButtonListener
import com.rsschool.quiz.listeners.StartOverButtonListener

class MainActivity : AppCompatActivity(), BackButtonListener, NextButtonListener,
    StartOverButtonListener {

    private var dataManager: DataManager = DataManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startFragmentQuestions(dataManager.getRadioButtonNumber(0))
    }

    private fun startFragmentQuestions(radioButtonNumber: Int) {
        val fragmentQuestions = FragmentQuestions.newInstance(radioButtonNumber)
        fragmentQuestions.setBackButtonListener(this)
        fragmentQuestions.setNextButtonListener(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentQuestions)
            .commit()
    }

    private fun startFragmentResult(result: Int, answers: ArrayList<String>) {
        val fragmentResult = FragmentResult.newInstance(result, answers)
        fragmentResult.setStartOverButtonListener(this)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragmentResult)
            .commit()
    }

    override fun onBackButtonListener(pageNumber: Int, radioButtonNumber: Int) {
        dataManager.saveRadioButtonNumber(pageNumber, radioButtonNumber)
        return when (pageNumber) {
            1 -> startFragmentQuestions(dataManager.getRadioButtonNumber(0))
            2 -> startFragmentQuestions(dataManager.getRadioButtonNumber(1))
            3 -> startFragmentQuestions(dataManager.getRadioButtonNumber(2))
            else -> startFragmentQuestions(dataManager.getRadioButtonNumber(3))
        }
    }

    override fun onNextButtonListener(pageNumber: Int, radioButtonNumber: Int) {
        dataManager.saveRadioButtonNumber(pageNumber, radioButtonNumber)
        return when (pageNumber) {
            0 -> startFragmentQuestions(dataManager.getRadioButtonNumber(1))
            1 -> startFragmentQuestions(dataManager.getRadioButtonNumber(2))
            2 -> startFragmentQuestions(dataManager.getRadioButtonNumber(3))
            3 -> startFragmentQuestions(dataManager.getRadioButtonNumber(4))
            else -> startFragmentResult(dataManager.getResult(), dataManager.getAnswers())

        }
    }

    override fun onStartOverButtonListener() {
        dataManager.clearAll()
        startFragmentQuestions(dataManager.getRadioButtonNumber(0))
    }
}