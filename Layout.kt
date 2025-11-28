package com.example.wealthwise

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

// Composition Local for NavController
val LocalNavController = compositionLocalOf<NavController> { error("No NavController provided") }

// Placeholder fetch functions (implement with actual API calls, e.g., using Retrofit)
suspend fun fetchTransactions(): List<Transaction> = emptyList()
suspend fun fetchCategories(): List<Category> = emptyList()
suspend fun deleteTransaction(id: String) {}
suspend fun fetchUser(): Map<String, Any> = emptyMap()
suspend fun fetchGroupMemberships(email: String): List<GroupMember> = emptyList()
suspend fun fetchGroups(): List<Group> = emptyList()
suspend fun createGroup(data: Group): Group { return Group("", "", "") }
suspend fun createGroupMember(data: GroupMember) {}
suspend fun joinGroup(code: String): Group? = null
suspend fun fetchGroup(id: String): Group? = null
suspend fun fetchGroupMembers(id: String): List<GroupMember> = emptyList()
suspend fun fetchGroupExpenses(id: String): List<GroupExpense> = emptyList()
suspend fun createGroupExpense(data: GroupExpense) {}
suspend fun createSettlement(data: Settlement) {}
suspend fun fetchBudgets(month: String): List<Budget> = emptyList()
suspend fun createBudget(data: Budget) {}
suspend fun exportCSV(transactions: List<Transaction>): String = ""
suspend fun logout() {}

// Placeholder chart composable (replace with actual chart libraries like MPAndroidChart)
@Composable
fun PieChartComposable(data: List<Map<String, Any>>, title: String) {
    Column {
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.size(300.dp).background(Color.Gray)) // Placeholder
    }
}

@Composable
fun BarChartComposable(data: List<Map<String, Any>>) {
    Column {
        Text("Income vs Expenses", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Box(modifier = Modifier.size(300.dp).background(Color.Gray)) // Placeholder
    }
}

data class NavItem(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val page: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutComposable(
    currentPageName: String,
    navController: NavController,
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var user by remember { mutableStateOf<Map<String, Any>>(emptyMap()) }

    LaunchedEffect(Unit) {
        user = fetchUser()
    }

    val navigation = listOf(
        NavItem("Dashboard", Icons.Default.Home, "Dashboard"),
        NavItem("Transactions", Icons.Default.DateRange, "Transactions"),
        NavItem("Budgets", Icons.Default.ShoppingCart, "Budgets"),
        NavItem("Groups", Icons.Default.AccountBox, "Groups"),
        NavItem("Settings", Icons.Default.Settings, "Settings")
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFF10B981), Color(0xFF047857))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Wealthwise", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text("Manage your money", fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                navigation.forEach { item ->
                    val isSelected = currentPageName == item.page
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(item.page) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                coroutineScope.launch { drawerState.close() }
                            }
                            .padding(16.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color(0xFFE6FFFA) else Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(item.icon, contentDescription = null, tint = if (isSelected) Color(0xFF047857) else Color.Gray)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(item.name, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                if (user.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.LightGray)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF047857)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                (user["full_name"] as? String)?.firstOrNull()?.uppercase() ?: "U",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                user["full_name"] as? String ?: "",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                user["email"] as? String ?: "",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextButton(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ExitToApp, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Logout")
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF10B981), Color(0xFF047857))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Wealthwise", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            },
            content = { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFFF8FAFC), Color(0xFFF1F5F9), Color(0xFFF1F5F9))
                            )
                        )
                ) {
                    content()
                }
            }
        )
    }
}