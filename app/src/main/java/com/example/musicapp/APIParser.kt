package com.example.musicapp

import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory

// Class responsible for parsing XML data into a list of APIDetails
class APIParser {
    // List to store parsed APIDetails objects
    val applicationInfo = ArrayList<APIDetails>()

    // Function to parse XML data and return a list of APIDetails
    fun parseXML(xmlData: String): ArrayList<APIDetails> {

        // Create a new XmlPullParserFactory
        val factory = XmlPullParserFactory.newInstance()
        factory.isNamespaceAware = true

        // Create a new XmlPullParser and set its input to the provided XML data
        val parser = factory.newPullParser()
        parser.setInput(xmlData.reader())

        // Variables for tracking parser events and text values
        var event = parser.eventType
        var textValue = ""
        var currentAPI = APIDetails()

        // Loop through the XML document
        while (event != XmlPullParser.END_DOCUMENT) {
            // Get the lowercase name of the current XML tag
            val tagText = parser.name?.toLowerCase()

            // Handle different XML events
            when (event) {
                XmlPullParser.START_TAG -> {
                    // If the current tag is "entry," create a new APIDetails object
                    if (tagText == "entry") {
                        currentAPI = APIDetails()
                    }
                }
                XmlPullParser.TEXT -> textValue = parser.text
                XmlPullParser.END_TAG -> {
                    // Depending on the closing tag, update the current APIDetails object
                    when (tagText) {
                        "entry" -> applicationInfo.add(currentAPI)
                        "title" -> currentAPI.song_name = textValue.split("-")[0]
                        "image" -> currentAPI.urlImg = textValue
                        "artist" -> currentAPI.singer_name = textValue
                    }
                }
            }
            // Move to the next XML event
            event = parser.next()
        }
        // Return the list of parsed APIDetails objects
        return applicationInfo
    }
}
