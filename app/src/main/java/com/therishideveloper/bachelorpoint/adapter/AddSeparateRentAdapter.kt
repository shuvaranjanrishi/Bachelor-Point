package com.therishideveloper.bachelorpoint.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.AddRentListener
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.SeparateRent

class AddSeparateRentAdapter(private var listener: AddRentListener, private val rentList: List<SeparateRent>) :
    RecyclerView.Adapter<AddSeparateRentAdapter.ViewHolder>() {

    private val TAG = "AddSeparateRentAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rent_input, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        rentList.sortedBy { it.name }

        val meal = rentList[position]

        holder.nameTv.text = meal.name
        holder.separateRentEt.setText(meal.separateRent)

        holder.separateRentEt.addTextChangedListener(
            onTextChanged = { _, _, _, _ ->
                try {
                    val inputs = holder.separateRentEt.text.toString().trim().toDouble()
                    if (inputs != 0.0) {
                        rentList[position].separateRent = inputs.toString()
                        listener.onChangeRent(rentList)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Exception: ${e.message}")
                }
            }
        )
    }

    override fun getItemCount(): Int {
        return rentList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val separateRentEt: EditText = itemView.findViewById(R.id.separateRentEt)
    }


}