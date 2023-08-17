package com.therishideveloper.bachelorpoint.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.ExpenseClosingListener
import com.therishideveloper.bachelorpoint.model.MealClosing

class ExpenseClosingAdapter(
    private var listener: ExpenseClosingListener,
    private var mealRate: String,
    private val mealList: List<MealClosing>
) :
    RecyclerView.Adapter<ExpenseClosingAdapter.ViewHolder>() {
    var m = 0.0
    var e = 0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal_closing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = mealList[position]
        holder.nameTv.text = member.name
        try {
            val totalCostOfMeal = (member.totalMeal!!.toInt() * mealRate.toDouble())
            val result = (member.totalExpense!!.toInt() - totalCostOfMeal)
            m += totalCostOfMeal
            e += result
            holder.totalMealTv.text = totalCostOfMeal.toString()
            holder.totalExpenditureTv.text = result.toString()

        } catch (e: Exception) {
            Log.e("onBindViewHolder", "" + e.localizedMessage)
        }

        listener.onChangeExpense(m.toString(), e.toString())

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