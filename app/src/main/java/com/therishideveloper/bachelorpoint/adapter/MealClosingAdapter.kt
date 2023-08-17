package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.MealClosingListener
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.MealClosing

class MealClosingAdapter(private var listener: MealClosingListener, private val mealList: List<MealClosing>) :
    RecyclerView.Adapter<MealClosingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal_closing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = mealList[position]

        holder.nameTv.text = member.name
//        holder.firstMealTv.text = member.firstMeal
//        holder.secondMealTv.text = member.secondMeal
        holder.totalMealTv.text = member.subTotalMeal
        holder.totalExpenditureTv.text = member.totalExpenditure

        listener.onChangeMeal(mealList)

    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
//        val firstMealTv: TextView = itemView.findViewById(R.id.firstMealTv)
//        val secondMealTv: TextView = itemView.findViewById(R.id.secondMealTv)
//        val thirdMealTv: TextView = itemView.findViewById(R.id.thirdMealTv)
        val totalMealTv: TextView = itemView.findViewById(R.id.totalMealTv)
        val totalExpenditureTv: TextView = itemView.findViewById(R.id.totalExpenditureTv)
    }


}