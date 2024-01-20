package com.example.musicapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

// ViewHolder class to hold the views for efficient recycling in the ListView
class ViewHolder(v: View) {
    val title: TextView = v.findViewById(R.id.songName1)
    val singer: TextView = v.findViewById(R.id.singerName1)
    val image: ImageView = v.findViewById(R.id.image_song1)
}

// Custom ArrayAdapter for displaying API data in a ListView
class APIAdapter(context: Context, private val resource: Int, private val apiData: ArrayList<APIDetails>) :
    ArrayAdapter<APIDetails>(context, resource, apiData) {

    // LayoutInflater for inflating the views
    private val inflater = LayoutInflater.from(context)

    // Override getView to customize how each item in the ListView is displayed
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        // Declare view and viewHolder variables
        val view: View
        val viewHolder: ViewHolder

        // Check if a recycled view is available
        if (convertView == null) {
            // If not, inflate a new view and create a new viewHolder
            view = inflater.inflate(resource, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder // Set the viewHolder as a tag to the view for recycling
        } else {
            // If recycled view is available, reuse it and get the viewHolder from the tag
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        // Get the current API data for the given position
        val currentAPI = apiData[position]

        // Set the TextViews and ImageView with data from the current API
        viewHolder.title.text = currentAPI.song_name
        viewHolder.singer.text = currentAPI.singer_name
        Picasso.get()
            .load(currentAPI.urlImg)
            .resize(100, 100)
            .centerCrop()
            .into(viewHolder.image)

        // Return the customized view
        return view
    }
}
