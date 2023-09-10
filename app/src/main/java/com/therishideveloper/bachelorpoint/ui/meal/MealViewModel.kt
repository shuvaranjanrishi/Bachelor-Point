package com.therishideveloper.bachelorpoint.ui.meal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.ui.member.MemberRepo
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
            mealRepo.getMealListResult(accountId, monthAndYear){ mealList ->
                mealRepo.getMembers(accountId){members ->
                    mealRepo.sumIndividualMeals(members, mealList)
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
        accountId: String,
        monthAndYear:String,
        database:DatabaseReference
    ) {
        viewModelScope.launch {
            mealRepo.getExpenseListResult(accountId,monthAndYear, database){expenseList->
                mealRepo.getMealListResult(accountId, monthAndYear){ mealList ->
                    mealRepo.getMembers(accountId){members ->
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