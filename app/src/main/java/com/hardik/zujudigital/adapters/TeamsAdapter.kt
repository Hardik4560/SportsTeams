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
import com.hardik.zujudigital.models.Team
import kotlinx.android.synthetic.main.item_team_row.view.*

class TeamsAdapter :
    RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder>() {

    inner class TeamsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamsViewHolder {
        return TeamsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_team_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TeamsViewHolder, position: Int) {
        val item = differ.currentList[position]
        holder.itemView.apply {
            txt_team_name.text = item.name
            Glide.with(this).load(Uri.parse(item.logo)).into(img_team)

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

    private var onItemClickListener: ((Team) -> Unit)? = null

    fun setOnItemClicklistener(listener: (Team) -> Unit) {
        onItemClickListener = listener
    }
}