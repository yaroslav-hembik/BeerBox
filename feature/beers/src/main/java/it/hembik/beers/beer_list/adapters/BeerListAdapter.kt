package it.hembik.beers.beer_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import it.hembik.beers.R
import it.hembik.beers.databinding.ItemLayoutBinding
import it.hembik.domain.model.Beer

class BeerListAdapter(
    private val onItemClick: (Int) -> Unit
) : PagingDataAdapter<Beer, BeerListAdapter.DataViewHolder>(BeerComparator) {

    class DataViewHolder(private val itemBinding: ItemLayoutBinding, private val onItemClick: (Int) -> Unit) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(beer: Beer) {
            itemBinding.tvBeerName.text = beer.name
            itemBinding.tvBeerTagline.text = beer.tagLine
            itemBinding.tvBeerDescription.text = beer.description
            itemBinding.imageViewAvatar.setImageResource(R.drawable.ic_beer_placeholder)
            beer.imageUrl?.let {
                Glide.with(itemBinding.imageViewAvatar.context)
                    .load(it)
                    .placeholder(R.drawable.ic_beer_placeholder)
                    .into(itemBinding.imageViewAvatar)
            }
            itemBinding.btnDeatils.setOnClickListener {
                onItemClick(beer.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemBinding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(itemBinding, onItemClick)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        getItem(position)?.let { safeItem ->
            holder.bind(safeItem)
        }
    }

    object BeerComparator : DiffUtil.ItemCallback<Beer>() {
        override fun areItemsTheSame(oldItem: Beer, newItem: Beer): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Beer, newItem: Beer): Boolean {
            return oldItem == newItem
        }
    }
}