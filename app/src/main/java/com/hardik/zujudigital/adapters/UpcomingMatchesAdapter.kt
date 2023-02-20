package com.hardik.zujudigital.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardik.zujudigital.R
import com.hardik.zujudigital.models.matches.Upcoming
import com.hardik.zujudigital.util.DateUtils
import kotlinx.android.synthetic.main.item_match_card.view.*

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

    override fun onBindViewHolder(holder: UpcomingMatchesViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            txt_team1_card.text = item.home
            txt_team2_card.text = item.away
            txt_date_card.text = DateUtils.dateToTime(item.date)

            var imgHomeTeam = img_t1_card
            var imgAwayTeam = img_t2_card

            val homeUrl = item.homeIcon
            val awayUrl = item.awayIcon
            homeUrl?.let {
                Glide.with(this).load(it).into(imgHomeTeam)
            }

            awayUrl?.let {
                Glide.with(this).load(Uri.parse(item.awayIcon)).into(imgAwayTeam)
            }

            txt_reminder.setOnClickListener {
                onItemClickListerner?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListerner: ((Upcoming) -> Unit)? = null

    fun setOnRemindMeClickListener(listener: (Upcoming) -> Unit) {
        onItemClickListerner = listener
    }
}