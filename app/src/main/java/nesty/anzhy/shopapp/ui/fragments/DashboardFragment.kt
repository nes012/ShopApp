package nesty.anzhy.shopapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.adapter.DashboardItemListAdapter
import nesty.anzhy.shopapp.databinding.FragmentDashboardBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.ui.activities.CartListActivity
import nesty.anzhy.shopapp.ui.activities.SettingsActivity

class DashboardFragment : Fragment() {

    //   private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart -> {
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun successDashboardItemsList(dashboardItemList: ArrayList<Product>){
        if(dashboardItemList.size>0){
            binding.textViewNoItems.visibility = View.INVISIBLE
            binding.recyclerViewDashboard.visibility = View.VISIBLE

            binding.recyclerViewDashboard.layoutManager = GridLayoutManager(activity, 2)
            binding.recyclerViewDashboard.setHasFixedSize(true)

            val adapter = DashboardItemListAdapter(requireActivity(), dashboardItemList)

            binding.recyclerViewDashboard.adapter = adapter
        }
        else {
            binding.textViewNoItems.visibility = View.GONE
            binding.recyclerViewDashboard.visibility = View.VISIBLE
        }
    }

    private fun getDashboardItemsList(){
        Firestore().getDashboardItemList(this@DashboardFragment)
    }
}