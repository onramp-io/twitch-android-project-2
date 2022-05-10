package com.example.voyagerx.ui.fragments.landing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
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
import com.example.voyagerx.util.SharedPreferencesManager
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LandingPageFragment : Fragment() {

    @Inject
    lateinit var launchRepository: LaunchRepository

    private val SharedPreferencesManager by lazy { SharedPreferencesManager(requireContext()) }

    private lateinit var binding: FragmentLandingPageBinding
    private var launches: List<Launch> = listOf()
    private lateinit var adapter: LaunchOverviewAdapter

    companion object {
        const val RECYCLER_VIEW_BUNDLE_KEY =  "launches_recycler_view"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLandingPageBinding.inflate(inflater)

        setFontSizes()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable(RECYCLER_VIEW_BUNDLE_KEY, binding.listing.list.layoutManager?.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        SharedPreferencesManager.setListingFilterSetting(false, adapter.filtersAllMatch)
        SharedPreferencesManager.setListingFilterSetting(true, adapter.filtersAnyMatch)
        super.onPause()
    }

    // this will restore the list position during orientation changes/after navigation
    private fun restoreRecyclerViewState(savedInstanceState: Bundle?) {
        savedInstanceState?.getParcelable<Parcelable>(RECYCLER_VIEW_BUNDLE_KEY).let {
            binding.listing.list.layoutManager?.onRestoreInstanceState(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        restoreRecyclerViewState(savedInstanceState)

        if (launches.isEmpty()) {
            // only do this once when fragment created/re-created
            fetchLaunches()
        } else {
            // use saved data
            setLaunchListing(launches)
        }

        addSearchListeners()
        setupFilterMenu()
    }

    private fun setupRecyclerView() {
        // Add button press animation
        adapter = LaunchOverviewAdapter(
            LaunchClickListener(this::navigateToLaunchDetails),
            this::setListHeaderText
        )
        binding.listing.list.adapter = adapter
    }

    private fun fetchLaunches() {
        showSpinner()
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            val result = launchRepository.getLaunches()
            hideSpinner()
            if (!result.isNullOrEmpty()) {
                setLaunchListing(result)
            } else {
                showNetworkError()
            }
        }
    }

    private fun applySavedFilters() {
        val allFilters = SharedPreferencesManager.getListingFilterSetting(false)
        allFilters?.forEach { (field, matches) ->
            matches.forEach { match -> addAllFilter(field, match) }
        }
        val anyFilters = SharedPreferencesManager.getListingFilterSetting(true)
        anyFilters?.forEach { (field, matches) ->
            matches.forEach { match -> addAnyFilter(field, match) }
        }
    }

    private fun setLaunchListing(launches: List<Launch?>) {
        this.launches = launches.filterNotNull()
        adapter.initializeList(this.launches)
        setListHeaderText(launches.size)
        applySavedFilters()
    }

    private fun navigateToLaunchDetails(launch: Launch) {
        val bundle = Bundle()
        bundle.putParcelable(Launch.BUNDLE_KEY, launch)

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

    private fun addFilterChip(field: String, filter: String) {
        val chipGroup = binding.filters.filterChipGroup
        val filterChipLayout = layoutInflater.inflate(
            R.layout.landing_page_filter_chip, null
        ) as ConstraintLayout
        val filterChip = filterChipLayout.getChildAt(0) as Chip
        filterChipLayout.removeView(filterChip)

        filterChip.setOnCloseIconClickListener {
            chipGroup.removeView(filterChip)
            adapter.removeAnyFilter(field, filter)
        }

        filterChip.text = filter
        chipGroup.addView(filterChip)
    }

    private fun addAnyFilter(field: String, filter: String) {
        if (!adapter.hasAnyFilter(field, filter)) {
            adapter.addAnyFilter(field, filter)
            addFilterChip(field, filter)
        }
    }

    private fun addAllFilter(field: String, filter: String) {
        if (!adapter.hasAllFilter(field, filter)) {
            adapter.addAllFilter(field, filter)
        }
    }

    private fun addSiteToSubMenu(subMenu: SubMenu, site: String) {
        subMenu.add(site).setOnMenuItemClickListener {
            addAnyFilter(LaunchDetailFields.launchSite, site)
            true
        }
    }

    private fun addCheckboxFilterToMenu(popupMenu: PopupMenu, filterText: String, field: String) {
        val articleItem = popupMenu.menu.add(filterText)
        articleItem.isCheckable = true
        articleItem.isChecked =
            adapter.hasAllFilter(field, filterText)
        articleItem.setOnMenuItemClickListener {
            it.isChecked = !it.isChecked
            if (it.isChecked) {
                addAllFilter(field, filterText)
            } else {
                adapter.removeAllFilter(field, filterText)
            }
            true
        }
    }

    private fun setupFilterMenu() {
        binding.filters.filterIcon.setOnClickListener { view ->
            val popupMenu = PopupMenu(context, view)

            val siteNames = adapter.getSiteFilters()
            if (siteNames.isNotEmpty()) {
                val siteMenu = popupMenu.menu.addSubMenu(getString(R.string.filter_site_submenu))
                siteNames.forEach {
                    addSiteToSubMenu(siteMenu, it)
                }
            }

            addCheckboxFilterToMenu(popupMenu, getString(R.string.filter_article_link),
                LaunchDetailFields.articleLink
            )

            addCheckboxFilterToMenu(popupMenu, getString(R.string.filter_image_link),
                LaunchDetailFields.imageLinks
            )

            addCheckboxFilterToMenu(popupMenu, getString(R.string.filter_video_link),
                LaunchDetailFields.videoLink
            )

            popupMenu.inflate(R.menu.filter_menu)
            popupMenu.show()

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

    private fun setFontSizes() {
        when {
            SharedPreferencesManager.getFontSize() == "Large" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.listing.header.setTextAppearance(R.style.landingPageHeader)
                }
            }
            SharedPreferencesManager.getFontSize() == "Medium" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.listing.header.setTextAppearance(R.style.landingPageHeader_Medium)
                }
            }
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    binding.listing.header.setTextAppearance(R.style.landingPageHeader_Small)
                }
            }
        }

    }
}