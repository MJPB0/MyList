package com.example.shoppinglist.added_product_info

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.shoppinglist.R
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.view_models.Communicator
import com.example.shoppinglist.view_models.ProductsViewModel

class AddedProductInformation : Fragment() {
    private lateinit var viewModel: ProductsViewModel
    private var communicator: Communicator?= null

    private lateinit var product: ListProduct
    private lateinit var nameText: TextView
    private lateinit var unitText: TextView
    private lateinit var priceText: TextView
    private lateinit var compositionText: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        communicator = ViewModelProvider(requireActivity())[Communicator::class.java]

        return inflater.inflate(R.layout.added_product_information_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.nameText = view.findViewById<TextView>(R.id.added_product_info_name)
        this.unitText = view.findViewById<TextView>(R.id.added_product_info_unit)
        this.priceText = view.findViewById<TextView>(R.id.added_product_info_cost)
        this.compositionText = view.findViewById<TextView>(R.id.added_product_info_description)

        this.product = viewModel.getProductById(communicator?.AddedProductId!!.value!!)
        this.nameText.text = product.product
        this.unitText.text = product.unit
        this.priceText.text = "${product.cost}"
        this.compositionText.text = product.description

        view.findViewById<Button>(R.id.added_product_information_save_button).apply {
            setOnClickListener {
                updateProduct()
            }
        }
    }

    private fun updateProduct() {
        if (this.priceText.text.toString() == ""){
            Toast.makeText(context, "Aby dodać produkt do listy należy najpierw podać jego cenę", Toast.LENGTH_SHORT).show()
            return
        }
        val cost = this.priceText.text.toString().toDoubleOrNull()
        if (cost == null) {
            Toast.makeText(context, "Upewnij się, że cena produktu jest w odpowiednim formacie", Toast.LENGTH_SHORT).show()
            return
        }

        Toast.makeText(context, "Zapisano cenę produktu", Toast.LENGTH_SHORT).show()
        this.product.cost = cost
        viewModel.updateListProduct(this.product)
    }
}