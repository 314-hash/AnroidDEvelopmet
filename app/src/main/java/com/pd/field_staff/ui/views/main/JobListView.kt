package com.pd.field_staff.ui.views.main

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.views.main.jobs.JobLocationView
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.R
import com.pd.field_staff.ui.components.LottieAnimationSpec
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.viewmodel.JobDetail
import com.pd.field_staff.ui.viewmodel.JobDetailViewModel
import com.pd.field_staff.ui.views.dialogs.MessageClientForm
import com.pd.field_staff.ui.views.dialogs.ShowSMSAndEmailDialog
import com.pd.field_staff.ui.views.dialogs.ShowSkipForm
import com.pd.field_staff.ui.views.main.jobs.JobDetailsView
import com.pd.field_staff.utils.extension.CacheUtils
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobListView(category: Int?) {

    val jobViewModel: JobDetailViewModel = koinViewModel()
    jobViewModel.loadJobList(category = category ?: 0)
    val jobList by jobViewModel.jobList.collectAsStateWithLifecycle()

    var jobAction by remember { mutableStateOf(JobItemSelected.START) }
    var selectedJob by remember { mutableStateOf<JobDetail?>(null) }
    var showMessageDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = false
    )

    LaunchedEffect(Unit) {
        jobViewModel.jobState.collect { state ->
            when(state){
                is JobDetailViewModel.JobState.Empty -> { }
                is JobDetailViewModel.JobState.Error -> { isLoading = false }
                is JobDetailViewModel.JobState.Loading -> { isLoading =  true }
                is JobDetailViewModel.JobState.Success -> { isLoading = false }
            }
        }
    }

    fun showModalSheet() {
        coroutineScope.launch {
            modalSheetState.show()
        }
    }

    fun hideModalSheet() {
        coroutineScope.launch {
            modalSheetState.hide()
        }
    }

    if(showMessageDialog){
        ShowSMSAndEmailDialog {
            showMessageDialog = false
        }
    }

    ModalBottomSheetLayout(
        sheetState = modalSheetState,
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetContent = {
            when(jobAction){
                JobItemSelected.SKIP -> ShowSkipForm()
                JobItemSelected.MESSAGE -> MessageClientForm(
                    onMessage = {
                        hideModalSheet()
                        showMessageDialog =  true
                    }
                )
                else -> { }
            }
        }
    ) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            var selectedTabIndex by remember { mutableIntStateOf(0) }
            val tabs = listOf("Todo", "In Progress")

            Column(
                verticalArrangement = Arrangement.Top
            ) {
                TabRow(
                    modifier = Modifier.padding(bottom = 20.dp)
                        .padding(horizontal = 10.dp),
                    containerColor = Color.White,
                    selectedTabIndex = selectedTabIndex
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title, style = RegularStyle) }
                        )
                    }
                }
                when (selectedTabIndex) {
                    0 -> TodoJob(
                        jobs = jobList,
                        skipJob = { job ->
                            selectedJob = job
                            jobAction = JobItemSelected.SKIP
                            showModalSheet()
                        },
                        messageClient = { job ->
                            selectedJob = job
                            jobAction = JobItemSelected.MESSAGE
                            showModalSheet()
                        }
                    )
                    1 -> InProgressJob { }
                }
            }

            if(isLoading) {
                LottieAnimationSpec(animRes = R.raw.loader_liquid_four_dot)
            }

        }

    }

}

@Composable
private fun TodoJob(
    jobs: List<JobDetail>,
    skipJob: (JobDetail) -> Unit,
    messageClient: (JobDetail) -> Unit
) {
    val currentContext = LocalContext.current
    val lazyState = rememberLazyListState()
    var expandedJobId by remember { mutableStateOf<String?>(null) }

    fun showJobLocation(job: JobDetail) {
        val intent = Intent(currentContext, JobLocationView::class.java)
        CacheUtils.selectedJob = job
        currentContext.startActivity(intent, currentContext.animationTransition())
    }

    fun showJobDetail(job: JobDetail) {
        val intent = Intent(currentContext, JobDetailsView::class.java)
        CacheUtils.selectedJob = job
        currentContext.startActivity(intent, currentContext.animationTransition())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyState
    ) {
        items(jobs, key = { it.id }) { job ->
            JobItemView(
                job = job,
                isMenuExpanded = expandedJobId == job.id,
                onMenuExpand = {
                    expandedJobId = if (expandedJobId == job.id) null else job.id
                },
                onJobSelected = { action ->
                    when(action){
                        JobItemSelected.START -> showJobDetail(job)
                        JobItemSelected.SKIP -> skipJob(job)
                        JobItemSelected.MESSAGE -> messageClient(job)
                        JobItemSelected.MAP -> showJobLocation(job)
                    }
                }
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }
    }
}

@Composable
private fun InProgressJob(showJob: ()-> Unit) {
    /*val inProgress  = listOf<Jobs>()
    val lazyState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyState
    ) {
        items(inProgress, key = { it.id }) {
            JobItemView(it) { showJob() }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }
    }*/
}

@Composable
private fun FinishedJob() {
    /*val lazyState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = lazyState
    ) {
        items(jobList, key = { it.id }) { job ->
            JobItemView(job) { }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }
    }*/
}

@Composable
private fun JobItemView(
    job: JobDetail,
    isMenuExpanded: Boolean,
    onMenuExpand: () -> Unit,
    onJobSelected: (JobItemSelected) -> Unit
) {
    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Image(
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)).clipToBounds(),
                    painter = painterResource(R.drawable.job_image),
                    contentDescription = null
                )

                Text(
                    text = job.distance, style = RegularStyle, color = ForestGreen,
                    modifier = Modifier.background(
                        color = Color.White,
                        shape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp)
                    ).padding(5.dp)
                )
            }

            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = job.name,
                    color = ForestGreen,
                    style = MediumStyle,
                    fontSize = 14.sp
                )
                Row {
                    Text(
                        text = "Start: ",
                        color = Color(0xFF777777),
                        style = RegularStyle,
                        fontSize = 12.sp
                    )
                    Text(
                        text = job.time,
                        color = Color.Black,
                        style = MediumStyle,
                        fontSize = 12.sp
                    )
                }
                Row {
                    Text(
                        text = "ETA: ",
                        color = Color(0xFF777777),
                        style = RegularStyle,
                        fontSize = 12.sp
                    )
                    Text(
                        text = job.duration,
                        color = Color.Black,
                        style = MediumStyle,
                        fontSize = 12.sp
                    )
                }
                Row {
                    Text(
                        text = "Location: ",
                        color = Color(0xFF777777),
                        style = RegularStyle,
                        fontSize = 12.sp
                    )
                    Text(
                        text = job.location,
                        color = Color.Black,
                        style = MediumStyle,
                        fontSize = 12.sp
                    )
                }
                Row {
                    Text(
                        text = "Client: ",
                        color = Color(0xFF777777),
                        style = RegularStyle,
                        fontSize = 12.sp
                    )
                    Text(
                        text = job.client.name,
                        color = Color.Black,
                        style = MediumStyle,
                        fontSize = 12.sp
                    )
                }

            }

            Icon(
                modifier = Modifier.clickable(onClick = onMenuExpand)
                    .background(color = ForestGreen, shape = CircleShape).padding(10.dp),
                imageVector = Icons.Default.MoreHoriz,
                tint = Color.White,
                contentDescription = null
            )
        }
        // menu here
        if(isMenuExpanded){
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { onJobSelected(JobItemSelected.START) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Text("Start", style = RegularStyle, color = Color.Black)
                    }

                }

                IconButton(
                    onClick = { onJobSelected(JobItemSelected.SKIP) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Text("Skip", style = RegularStyle, color = Color.Black)
                    }
                }

                IconButton(
                    onClick = { onJobSelected(JobItemSelected.MESSAGE) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Message,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Text("Message", style = RegularStyle, color = Color.Black)
                    }
                }

                IconButton(
                    onClick = { onJobSelected(JobItemSelected.MAP) }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = ForestGreen
                        )
                        Text("Map", style = RegularStyle, color = Color.Black)
                    }
                }


            }
        }
    }
}

enum class JobItemSelected {
    START, SKIP, MESSAGE, MAP
}