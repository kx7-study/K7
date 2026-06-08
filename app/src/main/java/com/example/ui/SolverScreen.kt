package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserProfile
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SolverScreen(
    viewModel: MainViewModel,
    profile: UserProfile,
    modifier: Modifier = Modifier
) {
    val solveLoading by viewModel.solveLoading.collectAsState()
    val solveResult by viewModel.solveResult.collectAsState()

    var activeInputText by remember { mutableStateOf("") }
    var selectedSubject by remember { mutableStateOf("Physics") }
    val subjects = listOf("Physics", "Chemistry", "Mathematics", "Biology", "English")

    var selectedTrack by remember { mutableStateOf(profile.curriculumTrack) }
    val tracks = listOf("NCTB_BD", "SAT_USA", "CAMBRIDGE_UK", "JEE_IN")

    var selectedLang by remember { mutableStateOf(profile.languagePreference) }
    val languages = mapOf("en" to "English", "bn" to "Bangla", "es" to "Spanish", "hi" to "Hindi")

    val personas = listOf("Feynman", "Einstein", "Fischer", "Military", "Exam Assassin")
    val selectedPersona = profile.activeMentorPersona

    // Dropdown expanded states
    var subjectExpanded by remember { mutableStateOf(false) }
    var trackExpanded by remember { mutableStateOf(false) }
    var langExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Header Section ---
        item {
            Column {
                Text(
                    text = "NEURAL SOLVER DESK",
                    style = MaterialTheme.typography.displayLarge,
                    color = NeonCyan,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Double model injection layers aligning with Universal Curriculum Matrix schemas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMutedGrey
                )
            }
        }

        // --- 1. Tactical Setup Configurations ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "curriculum configuration matrix".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Subject Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { subjectExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(selectedSubject, color = Color.White)
                                Icon(Icons.Default.MoreVert, contentDescription = "Expand", tint = NeonCyan)
                            }
                            DropdownMenu(
                                expanded = subjectExpanded,
                                onDismissRequest = { subjectExpanded = false }
                            ) {
                                subjects.forEach { sub ->
                                    DropdownMenuItem(
                                        text = { Text(sub) },
                                        onClick = {
                                            selectedSubject = sub
                                            subjectExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Track Dropdown
                        Box(modifier = Modifier.weight(1f)) {
                            OutlinedButton(
                                onClick = { trackExpanded = true },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(selectedTrack, color = Color.White)
                                Icon(Icons.Default.MoreVert, contentDescription = "Expand", tint = NeonCyan)
                            }
                            DropdownMenu(
                                expanded = trackExpanded,
                                onDismissRequest = { trackExpanded = false }
                            ) {
                                tracks.forEach { tr ->
                                    DropdownMenuItem(
                                        text = { Text(tr) },
                                        onClick = {
                                            selectedTrack = tr
                                            viewModel.updateProfileSettings(tr, selectedLang, profile.displayName, profile.missionTarget)
                                            trackExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Language Selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { langExpanded = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Language Output: ${languages[selectedLang] ?: "English"}", color = Color.White)
                            Icon(Icons.Default.MoreVert, contentDescription = "Expand", tint = NeonCyan)
                        }
                        DropdownMenu(
                            expanded = langExpanded,
                            onDismissRequest = { langExpanded = false }
                        ) {
                            languages.forEach { (code, name) ->
                                DropdownMenuItem(
                                    text = { Text(name) },
                                    onClick = {
                                        selectedLang = code
                                        viewModel.updateProfileSettings(selectedTrack, code, profile.displayName, profile.missionTarget)
                                        langExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. AI Pedagogical Mentor Persona Toggles ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "neural mentor pedagogical tone overlays".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonPurple
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(CyberBlack),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        personas.forEach { per ->
                            val isSelected = selectedPersona == per
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { viewModel.updateMentorPersona(per) }
                                    .background(if (isSelected) NeonPurple.copy(alpha = 0.2f) else Color.Transparent)
                                    .border(if (isSelected) 1.dp else 0.dp, NeonPurple)
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = per,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NeonCyan else TextMutedGrey
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Brief description of selected AI pedagogy
                    val desc = when (selectedPersona) {
                        "Einstein" -> "Einstein Mode: Prioritizes deep conceptual boundaries, space-time manifolds, and first-principles physics intuition."
                        "Fischer" -> "Fischer Mode: Ruthless, hyper-direct mathematical derivation. Cold, clock-driven logical deductions."
                        "Military" -> "Military Mode: Zero excuses, extreme discipline. Tells you precisely where your effort is failing. Strict tone."
                        "Exam Assassin" -> "Exam Assassin Mode: Purely tactical. Direct alignment with global marking schemes to maximize board percentages."
                        else -> "Feynman Mode: Translates complex high-level mathematical jargon into ultra-clear real-world analogies."
                    }

                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = NeonGreen,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // --- 3. Dynamic Problem Input panel ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderCyberDark, RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "academic problem query parameters".uppercase(Locale.getDefault()),
                        style = MaterialTheme.typography.labelMedium,
                        color = NeonCyan
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = activeInputText,
                        onValueChange = { activeInputText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("solver_input_field"),
                        placeholder = {
                            Text(
                                "Type or dictate academic challenge questions (e.g. 'A stone thrown vertically upwards under board gravity NCTB...')",
                                fontSize = 12.sp,
                                color = TextMutedGrey
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = BorderCyberDark,
                            focusedContainerColor = CyberBlack,
                            unfocusedContainerColor = CyberBlack
                        ),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Auxiliary simulated inputs: Scan & Dictate
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Scan-to-Solve emulation
                        Button(
                            onClick = {
                                activeInputText = "[SCANNED FORMULA] Integral of cot(x) csc(x) dx under boundary conditions."
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Scan", tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("SCAN MATRIX", fontSize = 10.sp, color = Color.White)
                        }

                        // Voice-to-Command Matrix emulation
                        Button(
                            onClick = {
                                activeInputText = "A projectile is launched from coordinates (0,0) at velocity 40 meters per second. Solve maximum flight duration."
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceCardLight),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.List, contentDescription = "Dictate", tint = NeonCyan, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("VOICE MATRIX", fontSize = 10.sp, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Solve submission trigger button
                    Button(
                        onClick = {
                            if (activeInputText.isNotEmpty()) {
                                viewModel.callNeuralSolver(activeInputText, selectedSubject)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("neural_solve_trigger"),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                        shape = RoundedCornerShape(8.dp),
                        enabled = activeInputText.isNotEmpty() && !solveLoading
                    ) {
                        if (solveLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                        } else {
                            Text("DISPATCH NEURAL SOLVER", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- 4. Answer Console Output readout ---
        item {
            AnimatedVisibility(
                visible = solveResult != null || solveLoading,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeonCyan, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(containerColor = CyberBlack)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "neural engine response stream".uppercase(Locale.getDefault()),
                                style = MaterialTheme.typography.labelMedium,
                                color = NeonGreen
                            )

                            // Clear output option to reset console
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Clear",
                                tint = TextMutedGrey,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable { viewModel.clearSolverResult() }
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 10.dp), color = BorderCyberDark)

                        if (solveLoading) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(color = NeonCyan)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Analyzing dynamic bounds. Aligning known vectors...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMutedGrey,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            val resultText = solveResult ?: ""
                            Text(
                                text = resultText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                                color = TextWhite,
                                modifier = Modifier.testTag("solver_result_text")
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action button to save this generated solution to Second Brain notes
                            Button(
                                onClick = {
                                    viewModel.addBrainNote(
                                        title = "Solver: $selectedSubject Vector Reference",
                                        content = resultText.take(500) + "\n\n[Full Solution Stream Cached]",
                                        subject = selectedSubject
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Vault Note", tint = Color.White)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("MIGRATE SOLUTION TO BRAIN VAULT", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
