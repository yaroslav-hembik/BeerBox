package it.hembik.beers.beer_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import it.hembik.beers.R
import it.hembik.beers.databinding.DialogDetailsBinding
import it.hembik.domain.model.Beer
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BeerDetailsDialogFragment: BottomSheetDialogFragment() {
    private val beerDetailsViewModel: BeerDetailsViewModel by viewModels()

    private var _binding: DialogDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
        arguments?.getInt("beerId")?.let { safeBeerId ->
            lifecycleScope.launch {
                beerDetailsViewModel.userIntent.send(BeerDetailsIntent.GetBeerDetails(safeBeerId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            beerDetailsViewModel.state.collect {
                when (it) {
                    BeerDetailsState.Idle -> {}
                    is BeerDetailsState.BeerDetails -> setupView(it.beer)
                    is BeerDetailsState.Error -> Toast.makeText(requireContext(), "Error occured", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupView(beer: Beer) {
        binding.tvBeerName.text = beer.name
        binding.tvBeerTagline.text = beer.tagLine
        binding.tvBeerDescription.text = beer.description
        beer.imageUrl?.let {
            Glide.with(binding.imageViewAvatar.context)
                .load(it)
                .placeholder(R.drawable.ic_beer_placeholder)
                .into(binding.imageViewAvatar)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}