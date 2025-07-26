import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.presentation.EditShopActivityViewModel
import com.example.shoppinglist.presentation.EditShopListActivity
import com.google.android.material.textfield.TextInputLayout

class EditShopItemFragment(
    private val screenMode: String = UNKNOWN_MODE,
    private val shopItemId: Int = ShopItem.UNDEFINED_ID
): Fragment(){

    private lateinit var nameEditText: EditText
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
        return inflater.inflate(R.layout.fragment_edit_shop_item, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parseIntent()
        initViews(view)
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

    }

    private fun parseIntent(){
        if (screenMode== UNKNOWN_MODE) {
            throw RuntimeException("Param screen mode is absent or unknown")
        }

        if (screenMode == EDIT_MODE) {
            if (shopItemId == ShopItem.UNDEFINED_ID) {
                throw RuntimeException("Param shop item id is absent")
            }
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
            requireActivity().finish()
        }
    }

    companion object{
        private const val EXTRA_MODE = "extra_mode"
        private const val EXTRA_ID = "extra_id"
        private const val EDIT_MODE = "edit_mode"
        private const val ADDING_MODE = "adding_mode"
        private const val UNKNOWN_MODE = ""


//        fun newIntentAdding(context: Context): Intent {
//            val intent = Intent(context, EditShopListActivity::class.java)
//            intent.putExtra(EXTRA_MODE, ADDING_MODE)
//            return intent
//        }
//        fun newIntentEditing(context: Context, id: Int): Intent {
//            val intent = Intent(context, EditShopListActivity::class.java)
//            intent.putExtra(EXTRA_MODE, EDIT_MODE)
//            intent.putExtra(EXTRA_ID, id)
//            return intent
//        }

    }
}