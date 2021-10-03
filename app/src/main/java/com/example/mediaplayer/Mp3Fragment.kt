                                                          package com.example.mediaplayer

import android.annotation.SuppressLint
import android.content.ContentUris
import android.database.Cursor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.mediaplayer.databinding.FragmentMp3Binding
import com.kelin.translucentbar.library.TranslucentBarManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Mp3Fragment : Fragment(), MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener {

    var a : Mp3Player ? = null
    var mediaPlayer: MediaPlayer? = null
    var post = 0
    var size: String = ""
    lateinit var mp3Player : ArrayList<Mp3Player>
    lateinit var bind: FragmentMp3Binding
    lateinit var handler: Handler
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mp3Player = ArrayList()
        val view = inflater.inflate(R.layout.fragment_mp3, container, false)
        bind = FragmentMp3Binding.bind(view)
        val translucentBarManager = TranslucentBarManager(this)
        translucentBarManager.transparent(requireActivity())
        // Inflate the layout for this fragment
        post = arguments?.getInt("post")!!
        val mp3Player = arguments?.getSerializable("mp3") as ArrayList<Mp3Player>
        bind.imgMusic.setImageURI(mp3Player[post].img)
        if(bind.imgMusic.drawable == null){
            bind.imgMusic.setImageResource(R.drawable.imusic)
        }
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(mp3Player[post].data)
        mediaPlayer?.prepare()
        mediaPlayer?.setOnCompletionListener(this)
        bind.progressBar.max = mediaPlayer?.duration!!
        mediaPlayer?.start()
        a = mp3Player[post]
        size = arguments?.getString("size")!!
        bind.nameSong.text = mp3Player[post].title
        bind.artst.text = mp3Player[post].artist
        bind.number.text = "${post + 1} / $size"

        bind.play.setOnClickListener {
            if (!mediaPlayer?.isPlaying!!) {
                mediaPlayer?.start()
                bind.play.visibility = View.INVISIBLE
                bind.pause.visibility = View.VISIBLE
            }
        }
        bind.pause.setOnClickListener {
            if (mediaPlayer?.isPlaying!!) {
                mediaPlayer?.pause()
                bind.pause.visibility = View.INVISIBLE
                bind.play.visibility = View.VISIBLE
            }
        }

        handler = Handler(Looper.getMainLooper())
        bind.progressBar.max = mediaPlayer!!.duration
        handler.postDelayed(runnable, 100)
        bind.progressBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        bind.left.setOnClickListener {
            if (mediaPlayer?.currentPosition!! > 0) {
                mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.minus(30000))
            } else {
                mediaPlayer?.seekTo(0);
            }
        }
        bind.right.setOnClickListener {
            if (mediaPlayer?.currentPosition!! < mediaPlayer!!.duration) {
                mediaPlayer?.seekTo(mediaPlayer?.currentPosition!!.plus(30000))
            } else {
                mediaPlayer?.seekTo(mediaPlayer?.duration!!);
            }
        }
        bind.next.setOnClickListener {
            if (size.toInt() != post + 1) {
                bind.play.visibility = View.INVISIBLE
                bind.pause.visibility = View.VISIBLE
                val mp3 = mp3Player[++post]
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(mp3.data + File.separator)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                bind.progressBar.max = mediaPlayer?.duration!!
                bind.imgMusic.setImageURI(mp3Player[post].img)
                if(bind.imgMusic.drawable == null){
                    bind.imgMusic.setImageResource(R.drawable.imusic)
                }
                bind.artst.text = mp3.artist
                bind.nameSong.text = mp3.title
                bind.number.text = "${post + 1} / $size"
                mediaPlayer?.setOnCompletionListener(this)
            }else{
                post = -1
                bind.play.visibility = View.INVISIBLE
                bind.pause.visibility = View.VISIBLE
                val mp3 = mp3Player[++post]
                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(mp3.data + File.separator)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                bind.progressBar.max = mediaPlayer?.duration!!
                bind.imgMusic.setImageURI(mp3Player[post].img)
                if(bind.imgMusic.drawable == null){
                    bind.imgMusic.setImageResource(R.drawable.imusic)
                }
                bind.artst.text = mp3.artist
                bind.nameSong.text = mp3.title
                bind.number.text = "${post + 1} / $size"
                mediaPlayer?.setOnCompletionListener(this)
            }
        }
        bind.skip.setOnClickListener {
            if (post != 0) {
                bind.play.visibility = View.INVISIBLE
                bind.pause.visibility = View.VISIBLE
                val mp3 = mp3Player[--post]

                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(mp3.data + File.separator)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                bind.progressBar.max = mediaPlayer?.duration!!
                bind.imgMusic.setImageURI(mp3Player[post].img)
                if(bind.imgMusic.drawable == null){
                    bind.imgMusic.setImageResource(R.drawable.imusic)
                }
                bind.artst.text = mp3.artist
                bind.nameSong.text = mp3.title
                bind.number.text = "${post+1} / $size"
                mediaPlayer?.setOnCompletionListener(this)
            }else{
                bind.play.visibility = View.INVISIBLE
                bind.pause.visibility = View.VISIBLE
                post = size.toInt()
                val mp3 = mp3Player[--post]

                mediaPlayer?.stop()
                mediaPlayer?.release()
                mediaPlayer = MediaPlayer()
                mediaPlayer?.setDataSource(mp3.data + File.separator)
                mediaPlayer?.prepare()
                mediaPlayer?.start()
                bind.progressBar.max = mediaPlayer?.duration!!
                bind.imgMusic.setImageURI(mp3Player[post].img)
                if(bind.imgMusic.drawable == null){
                    bind.imgMusic.setImageResource(R.drawable.imusic)
                }
                bind.artst.text = mp3.artist
                bind.nameSong.text = mp3.title
                bind.number.text = "${post+1} / $size"
                mediaPlayer?.setOnCompletionListener(this)
            }
        }
        return view
    }
    private var runnable = object : Runnable {
        override fun run() {
            if (mediaPlayer != null) {
                val a = SimpleDateFormat("mm:ss").format(
                    Date(
                        mediaPlayer?.currentPosition!!.toLong()
                    )
                )
                val b =  SimpleDateFormat("mm:ss").format(
                    Date(
                        mediaPlayer?.duration!!.toLong()
                    )
                )
                bind.duration.text = "$a / $b"
                bind.progressBar.progress = mediaPlayer?.currentPosition!!
                handler.postDelayed(this, 100)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onCompletion(mp: MediaPlayer?) {
        if (size.toInt() != post +1){
            mp?.reset()
            post += 1
            mp3Player = arguments?.getSerializable("mp3") as ArrayList<Mp3Player>
            mp?.setDataSource(mp3Player[post].data)
            mp?.prepare()
            mp?.start()
            bind.progressBar.max = mp?.duration!!
            bind.imgMusic.setImageURI(mp3Player[post].img)
            if(bind.imgMusic.drawable == null){
                bind.imgMusic.setImageResource(R.drawable.imusic)
            }
            bind.artst.text = mp3Player[post].artist
            bind.nameSong.text = mp3Player[post].title
            bind.number.text = "${post + 1} / $size"
            mp.setOnCompletionListener(this)
            bind.play.visibility = View.INVISIBLE
            bind.pause.visibility = View.VISIBLE
        }else{
            mp?.reset()
            post = 0
            mp3Player = arguments?.getSerializable("mp3") as ArrayList<Mp3Player>
            mp?.setDataSource(mp3Player[post].data)
            mp?.prepare()
            mp?.start()
            bind.progressBar.max = mp?.duration!!
            bind.imgMusic.setImageURI(mp3Player[post].img)
            if(bind.imgMusic.drawable == null){
                bind.imgMusic.setImageResource(R.drawable.imusic)
            }
            bind.artst.text = mp3Player[post].artist
            bind.nameSong.text = mp3Player[post].title
            bind.number.text = "${post + 1} / $size"
            mp.setOnCompletionListener(this)
            bind.play.visibility = View.INVISIBLE
            bind.pause.visibility = View.VISIBLE
        }
    }
    override fun onPrepared(mp: MediaPlayer?) {
        mp?.start()
        bind.progressBar.max = mediaPlayer?.duration!!
    }
}

