package com.example.shoppinglist.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.R
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.adapters.ProductsListAdapter
import com.example.shoppinglist.adapters.ShoppingListsAdapter
import com.example.shoppinglist.data.api.dto.ProductsListDto
import com.example.shoppinglist.data.api.services.ProductService
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainFragment : Fragment() {
    private lateinit var viewModel: ShoppingListsViewModel
    private var communicator: Communicator?= null

    private lateinit var shoppingLists : LiveData<List<ShoppingList>>

    private lateinit var fromDatePicker: DatePicker
    private lateinit var toDatePicker: DatePicker

    private lateinit var totalSumText: TextView
    private lateinit var payedSumText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ShoppingListsViewModel::class.java]
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pobranie DatePickerów
        this.fromDatePicker = view.findViewById<DatePicker>(R.id.main_fragment_from_date_picker)
        this.toDatePicker = view.findViewById<DatePicker>(R.id.main_fragment_to_date_picker)

        initializeDatePickers()

        // Pobranie text view sum
        this.totalSumText = view.findViewById<TextView>(R.id.main_fragment_total_sum_content)
        this.payedSumText = view.findViewById<TextView>(R.id.main_fragment_payed_content)

        filterShoppingLists()

        // Bindowanie przycisków
        view.findViewById<Button>(R.id.main_fragment_products_button).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.action_mainFragment_to_productsDisplay)
            }
        }

        view.findViewById<FloatingActionButton>(R.id.main_fragment_add_list_button).apply {
            setOnClickListener {
                view.findNavController().navigate(R.id.action_mainFragment_to_addList)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindListAdapter() {
        val shoppingListsAdapter= ShoppingListsAdapter(shoppingLists, viewModel, communicator!!)

        shoppingLists.observe(viewLifecycleOwner,
            Observer<List<ShoppingList>> {
                shoppingListsAdapter.notifyDataSetChanged()
                updatePrices(shoppingLists.value)
            }
        )

        val layoutManager= LinearLayoutManager(view?.context)
        view?.findViewById<RecyclerView>(R.id.main_fragment_shopping_lists).let {
            it!!.adapter=shoppingListsAdapter
            it.layoutManager=layoutManager
        }
    }

    private fun initializeDatePickers(){
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        this.fromDatePicker.init(
            year,
            if (month - 1 < 0) 11 else month - 1,
            day,
            DatePicker.OnDateChangedListener { _, _, _, _ -> filterShoppingLists() }
        )

        this.toDatePicker.init(
            year,
            month,
            day,
            DatePicker.OnDateChangedListener { _, _, _, _ -> filterShoppingLists() }
        )
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.getTime()
    }

    private fun filterShoppingLists() {
        val fromDate: Date = getDateFromDatePicker(this.fromDatePicker)
        val toDate: Date = getDateFromDatePicker(this.toDatePicker)

        this.shoppingLists = viewModel.getShoppingListInPeriodOfTime(fromDate, toDate)
        bindListAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun updatePrices(shoppingList: List<ShoppingList>?) {
        if (shoppingList == null){
            this.payedSumText.setText("0")
            this.totalSumText.setText("0")
            return
        }

        var total: Double = 0.0
        var payed: Double = 0.0
        for (list: ShoppingList in shoppingList){
            total += viewModel.getShoppingListTotalSumPrice(list.shoppingListId)
            payed += viewModel.getShoppingListPayedSumPrice(list.shoppingListId)
        }

        this.totalSumText.text = " ${total} zł"
        this.payedSumText.text = " ${payed} zł"
    }
}