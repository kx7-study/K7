package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.*
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create view model instance safely
        val viewModel = MainViewModel(application)

        setContent {
            MyApplicationTheme {
                val profileState by viewModel.userProfile.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CyberBlack),
                    containerColor = CyberBlack
                ) { screenPadding ->
                    val profile = profileState
                    if (profile == null) {
                        // Spectacular Calibration Loading Screen
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(CyberBlack)
                                .padding(screenPadding),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = NeonCyan, strokeWidth = 4.dp, modifier = Modifier.size(56.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "ALIGNING INTELLECT VECTORS...",
                                color = NeonCyan,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                "Initializing local memory cells during first boot parameters.",
                                color = TextMutedGrey,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        MainScreenLayout(viewModel = viewModel, profile = profile, parentModifier = Modifier.padding(screenPadding))
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreenLayout(
    viewModel: MainViewModel,
    profile: com.example.data.UserProfile,
    parentModifier: Modifier = Modifier
) {
    var activeTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = parentModifier.fillMaxSize(),
        containerColor = CyberBlack,
        bottomBar = {
            // Tactical Bottom Navigation Bar
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("kx7_bottom_bar")
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                containerColor = DeepSpaceSlate,
                tonalElevation = 8.dp
            ) {
                // Tab 0: Core Hub (Dashboard)
                NavigationBarItem(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Hub") },
                    label = { Text("CMD HUB", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMutedGrey,
                        unselectedTextColor = TextMutedGrey
                    ),
                    modifier = Modifier.testTag("tab_cmd_hub")
                )

                // Tab 1: Solver Desk
                NavigationBarItem(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    icon = { Icon(Icons.Default.Search, contentDescription = "Solver") },
                    label = { Text("SOLVER", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMutedGrey,
                        unselectedTextColor = TextMutedGrey
                    ),
                    modifier = Modifier.testTag("tab_solver")
                )

                // Tab 2: Smart recall (Memory Cards & Summarizer)
                NavigationBarItem(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    icon = { Icon(Icons.Default.Check, contentDescription = "Cards") },
                    label = { Text("RECALL", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMutedGrey,
                        unselectedTextColor = TextMutedGrey
                    ),
                    modifier = Modifier.testTag("tab_recall")
                )

                // Tab 3: Arena Duels
                NavigationBarItem(
                    selected = activeTab == 3,
                    onClick = { activeTab = 3 },
                    icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Arena") },
                    label = { Text("ARENA", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMutedGrey,
                        unselectedTextColor = TextMutedGrey
                    ),
                    modifier = Modifier.testTag("tab_arena")
                )

                // Tab 4: Black-Ops Logs & Shop
                NavigationBarItem(
                    selected = activeTab == 4,
                    onClick = { activeTab = 4 },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Operations") },
                    label = { Text("ADMIN", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonCyan,
                        selectedTextColor = NeonCyan,
                        indicatorColor = NeonCyan.copy(alpha = 0.15f),
                        unselectedIconColor = TextMutedGrey,
                        unselectedTextColor = TextMutedGrey
                    ),
                    modifier = Modifier.testTag("tab_admin")
                )
            }
        }
    ) { innerPadding ->
        // Render Active Screen Panels
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (activeTab) {
                0 -> DashboardScreen(viewModel = viewModel, profile = profile)
                1 -> SolverScreen(viewModel = viewModel, profile = profile)
                2 -> SmartCardsScreen(viewModel = viewModel)
                3 -> ArenaScreen(viewModel = viewModel, profile = profile)
                4 -> BlackOpsScreen(viewModel = viewModel, profile = profile)
            }
        }
    }
}
