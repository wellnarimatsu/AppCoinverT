package br.com.appcoinvert.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import br.com.appcoinvert.R
import br.com.appcoinvert.data.base.EndPoint
import br.com.appcoinvert.databinding.ActivityMainBinding
import br.com.appcoinvert.util.RetrofitInicializador
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response




class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getCurencies()

        binding.btnConverter.setOnClickListener {
            convertMoney()
        }





    }

    fun getCurencies(){
        val retrofitClient = RetrofitInicializador.getRetrofitInstance("https://cdn.jsdelivr.net/")
        //Criando endPoint
        val endPoint = retrofitClient.create(EndPoint::class.java)

        //Fazer  chamada
        endPoint.getCurrencies().enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                var data = mutableListOf<String>()

                response.body()?.keySet()?.iterator()?.forEach{
                    data.add(it)
                }

                //Vamos Populatular o spinner, criando um adapter

               val adapter = ArrayAdapter(baseContext, android.R.layout.simple_spinner_dropdown_item,data )

                binding.SpinnerFrom.adapter = adapter
                binding.SpinnerTo.adapter = adapter

                //colocando moeda comum de inicio
                val posBRL = data.indexOf("brl")
                val posUSD = data.indexOf("usd")

                binding.SpinnerFrom.setSelection(posBRL)
                binding.SpinnerTo.setSelection(posUSD)




//            debug para analizarmos a chamada api //    println(data.count())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Não Foi")
            }

        })
    }

    fun convertMoney(){
        val retrofitClient = RetrofitInicializador.getRetrofitInstance("https://cdn.jsdelivr.net/")
        //Criando endPoint
        val endPoint = retrofitClient.create(EndPoint::class.java)

        endPoint.getCurencyRate(binding.SpinnerFrom.toString(), binding.SpinnerTo.toString()).enqueue(object :
            retrofit2.Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                //entry set não retorno só string igual keyset, ele retorna chave/valor
                val data  = response.body()?.entrySet()?.find {it.key == binding.SpinnerTo.selectedItem.toString() }
                val rate : Double = data?.value.toString().toDouble()
                val conversion = binding.Input.text.toString().toDouble() * rate

                binding.textResult.setText(conversion.toString())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                println("Não foi")
            }

        })

    }

}