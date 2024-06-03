package com.eagletech.myvoice

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.eagletech.myvoice.MyShare.MyData
import com.eagletech.myvoice.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myData: MyData
    private lateinit var tts: TextToSpeech
    

    companion object {
        private const val TAG = "MainActivity"
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myData = MyData.getInstance(this)
        tts = TextToSpeech(this, this)
        clickApp()
    }

    private fun clickApp() {
        binding.button.setOnClickListener {
            Log.d(TAG, "Button clicked")
            if (myData.isPremiumSaves == true){
                val text = binding.editText.text.toString()
                if (text.isNotEmpty()) {
                    Log.d(TAG, "Button clicked, speaking text: $text")
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
                } else {
                    Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "No text entered")
                }
            } else if(myData.getSaves() > 0){
                val text = binding.editText.text.toString()
                if (text.isNotEmpty()) {
                    Log.d(TAG, "Button clicked, speaking text: $text")
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
                } else {
                    Toast.makeText(this, "Please enter text", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "No text entered")
                }
                myData.removeSaves()
            }
            else{
                Toast.makeText(this, "Please buy to continue", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, PayUseActivity::class.java)
                startActivity(intent)
            }
        }
        binding.topBar.info.setOnClickListener { showInfoBuy() }
        binding.topBar.menu.setOnClickListener {
            val intent = Intent(this, PayUseActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.ENGLISH)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "Language not supported", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Language not supported or missing data")
            } else {
                binding.button.isEnabled = true
                Log.d(TAG, "TextToSpeech initialized successfully")
            }
        } else {
            Toast.makeText(this, "Initialization failed", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "TextToSpeech initialization failed")
        }
    }

    private fun showInfoBuy() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Information")
            .setPositiveButton("Confirm") { dialog, _ -> dialog.dismiss() }
            .create()
        if (myData.isPremiumSaves == true) {
            dialog.setMessage("Successfully registered text to speech")
        } else {
            dialog.setMessage("You have ${myData.getSaves()} turns text to speech")
        }
        dialog.show()
    }
    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }


}
