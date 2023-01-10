package com.mbdev.criminalintent

import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    private val crimeRepository = CrimeRepository.get()

    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null)
    val crime: StateFlow<Crime?> = _crime.asStateFlow()

    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }

    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    suspend fun deleteCrime() {
        val crimeToBeDeleted = crime.value?.let { crimeRepository.getCrime(it.id) }
        if (crimeToBeDeleted != null) {
            crimeRepository.deleteCrime(crimeToBeDeleted)
        }
    }

    override fun onCleared() {
        super.onCleared()

        crime.value?.let { crimeRepository.updateCrime(it) }
    }
}

class CrimeDetailViewModelFactory(private val crimeId: UUID) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}


