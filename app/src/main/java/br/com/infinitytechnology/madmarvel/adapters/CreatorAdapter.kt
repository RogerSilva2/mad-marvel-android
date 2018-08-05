package br.com.infinitytechnology.madmarvel.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import br.com.infinitytechnology.madmarvel.R
import br.com.infinitytechnology.madmarvel.entities.Creator
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_creator.view.*

class CreatorAdapter(private val context: Context, private val listener: View.OnClickListener,
                     private val creators: ArrayList<Creator>) :
        RecyclerView.Adapter<CreatorAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val creator: LinearLayout = view.layout_creator
        val thumbnail: ImageView = view.image_view_thumbnail_creator
        val name: TextView = view.text_view_name_creator
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_creator, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val creator = creators[position]

        val builder = StringBuilder()
        builder.append(creator.thumbnail.path).append(".").append(creator.thumbnail.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail)

        holder.name.text = creator.fullName
        holder.creator.tag = position
        holder.creator.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return creators.size
    }
}