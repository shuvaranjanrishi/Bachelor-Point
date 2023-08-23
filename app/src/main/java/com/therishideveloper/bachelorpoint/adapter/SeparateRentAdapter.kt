package com.therishideveloper.bachelorpoint.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.SeparateRentListener
import com.therishideveloper.bachelorpoint.model.SeparateRent
import java.math.RoundingMode
import java.text.DecimalFormat

class SeparateRentAdapter(
    private var listener: SeparateRentListener,
    private var perHeadCost: Double,
    private val rentList: List<SeparateRent>
) :
    RecyclerView.Adapter<SeparateRentAdapter.ViewHolder>() {

    private val TAG = "AddSeparateRentAdapter"
    private var totalSeparateRent = 0.0
    private var totalPerHeadCost = 0.0
    private var totalRent = 0.0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_separate_rent, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.UP

        val meal = rentList[position]

        try {
            val separateRent = meal.separateRent!!.toDouble()
            holder.nameTv.text = meal.name
            holder.rentTv.text = decimalFormat.format(separateRent)
            holder.perHeadCostTv.text = decimalFormat.format(perHeadCost).toString()

            val subTotalSeparateRent = (separateRent) + (perHeadCost)
            totalSeparateRent += subTotalSeparateRent
            totalPerHeadCost += perHeadCost
            totalRent += separateRent

            holder.totalAmountTv.text = decimalFormat.format(subTotalSeparateRent)
        } catch (e: Exception) {
            Log.e(TAG, "onBindViewHolder: ${e.localizedMessage}")
        }

        listener.onChangeSeparateRent(
            decimalFormat.format(totalRent),
            decimalFormat.format(totalPerHeadCost),
            decimalFormat.format(totalSeparateRent)
        )
    }

    override fun getItemCount(): Int {
        return rentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val rentTv: TextView = itemView.findViewById(R.id.rentTv)
        val perHeadCostTv: TextView = itemView.findViewById(R.id.perHeadCostTv)
        val totalAmountTv: TextView = itemView.findViewById(R.id.totalAmountTv)
    }


}