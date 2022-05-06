package com.example.voyagerx.ui.fragments.landing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.voyagerx.LaunchDetailsFragment
import com.example.voyagerx.R
import com.example.voyagerx.data.LaunchDetailFields
import com.example.voyagerx.databinding.FragmentLandingPageBinding
import com.example.voyagerx.helpers.LaunchClickListener
import com.example.voyagerx.repository.LaunchRepository
import com.example.voyagerx.repository.model.Launch
import com.example.voyagerx.ui.fragments.landing.list.LaunchOverviewAdapter
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class LandingPageFragment : Fragment() {

    @Inject
    lateinit var launchRepository: LaunchRepository

    private lateinit var binding: FragmentLandingPageBinding
    private lateinit var launches: List<Launch>
    private lateinit var adapter: LaunchOverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::launches.isInitialized) {
            setupList()
        }
    }

    private fun navigateToLaunchDetails(launch: Launch) {
        val bundle = Bundle()
        bundle.apply {
            LaunchDetailFields.let {
                putString(it.id, launch.id)
                putString(it.missionName, launch.mission_name)
                putString(it.launchSite, launch.launch_site_long)
                putString(it.launchDate, launch.launch_date_utc)
                putString(it.launchYear, launch.launch_year)
                putString(it.details, launch.details)
                putString(it.articleLink, launch.article_link)
                putString(it.videoLink, launch.video_link)
                putStringArray(it.imageLinks, launch.image_links?.toTypedArray())
            }
        }

        val launchDetailsFragment = LaunchDetailsFragment()
        launchDetailsFragment.arguments = bundle

        // Button animation then fragment transition?
        parentFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.frame, launchDetailsFragment)
            .commit()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addSearchListeners() {
        binding.filters.search.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                adapter.updateSearchTerm(text?.lowercase())
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                adapter.updateSearchTerm(text?.lowercase())
                return false
            }
        })

        binding.root.setOnTouchListener { view, motionEvent ->
            view?.performClick()
            hideKeyboardOnTouchOutside(motionEvent)
            true
        }
    }

    private fun addFilterChip(field: String, filter: String, ) {
        val chipGroup = binding.filters.filterChipGroup
        val filterChipLayout = layoutInflater.inflate(
            R.layout.landing_page_filter_chip, null
        ) as ConstraintLayout
        val filterChip = filterChipLayout.getChildAt(0) as Chip
        filterChipLayout.removeView(filterChip)

        filterChip.setOnCloseIconClickListener {
            chipGroup.removeView(filterChip)
            adapter.removeFilter(field, filter)
        }

        filterChip.text = filter
        chipGroup.addView(filterChip)
    }

    private fun addFilter(field: String, filter: String) {
        if (!adapter.hasFilter(field, filter)) {
            adapter.addFilter(field, filter)
            addFilterChip(field, filter)
        }
    }

    private fun addSiteToSubMenu(subMenu: SubMenu, site: String) {
        subMenu.add(site).setOnMenuItemClickListener {
            addFilter(LaunchDetailFields.launchSite, site)
            true
        }
    }

    private fun setupFilterMenu() {
        binding.filters.filterIcon.setOnClickListener { view ->
            val siteNames = adapter.getSiteFilters()
            if (adapter.itemCount > 0 && siteNames.isNotEmpty()) {
                val popupMenu = PopupMenu(context, view)
                val siteMenu = popupMenu.menu.addSubMenu(getString(R.string.filter_site_submenu))
                siteNames.forEach {
                    addSiteToSubMenu(siteMenu, it)
                }
                popupMenu.inflate(R.menu.filter_menu)
                popupMenu.show()
            }
        }
    }

    private fun setupList() {
        addSearchListeners()
        setupFilterMenu()
        hideNetworkError()
        showSpinner()

        // Add button press animation
        adapter = LaunchOverviewAdapter(
            LaunchClickListener(this::navigateToLaunchDetails),
            this::setListHeaderText
        )
        binding.listing.list.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launchWhenResumed {

            val result = launchRepository.getLaunches()
            hideSpinner()

            if (result != null) {
                launches = result.filterNotNull()
                adapter.initializeList(launches)
                setListHeaderText(launches.size)
            } else {
                showNetworkError()
            }
        }

    }

    private fun hideKeyboardOnTouchOutside(event: MotionEvent) {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = activity?.currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
    }

    private fun pluralizeLaunches(amount: Int): String = when (amount) {
        0 -> "There aren't any launches..."
        1 -> "There is 1 launch \uD83D\uDE80"
        else -> "There are $amount total launches \uD83D\uDE80"
    }

    private fun setListHeaderText(amount: Int) {
        binding.listing.header.text = pluralizeLaunches(amount)
        binding.listing.header.visibility = View.VISIBLE
    }

    private fun showSpinner() {
        binding.listing.spinner.visibility = View.VISIBLE
    }

    private fun hideSpinner() {
        binding.listing.spinner.visibility = View.INVISIBLE
    }

    private fun showNetworkError() {
        binding.error.root.visibility = View.VISIBLE
        binding.listing.root.visibility = View.INVISIBLE
        binding.filters.root.visibility = View.INVISIBLE
    }

    private fun hideNetworkError() {
        binding.error.root.visibility = View.INVISIBLE
        binding.listing.root.visibility = View.VISIBLE
        binding.filters.root.visibility = View.VISIBLE
    }
}