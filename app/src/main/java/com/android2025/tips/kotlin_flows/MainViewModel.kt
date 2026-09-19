package com.android2025.tips.kotlin_flows

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android2025.tips.kotlin_flows.models.Post
import com.android2025.tips.kotlin_flows.models.ProfileState
import com.android2025.tips.kotlin_flows.models.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class MainViewModel: ViewModel() {

    private val _stateFlow = MutableStateFlow(0)
    val stateFlow = _stateFlow.asStateFlow()

    private val _sharedFlow = MutableSharedFlow<Int>(0)
    val sharedFlow = _sharedFlow.asSharedFlow()

    val countDownFlow = flow<Int> {
        val start = 5
        var currentValue = start
        emit(start)

        while(currentValue > 0) {
            delay(1000L.milliseconds)
            currentValue--
            emit(currentValue)
        }
    }

    /*
    Combine, Zip & Merge
        Combine: combine the flow with another flow
            when one of the flows changes, the combine function will be called

        Zip: combine the flow with another flow
            when BOTH flows change, the zip function will be called

        Merge: merge as many flows as you want
            when ONE of the flows changes, the merge function will be called

     */

    private val isAuthenticated = MutableStateFlow(true)
    private val user = MutableStateFlow<User?>(null)
    private val posts = MutableStateFlow(emptyList<Post>())

    private val _profileState = MutableStateFlow<ProfileState?>(null)
    val profileState = _profileState.asStateFlow()

    init {
//        user.combine(posts) { user, posts ->
//            _profileState.value = profileState.value?.copy(
//                profilePicUrl = user?.profilePicUrl,
//                username = user?.username,
//                description = user?.description,
//                posts = posts
//            )
//        }.combine(isAuthenticated) { posts, auth ->
//            if (auth) { _profileState.value } else { null }
//        }.launchIn(viewModelScope)

        isAuthenticated.combine(user) { isAuthenticated, user ->
            if (isAuthenticated) user else null
        }.combine(posts) { user, posts ->
            user?.let {
                _profileState.value = profileState.value?.copy(
                    profilePicUrl = user.profilePicUrl,
                    username = user.username,
                    description = user.description,
                    posts = posts
                )
            }
        }.launchIn(viewModelScope)

        /*
            .launchIn(viewModelScope)
                equal to
            viewmodelScope.launch {

            }
         */

//        collectFlow()
//        squareNumber(3)
        viewModelScope.launch {
            sharedFlow.collect {
                delay(2000L)
                println("FIRST FLOW: The received number is $it")
            }
        }

        viewModelScope.launch {
            sharedFlow.collect {
                delay(3000L)
                println("SECOND FLOW: The received number is $it")
            }
        }


    }

    /*
    StateFlow vs SharedFlow
    - StateFlow is a state holder and a hot flow
        save data across configuration changes
        collector receives the latest value

     - SharedFlow is a hot flow
        a flow that can be shared between multiple consumers
        one-time events: navigate, show snackbar, show dialog, etc.
     */

    fun incrementCounter() {
        _stateFlow.value += 1
    }

    fun squareNumber(number: Int) {
        viewModelScope.launch {
            _sharedFlow.emit(number * number)
        }
    }

    // collect: flow to list
    //  - Useful to get data from a flow
    // collectLatest: cancel flow and collect latest value
    //  - Useful to get the latest value from a flow (update UI last item)

    /*
        flow operators:
        - filter - filter the flow based on a condition
        - map - map the flow to a new value
        - onEach - do something with the flow
        - take - take the first n items from the flow
        - takeWhile - take the items from the flow while a condition is true
        - skip - skip the first n items from the flow
        - skipWhile - skip the items from the flow while a condition is true
        - distinct - filter the flow to only emit distinct values
        - distinctUntilChanged - filter the flow to only emit distinct values

        Terminate flow operators:
        - count - count the number of items in the flow
        - reduce - reduce the flow to a single value
            iterate one value after another and reduce it to a single value
        - fold - reduce the flow to a single value with an initial value

        Flattening flows:
        - flatMapConcat - flatten the flow of flows into a single flow
            useful when, for example, we have a flow of network requests and we want to
            process the responses one by one
        - flatMapMerge - flatten the flow of flows into a single flow
        - flatMapLatest - flatten the flow of flows into a single flow
     */
    private fun collectFlow() {
        viewModelScope.launch {
            val count = countDownFlow
                .filter { time ->
                    time % 2 == 0
                }
                .map { time ->
                    time * time
                }
                .onEach { time ->
                    println(time)
                }
                .count {
                    it % 2 == 0
                }
//                .collect { time ->
//                    println("The current time is $time")
//                }

            println(count)

            val reduce = countDownFlow
//                .reduce { accumulator, value ->
//                    accumulator + value
//                }
                .fold(100) { accumulator, value ->
                    accumulator + value
                }
            println(reduce)

            val flow1 = (1..5).asFlow()

            flow1.flatMapConcat { value ->
                flow {
                    emit(value + 1)
                    delay(500L)
                    emit(value + 2)

                    // getRecipeId(id)
                }
            }.collect { value ->
                println("The value is $value")
            }

            val flow2 = flow {
                delay(250L)
                emit("Appetizer")
                delay(1000L)
                emit("Main dish")
                delay(100L)
                emit("Dessert")
            }

            flow2.onEach {
                println("FLOW: $it is delivered")
            }
            .conflate() // buffer() // collectLatest()
            .collect {
                println("FLOW: Now eating $it")
                delay(1500L)
                println("FLOW: Finished eating $it")
            }
        }
    }
}