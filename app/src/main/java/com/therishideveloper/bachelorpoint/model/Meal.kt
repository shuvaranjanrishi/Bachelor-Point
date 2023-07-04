package com.therishideveloper.bachelorpoint.model

data class Meal(
    var id: String,
    var memberId: String,
    var name: String,
    var firstMeal: String,
    var secondMeal: String,
    var thirdMeal: String,
    var subTotalMeal: String,
    var createdAt: String,
    var updatedAt: String
)
