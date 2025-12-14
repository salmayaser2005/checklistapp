package com.checklistapp2.app.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.checklistapp2.app.data.entity.ChecklistCategory
import com.checklistapp2.app.data.relation.ChecklistWithItems
import com.checklistapp2.app.ui.navigation.Screen
import com.checklistapp2.app.ui.viewmodel.ChecklistViewModel
import com.checklistapp2.app.ui.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    checklistViewModel: ChecklistViewModel,
    userViewModel: UserViewModel
) {
    val checklistsWithItems by checklistViewModel.checklistsWithItems.collectAsState()
    val user by userViewModel.user.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var filterCategory by remember { mutableStateOf<ChecklistCategory?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Checklists", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Profile.route) }
                    ) {
                        if (user?.avatarUri != null) {
                            AsyncImage(
                                model = user?.avatarUri,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                            )
                        } else {
                            Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Checklist")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            CategoryFilterRow(
                selectedCategory = filterCategory,
                onCategorySelected = { category ->
                    filterCategory = category
                }
            )

            val filteredChecklists = if (filterCategory != null) {
                checklistsWithItems.filter {
                    it.checklist.category == filterCategory
                }
            } else {
                checklistsWithItems
            }

            if (filteredChecklists.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No checklists yet.\nTap + to create one!",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredChecklists) { checklistWithItems ->
                        ChecklistCard(
                            checklistWithItems = checklistWithItems,
                            onClick = {
                                navController.navigate(
                                    Screen.ChecklistDetail.createRoute(
                                        checklistWithItems.checklist.id
                                    )
                                )
                            },
                            onDelete = {
                                checklistViewModel.deleteChecklist(
                                    checklistWithItems.checklist
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDialog) {
        CreateChecklistDialog(
            onDismiss = { showDialog = false },
            onCreate = { name, category ->
                checklistViewModel.createChecklist(name, category)
                showDialog = false
            }
        )
    }
}

/* ---------------- CATEGORY FILTER ---------------- */

@Composable
fun CategoryFilterRow(
    selectedCategory: ChecklistCategory?,
    onCategorySelected: (ChecklistCategory?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                selected = selectedCategory == null,
                onClick = { onCategorySelected(null) },
                label = { Text("All") }
            )
        }

        items(ChecklistCategory.values()) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategorySelected(category) },
                label = { Text(category.name) }
            )
        }
    }
}

/* ---------------- CHECKLIST CARD ---------------- */

@Composable
fun ChecklistCard(
    checklistWithItems: ChecklistWithItems,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = checklistWithItems.checklist.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = checklistWithItems.checklist.category.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val total = checklistWithItems.items.size
            val done = checklistWithItems.items.count { it.isChecked }

            LinearProgressIndicator(
                progress = if (total > 0) done.toFloat() / total else 0f,
                modifier = Modifier.fillMaxWidth()
            )

            Text("$done / $total completed")
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Checklist") },
            text = { Text("Are you sure? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    onDelete()
                    showDeleteDialog = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

/* ---------------- CREATE DIALOG ---------------- */

@Composable
fun CreateChecklistDialog(
    onDismiss: () -> Unit,
    onCreate: (String, ChecklistCategory) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(ChecklistCategory.PERSONAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Checklist") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onCreate(name, category) },
                enabled = name.isNotBlank()
            ) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
