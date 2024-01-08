package com.example.tictactoeonline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictactoeonline.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlin.random.Random
import kotlin.random.nextInt

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOffline.setOnClickListener{
            createOfflineGame()
        }
        binding.playOnline.setOnClickListener{
            creatOnlineGame()
        }
        binding.JoinPlayOnline.setOnClickListener{
            joinOnlineGame()
        }
    }
    fun creatOnlineGame(){
        GameData.myID="X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.created,
                gameId = Random.nextInt(1000..9999).toString()
            )
        )
        startGame()
    }
    fun joinOnlineGame(){
        var gameId = binding.gameIdInput.text.toString()
        if(gameId.isEmpty()){
            binding.gameIdInput.setError("Please Enter game ID")
            return
        }
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model=it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.gameIdInput.setError("Please enter valid ID")
                }else{
                    model.gameStatus = GameStatus.joined
                    GameData.myID="O"
                    GameData.saveGameModel(model)
                    startGame()
                }
            }

    }
    fun createOfflineGame(){
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.joined
            )
        )
        startGame()
    }
    fun startGame(){
        startActivity(Intent(this,GameActivity::class.java))
    }
}