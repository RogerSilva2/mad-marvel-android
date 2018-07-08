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
import br.com.infinitytechnology.madmarvel.entities.Character
import br.com.infinitytechnology.madmarvel.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_character.view.*

class CharacterAdapter(private val context: Context, private val listener: View.OnClickListener,
                       private val characters: ArrayList<Character>) :
        RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val character: LinearLayout = view.layout_character
        val thumbnail: ImageView = view.image_view_thumbnail_character
        val name: TextView = view.text_view_name_character
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_character, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = characters[position]

        val builder = StringBuilder()
        builder.append(character.thumbnail.path).append(".").append(character.thumbnail.extension)

        Picasso.with(context)
                .load(builder.toString())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(CircleTransform())
                .into(holder.thumbnail)

        holder.name.text = character.name
        holder.character.tag = position
        holder.character.setOnClickListener(listener)
    }

    override fun getItemCount(): Int {
        return characters.size
    }
}