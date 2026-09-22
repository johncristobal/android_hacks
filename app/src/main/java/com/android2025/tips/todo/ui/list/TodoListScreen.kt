package com.android2025.tips.todo.ui.list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android2025.tips.R
import com.android2025.tips.todo.domain.Todo
import com.android2025.tips.ui.theme.HacksTheme
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TodoListRoute(
    onTodoClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    viewModel: TodoListViewModel = koinViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val deletedMessage = stringResource(R.string.todo_deleted)
    val undoLabel = stringResource(R.string.todo_undo)

    TodoListScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onTodoClick = onTodoClick,
        onAddClick = onAddClick,
        onToggle = viewModel::onToggle,
        onDelete = { todo ->
            viewModel.onDelete(todo)
            scope.launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                val result = snackbarHostState.showSnackbar(
                    message = deletedMessage,
                    actionLabel = undoLabel,
                    withDismissAction = true,
                )
                if (result == SnackbarResult.ActionPerformed) viewModel.onUndoDelete(todo)
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    state: TodoListUiState,
    snackbarHostState: SnackbarHostState,
    onTodoClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    onToggle: (id: Long, isDone: Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.todo_list_title)) }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.todo_add))
            }
        },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                state.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                state.hasError -> CenteredMessage(stringResource(R.string.todo_error))
                state.todos.isEmpty() -> CenteredMessage(stringResource(R.string.todo_empty))
                else -> TodoList(state.todos, onTodoClick, onToggle, onDelete)
            }
        }
    }
}

@Composable
private fun CenteredMessage(text: String) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Text(text, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun TodoList(
    todos: List<Todo>,
    onTodoClick: (Long) -> Unit,
    onToggle: (id: Long, isDone: Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // Leaves room so the last item is not hidden behind the FAB
        contentPadding = PaddingValues(bottom = 88.dp),
    ) {
        items(todos, key = { it.id }) { todo ->
            TodoRow(todo, onTodoClick, onToggle, onDelete)
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TodoRow(
    todo: Todo,
    onTodoClick: (Long) -> Unit,
    onToggle: (id: Long, isDone: Boolean) -> Unit,
    onDelete: (Todo) -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete(todo)
                true
            } else {
                false
            }
        },
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = stringResource(R.string.todo_delete),
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        },
    ) {
        val textColor = if (todo.isDone) {
            MaterialTheme.colorScheme.onSurfaceVariant
        } else {
            MaterialTheme.colorScheme.onSurface
        }
        ListItem(
            modifier = Modifier.fillMaxWidth().clickable { onTodoClick(todo.id) },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
            leadingContent = {
                Checkbox(checked = todo.isDone, onCheckedChange = { onToggle(todo.id, it) })
            },
            headlineContent = {
                Text(
                    text = todo.title.ifBlank { stringResource(R.string.todo_untitled) },
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textDecoration = if (todo.isDone) TextDecoration.LineThrough else null,
                )
            },
            supportingContent = todo.description.takeIf { it.isNotBlank() }?.let { description ->
                {
                    Text(
                        text = description,
                        color = textColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TodoListScreenPreview() {
    HacksTheme(dynamicColor = false) {
        TodoListScreen(
            state = TodoListUiState(
                todos = listOf(
                    Todo(1, "Buy milk", "2 litres, semi-skimmed", isDone = false, createdAt = 2),
                    Todo(2, "Call the bank", "", isDone = true, createdAt = 1),
                ),
                isLoading = false,
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onTodoClick = {},
            onAddClick = {},
            onToggle = { _, _ -> },
            onDelete = {},
        )
    }
}
