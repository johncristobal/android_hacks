package com.android2025.tips.todo

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android2025.tips.todo.data.TodoDao
import com.android2025.tips.todo.data.TodoDatabase
import com.android2025.tips.todo.data.TodoEntity
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TodoDaoTest {

    private lateinit var db: TodoDatabase
    private lateinit var dao: TodoDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, TodoDatabase::class.java).build()
        dao = db.todoDao()
    }

    @After
    fun tearDown() = db.close()

    @Test
    fun insertAndObserveOrdersOpenBeforeDoneThenNewestFirst() = runBlocking {
        val a = dao.insert(TodoEntity(title = "A", description = "", createdAt = 1))
        dao.insert(TodoEntity(title = "B", description = "", createdAt = 2))
        dao.insert(TodoEntity(title = "C", description = "", createdAt = 3))
        dao.setDone(a, true)

        assertThat(dao.observeAll().first().map { it.title })
            .containsExactly("C", "B", "A").inOrder()
    }

    @Test
    fun updateContentKeepsDoneFlag() = runBlocking {
        val id = dao.insert(TodoEntity(title = "Old", description = "", isDone = true))

        dao.updateContent(id, "New", "Desc")

        val item = dao.getById(id)!!
        assertThat(item.title).isEqualTo("New")
        assertThat(item.description).isEqualTo("Desc")
        assertThat(item.isDone).isTrue()
    }

    @Test
    fun deleteAndRestoreKeepsSameId() = runBlocking {
        val id = dao.insert(TodoEntity(title = "X", description = ""))
        val original = dao.getById(id)!!

        dao.delete(id)
        assertThat(dao.getById(id)).isNull()

        dao.insert(original)
        assertThat(dao.getById(id)).isEqualTo(original)
    }
}
