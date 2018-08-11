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
import br.com.infinitytechnology.madmarvel.entities.Story
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_story.view.*

class StoryAdapter(private val context: Context, private val listener: View.OnClickListener,
                   private val stories: ArrayList<Story>) :
        RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val story: LinearLayout = view.layout_story
        val thumbnail: ImageView = view.image_view_thumbnail_story
        val title: TextView = view.text_view_title_story
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_story, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val story = stories[position]

        val builder = StringBuilder()
        builder.append(story.thumbnail?.path).append(".").append(story.thumbnail?.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail)

        holder.title.text = story.title
        holder.story.tag = position
        holder.story.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return stories.size
    }
}