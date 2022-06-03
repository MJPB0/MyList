package com.example.shoppinglist.add_product_to_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.adapters.ProductsListAdapter
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.api.dto.ProductsListDto
import com.example.shoppinglist.data.api.services.ProductService
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel
import com.example.shoppinglist.view_models.ShoppingListsViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddProductToList : Fragment() {
    private lateinit var viewModel: ProductsViewModel
    private lateinit var service: ProductService
    private lateinit var shoppingListsViewModel: ShoppingListsViewModel
    private var communicator: Communicator?= null

    private var productsList: List<ProductDto> = listOf()
    private var productsNames: List<String> = listOf()
    private lateinit var providedProduct: ProductDto

    private lateinit var listNameText: TextView
    private lateinit var productDescriptionText: TextView

    private lateinit var productCostText: EditText
    private lateinit var productAmountText: EditText
    private lateinit var productUnitText: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        service = ProductService.create()
        viewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        shoppingListsViewModel = ViewModelProvider(this)[ShoppingListsViewModel::class.java]
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.add_product_to_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shoppingList: ShoppingList = shoppingListsViewModel.getListById(communicator?.ShoppingListId!!.value!!)

        this.productUnitText = view.findViewById<EditText>(R.id.add_product_to_list_unit)
        this.productDescriptionText = view.findViewById<TextView>(R.id.add_product_to_list_description)
        this.listNameText = view.findViewById<TextView>(R.id.add_product_to_list_title_label)
        this.productCostText = view.findViewById<EditText>(R.id.add_product_to_list_cost)
        this.productAmountText = view.findViewById<EditText>(R.id.add_product_to_list_amount)

        this.listNameText.text = " ${shoppingList.name}"

        service.getProducts().enqueue( object : Callback<ProductsListDto> {
            override fun onResponse(call: Call<ProductsListDto>?, response: Response<ProductsListDto>?) {
                if (response?.body() != null){
                    productsList = response.body()!!.products
                    productsNames = productsList.map { it.name }

                    val productDropdown: Spinner = view.findViewById(R.id.add_product_to_list_product)

                    val productsAdapterArray = activity?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_item,
                            productsNames
                        )
                    }

                    productsAdapterArray?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    productDropdown.adapter = productsAdapterArray

                    productDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                        override fun onItemSelected(p0: AdapterView<*>?, view: View, position: Int, l: Long) {
                            providedProduct = productsList[position]
                            productUnitText.setText(providedProduct.unit)
                            productDescriptionText.setText(providedProduct.description)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}
                    }
                }
            }

            override fun onFailure(call: Call<ProductsListDto>?, t: Throwable?) {
                if (t != null) {
                    throw Error("Pobranie produktów z API nie powiodło się: ${t.message}")
                }
            }
        })


        view.findViewById<Button>(R.id.add_product_to_list_button).apply {
            setOnClickListener {
                addProductToList()
            }
        }
    }

    private fun addProductToList() {
        if (this.providedProduct == null){
            Toast.makeText(context, "Należy wybrać produkt który chcemy dodać do listy zakupów", Toast.LENGTH_SHORT).show()
            return
        }

        if (this.productAmountText.text.toString() == ""){
            Toast.makeText(context, "Aby dodać produkt do listy należy najpierw podać jego ilość", Toast.LENGTH_SHORT).show()
            return
        }

        if (this.productCostText.text.toString() == ""){
            Toast.makeText(context, "Aby dodać produkt do listy należy najpierw podać jego cenę", Toast.LENGTH_SHORT).show()
            return
        }

        if (this.productUnitText.text.toString() == ""){
            Toast.makeText(context, "Aby dodać produkt do listy należy najpierw podać jego jednostkę", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = this.productAmountText.text.toString().toIntOrNull()
        val cost = this.productCostText.text.toString().toDoubleOrNull()

        if (amount == null){
            Toast.makeText(context, "Upewnij się, że ilość produktu jest liczbą całkowitą", Toast.LENGTH_SHORT).show()
            return
        }

        if (cost == null) {
            Toast.makeText(context, "Upewnij się, że cena produktu jest w odpowiednim formacie", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.insertListProduct(
            ListProduct(
                0L,
                communicator?.ShoppingListId!!.value!!,
                this.providedProduct.name,
                amount,
                this.productUnitText.text.toString(),
                cost,
                this.providedProduct.description,
                false
            )
        )
        Toast.makeText(context, "Dodano produkt do listy", Toast.LENGTH_SHORT).show()
        resetView()
    }

    private fun resetView() {
        this.productCostText.setText("")
        this.productAmountText.setText("")
        this.productUnitText.setText(this.providedProduct.unit)
    }
}