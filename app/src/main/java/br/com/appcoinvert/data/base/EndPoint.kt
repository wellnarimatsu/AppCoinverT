package br.com.appcoinvert.data.base

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EndPoint {
//URL RELATIVA, NÃO A ABSOLUTA
    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies.json")
    fun getCurrencies() : Call<JsonObject>


    //Vamos criar o método para a segunda consulta, que é a de conversão
    //Obs: Fizemos uma substituição na url "from/to" para sabermos qual coin será convertida
    //Mas precisamos referencia isso dentro da nossa função

    @GET("/gh/fawazahmed0/currency-api@1/latest/currencies/{from}/{to}.json")
    //utilizar o decorator path para conseguirmos definir que o que foi substituido
    fun getCurencyRate(
        @Path(value = "from", encoded = true) from: String,
        @Path(value = "to", encoded = true) to: String
    ) : Call<JsonObject>

}