package com.example.seucep

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.ProgressBar
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Carregamento : AppCompatActivity() {

    // chamando os textview da activity_carregamento.xml
    private lateinit var txtCep: TextView
    private lateinit var txtLogradouro: TextView
    private lateinit var txtBairro: TextView
    private lateinit var txtLocalidade: TextView
    private lateinit var txtUf: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_carregamento)

        findViewById<Button>(R.id.btnFechar).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


// Inicializar os TextViews
        txtCep = findViewById(R.id.txtCep)
        txtLogradouro = findViewById(R.id.txtLogradouro)
        txtBairro = findViewById(R.id.txtBairro)
        txtLocalidade = findViewById(R.id.txtLocalidade)
        txtUf = findViewById(R.id.txtUf)

        //chamada de API no aplicativo
        val cepService = RetrofitClient.cepService
        val call = cepService.getCepDetails("06447380")

        // ProgressBar começa aqui
        val progressBar = findViewById<ProgressBar>(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        call.enqueue(object  : Callback<CepResponse> {
            override fun onResponse(call: Call<CepResponse>, response: Response<CepResponse>) {

                // ProgressBar para aqui
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val cepResponse = response.body()

                    Log.d("DetalhesCEP", "CEP: ${cepResponse?.cep}")
                    Log.d("DetalhesCEP", "Logradouro: ${cepResponse?.logradouro}")
                    Log.d("DetalhesCEP", "Bairro: ${cepResponse?.bairro}")
                    Log.d("DetalhesCEP", "Localidade: ${cepResponse?.localidade}")
                    Log.d("DetalhesCEP", "UF: ${cepResponse?.uf}")

                    // Faça algo com os detalhes do CEP
                    if (cepResponse != null) {
                        // Exibir os detalhes do CEP em um diálogo de alerta
                        exibirDetalhesCepDialog(cepResponse)
                    } else {
                        // Resposta vazia ou inválida, trate aqui
                    }

                } else {
                    // Trate erros aqui
                }
            }

            override fun onFailure(call: Call<CepResponse>, t: Throwable) {

                // Parar a ProgressBar aqui
                progressBar.visibility = View.GONE


                // Trate falhas na requisição aqui

                // "Esconder" os TextViews novamente
                txtCep.visibility = View.VISIBLE
                txtLogradouro.visibility = View.INVISIBLE
                txtBairro.visibility = View.INVISIBLE
                txtLocalidade.visibility = View.INVISIBLE
                txtUf.visibility = View.INVISIBLE

            }
        })
    }



    @SuppressLint("SetTextI18n")
    private fun exibirDetalhesCepDialog(cepResponse: CepResponse) {

        txtCep.text = "CEP: ${cepResponse.cep}"
        txtLogradouro.text = "Logadouro: ${cepResponse.logradouro}"
        txtBairro.text = "Bairro: ${cepResponse.bairro}"
        txtLocalidade.text = "Localidade: ${cepResponse.localidade}"
        txtUf.text = "UF: ${cepResponse.uf}"

        // Exibir os TextViews alterando a visibilidade
        txtCep.visibility = View.VISIBLE
        txtLogradouro.visibility = View.VISIBLE
        txtBairro.visibility = View.VISIBLE
        txtLocalidade.visibility = View.VISIBLE
        txtUf.visibility = View.VISIBLE

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Detalhes do CEP: ${cepResponse.cep}")
        dialogBuilder.setMessage(
            "Logradouro: ${cepResponse.logradouro}\n" +
                    "Bairro: ${cepResponse.bairro}\n" +
                    "Localidade: ${cepResponse.localidade}\n" +
                    "UF: ${cepResponse.uf}"
        )
        dialogBuilder.setPositiveButton("OK", null)
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    // modelo de dados para representar os detalhes do cep
    data class CepResponse(
        val cep: String,
        val logradouro: String,
        val bairro: String,
        val localidade: String,
        val uf: String
    )

    //interface para definir os endpoints da API usando a biblioteca Retrofit
    interface CepService {
        @GET("/ws/{cep}/json/")
        fun getCepDetails(@Path("cep") cep: String): Call<CepResponse>
    }


    //instância do Retrofit e configurado o cliente OkHttp.
    object RetrofitClient {
        private const val BASE_URL = "https://viacep.com.br/"

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val cepService: CepService = retrofit.create(CepService::class.java)
    }





    class Anime{
        private fun fadeIn(view: View){
            val animacao = AlphaAnimation(0f,1f)
            animacao.duration = 500L
            view.startAnimation(animacao)
        }
        private fun fadeOut(view: View){
            val animacao = AlphaAnimation(1f,0f)
            animacao.duration = 500L
            view.startAnimation(animacao)
        }
        @Suppress("DEPRECATION")
        fun tradeView(view1: View, view2: View){
            fadeOut(view1)
            Handler().postDelayed({
                view1.visibility = View.INVISIBLE
                view2.visibility = View.INVISIBLE
                fadeIn(view2)
            },500L)
        }
    }
}