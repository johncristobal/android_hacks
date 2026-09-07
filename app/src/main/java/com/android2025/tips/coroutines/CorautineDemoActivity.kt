package com.android2025.tips.coroutines

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.android2025.tips.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class CorautineDemoActivity : AppCompatActivity() {
    private val api: MyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MyApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_corautine_demo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    // 1. Intro GlobalScope
    private fun intro() {
        /*
            Toda corrutina se debe lanzar en un scope
            GlobalScope => scope general de la app - NO Bloquea main UI
            Thread.current para saber que hilo trabaja
            - Fuera de scope = main thread
            - Dentro de scope = hilo de la app (

            delay (suspend) => para hacer una pausa en la corrutina (pero sigue trabajando)
                - solo se usa dentro de scope - corutina0
                - toda llamada aqui se ejecuta la mismo tiempo
            sleep => para hacer una pausa en el hilo (no sigue trabajando)
         */

        GlobalScope.launch {
            delay(3000L)
            val response = networkCall()
            val response2 = networkCall()
        }
    }

    suspend fun networkCall(): String {
        delay(3000L)
        return "Hello World!"
    }

    // 2. Contexto de corrutina
    private fun changeContext() {
        /*
           Contexto de corrutina
           Dispatchers.Main => hilo principal de la app
           Dispatchers.IO => para llamaras a servicios
           Dispatchers.Default => para llamaras a funciones pesadas (fibonnaci)

           withContext => para cambiar de contexto de corrutina
        */
        GlobalScope.launch(Dispatchers.IO) {

            withContext(Dispatchers.Main) {

            }
        }
    }

    // 3. runBlocking
    private fun runBlockingDemo() {

        /*
            Este si bloquea el hilo main
            Como su llamaras Thread.sleep, se bloquea el hilo main

            launch => lanzar una corrutina async dentro de un contexto
                puedes lanzar varias, todas son aync, se llaman al mismo tiempo
         */

        runBlocking {
            delay(1000L)
            launch(Dispatchers.IO) {
                delay(3000L)
            }
        }
    }

    // 4. jobs
    private fun jobWaitCancel() {
        /*
            jobs => definimos var tipo coroutin
                join - para esperar a que termine la corrutina
                cancel - para cancelar la corrutina

            withTimeout
                - usamos runBloickin para cancelar manualemtne
                - pero con withTimeout cancela automaticamente si se pasa el tiempo
         */
        val job = GlobalScope.launch(Dispatchers.Default) {

            withTimeout(3000L) {
                if (isActive) {

                }
            }
        }

        runBlocking {
            job.join()
            delay(2000L)
            job.cancel()
        }
    }

    // 5. async & await
    private fun asyncAwait() {
        GlobalScope.launch(Dispatchers.IO) {

            // esto seria una forma, pero no la mejor
            val job1 = launch { val ans1 = networkcall1() }
            val job2 = launch { val ans2 = networkcall2() }
            job1.join()
            job2.join()

            // ahora async - retorna un Deferred<T>
            // asn1.await() => para esperar a que termine la corrutina
            val ans1 = async { networkcall1() }
            val ans2 = async { networkcall2() }
            ans1.await()
            ans2.await()
        }
    }

    suspend fun networkcall1(): String {
        delay(3000L)
        return "Answer 1"
    }

    suspend fun networkcall2(): String {
        delay(3000L)
        return "Answer 2"
    }

    // 6 lifescope and viewmodelscope
    private fun lifeAndviewmodel() {
        /*
            Globalscope es para main, pero
            casi nunca se usa, si la activity se destruye, el scope sigue vivo

            usamos lifecuclescoe and viewmodelscoep
            para que cuanod un activity o voewmodel termine, se termine el task
         */

        lifecycleScope.launch {

        }
    }

    // 7. firebase
    private fun usingFirebase() {
        /*
        implementation("com.google.firebase:firebase-firestore-ktx:25.1.4")
        usamos await para obrtner data,
        OJO con los contextos
         */
        val doc = Firebase.firestore.collection("users")
            .document("user1")
        val peter = Person("peter", 25)

        GlobalScope.launch {
            delay(3000L)
            doc.set(peter).await()
            val person = doc.get().await().toObject(Person::class.java)
            withContext(Dispatchers.Main) {
                // update UI
            }
        }
    }

    // 8. retrofit
    private fun retrofitDemo() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = api.getComments()
            if(response.isSuccessful) {
                val data = response.body()!!
                withContext(Dispatchers.Main) {
                    // update UI
                }
            }
        }
    }

    // 9. error
    private fun errorHandling() {

    }

}

data class Person(
    val name: String,
    val age: Int
)

data class Comments(
    val id: Int,
    val body: String
)

interface MyApi {
    @GET("/comments")
    suspend fun getComments(): Response<List<Comments>>
}
