package com.therishideveloper.bachelorpoint.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.model.Module
import com.therishideveloper.bachelorpoint.session.SessionManager

class ModuleAdapter(
    private val navController: NavController,
    private val memberList: List<Module>
) :
    RecyclerView.Adapter<ModuleAdapter.ViewHolder>() {

    private lateinit var session: SessionManager
    private lateinit var auth: FirebaseAuth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        session = SessionManager(parent.context)
        auth = Firebase.auth

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_module, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = memberList[position]

        holder.iconIv.setImageResource(member.icon)
        holder.nameTv.text = member.name

        holder.itemView.setOnClickListener {
            if (position == 0)
                navController.navigate(R.id.action_nav_home_to_nav_meal)
            if (position == 1)
                navController.navigate(R.id.action_nav_home_to_nav_expenditure)
            if (position == 2)
                navController.navigate(R.id.action_nav_home_to_nav_rent)
            if (position == 3)
                navController.navigate(R.id.action_nav_home_to_nav_closing)
            if (position == 4)
                navController.navigate(R.id.action_nav_home_to_nav_member)
            if(position==5)
                logout()
        }
    }

    private fun logout() {
        auth.signOut()
        session.deleteSession()
        navController.navigate(R.id.action_nav_home_to_nav_sign_in)
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val iconIv: ImageView = itemView.findViewById(R.id.iconIv)
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
    }
}