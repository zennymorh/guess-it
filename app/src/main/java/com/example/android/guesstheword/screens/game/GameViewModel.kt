package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel(){
    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        const val COUNTDOWN_TIME = 10000L
    }

    private val timer : CountDownTimer

    private val _word = MutableLiveData<String>()
    private val _message = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()
    private val _eventGameFinished = MutableLiveData<Boolean>()
    private lateinit var wordList: MutableList<String>

    val word : LiveData<String>
        get() = _word

    val message : LiveData<String>
        get() = _message

    val score : LiveData<Int>
        get() = _score

    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    init {
        _eventGameFinished.value  = false
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                _eventGameFinished.value = true
            }
        }
//        DateUtils.formatElapsedTime()

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

//    Resets the list of words and randomizes the order
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

//      Moves to the next word in the list
    private fun nextWord() {
//      Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/
    fun onSkip() {
        if ((_score.value)!! > 0){
            _score.value = (score.value)?.minus(1)
//            _message.value = "You can't go back"
        }
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

}