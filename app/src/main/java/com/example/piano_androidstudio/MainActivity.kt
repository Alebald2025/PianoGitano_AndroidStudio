package com.example.piano_androidstudio

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<Int, Int>()

    private lateinit var songStatusText: TextView
    private lateinit var songSpinner: Spinner
    private val keyViews = mutableMapOf<Int, View>()

    private data class SongNote(val keyId: Int, val label: String)

    // ── Canciones ──────────────────────────────────────────────

    private val songs = listOf(
        // Estrellita dónde estás (Do Mayor)
        "Estrellita dónde estás" to listOf(
            SongNote(R.id.key_c3, "Do"), SongNote(R.id.key_c3, "Do"),
            SongNote(R.id.key_g3, "Sol"), SongNote(R.id.key_g3, "Sol"),
            SongNote(R.id.key_a3, "La"), SongNote(R.id.key_a3, "La"),
            SongNote(R.id.key_g3, "Sol"),
            SongNote(R.id.key_f3, "Fa"), SongNote(R.id.key_f3, "Fa"),
            SongNote(R.id.key_e3, "Mi"), SongNote(R.id.key_e3, "Mi"),
            SongNote(R.id.key_d3, "Re"), SongNote(R.id.key_d3, "Re"),
            SongNote(R.id.key_c3, "Do")
        ),

        // Megalovania – intro (Re menor, octava 2)
        "Megalovania (inicio)" to listOf(
            SongNote(R.id.key_d2, "Re"), SongNote(R.id.key_d2, "Re"),
            SongNote(R.id.key_d3, "Re"), SongNote(R.id.key_a2, "La"),
            SongNote(R.id.key_ab2, "Lab"), SongNote(R.id.key_g2, "Sol"),
            SongNote(R.id.key_f2, "Fa"), SongNote(R.id.key_d2, "Re"),
            SongNote(R.id.key_f2, "Fa"), SongNote(R.id.key_g2, "Sol")
        ),

        // Zelda OoT – Nana de Zelda (Zelda's Lullaby)
        "Nana de Zelda" to listOf(
            SongNote(R.id.key_b2, "Si"), SongNote(R.id.key_d3, "Re"),
            SongNote(R.id.key_a2, "La"), SongNote(R.id.key_b2, "Si"),
            SongNote(R.id.key_d3, "Re"), SongNote(R.id.key_a2, "La")
        ),

        // Zelda OoT – Canción de la Tormenta (Song of Storms)
        "Canción de la Tormenta" to listOf(
            SongNote(R.id.key_d2, "Re"), SongNote(R.id.key_f2, "Fa"),
            SongNote(R.id.key_d3, "Re"), SongNote(R.id.key_d2, "Re"),
            SongNote(R.id.key_f2, "Fa"), SongNote(R.id.key_d3, "Re")
        )
    )

    private var currentSongNotes: List<SongNote> = emptyList()
    private var currentSongIndex = 0
    private var songActive = false

    // ── Ciclo de vida ──────────────────────────────────────────

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ensure physical volume buttons change media volume, not ringer volume
        volumeControlStream = android.media.AudioManager.STREAM_MUSIC

        songStatusText = findViewById(R.id.song_status)
        songSpinner = findViewById(R.id.spinner_songs)

        initSoundPool()
        setupAllKeys()
        setupSongSpinner()
    }

    // ── SoundPool ──────────────────────────────────────────────

    private fun initSoundPool() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()

        // Load sounds
        soundMap[R.id.key_c2] = soundPool.load(this, R.raw.c2, 1)
        soundMap[R.id.key_db2] = soundPool.load(this, R.raw.db2, 1)
        soundMap[R.id.key_d2] = soundPool.load(this, R.raw.d2, 1)
        soundMap[R.id.key_eb2] = soundPool.load(this, R.raw.eb2, 1)
        soundMap[R.id.key_e2] = soundPool.load(this, R.raw.e2, 1)
        soundMap[R.id.key_f2] = soundPool.load(this, R.raw.f2, 1)
        soundMap[R.id.key_gb2] = soundPool.load(this, R.raw.gb2, 1)
        soundMap[R.id.key_g2] = soundPool.load(this, R.raw.g2, 1)
        soundMap[R.id.key_ab2] = soundPool.load(this, R.raw.ab2, 1)
        soundMap[R.id.key_a2] = soundPool.load(this, R.raw.a2, 1)
        soundMap[R.id.key_bb2] = soundPool.load(this, R.raw.bb2, 1)
        soundMap[R.id.key_b2] = soundPool.load(this, R.raw.b2, 1)

        soundMap[R.id.key_c3] = soundPool.load(this, R.raw.c3, 1)
        soundMap[R.id.key_db3] = soundPool.load(this, R.raw.db3, 1)
        soundMap[R.id.key_d3] = soundPool.load(this, R.raw.d3, 1)
        soundMap[R.id.key_eb3] = soundPool.load(this, R.raw.eb3, 1)
        soundMap[R.id.key_e3] = soundPool.load(this, R.raw.e3, 1)
        soundMap[R.id.key_f3] = soundPool.load(this, R.raw.f3, 1)
        soundMap[R.id.key_gb3] = soundPool.load(this, R.raw.gb3, 1)
        soundMap[R.id.key_g3] = soundPool.load(this, R.raw.g3, 1)
        soundMap[R.id.key_ab3] = soundPool.load(this, R.raw.ab3, 1)
        soundMap[R.id.key_a3] = soundPool.load(this, R.raw.a3, 1)
        soundMap[R.id.key_bb3] = soundPool.load(this, R.raw.bb3, 1)
        soundMap[R.id.key_b3] = soundPool.load(this, R.raw.b3, 1)

        soundMap[R.id.key_c4] = soundPool.load(this, R.raw.c4, 1)
    }

    // ── Teclas ──────────────────────────────────────────────────

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
        keyViews[keyId] = keyView
        keyView.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                    v.isPressed = true
                    handleNotePlayed(keyId)
                    v.performClick()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL -> {
                    v.isPressed = false
                }
            }
            true
        }
    }

    // ── Desplegable de canciones ────────────────────────────────

    private fun setupSongSpinner() {
        val songNames = mutableListOf("Seleccionar canción…")
        songNames.addAll(songs.map { it.first })

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, songNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        songSpinner.adapter = adapter

        songSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    // Opción por defecto – desactivar modo canción
                    songActive = false
                    currentSongNotes = emptyList()
                    clearKeyHighlights()
                    updateSongStatus("Selecciona una canción para empezar")
                    return
                }

                val selectedSong = songs[position - 1]
                currentSongNotes = selectedSong.second
                currentSongIndex = 0
                songActive = true
                updateSongStatus("Toca: ${currentSongNotes[currentSongIndex].label}")
                highlightCurrentSongKey()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                songActive = false
                clearKeyHighlights()
            }
        }
    }

    // ── Lógica de aprendizaje ───────────────────────────────────

    private fun handleNotePlayed(keyId: Int) {
        playSound(keyId)
        if (!songActive) return

        val expected = currentSongNotes[currentSongIndex]
        if (keyId == expected.keyId) {
            currentSongIndex++
            if (currentSongIndex >= currentSongNotes.size) {
                updateSongStatus("¡Muy bien! Canción terminada. 🎉")
                songActive = false
                clearKeyHighlights()
                songSpinner.setSelection(0)
            } else {
                updateSongStatus("Correcto ✓  Ahora toca: ${currentSongNotes[currentSongIndex].label}")
                highlightCurrentSongKey()
            }
        } else {
            updateSongStatus("Incorrecto ✗  Toca: ${expected.label}")
        }
    }

    private fun updateSongStatus(message: String) {
        songStatusText.text = message
    }

    private fun highlightCurrentSongKey() {
        clearKeyHighlights()
        if (!songActive) return

        val nextKeyId = currentSongNotes[currentSongIndex].keyId
        keyViews[nextKeyId]?.isSelected = true
    }

    private fun clearKeyHighlights() {
        keyViews.values.forEach { it.isSelected = false }
    }

    // ── Audio ───────────────────────────────────────────────────

    private fun playSound(keyId: Int) {
        val soundId = soundMap[keyId] ?: return
        soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
}