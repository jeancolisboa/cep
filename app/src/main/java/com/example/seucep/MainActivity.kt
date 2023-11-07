package com.example.seucep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnSend).setOnClickListener {
            if (findViewById<EditText>(R.id.caixaCep).text.toString().length == 8) {
                val intent = Intent(this, Carregamento::class.java)
                intent.putExtra("cep", findViewById<EditText>(R.id.caixaCep).text.toString())
                startActivity(intent)
            } else {
                Toast.makeText(this, "O CEP contém 8 números", Toast.LENGTH_LONG).show()
            }
        }
    }
}