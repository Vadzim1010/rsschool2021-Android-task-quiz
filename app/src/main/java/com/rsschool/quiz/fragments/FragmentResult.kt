package com.rsschool.quiz.fragments

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rsschool.quiz.R
import com.rsschool.quiz.data.DataManager
import com.rsschool.quiz.databinding.FragmentResultBinding
import com.rsschool.quiz.listeners.StartOverButtonListener


class FragmentResult : Fragment(R.layout.fragment_result) {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private var onStartOverButtonListener: StartOverButtonListener? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        context?.setTheme(R.style.Theme_Quiz)
        setPageStatusBar()
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = arguments?.getInt(RESULT) ?: 0
        val answers = arguments?.getStringArrayList(ANSWERS) ?: arrayListOf("", "", "", "", "")

        val resultString = "Your result: ${result.toString()}/5"
        binding.result.text = resultString

        binding.startOverButton.setOnClickListener {
            onStartOverButtonListener?.onStartOverButtonListener()
        }

        binding.shareButton.setOnClickListener {
            onShareButtonClicked(result, answers)
        }

        binding.finishButton.setOnClickListener {
            activity?.finish()
        }

    }

    private fun onShareButtonClicked(result: Int, answers: ArrayList<String>) {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Quiz results")
        intent.putExtra(Intent.EXTRA_TEXT, getAnswers(result, answers))
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent, "Share"))
    }

    private fun getAnswers(result: Int, answers: ArrayList<String>): String {
        var answersString = "Your result: ${result.toString()}/5\n\n"
        for (i in 0 until answers.size) {
            answersString += "${i + 1}) " + DataManager().getQuestion(i).question + "\n" + "Your answer: " + answers[i] + "\n\n"
        }
        return answersString
    }

    private fun setPageStatusBar() {
        val window = activity?.window
        val typedValue = TypedValue()
        val currentTheme = context?.theme
        currentTheme?.resolveAttribute(android.R.attr.statusBarColor, typedValue, true)
        window?.statusBarColor = typedValue.data
    }

    fun setStartOverButtonListener(listener: StartOverButtonListener) {
        onStartOverButtonListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        fun newInstance(result: Int, answers: ArrayList<String>): FragmentResult {
            val fragment = FragmentResult()
            val args = Bundle()
            args.putInt(RESULT, result)
            args.putStringArrayList(ANSWERS, answers)
            fragment.arguments = args
            return fragment
        }

        private const val RESULT = "RESULT"
        private const val ANSWERS = "ANSWERS"
    }
}