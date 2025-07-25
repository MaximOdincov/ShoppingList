package com.example.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputLayout

class EditShopListActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var countEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var textFieldName: TextInputLayout
    private lateinit var textFieldCount: TextInputLayout
    private lateinit var viewModel: EditShopActivityViewModel

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_shop_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = if (imeVisible) imeInsets.bottom else 0
            v.updatePadding(
                left = systemBars.left,
                top = systemBars.top,
                right = systemBars.right,
                bottom = maxOf(systemBars.bottom, imeHeight)
            )
            insets
        }
        parseIntent()
        initViews()
        viewModel = ViewModelProvider(this)[EditShopActivityViewModel::class.java]
        when(screenMode){
            EDIT_MODE ->{
                launchEditMode()
            }
            ADDING_MODE ->{
                launchAddingMode()
            }
        }
        observeViewModel()
        addTextChangeListeners()

    }

    private fun addTextChangeListeners() {
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputNameException()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        countEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.resetInputCountException()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.currentItem.observe(this) {
            nameEditText.setText(it.name)
            countEditText.setText(it.count.toString())
        }
        saveButton.setOnClickListener {
            viewModel.editItem(nameEditText.text?.toString(), countEditText.text?.toString())
        }
    }

    private fun launchAddingMode() {
        saveButton.setOnClickListener {
            viewModel.addItem(nameEditText.text?.toString(), countEditText.text?.toString())
        }
    }

    companion object{
        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_ID = "extra_id"
        private const val EDIT_MODE = "edit_mode"
        private const val ADDING_MODE = "adding_mode"
        private const val UNKNOWN_MODE = ""


        fun newIntentAdding(context: Context): Intent{
            val intent = Intent(context, EditShopListActivity::class.java)
            intent.putExtra(EXTRA_MODE, ADDING_MODE)
            return intent
        }
        fun newIntentEditing(context: Context, id: Int): Intent{
            val intent = Intent(context, EditShopListActivity::class.java)
            intent.putExtra(EXTRA_MODE, EDIT_MODE)
            intent.putExtra(EXTRA_ID, id)
            return intent
        }

    }

    private fun initViews(){
        nameEditText = findViewById(R.id.name_edit_text)
        countEditText = findViewById(R.id.count_edit_text)
        saveButton = findViewById(R.id.finish_edit_button)
        textFieldName = findViewById(R.id.textInputLayout)
        textFieldCount = findViewById(R.id.textInputLayout2)

    }

    private fun parseIntent(){
        if (!intent.hasExtra(EXTRA_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = intent.getStringExtra(EXTRA_MODE)
        if (mode != EDIT_MODE && mode != ADDING_MODE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(EXTRA_ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun observeViewModel() {
        viewModel.inputCountException.observe(this) {
            val message = if (it) {
                "ERROR"
            } else {
                null
            }
            countEditText.error = message
        }
        viewModel.inputNameException.observe(this) {
            val message = if (it) {
                "ERROR"
            } else {
                null
            }
            nameEditText.error = message
        }
        viewModel.isFinished.observe(this) {
            finish()
        }
    }

}