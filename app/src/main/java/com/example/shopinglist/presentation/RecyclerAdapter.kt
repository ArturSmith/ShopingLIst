package com.example.shopinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.shopinglist.R
import com.example.shopinglist.domain.ShopItem
import java.lang.RuntimeException

class RecyclerAdapter: ListAdapter<ShopItem, ViewHolder>(ShopItemDiffCallback()) {


    var shopItemLongClickListener : ((ShopItem) -> Unit)? = null
    var shopItemClickListener : ((ShopItem) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutId = when(viewType) {
            ENABLED_VIEW -> R.layout.item_shop_enabled
            DISABLED_VIEW -> R.layout.item_shop_disabled
            else -> throw RuntimeException("Layout is not found")
        }

        val view = LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shopItem = getItem(position)

        holder.tvName.text = shopItem.name
        holder.tvCount.text = shopItem.count.toString()

        holder.itemView.setOnLongClickListener {
            shopItemLongClickListener?.invoke(shopItem)
            true
        }

        holder.itemView.setOnClickListener {
            shopItemClickListener?.invoke(shopItem)

        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val item = getItem(position)
        return when(item.enabled) {
            true -> ENABLED_VIEW
            false -> DISABLED_VIEW
        }
    }

    companion object {
        const val ENABLED_VIEW = 1
        const val DISABLED_VIEW = 0
        const val MAX_POOL = 20
    }


}