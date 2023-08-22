package com.therishideveloper.bachelorpoint.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal

class AddMealAdapter(private var listener: MealListener, private val mealList: List<Meal>) :
    RecyclerView.Adapter<AddMealAdapter.ViewHolder>() {

    private val TAG = "AddMealAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_meal_input, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = mealList[position]

        holder.nameTv.text = meal.name
        holder.firstMealTv.text = meal.firstMeal
        holder.secondMealTv.text = meal.secondMeal
        holder.thirdMealTv.text = meal.thirdMeal

        var firstMeal: Int = holder.firstMealTv.text.toString().trim().toInt()
        var secondMeal: Int = holder.secondMealTv.text.toString().trim().toInt()
        var thirdMeal: Int = holder.thirdMealTv.text.toString().trim().toInt()
        var subTotalMeal = (firstMeal + secondMeal + thirdMeal)
        holder.subTotalMealTv.text = subTotalMeal.toString()
        Log.d(TAG, "$firstMeal $secondMeal $thirdMeal $subTotalMeal");

        holder.fPlusBtn.setOnClickListener {
            if (firstMeal > -1) {
                firstMeal++
                holder.firstMealTv.text = firstMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].firstMeal = firstMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
        holder.fMinusBtn.setOnClickListener {
            if (firstMeal > 0) {
                firstMeal--
                holder.firstMealTv.text = firstMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].firstMeal = firstMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
        holder.sPlusBtn.setOnClickListener {
            if (secondMeal > -1) {
                secondMeal++
                holder.secondMealTv.text = secondMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].secondMeal = secondMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
        holder.sMinusBtn.setOnClickListener {
            if (secondMeal > 0) {
                secondMeal--
                holder.secondMealTv.text = secondMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].secondMeal = secondMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
        holder.tPlusBtn.setOnClickListener {
            if (thirdMeal > -1) {
                thirdMeal++
                holder.thirdMealTv.text = thirdMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].thirdMeal = thirdMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
        holder.tMinusBtn.setOnClickListener {
            if (thirdMeal > 0) {
                thirdMeal--
                holder.thirdMealTv.text = thirdMeal.toString()
                subTotalMeal = (firstMeal + secondMeal + thirdMeal)
                holder.subTotalMealTv.text = subTotalMeal.toString()

                mealList[position].subTotalMeal = subTotalMeal.toString()
                mealList[position].thirdMeal = thirdMeal.toString()
                listener.onChangeMeal(mealList)
            }
        }
    }

    override fun getItemCount(): Int {
        return mealList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val firstMealTv: TextView = itemView.findViewById(R.id.firstMealTv)
        val secondMealTv: TextView = itemView.findViewById(R.id.secondMealTv)
        val thirdMealTv: TextView = itemView.findViewById(R.id.thirdMealTv)
        val fPlusBtn: ImageView = itemView.findViewById(R.id.fPlusBtn)
        val fMinusBtn: ImageView = itemView.findViewById(R.id.fMinusBtn)
        val sPlusBtn: ImageView = itemView.findViewById(R.id.sPlusBtn)
        val sMinusBtn: ImageView = itemView.findViewById(R.id.sMinusBtn)
        val tPlusBtn: ImageView = itemView.findViewById(R.id.tPlusBtn)
        val tMinusBtn: ImageView = itemView.findViewById(R.id.tMinusBtn)
        val subTotalMealTv: TextView = itemView.findViewById(R.id.subTotalMealTv)
    }


}