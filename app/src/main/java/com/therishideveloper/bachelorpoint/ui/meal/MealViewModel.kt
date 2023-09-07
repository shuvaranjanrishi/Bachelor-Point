package com.therishideveloper.bachelorpoint.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealViewModel @Inject constructor(private val mealRepo: MealRepo) : ViewModel() {

    val mealsLiveData get() = mealRepo.mealsLiveData
    val sumMealsLiveData get() = mealRepo.sumMealsLiveData
    val sumMealsClosingLiveData get() = mealRepo.sumMealsClosingLiveData
    val totalMealsLiveData get() = mealRepo.totalMealsLiveData
    val totalExpenseLiveData get() = mealRepo.totalExpenseLiveData
    val mealRateLiveData get() = mealRepo.mealRateLiveData

    fun getMealListOfADay(accountId: String, monthAndYear: String, date: String) {
        viewModelScope.launch {
            mealRepo.getMealListOfADay(accountId, monthAndYear, date)
        }
    }

    fun getMealListOfAMonth(accountId: String, monthAndYear: String) {
        viewModelScope.launch {
            mealRepo.getMealListOfAMonth(accountId, monthAndYear)
        }
    }

    fun sumIndividualMeals(memberList: List<User>, mealList: List<Meal>) {
        viewModelScope.launch {
            mealRepo.sumIndividualMeals(memberList, mealList)
        }
    }

    fun totalMeals(mealList: List<Meal>) {
        viewModelScope.launch {
            mealRepo.totalMeals(mealList)
        }
    }

    fun sumIndividualClosingMeals(
        mealList: List<Meal>,
        memberList: List<User>,
        expenseList: MutableList<Expense>
    ) {
        viewModelScope.launch {
            mealRepo.sumIndividualClosingMeals(mealList, memberList, expenseList)
        }
    }

    fun totalExpenseClosingMeals(mealList: List<MealClosing>) {
        viewModelScope.launch {
            mealRepo.totalExpenseClosingMeals(mealList)
        }
    }
}
//75