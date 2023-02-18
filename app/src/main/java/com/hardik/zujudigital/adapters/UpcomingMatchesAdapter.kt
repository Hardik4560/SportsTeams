package com.hardik.zujudigital.adapters

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
import com.hardik.zujudigital.models.matches.Upcoming
import com.hardik.zujudigital.ui.viewmodels.MainViewModel
import com.hardik.zujudigital.util.DateUtils

class UpcomingMatchesAdapter :
    RecyclerView.Adapter<UpcomingMatchesAdapter.UpcomingMatchesViewHolder>() {

    inner class UpcomingMatchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Upcoming>() {
        override fun areItemsTheSame(oldItem: Upcoming, newItem: Upcoming): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Upcoming, newItem: Upcoming): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMatchesViewHolder {
        return UpcomingMatchesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_match_card,
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UpcomingMatchesViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            findViewById<TextView>(R.id.txt_team1).text = item.home
            findViewById<TextView>(R.id.txt_team2).text = item.away
            findViewById<TextView>(R.id.txt_date).text = DateUtils.dateToTime(item.date)

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
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}