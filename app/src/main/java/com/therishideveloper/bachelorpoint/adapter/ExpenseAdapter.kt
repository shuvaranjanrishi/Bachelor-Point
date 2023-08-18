package com.therishideveloper.bachelorpoint.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.ExpenseListener
import com.therishideveloper.bachelorpoint.model.Expense

class ExpenseAdapter(
    private var listener: ExpenseListener,
    private val expenseList: List<Expense>
) :
    RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_expenditure, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val expense = expenseList[position]

        holder.dateTv.text = expense.date
        holder.nameTv.text = expense.memberName
        holder.amountTv.text = expense.totalCost

        listener.onChangeExpense(expenseList)

        if (expenseList.isNotEmpty()) {
            var totalCost = 0
            if (expense in expenseList) {
                totalCost += expense.totalCost!!.toInt();
            }
            Log.d("TAG", "totalCost: $totalCost")
        }

        val context: Context = holder.itemView.context
        holder.itemView.setOnClickListener {

            val memberId = expense.memberId;
            var totalCost = 0
            if (expenseList.isNotEmpty()) {
                for (expenditure in expenseList) {
                    if (expenditure.memberId == memberId) {
                        totalCost += expenditure.totalCost!!.toInt();
                    }
                }
            }

            val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
            val nullParent: ViewGroup? = null
            val view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_dialog, nullParent)

            val dateTv = view.findViewById<TextView>(R.id.dateTv)
            val nameTv = view.findViewById<TextView>(R.id.nameTv)
            val inTotalAmountTv = view.findViewById<TextView>(R.id.inTotalAmountTv)
            val totalAmountTv = view.findViewById<TextView>(R.id.totalAmountTv)
            val descriptionTv = view.findViewById<TextView>(R.id.descriptionTv)
            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

            dateTv.text = expense.date
            nameTv.text = expense.memberName
            inTotalAmountTv.text = totalCost.toString()
            totalAmountTv.text = expense.totalCost
            descriptionTv.text = expense.descripiton

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

    }

    override fun getItemCount(): Int {
        return expenseList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val dateTv: TextView = itemView.findViewById(R.id.dateTv)
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val amountTv: TextView = itemView.findViewById(R.id.amountTv)
    }


}