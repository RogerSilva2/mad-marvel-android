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
import br.com.infinitytechnology.madmarvel.entities.Event
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_event.view.*

class EventAdapter(private val context: Context, private val listener: View.OnClickListener,
                   private val events: ArrayList<Event>) :
        RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val event: LinearLayout = view.layout_event
        val thumbnail: ImageView = view.image_view_thumbnail_event
        val title: TextView = view.text_view_title_event
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_event, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]

        val builder = StringBuilder()
        builder.append(event.thumbnail.path).append(".").append(event.thumbnail.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail)

        holder.title.text = event.title
        holder.event.tag = position
        holder.event.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return events.size
    }
}