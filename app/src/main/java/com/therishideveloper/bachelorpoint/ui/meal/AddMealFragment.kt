package com.therishideveloper.bachelorpoint.ui.meal

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.AddMealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender

class AddMealFragment : Fragment(), MealListener {

    private val TAG = "AddMealFragment"

    private var _binding: FragmentAddMealBinding? = null
    private val binding get() = _binding!!

    private val memberViewModel: MemberViewModel by viewModels()
    private lateinit var mealList: List<Meal>
    private lateinit var database: DatabaseReference
    private lateinit var session: SharedPreferences
    private lateinit var auth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddMealBinding.inflate(inflater, container, false)

        auth = Firebase.auth
        database = Firebase.database.reference.child(getString(R.string.app_name)).child("Users")
        session = requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateTv.text = MyCalender.currentDayAndDate
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Adding Meals ...")
        progressDialog.setCancelable(false)

        val listener = this
        memberViewModel.data.observe(viewLifecycleOwner) {
            Log.d("TAG", "mealList.size: " + it.size.toString())
            val mealList: MutableList<Meal> = mutableListOf()
            for (user in it) {
                val meal = Meal(
                    "" + System.currentTimeMillis(),
                    "" + user.id,
                    "" + user.name,
                    "0",
                    "0",
                    "0",
                    "0",
                    "" + System.currentTimeMillis(),
                    "" + System.currentTimeMillis()
                )
                mealList.add(meal)
                Log.d(TAG, "onDataChange: mealList: $mealList")
            }
            this.mealList = mealList
            val adapter = AddMealAdapter(listener,mealList)
            binding.recyclerView.adapter = adapter
        }

        binding.saveBtn.setOnClickListener {
            progressDialog.show()

            if (mealList.isNotEmpty()) {
                if (binding.totalMealTv.text.equals("0")) {
                    Toast.makeText(
                        context,
                        "Zero Meal will not Added",
                        Toast.LENGTH_SHORT,
                    ).show()
                    progressDialog.dismiss()
                } else {
                    for (meal in mealList) {
                        addMeals(
                            meal.firstMeal!!,
                            meal.secondMeal!!,
                            meal.thirdMeal!!,
                            meal.subTotalMeal!!,
                            meal.memberId!!,
                            meal.name!!
                        )
                    }
                    progressDialog.dismiss()
                    Toast.makeText(
                        context,
                        "Meal Added Successful",
                        Toast.LENGTH_SHORT,
                    ).show()
                }

            }
        }

    }

    private fun addMeals(
        firstMeal: String,
        secondMeal: String,
        thirdMeal: String,
        subTotalMeal: String,
        memberId: String,
        name: String,
    ) {
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        val timestamp = "" + System.currentTimeMillis()
        val meal =
            Meal(
                timestamp,
                memberId,
                name,
                firstMeal,
                secondMeal,
                thirdMeal,
                subTotalMeal,
                MyCalender.currentDate,
                timestamp,
                timestamp
            )
        database.child(accountId).child("Meal").child(MyCalender.currentDate).child(memberId)
            .setValue(meal)
//        database.child(accountId).child("Meal").child(memberId).child(MyCalender.currentDate).setValue(meal)
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        Log.d("TAG", "mealList.size: " + mealList.size.toString())
        this.mealList = mealList
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