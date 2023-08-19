package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal
import java.math.RoundingMode
import java.text.DecimalFormat

class MealAdapter(private var listener: MealListener, private val mealList: List<Meal>) :
    RecyclerView.Adapter<MealAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP
        mealList.sortedBy { it.name }

        val member = mealList[position]

        holder.nameTv.text = member.name!!
        holder.firstMealTv.text = df.format(member.firstMeal!!.toDouble()).toString()
        holder.secondMealTv.text = df.format(member.secondMeal!!.toDouble()).toString()
        holder.thirdMealTv.text = df.format(member.thirdMeal!!.toDouble()).toString()
        holder.subTotalMealTv.text = df.format(member.subTotalMeal!!.toDouble()).toString()

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