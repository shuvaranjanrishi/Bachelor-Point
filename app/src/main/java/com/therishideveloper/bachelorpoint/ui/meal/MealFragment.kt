package com.therishideveloper.bachelorpoint.ui.meal

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.adapter.MealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.listener.MyDateAndDay
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.utils.MyCalender

class MealFragment : Fragment(),MealListener {

    private val TAG = "MealFragment"

    private var _binding: FragmentMealBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MealViewModel by viewModels()
    private lateinit var session: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMealBinding.inflate(inflater, container, false)
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = (MyCalender.dayToday + " " + MyCalender.currentDate)

        binding.dateTv.setOnClickListener {
            MyCalender.pickDateAndDay(activity, object : MyDateAndDay{
                override fun onPickDateAndDay(date: String?, day: String?) {
                    binding.dateTv.text = (day+" "+date)
                }
            })
        }

        val database: DatabaseReference =
            Firebase.database.reference.child("Bachelor Point").child("Users")
        val accountId = session.getString("ACCOUNT_ID", "").toString()

        val listener = this
        Log.d(TAG, "onDataChange: accountId: $accountId")

        database.child(accountId).child("Meal").orderByChild("date").equalTo(MyCalender.currentDate)
                .addListenerForSingleValueEvent(
                    object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val mealList: MutableList<Meal> = mutableListOf()
                            for (ds in dataSnapshot.children) {
                                val meal: Meal? = ds.getValue(Meal::class.java)
                                mealList.add(meal!!)
                            }
                            Log.d(TAG, "onDataChange: mealList: $mealList")
                            val adapter = MealAdapter(listener, mealList)
                            binding.recyclerView.adapter = adapter
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(TAG, "DatabaseError", error.toException())
                        }
                    }
                )
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        Log.d("TAG", "mealList.size: " + mealList.size.toString())
        if (mealList.isNotEmpty()) {
            var totalMeal = 0
            var totalFirstMeal = 0
            var totalSecondMeal = 0
            var totalThirdMeal = 0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal!!.toInt();
                totalSecondMeal += meal.secondMeal!!.toInt();
                totalThirdMeal += meal.thirdMeal!!.toInt();
                totalMeal += meal.subTotalMeal!!.toInt();

                binding.totalFirstMealTv.text = totalFirstMeal.toString()
                binding.totalSecondMealTv.text = totalSecondMeal.toString()
                binding.totalThirdMealTv.text = totalThirdMeal.toString()
                binding.totalMealTv.text = totalMeal.toString()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}