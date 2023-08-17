package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.ExpenditureListener
import com.therishideveloper.bachelorpoint.model.Expense

class AddExpenditureAdapter(private var listener: ExpenditureListener, private val expenditureList: List<Expense>) :
    RecyclerView.Adapter<AddExpenditureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_expenditure, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val expenditure = expenditureList[position]

        holder.dateTv.text = expenditure.date
        holder.nameTv.text = expenditure.memberName
        holder.amountTv.text = expenditure.totalCost

        listener.onChangeExpenditure(expenditureList)

    }

    override fun getItemCount(): Int {
        return expenditureList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val amountTv: TextView = itemView.findViewById(R.id.amountTv)
    }


}