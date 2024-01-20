package com.example.musicapp

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import java.net.URL
import kotlin.properties.Delegates

// Main activity class for the music app
class MainActivity : AppCompatActivity() {

    // UI elements
    private lateinit var listView: ListView
    private lateinit var pageTitle: TextView
    private var downloadAPIData: DownloadAPIData? = null

    // Default values for the feed URL, limit, and type
    private var feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
    private var feedLimit = 10
    private var feedType = ""

    // Initialize the activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI elements
        listView = findViewById(R.id.listView)
        pageTitle = findViewById(R.id.titreID)
        pageTitle.text = "Top 10 songs"

        // Execute AsyncTask to download and display API data
        val downloadAPIData = DownloadAPIData(this, listView)
        downloadAPIData.execute(feedURL.format(feedLimit))
    }

    // Inflate the options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.floating_menu, menu)
        return true
    }

    // Handle options menu item selection
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.mnuAlbums -> {
                feedType = "Albums"
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topalbums/limit=%d/xml"
            }
            R.id.mnuSongs -> {
                feedType = "Songs"
                feedURL = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topsongs/limit=%d/xml"
            }
            R.id.mnu_top10 -> {
                if (!item.isChecked) {
                    item.isChecked = true
                    feedLimit = 10
                }
            }
            R.id.mnu_top25 -> if (!item.isChecked) {
                item.isChecked = true
                feedLimit = 25
            }
            else -> return super.onOptionsItemSelected(item)
        }

        // Update UI with selected feed type and limit
        pageTitle.text = "Top $feedLimit $feedType"
        downloadData(feedURL.format(feedLimit))
        return true
    }

    // Download data using AsyncTask
    private fun downloadData(feedURL: String) {
        val downloadAPIData = DownloadAPIData(this, listView)
        downloadAPIData.execute(feedURL)
    }

    // Cancel AsyncTask when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()
        downloadAPIData?.cancel(true)
    }

    companion object {

        // AsyncTask to download API data in the background
        private class DownloadAPIData(context: Context, listView: ListView) : AsyncTask<String, Void, String>() {

            // Properties for context and ListView
            var propContext: Context by Delegates.notNull()
            var propListView: ListView by Delegates.notNull()

            init {
                propContext = context
                propListView = listView
            }

            // Execute after completing the background task
            override fun onPostExecute(result: String) {
                super.onPostExecute(result)

                // Parse the XML result and create an adapter to display data in the ListView
                val apiParser = APIParser()
                apiParser.parseXML(result)
                val apiAdapter = APIAdapter(propContext, R.layout.layout_item, apiParser.applicationInfo)
                propListView.adapter = apiAdapter
            }

            // Background task to download data from the specified URL
            override fun doInBackground(vararg p0: String?): String {
                return URL(p0[0]).readText()
            }
        }
    }
}
