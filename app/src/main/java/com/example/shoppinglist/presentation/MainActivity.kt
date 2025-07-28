package com.example.shoppinglist.presentation

import EditShopItemFragment
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), EditShopItemFragment.OnEditingFinishedListener {
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        shopItemContainer = findViewById(R.id.shop_item_container)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        setUpRecyclerView()
        setupButton()
        viewModel.shopList.observe(this){
            shopListAdapter.submitList(it)
        }

    }

    private fun setUpRecyclerView(){
        shopListAdapter = ShopListAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        with(recyclerView){
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
            recycledViewPool.setMaxRecycledViews(ShopListAdapter.VIEW_TYPE_DISABLED, ShopListAdapter.MAX_POOL_SIZE)
            setupSwipeListener(this)
            setupLongClickListener()
            setupClickListener()
        }
    }

    private fun setupSwipeListener(recyclerView: RecyclerView) {
        val callback = object : ItemTouchHelper.SimpleCallback(
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
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun setupLongClickListener() {
        shopListAdapter.shopItemLongClickListener = {
            viewModel.editItem(it)
        }
    }

    private fun setupClickListener() {
        shopListAdapter.shopItemClickListener = {
            if(shopItemContainer == null){
                val intent = EditShopListActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            }
            else{
                val fragment = EditShopItemFragment.newInstanceEdit(it.id)
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.shop_item_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }

        }
    }

    private fun setupButton(){
        val button = findViewById<FloatingActionButton>(R.id.floatingActionButton)
        button.setOnClickListener{
            if(shopItemContainer == null){
                val intent = EditShopListActivity.newIntentAddItem(this)
                startActivity(intent)
            }
            else{
                supportFragmentManager.popBackStack()
                val fragment = EditShopItemFragment.newInstanceAdd()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.shop_item_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
    }

}