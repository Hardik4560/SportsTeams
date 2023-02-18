package com.hardik.zujudigital.adapters

import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardik.zujudigital.R
import com.hardik.zujudigital.models.matches.Previous
import com.hardik.zujudigital.models.matches.Upcoming
import com.hardik.zujudigital.util.DateUtils

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
            var txtHome = findViewById<TextView>(R.id.txt_team1)
            txtHome.text = item.home
            var txtAway = findViewById<TextView>(R.id.txt_team2)
            txtAway.text = item.away

            var imgHomeTeam = findViewById<ImageView>(R.id.img_t1)
            var imgAwayTeam = findViewById<ImageView>(R.id.img_t2)

            val homeUrl = item.homeIcon
            val awayUrl = item.awayIcon
            homeUrl?.let {
                Glide.with(this).load(it).into(imgHomeTeam)
            }

            awayUrl?.let {
                Glide.with(this).load(Uri.parse(item.awayIcon)).into(imgAwayTeam)
            }

            if (item.winner == item.home) {
                txtHome.typeface = Typeface.DEFAULT_BOLD
                txtAway.typeface = Typeface.DEFAULT
            } else {
                txtAway.typeface = Typeface.DEFAULT_BOLD
                txtHome.typeface = Typeface.DEFAULT
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