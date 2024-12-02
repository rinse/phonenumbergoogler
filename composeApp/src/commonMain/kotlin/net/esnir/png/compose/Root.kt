package net.esnir.png.compose

import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import net.esnir.png.callhistory.CallContent
import net.esnir.png.callhistory.rememberCallHistory
import net.esnir.png.navigation.Navigation
import net.esnir.png.navigation.navTypeOf
import kotlin.reflect.typeOf

@Composable
fun RootContent() {
    val navController = rememberNavController()
    // Detect navigation and observe canGoBack
    val currentBackStack by navController.currentBackStack.collectAsState()
    val canGoBack = remember(currentBackStack.size) {
        navController.previousBackStackEntry != null
    }
    Scaffold(topBar = {
        CallHistoryTopAppBar(
            canGoBack = canGoBack,
            goBack = { navController.navigateUp() },
        )
    }) { padding ->
        val callHistory = rememberCallHistory()
        // アプリを開いた直後しか発火しないはず
        val isCallHistoryNotEmpty by remember {
            derivedStateOf { callHistory.isNotEmpty() }
        }
        LaunchedEffect(isCallHistoryNotEmpty) {
            val content = callHistory.firstOrNull()
            if (content != null) {
                navController.navigate(Navigation.Item(content))
            }
        }
        NavHost(
            navController = navController,
            startDestination = Navigation.List,
            enterTransition = { scaleIn() },
            exitTransition = { scaleOut() },
        ) {
            composable<Navigation.List> {
                CallHistoryList(
                    navController, callHistory,
                    modifier = Modifier.padding(padding)
                )
            }
            composable<Navigation.Item>(
                typeMap = mapOf(
                    typeOf<CallContent>() to navTypeOf<CallContent>(),
                ),
            ) { backStackEntry ->
                val item = backStackEntry.toRoute<Navigation.Item>()
                CallHistoryItem(
                    item.content,
                    modifier = Modifier.padding(padding),
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CallHistoryTopAppBar(
    canGoBack: Boolean,
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
            if (canGoBack) {
                IconButton(onClick = goBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                }
            }
        },
    )
}

@Composable
fun CallHistoryList(
    navController: NavController,
    callHistory: List<CallContent>,
    modifier: Modifier = Modifier,
) {
    CallHistory(
        callHistory,
        onClickItem = { item ->
            navController.navigate(Navigation.Item(item))
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
    )
}

@Composable
fun CallHistoryItem(item: CallContent, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
    ) {
        CallItemDetails(item)
    }
}
