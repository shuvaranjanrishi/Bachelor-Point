package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal

class MealAdapter(private var listener: MealListener, private val mealList: List<Meal>) :
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = mealList[position]

        holder.nameTv.text = member.name
        holder.firstMealTv.text = member.firstMeal
        holder.secondMealTv.text = member.secondMeal
        holder.thirdMealTv.text = member.thirdMeal
        holder.subTotalMealTv.text = member.subTotalMeal

        listener.onChangeMeal(mealList)

    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val firstMealTv: TextView = itemView.findViewById(R.id.firstMealTv)
        val secondMealTv: TextView = itemView.findViewById(R.id.secondMealTv)
        val thirdMealTv: TextView = itemView.findViewById(R.id.thirdMealTv)
        val subTotalMealTv: TextView = itemView.findViewById(R.id.subTotalMealTv)
    }


}