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
import androidx.compose.ui.text.style.TextAlign

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
                    } else if (!profile.isAuthenticated) {
                        UliteAuthScreen(viewModel = viewModel, modifier = Modifier.padding(screenPadding))
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
    val freeSeconds by viewModel.freeSecondsRemaining.collectAsState()
    val limitExceeded by viewModel.isDailyLimitExceeded.collectAsState()

    Scaffold(
        modifier = parentModifier.fillMaxSize(),
        containerColor = CyberBlack,
        topBar = {
            if (!profile.isPremium) {
                val minutesVal = freeSeconds / 60
                val secondsVal = freeSeconds % 60
                val hoursVal = minutesVal / 60
                val minDisplay = minutesVal % 60
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBlack)
                        .border(1.dp, WarningCrimson, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(if (freeSeconds > 600) NeonCyan else WarningCrimson)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "STATION ACCESS: FREE TIER",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = String.format("REMAINING: %02d:%02d:%02d", hoursVal, minDisplay, secondsVal),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (freeSeconds > 600) NeonCyan else WarningCrimson,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CyberBlack)
                        .border(1.dp, NeonPurple, RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(NeonGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "PREMIUM PROTOCOL ACTIVATED",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "UNLIMITED SOLVES",
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonGreen,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        },
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
            if (limitExceeded && activeTab != 4) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(CyberBlack)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, WarningCrimson, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Expired",
                                tint = WarningCrimson,
                                modifier = Modifier.size(56.dp)
                            )
                            
                            Text(
                                text = "STATION TIMELINE LIMIT DETECTED",
                                style = MaterialTheme.typography.titleLarge,
                                color = WarningCrimson,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            
                            Text(
                                text = "Your daily free allocation of 2 hours has been depleted. All active study nodes, arena matches, and AI resolution solves are locked.",
                                color = TextMutedGrey,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            
                            Divider(color = BorderCyberDark, thickness = 1.dp)
                            
                            Text(
                                text = "COGNITIVE UPGRADE OPTIONS:",
                                color = NeonPurple,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("1 Month: $27 (3700 BDT) sent to bKash", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text("3 Months: $77", color = TextWhite, fontSize = 13.sp)
                                Text("1 Year: $277", color = TextWhite, fontSize = 13.sp)
                            }
                            
                            Button(
                                onClick = { activeTab = 4 },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                            ) {
                                Text("BEYOND THE CELL: PAY WITH bKASH", color = TextWhite, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
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
}
