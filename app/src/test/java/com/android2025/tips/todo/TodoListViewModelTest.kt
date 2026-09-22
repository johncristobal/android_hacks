package com.android2025.tips.todo

import com.android2025.tips.todo.domain.Todo
import com.android2025.tips.todo.ui.list.TodoListViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {

    private val milk = Todo(1, "Milk", "", isDone = false, createdAt = 1)
    private val bank = Todo(2, "Bank", "call", isDone = false, createdAt = 2)

    @Before
    fun setUp() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun TodoListViewModel.keepActive() =
        uiState.onEach { }.launchIn(kotlinx.coroutines.CoroutineScope(Dispatchers.Main))

    @Test
    fun `emits todos and stops loading`() = runTest {
        val vm = TodoListViewModel(FakeTodoRepository(listOf(milk, bank)))
        vm.keepActive()

        assertThat(vm.uiState.value.isLoading).isFalse()
        assertThat(vm.uiState.value.todos.map { it.id }).containsExactly(2L, 1L).inOrder()
    }

    @Test
    fun `toggle marks done and moves item below open ones`() = runTest {
        val repo = FakeTodoRepository(listOf(milk, bank))
        val vm = TodoListViewModel(repo)
        vm.keepActive()

        vm.onToggle(bank.id, true)

        assertThat(vm.uiState.value.todos.map { it.id }).containsExactly(1L, 2L).inOrder()
        assertThat(vm.uiState.value.todos.last().isDone).isTrue()
    }

    @Test
    fun `delete then undo restores the same item`() = runTest {
        val repo = FakeTodoRepository(listOf(milk, bank))
        val vm = TodoListViewModel(repo)
        vm.keepActive()

        vm.onDelete(bank)
        assertThat(vm.uiState.value.todos).containsExactly(milk)

        vm.onUndoDelete(bank)
        assertThat(vm.uiState.value.todos.map { it.id }).containsExactly(2L, 1L).inOrder()
    }
}
