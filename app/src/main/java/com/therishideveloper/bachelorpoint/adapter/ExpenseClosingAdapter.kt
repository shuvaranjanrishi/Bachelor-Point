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
import java.math.RoundingMode
import java.text.DecimalFormat

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
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.UP

        val member = mealList[position]
        holder.nameTv.text = member.name
        try {
            val totalCostOfMeal = (member.totalMeal!!.toDouble() * mealRate.toDouble())
            val result = (member.totalExpense!!.toDouble() - totalCostOfMeal)
            m += totalCostOfMeal
            e += result
            holder.totalMealTv.text = df.format(totalCostOfMeal).toString()
            holder.totalExpenditureTv.text = df.format(result).toString()

        } catch (e: Exception) {
            Log.e("onBindViewHolder", "" + e.localizedMessage)
        }

        listener.onChangeExpense(df.format(m).toString(), df.format(e).toString())

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