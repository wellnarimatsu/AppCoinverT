package br.com.appcoinvert.util

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInicializador {

    //CRIANDO O UTILITÁRIO PARA INSTANCIAR NOSSO RETROFIT
    companion object {
        //Criamos uma função com o retorno do objeto retrofit
        fun getRetrofitInstance(path: String): Retrofit {


            return Retrofit.Builder()

                .baseUrl(path)
                  //Utilizamos o Gson para fazer a conversão de classes
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }


}