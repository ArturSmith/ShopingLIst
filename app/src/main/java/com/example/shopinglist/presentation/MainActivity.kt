package com.example.shopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.R
import com.example.shopinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupRecyclerView()
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.shopList.observe(this){
            recyclerAdapter.shopList = it
        }
    }

    fun setupRecyclerView() {
        with(recycler) {
            adapter = recyclerAdapter
            recycledViewPool.setMaxRecycledViews(RecyclerAdapter.ENABLED_VIEW, RecyclerAdapter.MAX_POOL)
            recycledViewPool.setMaxRecycledViews(RecyclerAdapter.DISABLED_VIEW, RecyclerAdapter.MAX_POOL)
        }
    }


    fun initViews() {
        recycler = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerAdapter()
    }
}