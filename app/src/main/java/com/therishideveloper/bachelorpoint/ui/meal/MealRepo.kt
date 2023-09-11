package com.therishideveloper.bachelorpoint.ui.meal

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DatabaseReference
import com.google.gson.Gson
import com.therishideveloper.bachelorpoint.api.ApiService
import com.therishideveloper.bachelorpoint.api.NetworkResult
import com.therishideveloper.bachelorpoint.model.Expense
import com.therishideveloper.bachelorpoint.model.ExpenseClosing
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.expense.ExpenseRepo
import com.therishideveloper.bachelorpoint.ui.member.MemberRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

/**
 * Created by Shuva Ranjan Rishi on 27,August,2023
 * BABL, Bangladesh,
 */

class MealRepo @Inject constructor(
    private val expenseRepo: ExpenseRepo,
    private val memberRepo: MemberRepo,
    private val apiService: ApiService
) {

    private val TAG = "MemberRepo"

    private val _mealsLiveData = MutableLiveData<NetworkResult<List<Meal>>>()
    val mealsLiveData get() = _mealsLiveData

    private val _sumMealsLiveData = MutableLiveData<NetworkResult<List<Meal>>>()
    val sumMealsLiveData get() = _sumMealsLiveData

    private val _totalMealsLiveData = MutableLiveData<NetworkResult<Meal>>()
    val totalMealsLiveData get() = _totalMealsLiveData

    private val _sumMealsClosingLiveData = MutableLiveData<NetworkResult<List<MealClosing>>>()
    val sumMealsClosingLiveData get() = _sumMealsClosingLiveData

    private val _totalExpenseLiveData = MutableLiveData<NetworkResult<List<MealClosing>>>()
    val totalExpenseLiveData get() = _totalExpenseLiveData

    private val _mealRateLiveData = MutableLiveData<NetworkResult<String>>()
    val mealRateLiveData get() = _mealRateLiveData

    var mealList: MutableList<Meal> = mutableListOf()

    fun getMembers(accountId: String, result: (members: List<User>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            val response = withContext(Dispatchers.Default) {
                memberRepo.getMembers(accountId)
            }
            result(response)
        }
    }

    fun getMealListResult(
        accountId: String,
        monthAndYear: String,
        result: (mealList: List<Meal>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = withContext(Dispatchers.Default) {
                getMealListOfAMonth(accountId, monthAndYear)
            }
            result(response)
        }
    }

    fun getExpenseListResult(
        expenseRef: DatabaseReference,
        result: (expenseList: List<Expense>) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = withContext(Dispatchers.Default) {
                expenseRepo.getExpenses(expenseRef)
            }
            result(response)
        }
    }

    suspend fun getMealListOfADay(accountId: String, monthAndYear: String, date: String) {
        mealList.clear()
        _mealsLiveData.postValue(NetworkResult.Loading())
        try {
            val response = apiService.getMealListOfADay(accountId, monthAndYear, date)
            if (response.isSuccessful && response.body() != null) {
                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val key = keys.next().toString()
                    val childObj: JSONObject = jsonObject.getJSONObject(key)

                    val meal = Gson().fromJson(
                        childObj.toString(), Meal::class.java
                    ) as Meal
                    mealList.add(meal)
                }
                _mealsLiveData.postValue(NetworkResult.Success(mealList.sortedBy { it.name }))
            } else if (response.errorBody() != null) {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            } else {
                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        } catch (e: Exception) {
            _mealsLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
    }

    private suspend fun getMealListOfAMonth(accountId: String, monthAndYear: String): List<Meal> {
        mealList.clear()
//        _mealsLiveData.postValue(NetworkResult.Loading())
        try {
            val response = apiService.getMealListOfAMonth(accountId, monthAndYear)

            if (response.isSuccessful && response.body() != null) {

                val jsonObject = JSONObject(response.body()!!.asJsonObject.toString())
                val keys: Iterator<Any> = jsonObject.keys()
                while (keys.hasNext()) {
                    val jsonObject1  = jsonObject.getJSONObject(keys.next().toString())
                    val keys1:Iterator<Any> = jsonObject1.keys()
                    while (keys1.hasNext()) {
                        val key1 = keys1.next().toString()
                        val childObj: JSONObject = jsonObject1.getJSONObject(key1)

                        val meal = Gson().fromJson(
                            childObj.toString(), Meal::class.java
                        ) as Meal
                        mealList.add(meal)
                    }
                }

//                _mealsLiveData.postValue(NetworkResult.Success(mealList.sortedBy { it.name }))

            } else if(response.errorBody()!=null){
                val jsonObject = JSONObject(response.errorBody()!!.charStream().readText())
//                _mealsLiveData.postValue(NetworkResult.Error(jsonObject.getString("message")))
            }else {
//                _mealsLiveData.postValue(NetworkResult.Error("Something went wrong"))
            }
        } catch (e: Exception) {
//            _mealsLiveData.postValue(NetworkResult.Error(e.localizedMessage))
        }
        return mealList
    }

    fun sumIndividualMeals(memberList: List<User>, mealList: List<Meal>) {

        val newList: MutableList<Meal> = ArrayList()
        for (i in memberList.indices) {
            var id = ""
            var name = ""
            var createdAt = ""
            var date = ""
            var updatedAt = ""
            var firstMeal = 0.0
            var secondMeal = 0.0
            var thirdMeal = 0.0
            var subTotalMeal = 0.0
            for (j in mealList.indices) {
                if (mealList[j].memberId.toString() == memberList.toTypedArray()[i].id) {
                    id = mealList[j].memberId.toString()
                    name = mealList[j].name.toString()
                    date = mealList[j].date.toString()
                    createdAt = mealList[j].createdAt.toString()
                    updatedAt = mealList[j].updatedAt.toString()
                    firstMeal += mealList[j].firstMeal!!.toDouble()
                    secondMeal += mealList[j].secondMeal!!.toDouble()
                    thirdMeal += mealList[j].thirdMeal!!.toDouble()
                    subTotalMeal += mealList[j].subTotalMeal!!.toDouble()
                }
            }
            newList.add(
                Meal(
                    "" + id,
                    "" + id,
                    "" + name,
                    "" + firstMeal,
                    "" + secondMeal,
                    "" + thirdMeal,
                    "" + subTotalMeal,
                    "" + date,
                    "" + createdAt,
                    "" + updatedAt
                )
            )
        }

        _sumMealsLiveData.postValue(NetworkResult.Success(newList.sortedBy { it.name }))

    }

    fun totalMeals(mealList: List<Meal>) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        if (mealList.isNotEmpty()) {
            var totalMeal = 0.0
            var totalFirstMeal = 0.0
            var totalSecondMeal = 0.0
            var totalThirdMeal = 0.0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal!!.toDouble()
                totalSecondMeal += meal.secondMeal!!.toDouble()
                totalThirdMeal += meal.thirdMeal!!.toDouble()
                totalMeal += meal.subTotalMeal!!.toDouble()
            }
            val newMeal = Meal(
                "",
                "",
                "",
                "" + df.format(totalFirstMeal).toString(),
                "" + df.format(totalSecondMeal).toString(),
                "" + df.format(totalThirdMeal).toString(),
                "" + df.format(totalMeal).toString(),
                "",
                "",
                "",
                "",
            )
            _totalMealsLiveData.postValue(NetworkResult.Success(newMeal))
        }
    }

    fun sumIndividualClosingMeals(
        mealList: List<Meal>,
        memberList: List<User>,
        expenseList: List<Expense>
    ) {

        val newList: MutableList<MealClosing> = ArrayList()
        for (i in memberList.indices) {
            var id = ""
            var name = ""
            var createdAt = ""
            var date = ""
            var updatedAt = ""
            var subTotalMeal = 0.0
            var totalExpense = 0.0

            for (j in mealList.indices) {
                if (mealList[j].memberId.toString() == memberList.toTypedArray()[i].id) {
                    id = mealList[j].memberId.toString()
                    name = mealList[j].name.toString()
                    date = mealList[j].date.toString()
                    createdAt = mealList[j].createdAt.toString()
                    updatedAt = mealList[j].updatedAt.toString()
                    subTotalMeal += mealList[j].subTotalMeal!!.toDouble()
                }
            }

            //sum expense
            if (expenseList.isNotEmpty()) {
                for (expenditure in expenseList) {
                    if (expenditure.memberId == memberList.toTypedArray()[i].id) {
                        totalExpense += expenditure.totalCost!!.toDouble();
                    }
                }
            }
            newList.add(
                MealClosing(
                    "" + id,
                    "" + id,
                    "" + name,
                    "" + subTotalMeal,
                    "" + totalExpense,
                    "" + date,
                    "" + createdAt,
                    "" + updatedAt
                )
            )
        }

        _sumMealsClosingLiveData.postValue(NetworkResult.Success(newList.sortedBy { it.name }))

    }

    fun totalExpenseClosingMeals(mealList: List<MealClosing>) {

        val newList: MutableList<ExpenseClosing> = ArrayList()
        try {
            if (mealList.isNotEmpty()) {
                var id: String
                var memberId: String
                var name: String
                var totalMeal = 0.0
                var totalExpense = 0.0
                for (meal in mealList) {
                    id = meal.id.toString()
                    memberId = meal.memberId.toString()
                    name = meal.name.toString()
                    totalMeal += meal.totalMeal!!.toDouble()
                    totalExpense += meal.totalExpense!!.toDouble()

                    newList.add(
                        ExpenseClosing(
                            "" + id,
                            "" + memberId,
                            "" + name,
                            "" + totalMeal,
                            "" + totalExpense,
                            "" + id,
                            "" + id
                        )
                    )
                }
                val mealRate: Double
                if (totalMeal > 0) {
                    mealRate = (totalExpense / totalMeal)
                    val df = DecimalFormat("#.##")
                    df.roundingMode = RoundingMode.UP
                    val rate:String = df.format(totalMeal) +"-"+df.format(totalExpense)+"-"+df.format(mealRate)
                    _mealRateLiveData.postValue(NetworkResult.Success(rate))
                }
            }
                _totalExpenseLiveData.postValue(NetworkResult.Success(mealList.sortedBy { it.name }))

            } catch (e: Exception) {
                Log.e(TAG, "Exception: ${e.message}")
            }
        }
    }
//309