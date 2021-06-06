package com.example.lab06_mediaplayer.fragments

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import com.example.lab06_mediaplayer.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AudioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AudioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mp: MediaPlayer? = null
    private var current: MutableList<Int> = mutableListOf(R.raw.audio)
    private var playButton: FloatingActionButton? = null
    private var pausebutton: FloatingActionButton? = null
    private var stopButton: FloatingActionButton? = null
    private var seekbaraudio: SeekBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_audio, container, false)

        playButton = view.findViewById(R.id.fab_play)
        pausebutton = view.findViewById(R.id.fab_pause)
        stopButton = view.findViewById(R.id.fab_stop)
        controlSound(current[0])

        return  view
    }

    private fun controlSound(id: Int) {
        playButton?.setOnClickListener {
            if( mp == null) {
                mp = MediaPlayer.create(context, id)
                Log.d("AudioFragment", "ID: ${mp!!.audioSessionId}")
                initializeSeekBar()
            }
            mp?.start()
            Log.d("AudioFragment", "Duration: ${mp!!.duration/1000} seconds")
        }

        pausebutton?.setOnClickListener {
            if ( mp !== null) mp?.pause()
            Log.d("AudioFragment", " Paused at: ${mp!!.duration/1000} seconds")
        }

        stopButton?.setOnClickListener {
            if ( mp !== null) {
                mp?.stop()
                mp?.reset()
                mp?.release()
                mp = null
            }
        }

        seekbaraudio?.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun initializeSeekBar() {
        seekbaraudio?.max = mp!!.duration

        var handler = Handler()
        handler.postDelayed(object: Runnable{
            override fun run() {
                try {
                    seekbaraudio?.progress = mp!!.currentPosition
                }catch (e: Exception){
                    seekbaraudio?.progress = 0
                }
            }
        }, 0)
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AudioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AudioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}