package com.example.wealthwise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

// TransactionsScreen
@Composable
fun TransactionsScreen() {
    val navController = LocalNavController.current
    var searchTerm by remember { mutableStateOf("") }
    var filterType by remember { mutableStateOf("all") }
    var filterCategory by remember { mutableStateOf("all") }
    var deleteDialogOpen by remember { mutableStateOf(false) }
    var transactionToDelete: Transaction? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    val transactions = remember { mutableStateListOf<Transaction>() }
    val categories = remember { mutableStateListOf<Category>() }

    LaunchedEffect(Unit) {
        transactions.addAll(fetchTransactions())
        categories.addAll(fetchCategories())
    }

    val filteredTransactions = transactions.filter { t ->
        (t.title.lowercase().contains(searchTerm.lowercase()) || t.categoryName.lowercase().contains(searchTerm.lowercase())) &&
                (filterType == "all" || t.type.name.lowercase() == filterType) &&
                (filterCategory == "all" || t.categoryName == filterCategory)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null)
            TextField(value = searchTerm, onValueChange = { searchTerm = it }, placeholder = { Text("Search...") })
        }
        Row {
            // Placeholder for DropdownMenu for type and category
            Button(onClick = { searchTerm = ""; filterType = "all"; filterCategory = "all" }) { Text("Clear") }
        }
        Button(onClick = { /* Export logic using exportCSV */ }) { Text("Export CSV") }
        LazyColumn {
            if (filteredTransactions.isEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No transactions found")
                        Button(onClick = { navController.navigate("AddTransaction") }) { Text("Add Your First Transaction") }
                    }
                }
            } else {
                items(filteredTransactions) { transaction ->
                    TransactionItemComposable(transaction, onDelete = { transactionToDelete = it; deleteDialogOpen = true })
                }
            }
        }
    }

    if (deleteDialogOpen) {
        AlertDialog(onDismissRequest = { deleteDialogOpen = false },
            title = { Text("Delete Transaction") },
            text = { Text("Are you sure?") },
            confirmButton = {
                Button(onClick = {
                    coroutineScope.launch { deleteTransaction(transactionToDelete?.title ?: ""); deleteDialogOpen = false; transactions.remove(transactionToDelete) }
                }) { Text("Delete") }
            },
            dismissButton = { Button(onClick = { deleteDialogOpen = false }) { Text("Cancel") } }
        )
    }
}

// GroupsScreen
@Composable
fun GroupsScreen() {
    val navController = LocalNavController.current
    var createDialog by remember { mutableStateOf(false) }
    var joinDialog by remember { mutableStateOf(false) }
    var groupName by remember { mutableStateOf("") }
    var groupDescription by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    val user = remember { mutableStateOf(emptyMap<String, Any>()) }
    val memberships = remember { mutableStateListOf<GroupMember>() }
    val allGroups = remember { mutableStateListOf<Group>() }

    LaunchedEffect(Unit) {
        user.value = fetchUser()
        memberships.addAll(fetchGroupMemberships(user.value["email"] as? String ?: ""))
        allGroups.addAll(fetchGroups())
    }

    val myGroups = allGroups.filter { g -> memberships.any { it.groupId == g.name } } // Adjust if fields differ

    Column {
        Button(onClick = { createDialog = true }) { Text("Create Group") }
        Button(onClick = { joinDialog = true }) { Text("Join Group") }
        LazyColumn {
            items(myGroups) { group ->
                Card(modifier = Modifier.clickable { navController.navigate("GroupDetail/${group.name}") }) { // Adjust id
                    Text(group.name)
                }
            }
        }
    }

    if (createDialog) {
        AlertDialog(onDismissRequest = { createDialog = false },
            title = { Text("Create Group") },
            text = {
                Column {
                    TextField(value = groupName, onValueChange = { groupName = it }, label = { Text("Group Name") })
                    TextField(value = groupDescription, onValueChange = { groupDescription = it }, label = { Text("Description") })
                }
            },
            confirmButton = {
                Button(onClick = {
                    // Create logic
                    createDialog = false
                }) { Text("Create") }
            },
            dismissButton = { Button(onClick = { createDialog = false }) { Text("Cancel") } }
        )
    }

    if (joinDialog) {
        AlertDialog(onDismissRequest = { joinDialog = false },
            title = { Text("Join Group") },
            text = { TextField(value = inviteCode, onValueChange = { inviteCode = it }, label = { Text("Invite Code") }) },
            confirmButton = { Button(onClick = { /* Join */ joinDialog = false }) { Text("Join") } },
            dismissButton = { Button(onClick = { joinDialog = false }) { Text("Cancel") } }
        )
    }
}

// GroupDetailScreen
@Composable
fun GroupDetailScreen(groupId: String) {
    val group = remember { mutableStateOf<Group?>(null) }
    val members = remember { mutableStateListOf<GroupMember>() }
    val expenses = remember { mutableStateListOf<GroupExpense>() }
    var expenseDialog by remember { mutableStateOf(false) }
    var settleDialog by remember { mutableStateOf(false) }
    var expenseForm by remember { mutableStateOf(mapOf<String, Any>()) }
    val user = remember { mutableStateOf(emptyMap<String, Any>()) }

    LaunchedEffect(Unit) {
        user.value = fetchUser()
        group.value = fetchGroup(groupId)
        members.addAll(fetchGroupMembers(groupId))
        expenses.addAll(fetchGroupExpenses(groupId))
    }

    Column {
        Text(group.value?.name ?: "")
        Button(onClick = { expenseDialog = true }) { Text("Add Expense") }
        LazyColumn {
            items(members) { member ->
                Text(member.userName)
            }
        }
        LazyColumn {
            items(expenses) { expense ->
                Text(expense.title)
            }
        }
    }

    if (expenseDialog) {
        AlertDialog(onDismissRequest = { expenseDialog = false },
            title = { Text("Add Expense") },
            text = {
                Column {
                    TextField(value = expenseForm["title"] as? String ?: "", onValueChange = { expenseForm = expenseForm + ("title" to it) }, label = { Text("Title") })
                    // Add other fields
                }
            },
            confirmButton = { Button(onClick = { /* Create */ expenseDialog = false }) { Text("Add") } },
            dismissButton = { Button(onClick = { expenseDialog = false }) { Text("Cancel") } }
        )
    }
}

// BudgetsScreen
@Composable
fun BudgetsScreen() {
    var dialogOpen by remember { mutableStateOf(false) }
    var formData by remember { mutableStateOf(mapOf<String, Any>()) }
    val currentMonth = SimpleDateFormat("yyyy-MM").format(Date())
    val budgets = remember { mutableStateListOf<Budget>() }
    val categories = remember { mutableStateListOf<Category>() }
    val transactions = remember { mutableStateListOf<Transaction>() }

    LaunchedEffect(Unit) {
        budgets.addAll(fetchBudgets(currentMonth))
        categories.addAll(fetchCategories())
        transactions.addAll(fetchTransactions())
    }

    Column {
        Button(onClick = { dialogOpen = true }) { Text("Create Budget") }
        LazyColumn {
            items(budgets) { budget ->
                Card {
                    Text(budget.categoryName)
                    // Progress bar
                    LinearProgressIndicator(progress = (budget.spentAmount / budget.limitAmount).toFloat())
                }
            }
        }
    }

    if (dialogOpen) {
        AlertDialog(onDismissRequest = { dialogOpen = false },
            title = { Text("Create Budget") },
            text = {
                Column {
                    // Category selection
                    TextField(value = formData["limit_amount"] as? String ?: "", onValueChange = { formData = formData + ("limit_amount" to it) }, label = { Text("Limit") })
                    // Other
                }
            },
            confirmButton = { Button(onClick = { /* Create */ dialogOpen = false }) { Text("Create") } },
            dismissButton = { Button(onClick = { dialogOpen = false }) { Text("Cancel") } }
        )
    }
}

// CategoryPieChartComposable
@Composable
fun CategoryPieChartComposable(data: List<Map<String, Any>>, title: String = "Expenses by Category") {
    Card {
        PieChartComposable(data, title)
    }
}

// DashboardScreen
@Composable
fun DashboardScreen() {
    val navController = LocalNavController.current
    val transactions = remember { mutableStateListOf<Transaction>() }

    LaunchedEffect(Unit) {
        transactions.addAll(fetchTransactions())
    }

    // Stats calculation placeholder
    val stats = remember { mutableStateOf(mapOf<String, Any>("balance" to 0.0, "income" to 0.0, "expense" to 0.0)) }

    Column {
        Text("Dashboard", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        Button(onClick = { navController.navigate("AddTransaction") }) { Text("Add Transaction") }
        Row {
            StatCardComposable("Balance", stats.value["balance"] as Double, Icons.Default.ShoppingCart, "balance")
            // Other stats
        }
        Row {
            IncomeExpenseChartComposable(emptyList())
            CategoryPieChartComposable(emptyList())
        }
        RecentTransactionsComposable(transactions.take(5))
    }
}

// TransactionItemComposable
@Composable
fun TransactionItemComposable(transaction: Transaction, onEdit: (Transaction) -> Unit = {}, onDelete: (Transaction) -> Unit = {}) {
    val isIncome = transaction.type == TransactionType.INCOME
    Card {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.background(if (isIncome) Color.Green else Color.Red).padding(8.dp)) {
                Icon(if (isIncome) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null)
            }
            Column {
                Text(transaction.title, fontWeight = FontWeight.Bold)
                Text(transaction.categoryName)
            }
            Text("${if (isIncome) "+" else "-"}${transaction.amount}", color = if (isIncome) Color.Green else Color.Red)
            IconButton(onClick = { onEdit(transaction) }) { Icon(Icons.Default.Edit, null) }
            IconButton(onClick = { onDelete(transaction) }) { Icon(Icons.Default.Delete, null) }
        }
    }
}

// IncomeExpenseChartComposable
@Composable
fun IncomeExpenseChartComposable(data: List<Map<String, Any>>) {
    Card {
        BarChartComposable(data)
    }
}

// SettingsScreen
@Composable
fun SettingsScreen() {
    var theme by remember { mutableStateOf("light") }
    var language by remember { mutableStateOf("en") }
    var deleteDialog by remember { mutableStateOf(false) }
    var deleteType by remember { mutableStateOf<String?>(null) }
    val user = remember { mutableStateOf(emptyMap<String, Any>()) }
    val transactions = remember { mutableStateListOf<Transaction>() }
    val categories = remember { mutableStateListOf<Category>() }

    LaunchedEffect(Unit) {
        user.value = fetchUser()
        transactions.addAll(fetchTransactions())
        categories.addAll(fetchCategories())
    }

    Column {
        Text("Settings", fontSize = 30.sp)
        // Theme and language selectors
        Button(onClick = { /* Export */ }) { Text("Export Data") }
        // Delete buttons
    }

    if (deleteDialog) {
        AlertDialog(onDismissRequest = { deleteDialog = false },
            title = { Text("Confirm Deletion") },
            confirmButton = { Button(onClick = { /* Delete */ deleteDialog = false }) { Text("Delete") } },
            dismissButton = { Button(onClick = { deleteDialog = false }) { Text("Cancel") } }
        )
    }
}

// RecentTransactionsComposable
@Composable
fun RecentTransactionsComposable(transactions: List<Transaction>) {
    val navController = LocalNavController.current
    Card {
        Text("Recent Transactions", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Button(onClick = { navController.navigate("Transactions") }) { Text("View All") }
        LazyColumn {
            items(transactions) { transaction ->
                Row {
                    Icon(if (transaction.type == TransactionType.INCOME) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, null)
                    Text(transaction.title)
                    Text("${transaction.amount}")
                }
            }
        }
    }
}

// StatCardComposable
@Composable
fun StatCardComposable(title: String, amount: Double, icon: androidx.compose.ui.graphics.vector.ImageVector, type: String, trend: Map<String, Any>? = null) {
    Card {
        Column {
            Text(title)
            Text("$${amount}")
            Icon(icon, null)
            // Trend
        }
    }
}

// AddTransactionScreen
@Composable
fun AddTransactionScreen() {
    val navController = LocalNavController.current
    var formData by remember { mutableStateOf(mapOf<String, Any>()) }
    val categories = remember { mutableStateListOf<Category>() }

    LaunchedEffect(Unit) {
        categories.addAll(fetchCategories())
    }

    Column {
        Text("Add Transaction", fontSize = 30.sp)
        TextField(value = formData["amount"] as? String ?: "", onValueChange = { formData = formData + ("amount" to it) }, label = { Text("Amount") })
        // Other fields
        Button(onClick = { /* Submit and navController.popBackStack() */ }) { Text("Save") }
    }
}

// TransactionCard from first response
@Composable
fun TransactionCard(transaction: Transaction) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Title: ${transaction.title}", fontWeight = FontWeight.Medium)
            Text(text = "Amount: $${transaction.amount}")
            Text(text = "Type: ${transaction.type.name.lowercase()}")
            Text(text = "Category: ${transaction.categoryName}")
            Text(text = "Date: ${transaction.date}")
            transaction.note?.let { Text(text = "Note: $it") }
            Text(text = "Privacy: ${transaction.privacy.name.lowercase()}")
        }
    }
}