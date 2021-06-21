package com.rsschool.quiz.data


class DataManager {
    private var listOfData = intArrayOf(-1, -1, -1, -1, -1)


    fun saveRadioButtonNumber(pageNumber: Int, radioButtonNumber: Int) {
        listOfData[pageNumber] = radioButtonNumber
    }

    fun getRadioButtonNumber(pageNumber: Int): Int {
        return listOfData[pageNumber]
    }

    fun getQuestion(pageNumber: Int): ListItem {
        return questionItems[pageNumber]
    }

    fun getAnswers(): ArrayList<String> {
        val listOfAnswers = arrayListOf("", "", "", "", "")
        for (i in listOfData.indices) {
            when (listOfData[i]) {
                1 -> listOfAnswers[i] = questionItems[i].option1
                2 -> listOfAnswers[i] = questionItems[i].option2
                3 -> listOfAnswers[i] = questionItems[i].option3
                4 -> listOfAnswers[i] = questionItems[i].option4
                5 -> listOfAnswers[i] = questionItems[i].option5
            }
        }
        return listOfAnswers
    }

    fun getResult(): Int {
        var result = 0
        for (i in listOfData.indices) {
            if (listOfData[i] == questionItems[i].numberOfCorrectAnswer) {
                ++result
            }
        }
        return result
    }

    fun clearAll() {
        listOfData = intArrayOf(-1, -1, -1, -1, -1)
    }
}


data class ListItem(
    val question: String,
    val option1: String,
    val option2: String,
    val option3: String,
    val option4: String,
    val option5: String,
    val numberOfCorrectAnswer: Int,
)

private val questionItems = listOf(
    ListItem("2+2=?", "1", "2", "3", "4", "6", 4),
    ListItem("2+3=?", "1", "2", "3", "4", "5", 5),
    ListItem("2+4=?", "1", "6", "3", "4", "5", 2),
    ListItem("2+5=?", "1", "2", "7", "4", "5", 3),
    ListItem("2+6=?", "8", "2", "3", "4", "5", 1)
)