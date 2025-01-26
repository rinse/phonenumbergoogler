package net.esnir.png.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.esnir.png.callhistory.CallContent
import net.esnir.png.callhistory.CallHistoryManager
import net.esnir.png.callhistory.rememberCallHistoryManager
import net.esnir.png.navigation.Navigation
import net.esnir.png.navigation.navTypeOf
import kotlin.reflect.typeOf

@Composable
fun RootContent() {
    val navController = rememberNavController()
    var topAppBar by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
    Scaffold(topBar = { topAppBar?.invoke() }) { paddings ->
        NavHost(
            navController = navController,
            startDestination = Navigation.List,
        ) {
            composable<Navigation.List> {
                val callHistoryManager = rememberCallHistoryManager()
                CallHistoryList(
                    navController, callHistoryManager,
                    modifier = Modifier.padding(paddings).fillMaxSize()
                )
                LaunchedEffect(Unit) {
                    topAppBar = {
                        CallHistoryListTopAppBar(
                            onRefresh = { callHistoryManager.refresh() },
                            onOpenLicence = { navController.navigate(Navigation.Licence) },
                        )
                    }
                }
            }
            composable<Navigation.Item>(
                typeMap = mapOf(
                    typeOf<CallContent>() to navTypeOf<CallContent>(),
                ),
            ) { backStackEntry ->
                val item = backStackEntry.toRoute<Navigation.Item>()
                CallHistoryItem(
                    item.content,
                    modifier = Modifier.padding(paddings).fillMaxSize()
                )
                LaunchedEffect(Unit) {
                    topAppBar = {
                        CallHistoryItemTopAppBar(
                            goBack = navController::navigateUp,
                        )
                    }
                }
            }
            composable<Navigation.Licence> {
                LicenceContent(modifier = Modifier.padding(paddings).fillMaxSize())
                LaunchedEffect(Unit) {
                    topAppBar = {
                        CallHistoryItemTopAppBar(
                            goBack = navController::navigateUp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CallHistoryList(
    navController: NavController,
    callHistoryManager: CallHistoryManager,
    modifier: Modifier = Modifier,
) {
    CallHistoryControl(
        manager = callHistoryManager,
        onClickItem = { item ->
            navController.navigate(Navigation.Item(item))
        },
        modifier = Modifier.then(modifier),
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CallHistoryListTopAppBar(
    onRefresh: () -> Unit,
    onOpenLicence: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isExpanded by mutableStateOf(false)
    TopAppBar(
        title = { Text("Phone Number Googler") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
        actions = {
            TopAppBarMenu(
                onRefresh = onRefresh,
                isExpanded = isExpanded,
                onExpandedChange = { isExpanded = it },
                onOpenLicence = onOpenLicence,
            )
        }
    )
}

@Composable
fun TopAppBarMenu(
    onRefresh: () -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOpenLicence: () -> Unit,
) {
    IconButton(onClick = { onExpandedChange(!isExpanded) }) {
        Icon(Icons.Default.Menu, "Menu")
    }
    DropdownMenu(expanded = isExpanded, onDismissRequest = { onExpandedChange(false) }) {
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.Default.Refresh, "Refresh") },
            text = { Text("Refresh") },
            onClick = { onExpandedChange(false); onRefresh() },
        )
        DropdownMenuItem(
            leadingIcon = { Icon(Icons.AutoMirrored.Filled.List, "License") },
            text = { Text("Licence Information") },
            onClick = { onExpandedChange(false); onOpenLicence() },
        )
    }
}

@Composable
fun CallHistoryItem(
    item: CallContent,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.then(modifier),
    ) {
        Surface {
            CallItemDetails(item)
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CallHistoryItemTopAppBar(
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = { Text("Phone Number Googler") },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = goBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        },
    )
}
