package com.example.shopinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shopinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var recycler: RecyclerView
    private lateinit var recyclerAdapter: RecyclerAdapter
    private lateinit var buttonAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupRecyclerView()
        setupShopList()
        addItem()
        shopItemLongClickListener()
        editItem()
        onSwiped()

    }

    private fun setupShopList() {
        viewModel.shopList.observe(this) {
            recyclerAdapter.submitList(it)
        }
    }

    private fun addItem() {
        buttonAdd.setOnClickListener {
            val intent = ShopItemActivity.newIntentAddItem(this)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        with(recycler) {
            adapter = recyclerAdapter
            recycledViewPool.setMaxRecycledViews(RecyclerAdapter.ENABLED_VIEW, RecyclerAdapter.MAX_POOL)
            recycledViewPool.setMaxRecycledViews(RecyclerAdapter.DISABLED_VIEW, RecyclerAdapter.MAX_POOL)
        }
    }

    private fun onSwiped() {
        val callBack = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = recyclerAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(item)
            }
        }

        val itemTouchHelper = ItemTouchHelper(callBack)

        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun editItem() {
        recyclerAdapter.shopItemClickListener = {
            val intent = ShopItemActivity.newIntentEditItem(this, it.id)
            startActivity(intent)
        }
    }

    private fun shopItemLongClickListener() {
        recyclerAdapter.shopItemLongClickListener = {
            viewModel.editShopItem(it)
        }
    }


    fun initViews() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        recycler = findViewById(R.id.recyclerView)
        recyclerAdapter = RecyclerAdapter()
        buttonAdd = findViewById(R.id.buttonAdd)
    }
}