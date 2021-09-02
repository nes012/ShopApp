package nesty.anzhy.shopapp.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.adapter.ProductAdapter
import nesty.anzhy.shopapp.databinding.FragmentProductsBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.ui.activities.AddProductActivity


class ProductsFragment : Fragment() {

    // private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentProductsBinding? = null

    private val binding get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {
        for (i in productsList) {
            Log.i("Product Name", i.title)
        }

        if (productsList.size > 0) {
            binding.recyclerViewProducts.visibility = View.VISIBLE
            binding.textViewNoProducts.visibility = View.GONE

            binding.recyclerViewProducts.layoutManager = LinearLayoutManager(activity)
            binding.recyclerViewProducts.setHasFixedSize(true)
            val adapterProducts = ProductAdapter(requireContext(), productsList, this)
            binding.recyclerViewProducts.adapter = adapterProducts;
        } else {
            binding.recyclerViewProducts.visibility = View.GONE
            binding.textViewNoProducts.visibility = View.VISIBLE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //  homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        /*  homeViewModel.text.observe(viewLifecycleOwner, Observer {
              textView.text = it
          })

         */

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.add_product_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getProductListFromFireStore() {
        Firestore().getProductList(this)
    }

    fun deleteProduct(productID: String) {
        showAlertDialogToDeleteProduct(productID)
    }

    override fun onResume() {
        super.onResume()
        getProductListFromFireStore()
    }

    fun productDeleteSuccess() {
        Toast.makeText(requireActivity(), "Product deleted successfully", Toast.LENGTH_LONG).show()

        getProductListFromFireStore()
    }


    private fun showAlertDialogToDeleteProduct(productID: String) {
        val builder = AlertDialog.Builder(requireActivity())

        builder.setTitle("Delete Product")
        builder.setMessage("Do you want to delete this product?")
        builder.setPositiveButton("yes") { dialogInterface, _ ->
            Firestore().deleteProduct(this, productID)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("no") { dialogInterface, _ ->
            dialogInterface.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()

        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}