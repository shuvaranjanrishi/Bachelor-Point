package com.therishideveloper.bachelorpoint.ui.meal

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
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
    val monthView get() = mealRepo.monthView

    fun getMealListOfADay(accountId: String, monthAndYear: String, date: String) {
        viewModelScope.launch {
            mealRepo.getMealListOfADay(accountId, monthAndYear, date)
        }
    }

    fun getMealListOfAMonth(accountId: String, monthAndYear: String) {
        viewModelScope.launch {
            mealRepo.getMealListResult(accountId, monthAndYear){ mealList ->
                mealRepo.getMembers(accountId){members ->
                    mealRepo.sumIndividualMeals(members, mealList)
                }
            }
        }
    }

    fun getMonthView(accountId: String, monthAndYear: String) {
        viewModelScope.launch {
            mealRepo.getMealListResult(accountId, monthAndYear){ mealList ->
                mealRepo.getMembers(accountId){members ->
                    mealRepo.getMonthView(members, mealList)
                }
            }
        }
    }

    fun totalMeals(mealList: List<Meal>) {
        viewModelScope.launch {
            mealRepo.totalMeals(mealList)
        }
    }

    fun sumIndividualClosingMeals(
        expenseRef: DatabaseReference,
        accountId: String,
        monthAndYear: String
    ) {
        viewModelScope.launch {
            mealRepo.getExpenseListResult(expenseRef.child(monthAndYear)) { expenseList ->
                mealRepo.getMealListResult(accountId, monthAndYear) { mealList ->
                    mealRepo.getMembers(accountId) { members ->
                        mealRepo.sumIndividualClosingMeals(mealList, members, expenseList)
                    }
                }
            }
        }
    }

    fun totalExpenseClosingMeals(mealList: List<MealClosing>) {
        viewModelScope.launch {
            mealRepo.totalExpenseClosingMeals(mealList)
        }
    }
}
//75