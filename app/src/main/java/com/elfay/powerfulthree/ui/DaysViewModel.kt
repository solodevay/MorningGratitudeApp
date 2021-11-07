package com.elfay.powerfulthree.ui

import android.widget.Toast
import androidx.lifecycle.*
import com.elfay.powerfulthree.data.Day
import com.elfay.powerfulthree.data.DayDao
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class DaysViewModel(private val dayDao: DayDao) : ViewModel() {

    companion object {
        //oneday in millisecoands
        const val OFFSET = 86400000
         val nonFormatedDate = MutableLiveData<Long>(0)

    }
    //val allDays: LiveData<List<Day>> = dayDao.getDaysOfMonth().asLiveData()
   init {
        setDate()

    }

    private fun insertDay(day: Day) {
        viewModelScope.launch {
            dayDao.insert(day)
        }
    }

    fun isDayExists(currentDay: String): LiveData<Boolean>{

       return dayDao.isExists(currentDay).asLiveData()

    }
    //currentDay for this method is gonna be always the nonFormatedDate
    private fun getNewDayEntry(currentDay: String,month: String, focusOn: String,gratefulFor:String,letGoOf:String): Day {
        return Day(
            currentDay = currentDay,
            month = month,
            focusOn = focusOn,
            gratefulFor=gratefulFor,
            letGoOf=letGoOf
        )
    }

    fun addNewDay(currentDay: String,month: String, focusOn: String,gratefulFor:String,letGoOf:String) {
        val newDay = getNewDayEntry(currentDay,month, focusOn,gratefulFor,letGoOf)
        insertDay(newDay)

    }

    fun setDate() {

        var calendar: Calendar
        calendar = Calendar.getInstance()
        nonFormatedDate.value = calendar.timeInMillis
    }

    fun formatDate(): String {

        var simpleDateFormat: SimpleDateFormat
        simpleDateFormat = SimpleDateFormat("EEEE\nLLL dd'th' yyyy ")
        return simpleDateFormat.format(nonFormatedDate.value).toString()

    }

    fun getMonth(): String {
        var simpleDateFormat: SimpleDateFormat
        simpleDateFormat = SimpleDateFormat("LLLL")
        return simpleDateFormat.format(nonFormatedDate.value).toString()
    }

    fun isEntryValid(focusOn: String,gratefulFor:String,letGoOf:String):Boolean{
        if(focusOn.isBlank()|| gratefulFor.isBlank() || letGoOf.isBlank()){
            return false
        }
        return true
    }

   fun minusDay(){

        nonFormatedDate.value = (nonFormatedDate.value)?.minus(OFFSET)

    }
    fun plusDay(){
        nonFormatedDate.value = (nonFormatedDate.value)?.plus(OFFSET)

    }
    fun retrieveDay(currentDay:String): LiveData<Day>{
        return dayDao.getDay(currentDay).asLiveData()
    }





    fun updateItem(day: Day){
        viewModelScope.launch {
            dayDao.update(day)
        }
    }

    private fun getUpdatedDayEntry(currentDay:String,month:String,focusOn:String,gratefulFor:String,letGoOf:String): Day{
        return Day(currentDay=currentDay,month=month,focusOn=focusOn,gratefulFor=gratefulFor,letGoOf=letGoOf)

    }

    fun updateItem(currentDay:String,month:String,focusOn:String,gratefulFor:String,letGoOf:String){
        val updatedDay = getUpdatedDayEntry(currentDay,month,focusOn,gratefulFor,letGoOf)
        updateItem(updatedDay)

    }
}


class DaysViewModelFactory(private val dayDao: DayDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DaysViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DaysViewModel(dayDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}