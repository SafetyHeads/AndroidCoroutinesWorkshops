package com.example.coroutinespresentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var console: TextView
    private lateinit var client: OkHttpClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        console = findViewById(R.id.console)

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        lifecycleScope
    }

    fun clear(v: View) {
        clear()
    }

    /**
     * Przydatne linki:
     * https://amitshekhar.me/blog/kotlin-coroutines
     * https://amitshekhar.me/blog/launch-vs-async-in-kotlin-coroutines
     * https://amitshekhar.me/blog/dispatchers-in-kotlin-coroutines
     * https://amitshekhar.me/blog/coroutinescope-vs-supervisorscope
     *
     * Flow:
     * https://kotlinlang.org/docs/flow.html
     * https://amitshekhar.me/blog/flow-api-in-kotlin
     */
    // 1.0
    fun agenda(v: View) {
        clear()
        appendWithBlank("Czym są Coroutines?")
        appendWithBlank("Dlaczego ich potrzebujemy?")
        appendWithBlank("Od czego zacząć?")
        appendWithBlank("Launch vs Async?")
        appendWithBlank("Czym są Dispatchers i po co nam one?")
        appendWithBlank("Czym są Scopes i po co nam one?")
        appendWithBlank("Obsługa błędów w Coroutines")
        appendWithBlank("Przykłady kodu")
        appendWithBlank("Przykładowa aplikacja")
    }

    // 2.0
    fun whatAreCoroutines(v: View) {
        clear()
        appendWithBlank("Czym są coroutines?")
        appendWithBlank("- Funkcje, które można zapauzować")
        appendWithBlank("- Zestaw narzędzi do zarządzania działaniami w tle")
        appendWithBlank("- Asynchroniczny kod pisany w synchroniczny sposób")
        appendWithBlank("- Kod uruchamiany w sposób bardziej wydajny względem klasycznych wątków")
        appendWithBlank("- Mogą działać równolegle")
        appendWithBlank("- Mogą działać po kolei")
        appendWithBlank("- Mogą się między sobą komunikować")
    }

//    Dzieje się w tle
//    fun main() {
//        a()
//        b()
//    }
//    suspend fun a() {
//        // flag 1
//        print("1")
//        // flag 2
//        print("3")
//        // flag 3
//        print("5")
//        // flag 4
//        print("7")
//    }
//
//    suspend fun b() {
//        // flag 1
//        print("2")
//        // flag 2
//        print("4")
//        // flag 3
//        print("6")
//        // flat 4
//        print("8")
//    }

    // 3.0
    fun whyWeNeedCoroutines(v: View) {
        clear()
        appendWithBlank("Dlaczego potrzebujemy coroutines?")
        appendWithBlank("Aplikacje komunikują się z siecią")
        appendWithBlank("Aplikacje wykonują operacje na plikach")
        appendWithBlank("Aplikacje wykonują operacje na bazach danych")
        appendWithBlank("Aplikacje wykorzystują bluetooth")
        appendWithBlank("Wszystkie te zadania są czasochłonne, a my chcemy zapewnić płynne działanie UI")
        appendWithBlank("Niektórych akcji nie da się wykonać na wątku głownym")
    }

    // 3.1
    fun fetchFromApiAndCrash(v: View) {
        client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/pikachu").build()).execute()
    }

    // 3.2
    fun fetchFromApiWithCallback(v: View) {
        line()
        append("Rozpoczynam pobieranie")
        client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/pikachu").build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val root = JsonParser.parseReader(response?.body?.charStream())
                console.post {
                    append(root.asJsonObject.get("name").asString)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                console.post {
                    append("Nie udało się pobrać danych")
                }
            }
        })
    }

    // 3.3
    fun fetchFromApiWithCallbackHell(v: View) {
        line()
        append("Rozpoczynam pobieranie")
        client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/pikachu").build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val root1 = JsonParser.parseReader(response.body?.charStream())
                console.post {
                    append("pokemon #1: ${root1.asJsonObject.get("name")}")
                }

                console.post {
                    append("Rozpoczynam pobieranie #2")
                }
                client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/raichu").build()).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val root2 = JsonParser.parseReader(response.body?.charStream())
                        console.post {
                            append("pokemon #2: ${root2.asJsonObject.get("name")}")
                        }

                        client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/raichu").build()).enqueue(object : Callback {
                            override fun onResponse(call: Call, response: Response) {
                                val root2 = JsonParser.parseReader(response.body?.charStream())
                                console.post {
                                    append("pokemon #2: ${root2.asJsonObject.get("name")}")
                                }

                                client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/raichu").build()).enqueue(object : Callback {
                                    override fun onResponse(call: Call, response: Response) {
                                        val root2 = JsonParser.parseReader(response.body?.charStream())
                                        console.post {
                                            append("pokemon #2: ${root2.asJsonObject.get("name")}")
                                        }

                                        client.newCall(Request.Builder().get().url("https://pokeapi.co/api/v2/pokemon/raichu").build()).enqueue(object : Callback {
                                            override fun onResponse(call: Call, response: Response) {
                                                val root2 = JsonParser.parseReader(response.body?.charStream())
                                                console.post {
                                                    append("pokemon #2: ${root2.asJsonObject.get("name")}")
                                                }
                                            }

                                            override fun onFailure(call: Call, e: IOException) {
                                                console.post {
                                                    append("Nie udało się pobrać danych #2")
                                                }
                                            }
                                        })
                                    }

                                    override fun onFailure(call: Call, e: IOException) {
                                        console.post {
                                            append("Nie udało się pobrać danych #2")
                                        }
                                    }
                                })
                            }

                            override fun onFailure(call: Call, e: IOException) {
                                console.post {
                                    append("Nie udało się pobrać danych #2")
                                }
                            }
                        })
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        console.post {
                            append("Nie udało się pobrać danych #2")
                        }
                    }
                })
            }

            override fun onFailure(call: Call, e: IOException) {
                console.post {
                    append("Nie udało się pobrać danych #1")
                }
            }
        })
    }

    // 4.0
    fun whereToStart(v: View) {
        clear()
        appendWithBlank("Od czego zacząć? Wystarczy dodać do build.gradle (Module: app) następujące wpisy:")
        append("implementation \"org.jetbrains.kotlinx:kotlinx-coroutines-core:x.x.x\"")
        append("implementation \"org.jetbrains.kotlinx:kotlinx-coroutines-android:x.x.x\"")
        append("Od teraz jesteśmy gotowi na pracę z Coroutines :)")

        appendWithBlank("Dodatkowo:")
        append("androidx.lifecycle:lifecycle-runtime-ktx:x.x.x - dla obsługi lifecycle")
        append("androidx.lifecycle:lifecycle-viewmodel-ktx:x.x.x - dla obsługi viewmodeli")
        append("androidx.lifecycle:lifecycle-livedata-ktx:x.x.x - dla rozszerzonej obsługi livedata")
    }

    // 5.0
    fun launchVsAsync(v: View) {
        clear()
        appendWithBlank("Launch wykonuje się po kolei w danej korutynie")
        append("Launch działa na zasadzie Fire and forget")
        append("Nieobsłużone błędy crashują appkę")
        append("Może zwracać wartości ale jest mniej zalecane")

        appendWithBlank("Async umożliwia nam wykonywanie obliczeń równolegle")
        append("Preferowany sposób uruchamiania dla korutyn zwracających wartość")
        append("Zwraca obiekt Deferred zawierający wartość lub błąd")
        append("Nieobsłużone błędy nie crashują appki ale mogą zatrzymać korutynę")
    }

    // 5.1 Podstawowa korutyna z delay
    fun simpleLaunch(v: View) {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                append("start")
            }
            delay(5000)
            withContext(Dispatchers.Main) {
                append("end")
            }
        }
    }

    // 5.2 Launch x2 i zsumowanie wartości
    fun simpleLaunchTimer(v: View) {
        GlobalScope.launch {
            val start = System.currentTimeMillis()
            val a = a()
            val b = b()

            append("Suma: ${a + b}")
            val end = System.currentTimeMillis()
            append("Finished at: ${end - start}")
        }
    }

    suspend fun a() : Int {
        delay(1000)
        return 10
    }

    suspend fun b() : Int {
        delay(2000)
        return 5
    }

    // 5.3 Async x2 i zsumowanie wartości - różnica w szybkości obliczenia
    fun simpleAsyncTimer(v: View) {
        lifecycleScope.launch {
            val start = System.currentTimeMillis()
            val a = async {
                delay(1000)
                return@async 10
            }
            val b = async {
                delay(2000)
                return@async 5
            }

            append("Suma: ${a.await() + b.await()}")
            val end = System.currentTimeMillis()
            append("Finished at: ${end - start}")
        }
    }

    suspend fun a1() : Int {
        delay(1000)
        return 10
    }

    suspend fun b1() : Int {
        delay(2000)
        return 5
    }

    // 6.0
    fun whatAreDispatchers(v: View) {
        clear()
        appendWithBlank("W coroutines mamy trzy główne Scope`y")
        append("Biblioteki wspierające Coroutines z założenia same decydują jaki jest dla nich najlepszy")

        appendWithBlank("Dispatchers.Default")
        append("Operacje na bitmapach")
        append("Operacje na dużych kolekcjach")
        append("Parsowanie JSONów")

        appendWithBlank("Dispatchers.Main")
        append("Zadania związane z UIem")
        append("Operacje na małych kolekcjach")

        appendWithBlank("Dispatchers.Io")
        append("Zapytania sieciowe")
        append("Otwieranie i pisanie do plików")
        append("Wysyłanie zapytań do baz danych")
    }

    // 6.1 launch z dispatcherem
    fun dispatchersWithLaunch(v: View) {
        line()
        lifecycleScope.launch(Dispatchers.IO) {
            append("Start!")
            delay(1000)
            append("Dziala!")
        }
    }

    // 6.2 przykład withContext
    fun dispatchersWithContext(v: View) {
        line()
        lifecycleScope.launch() {
            withContext(Dispatchers.IO) {
                append("Start!")
                delay(1000)
                append("Dziala!")
            }
        }
    }

    // 7.0
    fun whatAreScopes(v: View) {
        clear()
        appendWithBlank("Scope to określenie cyklu życia danej korutyny")
        append("Mamy GlobalScope, który żyje razem z appką ale nie zaleca się go używać")
        append("Mamy viewModelScope, który żyje tak długo jak ViewModel")
        append("Mamy lifecycleScope, który żyje tak długo jak nasza Activity / Fragment")


    }

    // 8.0
    fun errorHandling(v: View) {
        clear()
        appendWithBlank("Error handling w coroutines to ich największa wada")
        append("Używamy zwykłego try catch")
        append("Jest dosyć nieintuicyjny")
        append("Inaczej obsługujemy launch a inaczej async")
        append("Alternatywnie dla try catch możemy użyć handlery")
    }

    // 8.1 launch z crashem
    fun launchWithCrash(v: View) {
        lifecycleScope.launch {
            abc()
        }
    }

    suspend fun abc() {
        delay(1000)
        throw RuntimeException("Oh no!")
    }

    // 8.2 launch try catch
    fun launchWithTryCatch(v: View) {
        lifecycleScope.launch {
            try {
                abc()
            } catch (e : Exception) {
                append("Przechwycono błąd")
            }
        }
    }

    // 8.3 launch z handlerem
    fun launchWithHandler(v: View) {
        lifecycleScope.launch(CoroutineExceptionHandler { _, e ->
            append("Przechwycono błąd")
        }) {
            abc()
        }
    }

    // 8.4 async z crashem
    fun asyncWithCrash(v: View) {
        lifecycleScope.launch {
            val result = async { getValue() }
            append(result.await().toString())
        }
    }

    suspend fun getValue() : Int {
        delay(1000)
        throw RuntimeException("Oh no!")
    }

    suspend fun return10() : Int {
        delay(2000)
        return 10
    }

    // 8.5 async z try catch
    fun asyncWithTryCatch(v: View) {
        lifecycleScope.launch {
            val result = async { getValue() }

            try {
                append(result.await().toString())
            } catch(e: Exception) {
                append("Zlapalem blad!")
            }
        }
    }

    // 8.6 async z coroutine scope try { coroutineScope {...}}
    fun asyncWithCoroutineScope(v: View) {
        lifecycleScope.launch {
            try {
                coroutineScope {
                    val result = async { getValue() }
                    val ten = async { return10() }
                    append(result.await().toString())
                    append(ten.await().toString())
                }
            } catch(e: Exception) {
                append("Zlapalem blad!")
            }
        }
    }

    // 8.7 async z supervisor scope supervisorScope { try...try...}
    fun asyncWithSupervisorScope(v: View) {
        lifecycleScope.launch {
            try {
                supervisorScope {
                    val error = async { getValue() }
                    val ten = async { return10() }

                    try {
                        append(error.await().toString())
                    } catch (e: Exception) {
                        append("result rzucil blad")
                    }

                    try {
                        append(ten.await().toString())
                    } catch (e: Exception) {
                        append("ten rzucil blad")
                    }
                }
            } catch(e: Exception) {
                append("Zlapalem blad!")
            }
        }
    }

    // 9.0 Flow
    // 9.1 Prosta flow
    fun mySimpleFlow(): Flow<Int> = flow {
        for (i in 1..10) {
            delay(1000)
            emit(i)
        }
    }

    fun simpleFlow(v: View) {
        lifecycleScope.launch {
            mySimpleFlow()
                .collect { ourValue ->
                    append(ourValue.toString())
                }
        }
    }

    // 9.2 Flow z try catch
    fun mySimpleFlowWithError(): Flow<Int> = flow {
        for (i in 1..10) {
            if (i > 5) {
                throw RuntimeException("OH NO!")
            }
            delay(1000)
            emit(i)
        }
    }
    fun tryCatchFlow(v: View) {
        lifecycleScope.launch {
            try {
                mySimpleFlowWithError()
                    .collect { ourValue ->
                        append(ourValue.toString())
                    }
            } catch(e: java.lang.Exception) {
                append("Wystapil blad!")
            }
        }
    }

    // 9.3 Flow z .catch{}
    fun onErrorFlow(v: View) {
        lifecycleScope.launch {
            mySimpleFlowWithError()
                .catch {
                    append("Wystapil blad!")
                }
                .collect { ourValue ->
                    append(ourValue.toString())
                }
        }
    }

    // 9.4 Flow .onContext()
    fun flowOnFlow(v: View) {
        lifecycleScope.launch {
            mySimpleFlowWithError()
                .flowOn(Dispatchers.IO)
                .collect { ourValue ->
                    append(ourValue.toString())
                }
        }
    }

    // 9.5 Flow timing
    var start = System.currentTimeMillis()
    fun delayedFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            append("emitted $i at ${System.currentTimeMillis() - start} ms")
            emit(i)
        }
    }
    fun flowTiming(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            delayedFlow()
                .collect {
                    delay(300)
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.6 Buffer (emit co 100ms collect co 300)
    fun bufferFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            delayedFlow()
                .buffer()
                .collect {
                    delay(300)
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.7 Conflate (emit co 100ms collect co 300)
    fun conflateFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            delayedFlow()
                .conflate()
                .collect {
                    delay(300)
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.8 map filter distinctUntilChanged
//    fun operatorsFlow(v: View) {
//        start = System.currentTimeMillis()
//        lifecycleScope.launch {
//            delayedFlow()
//                .map {
//                    it * 100
//                }
//                .collect {
//                    delay(300)
//                    append("$it at ${System.currentTimeMillis() - start} ms")
//                }
//        }
//    }

//        fun operatorsFlow(v: View) {
//            start = System.currentTimeMillis()
//            lifecycleScope.launch {
//                delayedFlow()
//                    .filter {
//                        it > 1
//                    }
//                    .collect {
//                        delay(300)
//                        append("$it at ${System.currentTimeMillis() - start} ms")
//                    }
//            }
//        }

    fun myDuplicateFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
            emit(i)
        }
    }
    fun operatorsFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            delayedFlow()
                .onEach {
                    append("emitted $it at ${System.currentTimeMillis() - start} ms")
                }
                .distinctUntilChanged()
                .collect {
                    delay(300)
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.9 Flow działają sekwencyjnie (filter, map, collect z logami)
    fun sequentialFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            delayedFlow()
                .map {
                    append("Mapujemy $it")
                    it * 100
                }
                .filter {
                    append("Filtrujemy $it")
                    it > 100
                }
                .collect {
                    delay(300)
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.10 Zip
    fun flowA(): Flow<Int> = flow {
        for (i in 1..5) {
            delay(100)
            append("emitted A $i at ${System.currentTimeMillis() - start} ms")
            emit(i)
        }
    }

    fun flowB(): Flow<Int> = flow {
        for (i in 10..15) {
            delay(300)
            append("emitted B $i at ${System.currentTimeMillis() - start} ms")
            emit(i)
        }
    }

    fun zipFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            flowA()
                .zip(flowB()) { a, b ->
                    "$a,$b"
                }
                .collect {
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.11 Combine
    fun combineFlow(v: View) {
        start = System.currentTimeMillis()
        lifecycleScope.launch {
            flowA()
                .combine(flowB()) { a, b ->
                    "$a,$b"
                }
                .collect {
                    append("$it at ${System.currentTimeMillis() - start} ms")
                }
        }
    }

    // 9.12 FlatMap
    suspend fun getFromDatabase(key: String): Flow<Int> = flow {
        delay(1000)
        emit(key.length)
    }.flowOn(Dispatchers.IO)

    fun words(): Flow<String> = listOf(
        "a",
        "ab",
        "abc",
        "abcd",
        "abcde",
    ).asFlow()

    fun flatMap(v: View) {
        lifecycleScope.launch {
            words()
                .flatMapConcat {
                    getFromDatabase(it)
                }
                .collect {
                    append("Emit: $it")
                }
        }
    }

    // 9.13 wykrywanie zakonczenia flow (try catch finally)
    fun checkCoroutineEnd(v: View) {
        lifecycleScope.launch {
            try {
                delayedFlow()
                    .collect {
                        append("Emit: $it")
                    }
            } catch (e: java.lang.Exception) {

            } finally {
                append("Finally")
            }
        }
    }

    // 9.14 wykrywanie stanu flow (operatory .on...)
    fun checkCoroutineState(v: View) {
        lifecycleScope.launch {
            delayedFlow()
                .onStart { append("Start") }
                .onEach { append("Wyemitowano $it") }
                .onCompletion { append("Complete") }
                .collect {
                    append("Emit: $it")
                }
        }
    }

    // 9.15 StateFlow i po co nam jest
    // 9.16 SharedFlow i po co nam jest
    var counter = 10
    var sharedFlow = MutableSharedFlow<Int>()
    var stateFlow = MutableStateFlow(0)
    fun hotFlow(v: View) {
        lifecycleScope.launch {
            stateFlow.emit(counter)
            counter++
        }
    }

    // Odkomentujcie ten kod, żeby przetestować 9.15 i 9.16 :)


//    override fun onResume() {
//        super.onResume()
//        lifecycleScope.launch {
//            stateFlow.collect{
//                append("#1 ${it.toString()}")
//            }
//        }
//        lifecycleScope.launch {
//            stateFlow.collect{
//                append("#2 ${it.toString()}")
//            }
//        }
//    }

    private fun append(line: String) {
        console.post {
            console.text = console.text.toString() + line + "\n"
        }
    }

    private fun appendWithBlank(line: String) {
        console.post {
            console.text = console.text.toString() + "\n" + line + "\n"
        }
    }

    private fun line() {
        console.post {
            console.append("//////////////\n")
        }
    }

    private fun clear() {
        console.post {
            console.text = ""
        }
    }
}