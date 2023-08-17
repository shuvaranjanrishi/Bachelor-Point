package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.ExpenseClosingListener
import com.therishideveloper.bachelorpoint.listener.MealClosingListener
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.ExpenseClosing
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing

class ExpenseClosingAdapter(private var listener: ExpenseClosingListener, private val mealList: List<ExpenseClosing>) :
    RecyclerView.Adapter<ExpenseClosingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal_closing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = mealList[position]

        holder.nameTv.text = member.name
        holder.totalMealTv.text = member.totalMeal
        holder.totalExpenditureTv.text = member.totalExpense

        listener.onChangeExpense(mealList)

    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val totalMealTv: TextView = itemView.findViewById(R.id.totalMealTv)
        val totalExpenditureTv: TextView = itemView.findViewById(R.id.totalExpenditureTv)
    }


}