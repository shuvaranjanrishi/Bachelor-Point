package com.therishideveloper.bachelorpoint.model

data class Expenditure(
    var id: String,
    var date: String,
    var memberId: String,
    var name: String,
    var totalCost: String,
    var createdAt: String,
    var updatedAt: String
)
