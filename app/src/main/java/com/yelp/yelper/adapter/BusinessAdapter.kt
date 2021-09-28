package com.yelp.yelper.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.yelp_td.databinding.ListBusinessItemBinding
import com.yelp.yelper.model.Business

class BusinessAdapter : BaseAdapter() {

    private var listOfBusinesses: List<Business> = emptyList()

    fun setItems(list: List<Business>) {
        listOfBusinesses = list
    }

    override fun getCount(): Int = listOfBusinesses.size

    override fun getItem(p0: Int): Any = listOfBusinesses[p0]

    override fun getItemId(p0: Int): Long = p0.toLong()

    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val binding = ListBusinessItemBinding.inflate(LayoutInflater.from(p2?.context))
        val view = binding.root

        binding.apply {
            business = listOfBusinesses[p0]
            executePendingBindings()
        }

        return view
    }
}