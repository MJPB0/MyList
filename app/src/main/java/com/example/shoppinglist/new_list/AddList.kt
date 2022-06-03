package com.example.shoppinglist.new_list

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
import androidx.navigation.findNavController
import com.example.shoppinglist.R
import com.example.shoppinglist.main.MainFragment
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import org.w3c.dom.Text
import java.util.*

class AddList : Fragment() {
    private lateinit var viewModel: ShoppingListsViewModel

    private lateinit var datePicker: DatePicker
    private lateinit var nameText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[ShoppingListsViewModel::class.java]

        return inflater.inflate(R.layout.add_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.datePicker = view.findViewById<DatePicker>(R.id.add_list_date_picker)
        this.nameText = view.findViewById<TextView>(R.id.add_list_name_content)

        initializeDatePicker()

        view.findViewById<Button>(R.id.add_list_button).apply {
            setOnClickListener {
                addNewList()
            }
        }
    }

    private fun addNewList() {
        if (nameText.text == ""){
            Toast.makeText(context, "Przed dodaniem listy należy podać jej nazwę", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addShoppingList(nameText.text.toString(), getDateFromDatePicker(this.datePicker))
        Toast.makeText(context, "Dodano nową listę zakupową", Toast.LENGTH_SHORT).show()
        resetView()
    }

    private fun initializeDatePicker(){
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]

        this.datePicker.updateDate(year, month, day)
    }

    private fun getDateFromDatePicker(datePicker: DatePicker): Date {
        val day = datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        return calendar.getTime()
    }

    private fun resetView() {
        initializeDatePicker()
        this.nameText.setText("")
    }
}