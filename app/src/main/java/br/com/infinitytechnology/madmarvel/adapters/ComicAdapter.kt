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
import br.com.infinitytechnology.madmarvel.entities.Comic
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_comic.view.*

class ComicAdapter(private val context: Context, private val listener: View.OnClickListener,
                   private val comics: ArrayList<Comic>) :
        RecyclerView.Adapter<ComicAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val comic: LinearLayout = view.layout_comic
        val thumbnail: ImageView = view.image_view_thumbnail_comic
        val title: TextView = view.text_view_title_comic
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comic, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comic = comics[position]

        val builder = StringBuilder()
        builder.append(comic.thumbnail.path).append(".").append(comic.thumbnail.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail)

        holder.title.text = comic.title
        holder.comic.tag = position
        holder.comic.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return comics.size
    }
}