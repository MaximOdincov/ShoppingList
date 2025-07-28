import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Visibility
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.EditShopActivityViewModel
import com.example.shoppinglist.presentation.EditShopListActivity
import com.google.android.material.textfield.TextInputLayout

class EditShopItemFragment(): Fragment(){

    private var screenMode = EditShopListActivity.UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID
    private lateinit var onEditingFinishedListener: OnEditingFinishedListener
    private var shouldShowAppTitle = false

    private lateinit var nameEditText: EditText
    private lateinit var appTitle: TextView
    private lateinit var countEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var textFieldName: TextInputLayout
    private lateinit var textFieldCount: TextInputLayout
    private lateinit var viewModel: EditShopActivityViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("LifeCycle", "OnCreateView")
        return inflater.inflate(R.layout.fragment_edit_shop_item, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnEditingFinishedListener){
            onEditingFinishedListener = context
        }
        else throw RuntimeException("Activity must implement OnEditingFinishedListener")

        if(context is ShouldShowAppTitle){
            shouldShowAppTitle = context.shouldShowAppTitle()
        }
        Log.d("LifeCycle", "OnCreateAttach")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("LifeCycle", "OnViewCreated")
        initViews(view)
        viewModel = ViewModelProvider(this)[EditShopActivityViewModel::class.java]
        if(savedInstanceState == null) {
            when (screenMode) {
                EDIT_MODE -> {
                    launchEditMode()
                }

                ADDING_MODE -> {
                    launchAddingMode()
                }
            }
            observeViewModel()
            addTextChangeListeners()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LifeCycle", "OnCreate")
        parseIntent()
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "OnStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "OnResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "OnPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle", "OnStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("LifeCycle", "OnDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "OnDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("LifeCycle", "OnDetach")
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

    @SuppressLint("SetTextI18n")
    private fun launchEditMode() {
        viewModel.getShopItem(shopItemId)
        viewModel.currentItem.observe(viewLifecycleOwner) {
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


    private fun initViews(view: View){
        nameEditText = view.findViewById(R.id.name_edit_text)
        countEditText = view.findViewById(R.id.count_edit_text)
        saveButton = view.findViewById(R.id.finish_edit_button)
        textFieldName = view.findViewById(R.id.textInputLayout)
        textFieldCount = view.findViewById(R.id.textInputLayout2)
        appTitle = view.findViewById<TextView?>(R.id.appTitle).apply {
            if(!shouldShowAppTitle) text = ""
        }

    }

    private fun parseIntent(){
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Param screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != EditShopListActivity.EDIT_MODE && mode != EditShopListActivity.ADDING_MODE) {
            throw RuntimeException("Unknown screen mode $mode")
        }
        screenMode = mode
        if (screenMode == EditShopListActivity.EDIT_MODE) {
            if (!args.containsKey(ID)) {
                throw RuntimeException("Param shop item id is absent")
            }
            shopItemId = args.getInt(ID)
        }
    }

    private fun observeViewModel() {
        viewModel.inputCountException.observe(viewLifecycleOwner) {
            val message = if (it) {
                "ERROR"
            } else {
                null
            }
            countEditText.error = message
        }
        viewModel.inputNameException.observe(viewLifecycleOwner) {
            val message = if (it) {
                "ERROR"
            } else {
                null
            }
            nameEditText.error = message
        }
        viewModel.isFinished.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    interface OnEditingFinishedListener{
        fun onEditingFinished()
    }

    interface ShouldShowAppTitle{
        fun shouldShowAppTitle(): Boolean
    }

    companion object{
        private const val EDIT_MODE = "edit_mode"
        private const val ADDING_MODE = "adding_mode"
        private const val UNKNOWN_MODE = ""
        private const val SCREEN_MODE = "screen_mode"
        private const val ID = "id"

        fun newInstanceAdd(): Fragment{
            val args = Bundle().apply {
                putString(SCREEN_MODE, ADDING_MODE)
            }
            EditShopItemFragment().apply {
                arguments = args
                return this
            }
        }

        fun newInstanceEdit(id: Int): Fragment{
            val args = Bundle().apply {
                putString(SCREEN_MODE, EDIT_MODE)
                putInt(ID, id)
            }
            EditShopItemFragment().apply {
                arguments = args
                return this
            }
        }
    }
}