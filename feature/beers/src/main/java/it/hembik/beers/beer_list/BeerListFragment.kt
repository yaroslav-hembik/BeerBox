package it.hembik.beers.beer_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import it.hembik.beers.R
import it.hembik.beers.beer_list.adapters.BeerListAdapter
import it.hembik.beers.beer_list.adapters.FilterAdapter
import it.hembik.beers.databinding.FragmentBeerListBinding
import it.hembik.domain.hideKeyboard
import it.hembik.domain.formatSearchInput
import it.hembik.domain.formatFiltersString
import it.hembik.domain.model.Beer
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeerListFragment : Fragment() {
    private var _binding: FragmentBeerListBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: BeerListViewModel by viewModels()
    private val beersAdapter = BeerListAdapter(this::onItemClick)
    private lateinit var filterAdapter: FilterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeerListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModel()
        setupClicks()

        if (savedInstanceState == null) {
            lifecycleScope.launch {
                mainViewModel.userIntent.send(BeerListIntent.FetchBeerList)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupClicks() {
        binding.searchTiet.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                lifecycleScope.launch {
                    binding.searchTiet.hideKeyboard()
                    filterAdapter.resetFilters()
                    if (binding.searchTiet.text.toString().isEmpty()) {
                        mainViewModel.userIntent.send(BeerListIntent.FetchBeerList)
                    } else {
                        mainViewModel.userIntent.send(
                            BeerListIntent.FetchBeersWithFilters(
                                binding.searchTiet.text.toString().formatSearchInput()
                            )
                        )
                    }

                }
            }
            true
        }
    }

    private fun setupUI() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    binding.recyclerView.context,
                    (binding.recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }

        beersAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                binding.progressBar.isVisible = true
                binding.emptyList.emptyListRoot.isVisible = false
            } else {
                // Only show the list if refresh succeeds, either from the the local db or the remote.
                binding.recyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                binding.progressBar.isVisible = false
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && beersAdapter.itemCount == 0
                if (isListEmpty) {
                    binding.recyclerView.isVisible = false
                    binding.emptyList.emptyListRoot.isVisible = true
                } else {
                    binding.recyclerView.isVisible = true
                    binding.emptyList.emptyListRoot.isVisible = false
                }

                // If we have an error, show a toast
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    binding.recyclerView.isVisible = false
                    binding.emptyList.emptyListRoot.isVisible = true
                    Toast.makeText(requireContext(), it.error.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        binding.recyclerView.adapter = beersAdapter
        filterAdapter = FilterAdapter(
            requireContext().resources.getStringArray(R.array.filters).asList(),
            this::onFilterUpdated
        )
        binding.filtersRecyclerView.adapter = filterAdapter
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            mainViewModel.state.collect {
                when (it) {
                    is BeerListState.Idle -> {}
                    is BeerListState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is BeerListState.Beers -> {
                        binding.progressBar.isVisible = false
                        renderList(it.beers)
                    }
                    is BeerListState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun renderList(beers: PagingData<Beer>) {
        binding.recyclerView.visibility = View.VISIBLE
        beersAdapter.submitData(lifecycle, beers)
    }

    private fun onItemClick(itemId: Int) {
        findNavController().navigate(
            R.id.action_beerListFragment_to_beerDetails,
            bundleOf("beerId" to itemId)
        )
    }

    private fun onFilterUpdated(filter: String?) {
        lifecycleScope.launch {
            binding.searchTiet.text?.clear()
            filter?.let { safeFilter ->
                mainViewModel.userIntent.send(BeerListIntent.FetchBeersWithFilters(safeFilter.formatFiltersString()))
            } ?: run {
                mainViewModel.userIntent.send(BeerListIntent.FetchBeerList)
            }
        }
    }
}