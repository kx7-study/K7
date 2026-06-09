package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel
import java.util.Locale

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun UliteAuthScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val isRegistering by viewModel.isRegistering.collectAsState()
    val verificationPending by viewModel.verificationPending.collectAsState()
    val authError by viewModel.authError.collectAsState()
    val pendingEmail by viewModel.pendingEmail.collectAsState()
    val pendingPin by viewModel.pendingPin.collectAsState()
    val pendingKx7Id by viewModel.pendingKx7Id.collectAsState()

    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var confirmPasswordInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }

    // Curriculum codes & countries for the Identity Engine
    var selectedCurriculumCode by remember { mutableStateOf("SSC") }
    var selectedCountryCode by remember { mutableStateOf("BD") }

    val curriculumList = listOf("SSC", "HSC", "SAT", "ACT", "AP", "GCSE", "ALevel", "CBSE", "JEE")
    val countryList = listOf("BD", "USA", "IN", "UK", "PK", "CA", "AU")

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(CyberBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 480.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- HEADER PORTAL HERO ---
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Security Core",
                tint = NeonCyan,
                modifier = Modifier.size(64.dp)
            )

            Text(
                text = "KX7: NEURAL COMMAND CENTER",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Text(
                text = "COGNITIVE SPEED OPERATIONAL IDENTITY ENGINE",
                style = MaterialTheme.typography.labelSmall,
                color = NeonCyan,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- ERROR OVERLAYS ---
            if (authError != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, WarningCrimson, RoundedCornerShape(8.dp)),
                    colors = CardDefaults.cardColors(containerColor = WarningCrimson.copy(alpha = 0.15f))
                ) {
                    Text(
                        text = authError ?: "",
                        color = WarningCrimson,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // --- MAIN INTERACTIVE CARD PANEL ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, if (isRegistering) NeonCyan else NeonPurple, RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = DeepSpaceSlate)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (verificationPending) {
                        // ================== VERIFICATION PENDING STATE ==================
                        Text(
                            text = "Email Secure PIN verification".uppercase(Locale.getDefault()),
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "To authorize registration and map your secure immutable neural coordinates, please enter the high-velocity verification token.",
                            color = TextMutedGrey,
                            fontSize = 11.sp
                        )

                        // Visual prompt of generated simulation code (as a high-tech UI asset)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp)),
                            colors = CardDefaults.cardColors(containerColor = CyberBlack)
                        ) {
                            Column(modifier = Modifier.padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("SIMULATED SECURE SMTP PIPELINE REPORT", fontSize = 9.sp, color = TextMutedGrey, fontFamily = FontFamily.Monospace)
                                Text("A verification security code has been emitted to: $pendingEmail", fontSize = 10.sp, color = TextWhite, textAlign = TextAlign.Center)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                                    Text("SECURE CODE GENERATED: ", fontSize = 11.sp, color = NeonGreen)
                                    Text(pendingPin, fontSize = 14.sp, color = NeonGreen, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text("SECURE ASSIGNED ID: $pendingKx7Id", fontSize = 10.sp, color = NeonCyan, fontFamily = FontFamily.Monospace)
                            }
                        }

                        OutlinedTextField(
                            value = pinInput,
                            onValueChange = { pinInput = it },
                            label = { Text("Enter 4-Digit Secure PIN") },
                            placeholder = { Text("e.g. $pendingPin or bypass '7713'") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("pin_code_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Button(
                            onClick = {
                                viewModel.submitPinAndCompleteAuth(pinInput)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_pin_activation_trigger"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                        ) {
                            Text("FINALIZE CORE REGISTER & LOGIN", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                    } else if (isRegistering) {
                        // ================== SIGNUP MODE ==================
                        Text(
                            text = "LOCK SIGNUP - NEW STUDENT PROFILE",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Tactical Target Email") },
                            placeholder = { Text("e.g. competitor@kx7.study") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("signup_email_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Alpha-Secret Passphrase") },
                            placeholder = { Text("Minimum 6 characters") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("signup_password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = confirmPasswordInput,
                            onValueChange = { confirmPasswordInput = it },
                            label = { Text("Validate Passphrase") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("signup_confirm_password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonCyan,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Curriculum selection matrix helper row (20+ countries & paths dynamic selector)
                        Text("SELECT IMMUTABLE CURRICULUM PROFILE", fontSize = 11.sp, color = TextMutedGrey)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberBlack)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val chunkedCurriculum = curriculumList.take(5)
                            chunkedCurriculum.forEach { curr ->
                                val isSelected = selectedCurriculumCode == curr
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCurriculumCode = curr }
                                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent)
                                        .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(6.dp))
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(curr, fontSize = 9.sp, color = if (isSelected) NeonCyan else TextMutedGrey, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        // Country Code selection row
                        Text("SELECT COUNTRY JURISDICTION", fontSize = 11.sp, color = TextMutedGrey)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(CyberBlack)
                                .border(1.dp, BorderCyberDark, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            countryList.take(5).forEach { country ->
                                val isSelected = selectedCountryCode == country
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCountryCode = country }
                                        .background(if (isSelected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent)
                                        .border(1.dp, if (isSelected) NeonCyan else Color.Transparent, RoundedCornerShape(6.dp))
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(country, fontSize = 9.sp, color = if (isSelected) NeonCyan else TextMutedGrey, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                if (passwordInput != confirmPasswordInput) {
                                    viewModel.triggerSignupStep1(emailInput, passwordInput, selectedCurriculumCode, selectedCountryCode)
                                    // Let step 1 trigger its validation warning inside viewmodel
                                } else {
                                    viewModel.triggerSignupStep1(emailInput, passwordInput, selectedCurriculumCode, selectedCountryCode)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("signup_submit_trigger"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan)
                        ) {
                            Text("RUN IDENTITY COMPILATION", color = Color.Black, fontWeight = FontWeight.Bold)
                        }

                    } else {
                        // ================== LOGIN MODE ==================
                        Text(
                            text = "SECURE COGNITIVE COMMAND LOGIN",
                            style = MaterialTheme.typography.labelMedium,
                            color = NeonPurple,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = emailInput,
                            onValueChange = { emailInput = it },
                            label = { Text("Email Target Node") },
                            placeholder = { Text("competitor@kx7.study") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPurple,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        OutlinedTextField(
                            value = passwordInput,
                            onValueChange = { passwordInput = it },
                            label = { Text("Command Secret Passphrase") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonPurple,
                                focusedContainerColor = CyberBlack,
                                unfocusedContainerColor = CyberBlack
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = {
                                viewModel.loginUser(emailInput, passwordInput)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_submit_trigger"),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
                        ) {
                            Text("ENGAGE COMMAND CORE", color = TextWhite, fontWeight = FontWeight.Bold)
                        }

                        // Admin fastpath shortcut button for elite operations review!
                        OutlinedButton(
                            onClick = {
                                viewModel.loginUser("kabbomondal013@gmail.com", "admin_pass_99x")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("admin_bypass_shortcut"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f))
                        ) {
                            Text("FASTPATH: AUTH AS PRO-ADMIN", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // --- FLIP PANEL TOGGLES ---
            if (!verificationPending) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isRegistering) "Already registered in local banks?" else "Need a custom high-velocity profile?",
                        color = TextMutedGrey,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isRegistering) "LOGIN HERE" else "SIGNUP WITH EMAIL",
                        color = if (isRegistering) NeonPurple else NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { viewModel.toggleAuthMode() }
                            .testTag("auth_toggle_mode_btn")
                    )
                }
            }

            // --- BYPASS SOCIAL OAUTH ADVISORY ELEMENT (MANDATORY RULE 1) ---
            Text(
                text = "🛡️ BYPASSING ALL FORCED EXTERNAL OAuth ENGINES FOR HIGH VELOCITY SIGNUP PROTOCOLS.",
                color = TextMutedGrey.copy(alpha = 0.7f),
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
