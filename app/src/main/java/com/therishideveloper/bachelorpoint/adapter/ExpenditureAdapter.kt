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
import com.therishideveloper.bachelorpoint.listener.ExpenditureListener
import com.therishideveloper.bachelorpoint.model.Expenditure
import kotlin.math.exp

class ExpenditureAdapter(
    private var listener: ExpenditureListener,
    private val expenditureList: List<Expenditure>
) :
    RecyclerView.Adapter<ExpenditureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_expenditure, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val expenditure = expenditureList[position]

        holder.dateTv.text = expenditure.date
        holder.nameTv.text = expenditure.name
        holder.amountTv.text = expenditure.totalCost

        listener.onChangeExpenditure(expenditureList)

        if (expenditureList.isNotEmpty()) {
            var totalCost = 0
            if (expenditure in expenditureList) {
                totalCost += expenditure.totalCost.toInt();
            }
            Log.d("TAG", "totalCost: $totalCost")
        }

        val context: Context = holder.itemView.context
        holder.itemView.setOnClickListener {

            val memberId = expenditure.memberId;
            var totalCost = 0
            if (expenditureList.isNotEmpty()) {
                for (expenditure in expenditureList) {
                    if (expenditure.memberId == memberId) {
                        totalCost += expenditure.totalCost.toInt();
                    }
                }
            }

            val dialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
            val nullParent: ViewGroup? = null;
            val view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_dialog, nullParent)

            val dateTv = view.findViewById<TextView>(R.id.dateTv)
            val nameTv = view.findViewById<TextView>(R.id.nameTv)
            val inTotalAmountTv = view.findViewById<TextView>(R.id.inTotalAmountTv)
            val totalAmountTv = view.findViewById<TextView>(R.id.totalAmountTv)
            val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)

            dateTv.text = expenditure.date
            nameTv.text = expenditure.name
            inTotalAmountTv.text = totalCost.toString()
            totalAmountTv.text = expenditure.totalCost

            btnClose.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }

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