package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: MainViewModel,
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    val activeTimer by viewModel.focusTimerSeconds.collectAsState()
    val isFocusActive by viewModel.isFocusActive.collectAsState()
    val gazeTrackingTriggered by viewModel.gazeTrackingTriggered.collectAsState()

    var selectedNodeName by remember { mutableStateOf<String?>("Algebra") }
    var selectedNodeFormula by remember { mutableStateOf("y = ax² + bx + c") }
    var selectedNodeStatus by remember { mutableStateOf("Conquered: Territory secure (+100% Mastery)") }

    val nodes = remember {
        listOf(
            ConceptNode("Algebra", Offset(150f, 150f), "f(x) = ax + b", "Conquered: Territory secure (+100% Mastery)", NeonCyan, true),
            ConceptNode("Functions", Offset(350f, 280f), "g(f(x)) composition", "Secured territory. Active defense.", NeonCyan, true),
            ConceptNode("Graphs", Offset(550f, 160f), "Slope m = (y2-y1)/(x2-x1)", "Contested frontier (+45% Mastery)", NeonGreen, false),
            ConceptNode("Quadratics", Offset(750f, 320f), "x = [-b \u00b1 \u221ad]/2a", "Locked enemy territory (0% Mastery)", TextMutedGrey, false)
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- 1. Aura Status Row ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "competitor profile".uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonCyan
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = profile.displayName,
                                style = MaterialTheme.typography.titleLarge,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Premium status badge
                        if (profile.isPremium) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonPurple.copy(alpha = 0.2f))
                                    .border(1.dp, NeonPurple, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "PREMIUM",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = NeonPurple,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp), color = BorderCyberDark)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("AURA STATUS", style = MaterialTheme.typography.labelMedium, color = TextMutedGrey)
                            Text(
                                text = "${profile.auraPoints} AP",
                                style = MaterialTheme.typography.headlineMedium,
                                color = NeonCyan,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("MASTER STREAK", style = MaterialTheme.typography.labelMedium, color = TextMutedGrey)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "Streak",
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${profile.currentStreak} DAYS",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = NeonGreen,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }

                    // Strict loss warnings if streak is in danger
                    if (profile.currentStreak > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarningCrimson.copy(alpha = 0.1f))
                                .border(1.dp, WarningCrimson.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = "Decay", tint = WarningCrimson, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Critical decay matrix active. Complete your daily mock challenge to defend status.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarningCrimson,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. Focus Protocol Area (Biometric Calibration) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        if (gazeTrackingTriggered) WarningCrimson else BorderCyberDark,
                        RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (gazeTrackingTriggered) WarningCrimson.copy(alpha = 0.08f) else DeepSpaceSlate
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "focus flow-state calibration protocol".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = if (gazeTrackingTriggered) WarningCrimson else NeonCyan,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    if (gazeTrackingTriggered) {
                        // High intensity distraction warning
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(WarningCrimson.copy(alpha = 0.2f))
                                .clickable { viewModel.clearGazeAlert() }
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Info, contentDescription = "Alert", tint = WarningCrimson, modifier = Modifier.size(32.dp))
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ATTENTION DISRUPTION DETECTED",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Gaze-vector vector drifted off screen. Tap here to recalibrate biometric focus.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = WarningCrimson,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        // Regular focus clock
                        val totalMinutes = activeTimer / 60
                        val remainingSeconds = activeTimer % 60
                        val timeStr = String.format("%02d:%02d", totalMinutes, remainingSeconds)

                        Text(
                            text = timeStr,
                            style = MaterialTheme.typography.displayLarge,
                            color = if (isFocusActive) NeonGreen else TextWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.testTag("focus_timer_text")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (!isFocusActive) {
                                Button(
                                    onClick = { viewModel.startFocusSession(25) },
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                                    modifier = Modifier.testTag("start_focus_button")
                                ) {
                                    Icon(Icons.Default.PlayArrow, contentDescription = "Start", tint = Color.Black)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("INITIATE DURATION (25M)", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            } else {
                                Button(
                                    onClick = { viewModel.stopFocusSession() },
                                    colors = ButtonDefaults.buttonColors(containerColor = WarningCrimson),
                                    modifier = Modifier.testTag("stop_focus_button")
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = "Abort")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("ABORT FOCUS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Interactive Constellation War Map ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "curriculum war map (glowing nodes)".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )
                    Text(
                        text = "Tap concept node nodes to capture territory and display formulation dynamics.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMutedGrey,
                        fontSize = 11.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Draw node map in horizontal Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(CyberBlack)
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    // Hit test logic
                                    var clicked: ConceptNode? = null
                                    for (node in nodes) {
                                        val distSq = (node.pos.x - offset.x) * (node.pos.x - offset.x) +
                                                (node.pos.y - offset.y) * (node.pos.y - offset.y)
                                        if (distSq < 1200f) { // 35px radius approx
                                            clicked = node
                                            break
                                        }
                                    }
                                    if (clicked != null) {
                                        selectedNodeName = clicked.name
                                        selectedNodeFormula = clicked.formula
                                        selectedNodeStatus = clicked.status
                                    }
                                }
                            }
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Draw Connecting Lines
                            for (i in 0 until nodes.size - 1) {
                                val n1 = nodes[i]
                                val n2 = nodes[i + 1]
                                drawLine(
                                    color = if (n1.isFinished && n2.isFinished) NeonCyan else BorderCyberDark,
                                    start = n1.pos,
                                    end = n2.pos,
                                    strokeWidth = 3f
                                )
                            }

                            // Draw Nodes
                            for (node in nodes) {
                                drawCircle(
                                    color = node.color,
                                    radius = 20f,
                                    center = node.pos
                                )
                                drawCircle(
                                    color = Color.Black,
                                    radius = 8f,
                                    center = node.pos
                                )
                                // Glowing outer border
                                drawCircle(
                                    color = node.color.copy(alpha = 0.3f),
                                    radius = 32f,
                                    center = node.pos,
                                    style = Stroke(width = 2f)
                                )
                            }
                        }

                        // Drawing text tags near coordinates
                        for (node in nodes) {
                            Box(
                                modifier = Modifier
                                    .offset(
                                        x = (node.pos.x / 2.3f).dp,
                                        y = (node.pos.y / 2.5f).dp
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    text = node.name,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp,
                                    modifier = Modifier.background(Color.Black.copy(alpha = 0.6f))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Selected node console readout
                    AnimatedVisibility(
                        visible = selectedNodeName != null,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SurfaceCardDark)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Node: ${selectedNodeName?.uppercase(Locale.getDefault())}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(Icons.Default.Check, contentDescription = "Status", tint = NeonGreen)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Formula: $selectedNodeFormula",
                                style = MaterialTheme.typography.bodyLarge,
                                color = TextWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedNodeStatus,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMutedGrey
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Cognitive intelligence Twin Metrics ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "cognitive intelligence profile (twin)".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonPurple
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Learning Speed LSI", style = MaterialTheme.typography.bodyMedium, color = TextMutedGrey)
                            Text("${(profile.lsi * 100).toInt()}% Velocity", style = MaterialTheme.typography.titleLarge, color = TextWhite, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Burnout Risk Parameter", style = MaterialTheme.typography.bodyMedium, color = TextMutedGrey)
                            Text("${profile.burnoutRiskScore} / 100", style = MaterialTheme.typography.titleLarge, color = WarningCrimson, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Peak Performance Window", style = MaterialTheme.typography.bodyMedium, color = TextMutedGrey)
                    Text(
                        text = "${formatHour(profile.peakStudyStartHour)} \u2014 ${formatHour(profile.peakStudyEndHour)} AM (Biometric sync)",
                        style = MaterialTheme.typography.bodyLarge,
                        color = NeonGreen,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Solving Strategy Alignment", style = MaterialTheme.typography.bodyMedium, color = TextMutedGrey)
                    Text(
                        text = profile.primarySolvingStyle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = NeonCyan,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // --- 5. Target Mission Campaign ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(NeonPurple.copy(alpha = 0.2f))
                            .border(1.dp, NeonPurple, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Favorite, contentDescription = "Uni Target", tint = NeonPurple)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "target university mission".uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPurple
                        )
                        Text(
                            text = "Campaign: Destination ${profile.missionTarget}",
                            style = MaterialTheme.typography.titleLarge,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Difficulty scaling automatically dynamically synchronized.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMutedGrey,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

private fun formatHour(hour: Int): String {
    return String.format("%02d:00", hour)
}

data class ConceptNode(
    val name: String,
    val pos: Offset,
    val formula: String,
    val status: String,
    val color: Color,
    val isFinished: Boolean
)
