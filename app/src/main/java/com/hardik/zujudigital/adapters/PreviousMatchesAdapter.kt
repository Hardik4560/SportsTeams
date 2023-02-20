package com.hardik.zujudigital.adapters

import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardik.zujudigital.R
import com.hardik.zujudigital.models.matches.Previous
import kotlinx.android.synthetic.main.item_match_row.view.*


class PreviousMatchesAdapter :
    RecyclerView.Adapter<PreviousMatchesAdapter.PreviousMatchesViewHolder>() {

    inner class PreviousMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Previous>() {
        override fun areItemsTheSame(oldItem: Previous, newItem: Previous): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Previous, newItem: Previous): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousMatchesViewHolder {
        return PreviousMatchesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_match_row,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PreviousMatchesViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            txt_team1.text = item.home
            txt_team2.text = item.away

            var imgHomeTeam = img_t1
            var imgAwayTeam = img_t2

            val homeUrl = item.homeIcon
            val awayUrl = item.awayIcon
            homeUrl?.let {
                Glide.with(this).load(it).into(imgHomeTeam)
            }

            awayUrl?.let {
                Glide.with(this).load(Uri.parse(item.awayIcon)).into(imgAwayTeam)
            }

            val typeface = ResourcesCompat.getFont(context, R.font.lato)
            val typefaceBold = ResourcesCompat.getFont(context, R.font.lato_bold)
            if (item.winner == item.home) {
                txt_team1.typeface = typefaceBold
                txt_team2.typeface = typeface
            } else {
                txt_team2.typeface = typefaceBold
                txt_team1.typeface = typeface
            }

            setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Previous) -> Unit)? = null

    fun setOnItemClickListener(listener: (Previous) -> Unit) {
        onItemClickListener = listener
    }
}