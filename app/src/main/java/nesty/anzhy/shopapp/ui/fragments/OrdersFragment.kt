package nesty.anzhy.shopapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.adapter.OrdersAdapter
import nesty.anzhy.shopapp.databinding.FragmentOrdersBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Order

class OrdersFragment : Fragment() {

 //   private lateinit var notificationsViewModel: NotificationsViewModel
    private var _binding: FragmentOrdersBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
     //   notificationsViewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun populateOrdersListInUI(ordersList: ArrayList<Order>){
        if(ordersList.size>0){
            binding.recyclerViewOrdersFragment.visibility = View.VISIBLE
            binding.tvNoOrdersPlacedYet.visibility = View.GONE

            binding.recyclerViewOrdersFragment.layoutManager = LinearLayoutManager(context)
            binding.recyclerViewOrdersFragment.setHasFixedSize(true)

            val adapter = OrdersAdapter(requireContext(), ordersList)
            binding.recyclerViewOrdersFragment.adapter = adapter

        }
    }

    fun getMyOrderList(){
        Firestore().getMyOrdersList(this@OrdersFragment)
    }

    override fun onResume() {
        super.onResume()
        getMyOrderList()
    }
}