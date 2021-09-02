package nesty.anzhy.shopapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.adapter.SoldProductsAdapter
import nesty.anzhy.shopapp.databinding.FragmentSoldProductsBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.SoldProduct

class SoldProductsFragment : Fragment() {

    private var _binding: FragmentSoldProductsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSoldProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getSoldProductList()
    }

    private fun getSoldProductList(){
        Firestore().getSoldProductsList(this)

    }

    fun successSoldProductsList(soldProductsList: ArrayList<SoldProduct>){
        if(soldProductsList.size>0){
            binding.rvSoldProducts.visibility = View.VISIBLE
            binding.tvNoProductsSoldYet.visibility = View.GONE

            binding.rvSoldProducts.layoutManager = LinearLayoutManager(context)
            binding.rvSoldProducts.setHasFixedSize(true)

            val adapterSoldProduct = SoldProductsAdapter(requireContext(), soldProductsList)
            binding.rvSoldProducts.adapter = adapterSoldProduct
        }
        else{
            binding.rvSoldProducts.visibility = View.GONE
            binding.tvNoProductsSoldYet.visibility = View.VISIBLE
        }
    }
}