package com.example.piano_androidstudio

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSoundPool()
        setupAllKeys()
    }

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sounds
        soundMap[R.id.key_c2] = soundPool.load(this, R.raw.C2, 1)
        soundMap[R.id.key_db2] = soundPool.load(this, R.raw.Db2, 1)
        soundMap[R.id.key_d2] = soundPool.load(this, R.raw.D2, 1)
        soundMap[R.id.key_eb2] = soundPool.load(this, R.raw.Eb2, 1)
        soundMap[R.id.key_e2] = soundPool.load(this, R.raw.E2, 1)
        soundMap[R.id.key_f2] = soundPool.load(this, R.raw.F2, 1)
        soundMap[R.id.key_gb2] = soundPool.load(this, R.raw.Gb2, 1)
        soundMap[R.id.key_g2] = soundPool.load(this, R.raw.G2, 1)
        soundMap[R.id.key_ab2] = soundPool.load(this, R.raw.Ab2, 1)
        soundMap[R.id.key_a2] = soundPool.load(this, R.raw.A2, 1)
        soundMap[R.id.key_bb2] = soundPool.load(this, R.raw.Bb2, 1)
        soundMap[R.id.key_b2] = soundPool.load(this, R.raw.B2, 1)

        soundMap[R.id.key_c3] = soundPool.load(this, R.raw.C3, 1)
        soundMap[R.id.key_db3] = soundPool.load(this, R.raw.Db3, 1)
        soundMap[R.id.key_d3] = soundPool.load(this, R.raw.D3, 1)
        soundMap[R.id.key_eb3] = soundPool.load(this, R.raw.Eb3, 1)
        soundMap[R.id.key_e3] = soundPool.load(this, R.raw.E3, 1)
        soundMap[R.id.key_f3] = soundPool.load(this, R.raw.F3, 1)
        soundMap[R.id.key_gb3] = soundPool.load(this, R.raw.Gb3, 1)
        soundMap[R.id.key_g3] = soundPool.load(this, R.raw.G3, 1)
        soundMap[R.id.key_ab3] = soundPool.load(this, R.raw.Ab3, 1)
        soundMap[R.id.key_a3] = soundPool.load(this, R.raw.A3, 1)
        soundMap[R.id.key_bb3] = soundPool.load(this, R.raw.Bb3, 1)
        soundMap[R.id.key_b3] = soundPool.load(this, R.raw.B3, 1)

        soundMap[R.id.key_c4] = soundPool.load(this, R.raw.C4, 1)
    }

    private fun setupAllKeys() {
        val keys = listOf(
            R.id.key_c2, R.id.key_db2, R.id.key_d2, R.id.key_eb2, R.id.key_e2, R.id.key_f2, R.id.key_gb2, R.id.key_g2, R.id.key_ab2, R.id.key_a2, R.id.key_bb2, R.id.key_b2,
            R.id.key_c3, R.id.key_db3, R.id.key_d3, R.id.key_eb3, R.id.key_e3, R.id.key_f3, R.id.key_gb3, R.id.key_g3, R.id.key_ab3, R.id.key_a3, R.id.key_bb3, R.id.key_b3,
            R.id.key_c4
        )

        for (keyId in keys) {
            setupKey(keyId)
        }
    }

    private fun setupKey(keyId: Int) {
        val keyView = findViewById<View>(keyId)
        keyView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    v.isPressed = true
                    playSound(keyId)
                    v.performClick()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                    v.isPressed = false
                }
            }
            true
        }
    }

    private fun playSound(keyId: Int) {
        val soundId = soundMap[keyId] ?: return
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}