package com.example.mediaplayer

import android.Manifest
import android.content.ContentUris
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mediaplayer.adapters.RvAdapter
import com.example.mediaplayer.databinding.FragmentHomeBinding
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.kelin.translucentbar.library.TranslucentBarManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class HomeFragment : Fragment() {
    

    lateinit var rvAdapter: RvAdapter
    lateinit var bind: FragmentHomeBinding
    val songs = ArrayList<Mp3Player>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        bind = FragmentHomeBinding.bind(view)
        denyPermission()
        val translucentBarManager = TranslucentBarManager(this)
        translucentBarManager.transparent(requireActivity())

        return view
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getAllSongs()
        }

        rvAdapter = RvAdapter(songs, object : RvAdapter.MyInterface {
            override fun click(mp3Player: Mp3Player, position: Int) {
                val bundle = Bundle().apply {
                    putSerializable("mp3", songs)
                    putInt("post", position)
                    putString("size", songs.size.toString())
                }
                findNavController().navigate(R.id.mp3Fragment, bundle)
            }

        })
        bind.rv.adapter = rvAdapter
    }
    private fun getAllSongs() {
        val cursor: Cursor? = requireActivity().contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        while (cursor!!.moveToNext()) {
            val mp3 = Mp3Player()

            mp3.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
            mp3.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
            mp3.data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
            mp3.displayName = cursor.getString(3)
            var albumUri: Uri? = null
            mp3.duration = SimpleDateFormat("mm:ss").format(
                Date(
                    cursor.getLong(
                        cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    )
                )
            )
            val albumId =
                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID));
            albumUri = ContentUris.withAppendedId(
                Uri.parse("content://media/external/audio/albumart"),
                albumId
            )
            mp3.img = albumUri
            println(albumUri)
            songs.add(mp3)
        }
    }

    private fun denyPermission() {
        askPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            getAllSongs()
            rvAdapter = RvAdapter(songs, object : RvAdapter.MyInterface {
                override fun click(mp3Player: Mp3Player, position: Int) {
                    val bundle = Bundle().apply {
                        putSerializable("mp3", songs)
                        putInt("post", position)
                        putString("size", songs.size.toString())
                    }
                    findNavController().navigate(R.id.mp3Fragment, bundle)
                }

            })
            bind.rv.adapter = rvAdapter
        }.onDeclined { e ->
            if (e.hasDenied()) {
                AlertDialog.Builder(requireContext())
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain()
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show()
            }
            if (e.hasForeverDenied()) {
                e.goToSettings();
            }
        }
    }

}