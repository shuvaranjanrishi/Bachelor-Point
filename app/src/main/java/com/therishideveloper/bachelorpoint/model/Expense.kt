package com.therishideveloper.bachelorpoint.model

data class Expense(
    var id: String? = null,
    var uid: String? = null,
    var date: String? = null,
    var monthAndYear: String? = null,
    var memberId: String? = null,
    var memberName: String? = null,
    var totalCost: String? = null,
    var descripiton: String? = null,
    var createdAt: String? = null,
    var updatedAt: String? = null
)
