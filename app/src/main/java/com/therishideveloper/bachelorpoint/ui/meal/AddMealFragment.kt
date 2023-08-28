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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.adapter.AddMealAdapter
import com.therishideveloper.bachelorpoint.databinding.FragmentAddMealBinding
import com.therishideveloper.bachelorpoint.listener.MealListener
import com.therishideveloper.bachelorpoint.listener.MyDayMonthYear
import com.therishideveloper.bachelorpoint.model.Meal
import com.therishideveloper.bachelorpoint.model.User
import com.therishideveloper.bachelorpoint.ui.member.MemberViewModel
import com.therishideveloper.bachelorpoint.utils.MyCalender
import java.math.RoundingMode
import java.text.DecimalFormat

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
    private lateinit var dayName: String
    private lateinit var monthAndYear: String
    private lateinit var date: String

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

        setupDatePicker()

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Adding Meals ...")
        progressDialog.setCancelable(false)

        getMembers()

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
                        val firstMeal: Double =
                            if (binding.checkBox1.isChecked && meal.firstMeal.equals("1")) 0.5 else meal.firstMeal!!.toDouble()
                        val secondMeal: Double =
                            if (binding.checkBox2.isChecked && meal.secondMeal.equals("1")) 0.5 else meal.secondMeal!!.toDouble()
                        val thirdMeal: Double =
                            if (binding.checkBox3.isChecked && meal.thirdMeal.equals("1")) 0.5 else meal.thirdMeal!!.toDouble()
                        val subTotalMeal: Double = (firstMeal + secondMeal + thirdMeal)

                        addMeals(
                            firstMeal.toString(),
                            secondMeal.toString(),
                            thirdMeal.toString(),
                            subTotalMeal.toString(),
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

    private fun setupDatePicker() {
        binding.dateTv.text = MyCalender.currentDayAndDate
        monthAndYear = MyCalender.currentMonthYear
        date = MyCalender.currentDate
        binding.dateTv.setOnClickListener {
            MyCalender.pickDayMonthYear(activity, object : MyDayMonthYear {
                override fun onPickDayMonthYear(dayName: String?,monthYear: String?,date: String?) {
                    if (dayName != null) {
                        this@AddMealFragment.dayName = dayName
                        this@AddMealFragment.monthAndYear = monthAndYear
                        if (date != null) {
                            this@AddMealFragment.date = date
                        }
                        binding.dateTv.text = dayName + " " + date
                    }
                    Log.d(TAG, "monthAndYear: $monthAndYear")
                }
            }
            )
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
                ""+timestamp,
                ""+memberId,
                ""+name,
                ""+firstMeal,
                ""+secondMeal,
                ""+thirdMeal,
                ""+subTotalMeal,
                ""+monthAndYear,
                ""+date,
                ""+timestamp,
                ""+timestamp
            )
        database.child(accountId).child("Meal").child(monthAndYear).child(date).child(memberId)
            .setValue(meal)
    }

    override fun onChangeMeal(mealList: List<Meal>) {
        Log.d("TAG", "mealList.size: " + mealList.size.toString())
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        this.mealList = mealList
        if (mealList.isNotEmpty()) {
            var totalMeal = 0.0
            var totalFirstMeal = 0.0
            var totalSecondMeal = 0.0
            var totalThirdMeal = 0.0
            for (meal in mealList) {
                totalFirstMeal += meal.firstMeal!!.toDouble();
                totalSecondMeal += meal.secondMeal!!.toDouble();
                totalThirdMeal += meal.thirdMeal!!.toDouble();
                totalMeal += meal.subTotalMeal!!.toDouble();

                binding.totalFirstMealTv.text = df.format(totalFirstMeal).toString()
                binding.totalSecondMealTv.text = df.format(totalSecondMeal).toString()
                binding.totalThirdMealTv.text = df.format(totalThirdMeal).toString()
                binding.totalMealTv.text = df.format(totalMeal).toString()
            }
        }
    }

    private fun getMembers() {
        val session: SharedPreferences =
            requireContext().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
        val accountId = session.getString("ACCOUNT_ID", "").toString()
        database.child(accountId).child("Members")
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val memberList: MutableList<User> = mutableListOf()
                        for (ds in dataSnapshot.children) {
                            val user: User? = ds.getValue(User::class.java)
                            memberList.add(user!!)
                        }
                        setupMealList(memberList.sortedBy { it.name })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "DatabaseError", error.toException())
                    }
                }
            )
    }

    private fun setupMealList(it: List<User>) {
        val listener = this
//        memberViewModel.data.observe(viewLifecycleOwner) {
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
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}