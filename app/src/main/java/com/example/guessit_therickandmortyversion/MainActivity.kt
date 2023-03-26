package com.example.guessit_therickandmortyversion

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {
    var characterName = ""
    var characterImgURL = ""
    var characterStatus = ""
    var characterEpisode = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.checkGuessButton)
        val image = findViewById<ImageView>(R.id.characterImage)
        val status = findViewById<TextView>(R.id.status)
        val episode = findViewById<TextView>(R.id.episode)

        getNextCharacter(button, image, status, episode)

    }

    private fun generateNumber(): Int {
        val rand = (0..826).random()
        Log.d("Character Number", "The character number is " + rand.toString())
        return rand
    }

    private fun getCharacter() {
        val client = AsyncHttpClient()
        val rand = generateNumber()
        val link = "https://rickandmortyapi.com/api/character/" + rand.toString()
        client[link, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Character Fetching", "response successful")
                Log.d("Dog", "response successful$json")

                characterImgURL = json.jsonObject.getString("image")
                characterName =json.jsonObject.getString("name")
                characterStatus = json.jsonObject.getString("status")
                characterEpisode = json.jsonObject.getString("episode").substring(47, 49)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Character Error", "errorResponse")
            }
        }]
    }

    private fun getNextCharacter(button: Button, image: ImageView, status: TextView, episode: TextView) {
        button.setOnClickListener {

            getCharacter()

            Glide.with(this)
                .load(characterImgURL)
                .fitCenter()
                .into(image)

            //load status
            val statusText = "Status: " + characterStatus
            status.setText(statusText)
            
            //load episode
            val episodeText = "Episode: " + characterEpisode
            episode.setText(episodeText)

            checkAnswer()

        }
    }

    private fun checkAnswer() {

        val guess =  findViewById<EditText>(R.id.guessInput)
        val result = findViewById<ImageView>(R.id.result)

        if (guess.text.toString() == characterName) {
            result.setImageResource(R.drawable.correct)
        } else {
            result.setImageResource(R.drawable.wrong)
        }
    }

}