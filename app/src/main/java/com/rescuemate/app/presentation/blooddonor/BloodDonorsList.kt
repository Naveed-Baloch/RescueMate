package com.rescuemate.app.presentation.blooddonor

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.rescuemate.app.R
import com.rescuemate.app.dto.BloodDonor
import com.rescuemate.app.extensions.clickableWithOutRipple
import com.rescuemate.app.extensions.isVisible
import com.rescuemate.app.extensions.openDialPanel
import com.rescuemate.app.extensions.openWhatsApp
import com.rescuemate.app.extensions.progressBar
import com.rescuemate.app.presentation.theme.RescueMateTheme
import com.rescuemate.app.presentation.theme.primaryColor
import com.rescuemate.app.presentation.viewmodel.BloodVM
import com.rescuemate.app.repository.Result
import com.rescuemate.app.utils.TopBar

@Composable
fun BloodDonorsScreen(bloodGroup: String, city: String, bloodVM: BloodVM = hiltViewModel(), navController: NavHostController) {
    val context = LocalContext.current
    val progressBar = remember { context.progressBar() }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        isLoading = true
        progressBar.isVisible(true)
        bloodVM.getBloodDonors(bloodGroup = bloodGroup, city = city).collect {
            if (it !is Result.Loading) {
                isLoading = false
                progressBar.dismiss()
            }
        }
    }

    LazyColumn(modifier = Modifier.padding(vertical = 10.dp, horizontal = 20.dp)) {
        item {
            Box(modifier = Modifier.height(45.dp)) {
                TopBar(text = "Matching Donors") {
                    navController.navigateUp()
                }
            }

        }
        items(bloodVM.bloodDonors) { donor ->
            BloodDonorItem(bloodDonor = donor, onClick = {})
        }
    }

    BackHandler(enabled = progressBar.isShowing) {
        progressBar.dismiss()
        return@BackHandler
    }

    if (!isLoading && bloodVM.bloodDonors.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "No Donor Found", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        }
    }

}

@Composable
fun BloodDonorItem(bloodDonor: BloodDonor, onClick: () -> Unit) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 0.6f),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickableWithOutRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier) {
                    AsyncImage(
                        model = bloodDonor.profilePicUrl,
                        contentDescription = "",
                        placeholder = painterResource(id = R.drawable.ic_profile),
                        error = painterResource(id = R.drawable.ic_profile),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickableWithOutRipple { }
                    )
                    Column {
                        Text(
                            text = bloodDonor.name,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 10.dp),
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = bloodDonor.address,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                            modifier = Modifier.padding(start = 10.dp),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(45.dp)
                        .background(primaryColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = bloodDonor.bloodGroup,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2, color = Color.White,
                        fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
                        modifier = Modifier,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

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
                            context.openWhatsApp(bloodDonor.phoneNumber)
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
                            context.openDialPanel(bloodDonor.phoneNumber)
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
        }
    }
}

@Preview
@Composable
fun BloodDonorItemPreview() {
    RescueMateTheme {
        Box(modifier = Modifier.background(Color.White)) {
            BloodDonorItem(
                bloodDonor = BloodDonor(
                    userId = "",
                    name = "Naveed",
                    address = "Ali Town Lahore",
                    bloodGroup = "A+",
                    howOftenCanDonate = 5,
                    medicalHistory = "Non ",
                    isAvailable = true
                )
            ) {

            }
        }
    }
}