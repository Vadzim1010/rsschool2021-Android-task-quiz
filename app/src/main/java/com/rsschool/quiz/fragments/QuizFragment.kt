package com.rsschool.quiz.fragments

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.data.DataManager
import com.rsschool.quiz.databinding.FragmentQuizBinding
import com.rsschool.quiz.listeners.BackButtonListener
import com.rsschool.quiz.listeners.NextButtonListener

class QuizFragment : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var onBackButtonListener: BackButtonListener? = null
    private var onNextButtonListener: NextButtonListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                onButtonBackPressed()
            }
        }
        if (pageNumber != 0) {
            requireActivity().onBackPressedDispatcher.addCallback(
                this,
                callback
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setPageTheme()
        setPageStatusBar()
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioButtonNumber = arguments?.getInt(KEY_RADIO_BUTTON_NUMBER) ?: -1

        setPageQuestion(pageNumber)
        checkRadioButton(radioButtonNumber)
        radioButtonSelected()

        binding.toolbar.setOnClickListener {
            onButtonBackPressed()
        }

        binding.previousButton.setOnClickListener {
            onButtonBackPressed()
        }

        binding.nextButton.setOnClickListener {
            onButtonNextPressed()
        }
    }

    private fun onButtonBackPressed() {
        onBackButtonListener?.onBackButtonListener(pageNumber, getCheckedRadioButtonNumber())
        if (pageNumber > 0) {
            --pageNumber
        }
    }

    private fun onButtonNextPressed() {
        onNextButtonListener?.onNextButtonListener(pageNumber, getCheckedRadioButtonNumber())
        if (pageNumber < 5) {
            ++pageNumber
        }
        if (pageNumber == 5) {
            pageNumber = 0
        }
    }

    private fun getCheckedRadioButtonNumber(): Int {
        var radioButtonNumber = -1
        with(binding) {
            when {
                optionOne.isChecked -> {
                    radioButtonNumber = 1
                }
                optionTwo.isChecked -> {
                    radioButtonNumber = 2
                }
                optionThree.isChecked -> {
                    radioButtonNumber = 3
                }
                optionFour.isChecked -> {
                    radioButtonNumber = 4
                }
                optionFive.isChecked -> {
                    radioButtonNumber = 5
                }
            }
        }
        return radioButtonNumber
    }

    private fun checkRadioButton(radioButtonNumber: Int) {
        with(binding) {
            when (radioButtonNumber) {
                1 -> optionOne.isChecked = true
                2 -> optionTwo.isChecked = true
                3 -> optionThree.isChecked = true
                4 -> optionFour.isChecked = true
                5 -> optionFive.isChecked = true
            }
        }
    }

    private fun radioButtonSelected() {
        with(binding) {
            if (radioGroup.checkedRadioButtonId == -1) {
                nextButton.isEnabled = false
            }
            radioGroup.setOnCheckedChangeListener { _, _ -> nextButton.isEnabled = true }
        }
    }

    private fun setPageQuestion(pageNumber: Int) {
        val questions = DataManager().getQuestion(pageNumber)
        with(binding) {
            if (pageNumber == 0) {
                previousButton.isEnabled = false
                toolbar.isEnabled = false
                toolbar.navigationIcon = null
            }
            if (pageNumber == 4) {
                nextButton.text = getString(R.string.submit)
            }
            question.text = questions.question
            optionOne.text = questions.option1
            optionTwo.text = questions.option2
            optionThree.text = questions.option3
            optionFour.text = questions.option4
            optionFive.text = questions.option5
            toolbar.title = "Question ${pageNumber + 1}"
        }
    }

    private fun setPageStatusBar() {
        val window = activity?.window
        val typedValue = TypedValue()
        val currentTheme = context?.theme
        currentTheme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        window?.statusBarColor = typedValue.data
    }

    private fun setPageTheme() {
        when (pageNumber) {
            0 -> {
                context?.setTheme(R.style.Theme_Quiz_First)
            }
            1 -> {
                context?.setTheme(R.style.Theme_Quiz_Second)
            }
            2 -> {
                context?.setTheme(R.style.Theme_Quiz_Third)
            }
            3 -> {
                context?.setTheme(R.style.Theme_Quiz_Fourth)
            }
            4 -> {
                context?.setTheme(R.style.Theme_Quiz_Fifth)
            }
        }
    }

    fun setBackButtonListener(listener: BackButtonListener) {
        onBackButtonListener = listener
    }

    fun setNextButtonListener(listener: NextButtonListener) {
        onNextButtonListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(radioButtonNumber: Int): QuizFragment {
            val fragment = QuizFragment()
            val bundle = bundleOf(
                KEY_RADIO_BUTTON_NUMBER to radioButtonNumber
            )
            fragment.arguments = bundle
            return fragment
        }

        private const val KEY_RADIO_BUTTON_NUMBER = "KEY_RADIO_BUTTON_ID"
        private var pageNumber: Int = 0
    }
}