package com.example.shoppinglist.edit_list

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.adapters.ShoppingListProductsAdapter
import com.example.shoppinglist.adapters.ShoppingListsAdapter
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class ListEdit : Fragment() {
    private lateinit var viewModel: ShoppingListsViewModel
    private lateinit var productViewModel: ProductsViewModel
    private var communicator: Communicator?= null

    private lateinit var chosenShoppingList: ShoppingList
    private lateinit var shoppingListProducts: LiveData<List<ListProduct>>

    private lateinit var nameText: TextView
    private lateinit var datePicker: DatePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ShoppingListsViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.list_edit_fragment, container, false)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.nameText = view.findViewById<TextView>(R.id.list_edit_name_content)
        this.datePicker = view.findViewById<DatePicker>(R.id.list_edit_date_picker)

        this.chosenShoppingList = viewModel.getListById(communicator?.ShoppingListId!!.value!!)
        this.shoppingListProducts = viewModel.getProductsForShoppingList(this.chosenShoppingList.shoppingListId)

        val date: Date = this.chosenShoppingList.date
        val dateValue = Calendar.getInstance(TimeZone.getTimeZone("Europe/Poland"))
        dateValue.time = date

        this.datePicker.init(
            dateValue.get(Calendar.YEAR),
            dateValue.get(Calendar.MONTH),
            dateValue.get(Calendar.DAY_OF_MONTH),
            DatePicker.OnDateChangedListener { _, _, _, _ ->  }
        )

        this.nameText.text = this.chosenShoppingList.name

        val shoppingListProductsAdapter = ShoppingListProductsAdapter(shoppingListProducts, productViewModel, communicator!!)

        shoppingListProducts.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer<List<ListProduct>> { shoppingListProductsAdapter.notifyDataSetChanged() }
        )

        val layoutManager= LinearLayoutManager(view.context)
        view.findViewById<RecyclerView>(R.id.list_edit_products_list).let {
            it!!.adapter=shoppingListProductsAdapter
            it.layoutManager=layoutManager
        }

        view.findViewById<FloatingActionButton>(R.id.list_edit_add_product_button).apply {
            setOnClickListener {
                communicator!!.setShoppingListId(chosenShoppingList.shoppingListId)
                view.findNavController().navigate(R.id.action_listEdit_to_addProductToList)
            }
        }

        view.findViewById<Button>(R.id.list_edit_save_list_button).apply {
            setOnClickListener {
                val newName: String = nameText.text.toString()
                val newDate: Date = getDateFromDatePicker(datePicker)

                chosenShoppingList.name = newName
                chosenShoppingList.date = newDate

                viewModel.updateShoppingList(chosenShoppingList)
                Toast.makeText(context, "Zapisano wprowadzone zmiany", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.getTime()
    }
}