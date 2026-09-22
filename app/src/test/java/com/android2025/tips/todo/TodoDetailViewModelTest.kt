package com.android2025.tips.todo

import com.android2025.tips.todo.domain.Todo
import com.android2025.tips.todo.ui.detail.TodoDetailState
import com.android2025.tips.todo.ui.detail.TodoDetailViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoDetailViewModelTest {

    private val existing = Todo(1, "Milk", "2 litres", isDone = true, createdAt = 1)
    private val dispatcher = UnconfinedTestDispatcher()
    private val appScope = CoroutineScope(SupervisorJob() + dispatcher)

    @Before
    fun setUp() = Dispatchers.setMain(dispatcher)

    @After
    fun tearDown() = Dispatchers.resetMain()

    private fun viewModel(id: Long?, repo: FakeTodoRepository) =
        TodoDetailViewModel(id, repo, appScope)

    @Test
    fun `new todo starts loaded without original`() {
        val vm = viewModel(null, FakeTodoRepository())
        assertThat(vm.state.value).isEqualTo(TodoDetailState.Loaded(original = null))
    }

    @Test
    fun `existing todo is loaded`() {
        val vm = viewModel(1, FakeTodoRepository(listOf(existing)))
        assertThat(vm.state.value).isEqualTo(TodoDetailState.Loaded(existing))
    }

    @Test
    fun `unknown id ends in not found and save is a no-op`() {
        val repo = FakeTodoRepository()
        val vm = viewModel(42, repo)

        assertThat(vm.state.value).isEqualTo(TodoDetailState.NotFound)
        vm.save("x", "y")
        assertThat(repo.writeCount).isEqualTo(0)
    }

    @Test
    fun `new todo with title is created and trimmed`() {
        val repo = FakeTodoRepository()
        viewModel(null, repo).save("  Buy bread  ", "fresh")

        assertThat(repo.snapshot().map { it.title to it.description })
            .containsExactly("Buy bread" to "fresh")
    }

    @Test
    fun `new todo with blank title is discarded`() {
        val repo = FakeTodoRepository()
        viewModel(null, repo).save("   ", "some description")
        assertThat(repo.snapshot()).isEmpty()
    }

    @Test
    fun `edit saves title and description and keeps done state`() {
        val repo = FakeTodoRepository(listOf(existing))
        viewModel(1, repo).save("Oat milk", "1 litre")

        val saved = repo.snapshot().single()
        assertThat(saved.title).isEqualTo("Oat milk")
        assertThat(saved.description).isEqualTo("1 litre")
        assertThat(saved.isDone).isTrue()
    }

    @Test
    fun `blank title on existing todo keeps old title but saves description`() {
        val repo = FakeTodoRepository(listOf(existing))
        viewModel(1, repo).save("  ", "changed")

        val saved = repo.snapshot().single()
        assertThat(saved.title).isEqualTo("Milk")
        assertThat(saved.description).isEqualTo("changed")
    }

    @Test
    fun `unchanged content does not write`() {
        val repo = FakeTodoRepository(listOf(existing))
        viewModel(1, repo).save("Milk", "2 litres")
        assertThat(repo.writeCount).isEqualTo(0)
    }

    @Test
    fun `saving twice only writes once`() {
        val repo = FakeTodoRepository()
        val vm = viewModel(null, repo)
        vm.save("Once", "")
        vm.save("Once", "")
        assertThat(repo.snapshot()).hasSize(1)
    }
}
