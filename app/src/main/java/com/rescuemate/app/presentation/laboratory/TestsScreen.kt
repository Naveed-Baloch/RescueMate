package com.rescuemate.app.presentation.laboratory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.extensions.showToast
import com.rescuemate.app.presentation.blooddonor.BloodGroupDropdown
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.LaboratoryVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.CustomEditText
import com.rescuemate.app.utils.LaboratoryTestDropDown
import com.rescuemate.app.utils.TopBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun TestsScreen(
    laboratoryVM: LaboratoryVM = hiltViewModel(),
    navController: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var keepShowingLoading by remember { mutableStateOf(false) }
    var showTestEntryDialog by remember { mutableStateOf(false) }
    val progressBar = remember { context.progressBar() }

    LaunchedEffect(key1 = Unit) {
        laboratoryVM.getUserLaboratory().collect{}
    }

    LaunchedEffect(key1 = laboratoryVM.isLoading) {
        if (!laboratoryVM.isLoading) delay(1000)
        keepShowingLoading = laboratoryVM.isLoading
        progressBar.isVisible(keepShowingLoading)
    }

    LazyColumn(modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp)) {
        item {
            Box(modifier = Modifier.height(60.dp)) {
                TopBar(text = "Laboratory's Tests") {
                    navController.popBackStack()
                }
            }
        }
        val tests = laboratoryVM.userLab?.tests ?: emptyList()
        if (tests.isEmpty() && !laboratoryVM.isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "No Tests Found", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
        } else {
            items(tests) { test ->
                Spacer(modifier = Modifier.height(15.dp))
                TestItem(test = test)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        FloatingActionButton(containerColor = primaryColor, contentColor = Color.White, onClick = { showTestEntryDialog = true }) {
            Text(text = "Add Test", modifier = Modifier.padding(10.dp))
        }
    }

    val currentLaboratoryId = laboratoryVM.userLab?.ownerId
    if (showTestEntryDialog && currentLaboratoryId != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.1f))
        ) {
            AddTestDialog(onDismissRequest = { showTestEntryDialog = false }) { name, price ->
                scope.launch {
                    laboratoryVM.addLaboratoryTest(LaboratoryTest(name = name, price = price), laboratoryId = currentLaboratoryId).collect {
                        when (it) {
                            is Result.Failure -> {
                                progressBar.dismiss()
                                context.showToast(it.exception.message ?: "Something went Wrong")
                            }

                            is Result.Success -> {
                                progressBar.dismiss()
                                context.showToast(it.data)
                                showTestEntryDialog = false
                                laboratoryVM.getUserLaboratory().collect{}
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }

    BackHandler(enabled = progressBar.isShowing) {
        progressBar.dismiss()
        return@BackHandler
    }
}

@Composable
fun AddTestDialog(onDismissRequest: () -> Unit, actionAddTest: (String, String) -> Unit) {
    val context = LocalContext.current
    Dialog(onDismissRequest = onDismissRequest) {
        var testName by remember { mutableStateOf("") }
        var testPrice by remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Add Test to your Laboratory!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Select Test",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            LaboratoryTestDropDown(
                modifier = Modifier.fillMaxWidth(),
                onBloodTestSelected = { testName = it }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Price",
                style = MaterialTheme.typography.titleMedium.copy(Color.Black.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(5.dp))
            CustomEditText(
                value = testPrice,
                label = "Enter test price",
                keyboardType = KeyboardType.Number,
                isError = false,
                onValueChange = {
                    testPrice = it
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.Red)
                    .clickableWithOutRipple {
                        if (testName.isNotEmpty() && testPrice.isNotEmpty()) {
                            actionAddTest(testName, testPrice)

                        } else {
                            context.showToast("Please fill all fields")
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Add Test".uppercase(),
                    color = Color.White, fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun TestItem(test: LaboratoryTest) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .fillMaxWidth(),

        ) {
            Text(
                text = "Test Name: ${test.name}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Test Price: ${test.price}",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview
@Composable
fun TestItemPreview() {
    AddTestDialog(onDismissRequest = {}, actionAddTest = { s: String, i: String -> })
}