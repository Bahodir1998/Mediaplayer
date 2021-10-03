package com.example.mediaplayer

import android.net.Uri
import java.io.Serializable

class Mp3Player() : Serializable {
    var artist: String? = null
    var title: String? = null
    var data: String? = null
    var displayName: String? = null
    var duration: String? = null
    var img:Uri?= null
}