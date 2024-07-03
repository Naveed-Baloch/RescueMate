package com.rescuemate.app.presentation.laboratory

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.rescuemate.app.dto.Laboratory
import com.rescuemate.app.dto.LaboratoryTest
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.openDialPanel
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.LaboratoryVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.TopBar
import com.rescuemate.app.utils.openWhatsApp

@Composable
fun LaboratoriesScreen(
    city: String,
    laboratoryTest: String,
    laboratoryVM: LaboratoryVM = hiltViewModel(),
    navController: NavHostController,
) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        progressBar.isVisible(true)
        laboratoryVM.getLaboratories(city = city,  laboratoryTest = laboratoryTest).collect {
            if (it !is Result.Loading) {
                isLoading = false
                progressBar.dismiss()
            }
        }
    }

    LazyColumn(modifier = Modifier.padding(vertical = 30.dp, horizontal = 20.dp)) {
        item {
            Box(modifier = Modifier.height(60.dp)) {
                TopBar(text = "Test Centers near by") {
                    navController.popBackStack()
                }
            }

        }
        items(laboratoryVM.laboratories) { laboratory ->
            LaboratoryItem(laboratory = laboratory)
        }
    }

    BackHandler(enabled = progressBar.isShowing) {
        progressBar.dismiss()
        return@BackHandler
    }

    if (!isLoading && laboratoryVM.laboratories.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "No Test Center Found", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }

}

@Composable
fun LaboratoryItem(laboratory: Laboratory) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .clickableWithOutRipple {
                isExpanded = !isExpanded
            },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = laboratory.name,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 10.dp),
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = laboratory.address,
                style = MaterialTheme.typography.bodySmall, maxLines = 2,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 10.dp),
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally)
            ) {
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primaryColor)
                        .clickableWithOutRipple {
                            context.openWhatsApp(laboratory.phoneNumber)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chat".uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(primaryColor)
                        .clickableWithOutRipple {
                            context.openDialPanel(laboratory.phoneNumber)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Call".uppercase(),
                        color = Color.White, fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 5.dp)
                    )
                }
            }

            Text(
                text = if (isExpanded) "Click to hide Tests" else "Click to view tests", fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.bodySmall, maxLines = 2,
                fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                overflow = TextOverflow.Ellipsis
            )
            if (!isExpanded) return
            if (laboratory.tests.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
                Text(
                    text = "Tests",
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 2,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 5.dp),
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Test Name",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 5.dp),
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "Test Price",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 5.dp),
                        overflow = TextOverflow.Ellipsis
                    )

                }
                laboratory.tests.forEach {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 5.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = it.name,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = it.price,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 5.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                Text(
                    text = "No Tests Available ",
                    style = MaterialTheme.typography.bodySmall, maxLines = 2,
                    fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun BloodDonorItemPreview() {
    RescueMateTheme {
        Box(modifier = Modifier.background(Color.White)) {
            LaboratoryItem(
                laboratory = Laboratory(
                    ownerId = "",
                    city = "Lahore",
                    name = "Selena Willis",
                    phoneNumber = "(692) 356-1516",
                    isAvailable = false,
                    address = "eget",
                    tests = listOf(LaboratoryTest( name = "Santiago Valencia", price = "luctus"))
                )
            )
        }
    }
}