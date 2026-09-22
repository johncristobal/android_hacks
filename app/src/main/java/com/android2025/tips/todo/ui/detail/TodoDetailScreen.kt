package com.android2025.tips.todo.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android2025.tips.R
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TodoDetailRoute(
    todoId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TodoDetailViewModel = koinViewModel(parameters = { parametersOf(todoId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val current = state) {
        TodoDetailState.Loading -> LoadingScaffold(onNavigateBack)

        TodoDetailState.NotFound -> {
            LoadingScaffold(onNavigateBack)
            LaunchedEffect(Unit) { onNavigateBack() }
        }

        is TodoDetailState.Loaded -> {
            val original = current.original
            // Keyed on the id so a different to-do never reuses stale text
            var title by rememberSaveable(todoId) { mutableStateOf(original?.title.orEmpty()) }
            var description by rememberSaveable(todoId) {
                mutableStateOf(original?.description.orEmpty())
            }
            val saveAndClose = {
                viewModel.save(title, description)
                onNavigateBack()
            }

            // System back and the top-bar arrow share the same save-then-pop path
            BackHandler(onBack = saveAndClose)

            TodoDetailScreen(
                isNew = original == null,
                title = title,
                description = description,
                onTitleChange = { title = it },
                onDescriptionChange = { description = it },
                onBackClick = saveAndClose,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadingScaffold(onBackClick: () -> Unit) {
    Scaffold(topBar = { DetailTopBar(isNew = false, onBackClick = onBackClick) }) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopBar(isNew: Boolean, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                stringResource(
                    if (isNew) R.string.todo_new_title else R.string.todo_edit_title,
                ),
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.todo_back),
                )
            }
        },
    )
}

@Composable
fun TodoDetailScreen(
    isNew: Boolean,
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    val titleFocus = remember { FocusRequester() }
    // Only a new to-do opens the keyboard straight away
    LaunchedEffect(isNew) { if (isNew) titleFocus.requestFocus() }

    Scaffold(topBar = { DetailTopBar(isNew, onBackClick) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                modifier = Modifier.fillMaxWidth().focusRequester(titleFocus),
                label = { Text(stringResource(R.string.todo_field_title)) },
                singleLine = true,
                isError = title.isBlank(),
                supportingText = if (title.isBlank()) {
                    { Text(stringResource(R.string.todo_title_required)) }
                } else {
                    null
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next,
                ),
            )
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.todo_field_description)) },
                minLines = 5,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            )
        }
    }
}
