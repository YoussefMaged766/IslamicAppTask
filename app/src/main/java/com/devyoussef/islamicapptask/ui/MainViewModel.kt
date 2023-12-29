package com.devyoussef.islamicapptask.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devyoussef.islamicapptask.data.remote.Repo
import com.devyoussef.islamicapptask.model.state.StateModel
import com.devyoussef.islamicapptask.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {
    private val _state = MutableStateFlow(StateModel())
    val state = _state.asStateFlow()

    fun getPrayers(year: Int, month: Int, city: String, method: Int, country: String) =
        viewModelScope.launch {
            repo.getAzanDates(year, month, city, country, method).collect { status ->
                when (status) {
                    is Status.Loading -> {
                        _state.value = StateModel(isLoading = true)
                    }

                    is Status.Success -> {
                        _state.value = StateModel(
                            isLoading = false,
                            status = "OK",
                            error = null,
                            prayer = status.data
                        )
                    }

                    is Status.Error -> {
                        _state.value = StateModel(
                            isLoading = false,
                            error = status.message,
                            success = null,
                            status = "BAD_REQUEST"
                        )
                    }
                }
            }
        }
}