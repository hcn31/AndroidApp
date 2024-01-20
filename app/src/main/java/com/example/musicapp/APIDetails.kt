package com.example.musicapp

// Class representing details obtained from an API for a music application
class APIDetails {

    // Properties to store information about a song
    var song_name: String = ""
    var singer_name: String = ""
    var urlImg: String = ""

    // Override toString() method to provide a string representation of the object
    override fun toString(): String {
        // Return a formatted string with important details for debugging or logging
        return "APIDetails(song_name='$song_name', singer_name='$singer_name', urlImg='$urlImg')"
    }
}
