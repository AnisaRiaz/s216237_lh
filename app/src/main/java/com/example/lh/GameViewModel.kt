package com.example.lh



import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

val cars = listOf(
    "AUDI",
    "MERCEDES",
    "BMW",
    "TESLA",
)

val cities = listOf(
    "LONDON",
    "ODENSE",
    "MADRID",
    "LAHORE",
)
data class UiState(
    val point : Int = 0,
    var wheel : Int = 0,
    val life : Int  = 0,
    val word : String = "",
    val category : String = "",
    val foundWord : String = ""
) {
    fun gameOver() : Boolean = life <= 0 || foundWord == word
}
class GameViewModel() : ViewModel() {
    val _state = MutableStateFlow( UiState())
    val state = _state.asStateFlow()


    init {
        createNewGame()
        viewModelScope.launch {
            while (true) {
                _state.emit(_state.value)
                delay(10)
            }
        }

    }
    fun turnWheel() {
        if(!_state.value.gameOver() && _state.value.wheel == 0) {
            val newWheel = (0..10).random()
            if(newWheel == 0) {
                _state.update {
                    it.copy(point = 0, wheel = newWheel)
                }
                return
            }
            _state.update {  it.copy(wheel = newWheel)}

        }

    }
    fun guessACharacter(g : String) {
        if(!_state.value.gameOver() && _state.value.wheel != 0) {
            val listOfMatches = mutableListOf<Int>()
            for (i in 0 .. _state.value.word.length-1) {
                if(_state.value.word[i] == g[0])
                    listOfMatches.add(i)
            }

            for (i in 0.. _state.value.foundWord.length-1)
                if(listOfMatches.contains(i))
                    _state.update {
                        var newFound = it.foundWord
                        newFound = newFound.substring(0, i) + g + newFound.substring(i+1)
                        it.copy(foundWord = newFound)
                    }
            if(listOfMatches.size > 0){
                _state.update {
                    it.copy(point = it.wheel*100*listOfMatches.size + it.point)
                }
            }
            else {
                _state.update {
                    it.copy(life = it.life -1)
                }
            }
            _state.update {
                it.copy(wheel = 0)
            }
        }
    }
    fun createNewGame() {
        val category = listOf("cars", "cities").random()
        val word = when(category) {
            "cars" -> cars.random()
            else -> cities.random()
        }
        var newHidden = ""
        for (i in word)
            newHidden +="_"
        _state.update {
            it.copy(
                point = 0,
                wheel = 0,
                life = 5,
                word = word,
                foundWord = newHidden,
                category = category
            )
        }
    }


}