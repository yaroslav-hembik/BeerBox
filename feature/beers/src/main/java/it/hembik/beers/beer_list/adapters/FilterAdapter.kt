package it.hembik.beers.beer_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import it.hembik.beers.R
import it.hembik.beers.databinding.ItemFilterLayoutBinding

class FilterAdapter(
    private var items: List<String>,
    private val onFilterUpdated: (String?) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    var selectedPosition = -1

    class FilterViewHolder(val itemBinding: ItemFilterLayoutBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(filter: String) {
            itemBinding.filterText.text = filter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val itemBinding = ItemFilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(items[position])
        if (selectedPosition == position && !holder.itemBinding.filterText.isSelected) {
            holder.itemBinding.filterText.isSelected = true
            holder.itemBinding.filterText.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.secondaryColor))
            holder.itemBinding.filterText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        } else {
            holder.itemBinding.filterText.isSelected = false
            holder.itemBinding.filterText.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.primaryLightColor))
            holder.itemBinding.filterText.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.primaryTextColor))
        }

        holder.itemView.setOnClickListener {
            if (selectedPosition == holder.absoluteAdapterPosition && holder.itemBinding.filterText.isSelected) {
                onFilterUpdated(null)
            } else {
                notifyItemChanged(selectedPosition)
                selectedPosition = holder.absoluteAdapterPosition
                onFilterUpdated(items[position])
            }
            notifyDataSetChanged()
        }
    }

    fun resetFilters() {
        selectedPosition = -1
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size
}