package com.example.shopinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.shopinglist.R
import com.google.android.material.textfield.TextInputLayout
import java.lang.RuntimeException

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var et_name: EditText
    private lateinit var et_count: EditText
    private lateinit var buttonSave: Button

    private var mode: String? = UNKNOWN_MODE
    private var itemID: Int = DEFAULT_ITEM_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        checkIntent()
        initViews()

        when (mode) {
            EXTRA_MODE_EDIT -> launchEditMode()
            EXTRA_MODE_ADD -> launchAddMode()
        }

        addTextChangeListeners()

        setupErrors()

        finishActivity()

    }

    private fun finishActivity() {
        viewModel.shouldFinishActivity.observe(this) {
            finish()
        }
    }

    private fun setupErrors() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                "Enter name"
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                "Enter count"
            } else {
                null
            }
            tilCount.error = message
        }
    }

    private fun addTextChangeListeners() {
        et_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputName()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        et_count.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.resetErrorInputCount()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(itemID)

        viewModel.shopItemGetter.observe(this) {
            et_name.setText(it.name)
            et_count.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(et_name?.text.toString(), et_count?.text.toString())
        }
    }

    private fun launchAddMode() {
        buttonSave.setOnClickListener {
            viewModel.addShopItem(et_name?.text.toString(), et_count?.text.toString())
        }
    }


    private fun checkIntent() {

        if (!intent.hasExtra(EXTRA_ACTIVITY_MODE)) {
            throw RuntimeException("Intent has not extra")
        }

        if (mode != EXTRA_MODE_EDIT && mode != EXTRA_MODE_ADD && mode == null) {
            throw RuntimeException("Unknown activity mode $mode")
        }

        mode = intent.getStringExtra(EXTRA_ACTIVITY_MODE)

        if (mode == EXTRA_MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_SHOP_ITEM_ID)) {
                throw RuntimeException("Item id doesn't exist")
            }
            itemID = intent.getIntExtra(EXTRA_SHOP_ITEM_ID, DEFAULT_ITEM_ID)
        }
    }


    private fun initViews() {
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
        et_name = findViewById(R.id.et_name)
        et_count = findViewById(R.id.et_count)
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        buttonSave = findViewById(R.id.buttonSave)
    }

    companion object {
        private const val EXTRA_ACTIVITY_MODE = "extra_activity_mode"
        private const val EXTRA_MODE_EDIT = "extra_mode_edit"
        private const val EXTRA_MODE_ADD = "extra_mode_add"
        private const val EXTRA_SHOP_ITEM_ID = "extra_shop_item_id"
        private const val DEFAULT_ITEM_ID = -1
        private const val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_ACTIVITY_MODE, EXTRA_MODE_ADD)
            return intent
        }

        fun newIntentEditItem(context: Context, shopItemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_ACTIVITY_MODE, EXTRA_MODE_EDIT)
            intent.putExtra(EXTRA_SHOP_ITEM_ID, shopItemId)
            return intent
        }
    }
}
