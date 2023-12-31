package com.therishideveloper.bachelorpoint.model

import com.therishideveloper.bachelorpoint.listener.MyMonthAndYear

data class Meal(
    var id: String? = null,
    var memberId: String? = null,
    var name: String? = null,
    var firstMeal: String? = null,
    var secondMeal: String? = null,
    var thirdMeal: String? = null,
    var subTotalMeal: String? = null,
    var monthAndYear: String? = null,
    var date: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)
