package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)

class GameViewModel : ViewModel(){

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)

    }

    companion object {
        // These represent different important times
        // This is when the game is over
        var DONE = 0L
        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1000L
        // This is the total time of the game
        var COUNTDOWN_TIME = 60000L
    }

    private val timer : CountDownTimer

    private val _word = MutableLiveData<String>()
    private val _score = MutableLiveData<Int>()
    private val _eventGameFinished = MutableLiveData<Boolean>()
    private lateinit var wordList: MutableList<String>
    private val _timeLeft = MutableLiveData<Long>()
    private val _eventBuzzFinish = MutableLiveData<BuzzType>()

    val timeLeftString = Transformations.map(timeLeft) { time ->
        DateUtils.formatElapsedTime(time)
    }


    val word : LiveData<String>
        get() = _word

    val score : LiveData<Int>
        get() = _score

    val eventGameFinished : LiveData<Boolean>
        get() = _eventGameFinished

    val timeLeft: LiveData<Long>
        get() = _timeLeft

    val eventBuzzFinish: LiveData<BuzzType>
        get() = _eventBuzzFinish

    init {
        _eventGameFinished.value  = false
        resetList()
        nextWord()
        _score.value = 0

        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = (millisUntilFinished/ ONE_SECOND)
                if (millisUntilFinished / ONE_SECOND <= 10L) {
                    _eventBuzzFinish.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _eventGameFinished.value = true
                _timeLeft.value = DONE
                _eventBuzzFinish.value = BuzzType.GAME_OVER
            }
        }
        timer.start()
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
        }
        nextWord()
    }

    fun onCorrect() {
        _score.value = (score.value)?.plus(1)
        _eventBuzzFinish.value = BuzzType.CORRECT
        nextWord()
    }

    fun onGameFinishComplete() {
        _eventGameFinished.value = false
    }

    fun onBuzzComplete(){
        _eventBuzzFinish.value = BuzzType.NO_BUZZ
    }

}