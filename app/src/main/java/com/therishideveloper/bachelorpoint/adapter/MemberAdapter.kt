package com.therishideveloper.bachelorpoint.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.model.User

class MemberAdapter(private val navController: NavController, private val memberList: List<User>) :
    RecyclerView.Adapter<MemberAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val member = memberList[position]

        holder.nameTv.text = member.name
        holder.phoneTv.text = member.phone

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("UID", member.uid) // Serializable Object
            navController.navigate(R.id.action_nav_member_to_member_details, bundle)
        }

    }

    override fun getItemCount(): Int {
        return memberList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val phoneTv: TextView = itemView.findViewById(R.id.phoneTv)
    }


}