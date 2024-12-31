package com.pd.field_staff.ui.views.main.jobs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardDoubleArrowDown
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.pd.field_staff.R
import com.pd.field_staff.ui.models.Jobs
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.InterFonts
import com.pd.field_staff.ui.theme.LightGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.FileItem
import com.pd.field_staff.ui.viewmodel.UploadViewModel
import com.pd.field_staff.ui.views.dialogs.AddPhotoDialog
import com.pd.field_staff.ui.views.main.DashboardView
import com.pd.field_staff.utils.NavigateInfoDialog
import com.pd.field_staff.utils.NavigationApp
import com.pd.field_staff.utils.createGoogleMapsIntent
import com.pd.field_staff.utils.createWazeIntent
import com.pd.field_staff.utils.extension.CacheUtils
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.utils.extension.showToast
import com.pd.field_staff.utils.isAppInstalled
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.io.File
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class JobDetailsView: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
        ExperimentalUuidApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                val context = LocalContext.current
                val longText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed eu lorem non mi rutrum lacinia at eu tortor. Mauris non tristique arcu, at imperdiet arcu. Duis vehicula sagittis tortor nec varius. Maecenas massa arcu, maximus vel lorem nec, dictum tincidunt odio. Vestibulum dignissim id mi luctus fermentum."
                val uploadViewModel: UploadViewModel = koinViewModel()
                val scrollState = rememberScrollState()
                val imageUri = remember { mutableStateOf<Uri?>(null) }
                val fileList by uploadViewModel.fileList.collectAsState()

                // Accompanist Permission State for Camera
                val cameraPermissionState = rememberPermissionState(permission =  android.Manifest.permission.CAMERA)
                var showAddPhotoDialog by remember { mutableStateOf(false) }
                var startedJob by remember { mutableStateOf(false) }

                // ActivityResultLauncher to take a picture and save it to the given URI
                val takePictureLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.TakePicture()
                ) { success ->
                    if (success) {
                        showAddPhotoDialog = true
                    } else {
                        imageUri.value = null
                    }
                }

                fun generateFileName(): Uri {
                     val imageFile = File(
                             applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                             "camera_capture_${System.currentTimeMillis()}.jpg"
                         )

                   return FileProvider.getUriForFile(
                        applicationContext,
                        "${applicationContext.packageName}.fileprovider",
                        imageFile
                    )
                }

                fun openCamera() {
                    when {
                        cameraPermissionState.status.isGranted -> {
                            // Permission is granted, launch camera
                            val imageFileUri = generateFileName()
                            imageUri.value = imageFileUri
                            takePictureLauncher.launch(imageFileUri)
                        }
                        cameraPermissionState.status.shouldShowRationale -> {
                            // Show rationale and request permission
                            cameraPermissionState.launchPermissionRequest()
                        }
                        else -> {
                            // Request permission
                            cameraPermissionState.launchPermissionRequest()
                        }
                    }
                }

                fun gotoDashboard() {
                    val intent = Intent(applicationContext, DashboardView::class.java)
                    startActivity(intent, animationTransition())
                    finish()
                }

                Scaffold (
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                Text(text = "Job Details", color = Color.Black, style = MediumStyle, fontSize = 14.sp)
                            }, navigationIcon = {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Color.Black,
                                    modifier = Modifier.clickable(onClick = { finish() }))

                            }
                        )
                    },
                    bottomBar = {
                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {
                            if(!startedJob) {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { startedJob = true }
                                ) {
                                    Text("Start the Job", style = MediumStyle)
                                }
                            }else {
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,
                                        contentColor = ForestGreen
                                    ),
                                    border = BorderStroke(1.dp, ForestGreen),
                                    onClick = { showToast("Successfully close the Job!") }
                                ) {
                                    Text("Close the Job", style = MediumStyle)
                                }
                            }
                        }
                    }
                ){ paddingValue ->

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                top = paddingValue.calculateTopPadding(),
                                bottom = paddingValue.calculateBottomPadding(),
                                start = 20.dp,
                                end = 20.dp
                            )

                    ) {
                        Column(
                            modifier = Modifier
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(CircleShape)
                                        .clipToBounds(),
                                    painter = painterResource(R.drawable.job_image),
                                    contentDescription = null
                                )
                                Column(
                                    modifier = Modifier.padding(start = 10.dp)
                                ) {
                                    Text("Plow the Snow", style = MediumStyle, fontSize = 15.sp, color = ForestGreen)
                                    // TODO refactor later
                                    val styledText = buildAnnotatedString {
                                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal, fontSize = 15.sp, fontFamily = InterFonts)) {
                                            append(" 46 ")
                                        }
                                        withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.Normal, fontSize = 12.sp, fontFamily = InterFonts)) {
                                            append("(28)")
                                        }
                                    }
                                    Text(text = styledText, textAlign = TextAlign.Center)
                                }
                            }


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 5.dp)
                            ) {
                                HorizontalDivider(Modifier.padding(bottom = 20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("Date", style = RegularStyle)
                                        Text("12 May 2024", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                                    }
                                    VerticalDivider(thickness = 2.dp, color = Color.DarkGray)
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text("Start time", style = RegularStyle)
                                        Text("9:00-11:00 AM", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                                    }
                                }
                                HorizontalDivider(Modifier.padding(top = 20.dp))
                            }


                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Estimated Labor Time", style = RegularStyle)
                                Text("60 minutes", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                                HorizontalDivider(Modifier.padding(top = 20.dp))
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Size of the home", style = RegularStyle)
                                Text("1,000 - 3,000 sq ft", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                                HorizontalDivider(Modifier.padding(top = 20.dp))
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Address", style = RegularStyle)
                                Text("8605 Freeport Pkwy", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                                HorizontalDivider(Modifier.padding(top = 20.dp))
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 5.dp, bottom = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text("Total fee", style = RegularStyle)
                                Text("$ 150.00", style = BoldStyle, fontSize = 15.sp, color = Color.Black)
                                HorizontalDivider(Modifier.padding(top = 20.dp))
                            }



                            Text( modifier = Modifier.padding(vertical = 10.dp),
                                text = "Check List:",  style = MediumStyle, color = ForestGreen, fontSize = 17.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(checked = true, onCheckedChange = { })
                                Text("Lorem Ipsum loren ipsum - 1",  style = RegularStyle)
                            }


                            Text(modifier = Modifier.padding(vertical = 10.dp),
                                text = "Notes:",  style = MediumStyle, color = ForestGreen, fontSize = 17.sp)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        color = Color(0xFFDEE0E4).copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(10.dp),
                                text = longText,
                                textAlign = TextAlign.Justify,
                                style = RegularStyle,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(20.dp))

                            Text(modifier = Modifier.padding(vertical = 10.dp),
                                text = "Are there any photos you'd like to share?",  style = RegularStyle, fontSize = 15.sp)


                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    modifier = Modifier.size(50.dp).clickable(onClick = { openCamera() }),
                                    painter = painterResource(R.drawable.ic_take_picture),
                                    contentDescription = null
                                )
                                LazyRow(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    items(fileList, key = {it.id}) { file ->
                                        FileItemView(
                                            fileItem = file,
                                            onRemove = {  },
                                            onStart = {  },
                                            onCancel = {  }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(color = LightGreen)
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Reports", style = RegularStyle)
                                Icon(imageVector = Icons.Default.KeyboardDoubleArrowDown, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    if(showAddPhotoDialog && imageUri.value != null) {
                        AddPhotoDialog(
                            uri = imageUri.value!!,
                            onDismissRequest = { showAddPhotoDialog = false },
                            onConfirm = { description ->
                                showAddPhotoDialog = false
                                uploadViewModel.addFile(
                                    FileItem(
                                        id = Uuid.random().toString(),
                                        name = description,
                                        uri = imageUri.value!!
                                    )
                                )
                                // Handle the confirmed action here
                            }
                        )
                    }



                }
            }
        }
    }

}

@Composable
fun FileItemView(
    fileItem: FileItem,
    onRemove: () -> Unit,
    onStart: () -> Unit,
    onCancel: () -> Unit
) {

    //val progress by fileItem.progress
    //val uploadStatus by fileItem.status

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .border(width = Dp.Hairline, color = ForestGreen)
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(fileItem.uri),
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            //Text(text = fileItem.name)

            /*Text(
                text = when (uploadStatus) {
                    UploadStatus.Pending -> "Pending"
                    UploadStatus.Uploading -> "Uploading.Please wait..."
                    UploadStatus.Success -> "Uploaded Successfully"
                    UploadStatus.Failed -> "Upload Failed"
                },
                color = when (uploadStatus) {
                    UploadStatus.Success -> PrimaryColor
                    UploadStatus.Failed -> Color.Red
                    else -> Color.Black
                }
            )
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onStart, enabled = !fileItem.isUploading) {
                    Icon(Icons.Default.Upload, contentDescription = "Start Upload")
                }
                IconButton(onClick = onCancel, enabled = fileItem.isUploading) {
                    Icon(Icons.Default.Cancel, contentDescription = "Cancel Upload")
                }
                IconButton(onClick = onRemove, enabled = !fileItem.isUploading) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove File")
                }
            }*/
        }
    }
}
