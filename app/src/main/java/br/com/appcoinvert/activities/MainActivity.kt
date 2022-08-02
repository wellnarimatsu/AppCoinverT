package br.com.appcoinvert.activities

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import br.com.appcoinvert.R
import br.com.appcoinvert.data.base.EndPoint
import br.com.appcoinvert.databinding.ActivityMainBinding
import br.com.appcoinvert.util.RetrofitInicializador
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var spFrom : Spinner
    private lateinit var spTo : Spinner
    private lateinit var btConvert : Button
    private lateinit var tvResult : TextView
    private lateinit var etValueFrom : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spFrom = findViewById(R.id.Spinner_from)
        spTo = findViewById(R.id.Spinner_to)
        btConvert = findViewById(R.id.btn_converter)
        tvResult = findViewById(R.id.text_result)
        etValueFrom = findViewById(R.id.Input)

        getCurencies()

        btConvert.setOnClickListener {
            convertMoney()
        }





    }
    fun convertMoney(){
        val retrofitClient = RetrofitInicializador.getRetrofitInstance("https://cdn.jsdelivr.net/")
        //Criando endPoint
        val endPoint = retrofitClient.create(EndPoint::class.java)

        endPoint.getCurencyRate(spFrom.selectedItem.toString(), spTo.selectedItem.toString()).enqueue(object :
            retrofit2.Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //entry set não retorno só string igual keyset, ele retorna chave/valor
                var data  = response.body()?.entrySet()?.find {it.key == spTo.selectedItem.toString() }
                val rate : Double = data?.value.toString().toDouble()


                val conversion = etValueFrom.text.toString().toDouble() * rate

                tvResult.setText(conversion.toString())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Não foi")
            }

        })

    }

    fun getCurencies(){
        val retrofitClient = RetrofitInicializador.getRetrofitInstance("https://cdn.jsdelivr.net/")
        //Criando endPoint
        val endPoint = retrofitClient.create(EndPoint::class.java)

        //Fazer  chamada
        endPoint.getCurrencies().enqueue(object : retrofit2.Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                var data = mutableListOf<String>()

                response.body()?.keySet()?.iterator()?.forEach{
                    data.add(it)
                }
                //colocando moeda comum de inicio
                val posBRL = data.indexOf("brl")
                val posUSD = data.indexOf("usd")

                //Vamos Populatular o spinner, criando um adapter

               val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item,data )

                spFrom.adapter = adapter
                spTo.adapter = adapter



                spFrom.setSelection(posBRL)
                spTo.setSelection(posUSD)




//            debug para analizarmos a chamada api //    println(data.count())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Não Foi")
            }

        })
    }





}