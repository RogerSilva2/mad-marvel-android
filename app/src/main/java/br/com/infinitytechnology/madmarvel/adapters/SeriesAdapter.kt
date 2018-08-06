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
import br.com.infinitytechnology.madmarvel.entities.Series
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_series.view.*

class SeriesAdapter(private val context: Context, private val listener: View.OnClickListener,
                    private val series: ArrayList<Series>) :
        RecyclerView.Adapter<SeriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val event: LinearLayout = view.layout_series
        val thumbnail: ImageView = view.image_view_thumbnail_series
        val title: TextView = view.text_view_title_series
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_series, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val series = series[position]

        val builder = StringBuilder()
        builder.append(series.thumbnail.path).append(".").append(series.thumbnail.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail)

        holder.title.text = series.title
        holder.event.tag = position
        holder.event.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return series.size
    }
}