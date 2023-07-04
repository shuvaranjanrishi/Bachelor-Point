package com.therishideveloper.bachelorpoint.adapter.spinner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.therishideveloper.bachelorpoint.R
import com.therishideveloper.bachelorpoint.model.Member

class MemberSpinnerAdapter(context: Context, memberList: List<Member>) :
    ArrayAdapter<Member>(context, 0, memberList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val member = getItem(position)!!
        val view = LayoutInflater.from(context).inflate(R.layout.item_spinner, parent, false)
        val nameTv = view.findViewById<TextView>(R.id.nameTv)
        nameTv.text = member.name
        return view
    }
}