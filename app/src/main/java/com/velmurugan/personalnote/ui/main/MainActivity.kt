package com.velmurugan.personalnote.ui.main

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.velmurugan.personalnote.ui.home.HomeScreen
import com.velmurugan.personalnote.ui.routine.RoutineScreen
import com.velmurugan.personalnote.ui.shopping.ShoppingScreen
import com.velmurugan.personalnote.ui.tasks.TaskScreen
import com.velmurugan.personalnote.ui.theme.PersonalNoteTheme
import com.velmurugan.personalnote.ui.weight.WeightScreen
import com.velmurugan.personalnote.views.PopupMenuItems
import com.velmurugan.personalnote.views.SubtitleText
import com.velmurugan.personalnote.views.TitleText
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var shareFileLauncher: ActivityResultLauncher<Intent>
    private lateinit var pickFileLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pickFileLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let {
                    val file = fileFromContentUri(context = this@MainActivity, it)
                    if (file.exists()) {
                        mainViewModel.restoreDatabase(file)
                    }
                }
            }

        shareFileLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                // Handle the result if needed
                if (result.resultCode == RESULT_OK) {

                } else {

                }
            }
        setContent {
            PersonalNoteTheme {
                val context = LocalContext.current
                val mainUiState by mainViewModel.mainUiState.collectAsState()

                val navController = rememberNavController()
                var toolbarTitle by remember {
                    mutableStateOf("Personal Note")
                }

                var actionText by remember {
                    mutableStateOf("")
                }

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val isShowBackNavigator = when (navBackStackEntry?.destination?.route) {
                    HOME -> false
                    else -> true
                }

                val isActionVisible = when (navBackStackEntry?.destination?.route) {
                    HOME -> true
                    else -> true
                }

                val isHome = navBackStackEntry?.destination?.route == HOME

                Scaffold(modifier = Modifier.fillMaxSize(),

                    topBar = {
                        TopAppBar(title = {
                            TitleText(text = toolbarTitle)
                        },
                            navigationIcon = {
                                if (isShowBackNavigator) {
                                    Icon(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clickable {
                                                navController.navigateUp()
                                            },
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "back"
                                    )
                                }
                            },
                            actions = {
                                if (isActionVisible) {
                                    /*if (!isHome) {
                                        SubtitleText(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            text = actionText
                                        )
                                    }*/

                                    var expanded by remember { mutableStateOf(false) }
                                    Icon(imageVector = Icons.Default.MoreVert,
                                        contentDescription = "More",
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .clickable {
                                                expanded = !expanded
                                            })

                                    PopupMenuItems(
                                        expanded = expanded,
                                        onDismissRequest = {
                                            expanded = false
                                        },
                                        menuItems = mainViewModel.getMenuItems(navBackStackEntry?.destination?.route.orEmpty()),
                                        onMenuAction = {
                                            when (it) {
                                                "Backup" -> {
                                                    expanded = false
                                                    val file =
                                                        File(context.filesDir, "backup.json")
                                                    mainViewModel.backupDatabase(file)
                                                }

                                                "Restore" -> {
                                                    expanded = false
                                                    pickFileLauncher.launch("*/*")
                                                }
                                                "Reset Routine" -> {
                                                    expanded = false
                                                    mainViewModel.resetRoutine()
                                                }
                                                "Reset Task" -> {
                                                    expanded = false
                                                    mainViewModel.resetTask()
                                                }
                                                "Clear Shopping" -> {
                                                    expanded = false
                                                    mainViewModel.resetShopping()
                                                }
                                                else -> {}

                                            }
                                        })

                                }
                            })
                    }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavHost(navController = navController, startDestination = HOME) {
                            composable(HOME) {
                                toolbarTitle = "Personal Note"
                                HomeScreen(navController)

                            }

                            composable(TASK) {
                                actionText = mainUiState.tasksCount
                                toolbarTitle = "Task List"
                                TaskScreen(navController = navController)
                            }

                            composable(ROUTINE) {
                                actionText = mainUiState.routinesCount
                                toolbarTitle = "Routines"
                                RoutineScreen(navController = navController)
                            }

                            composable(SHOPPING) {
                                actionText = mainUiState.shoppingCount
                                toolbarTitle = "Shopping List"
                                ShoppingScreen(navController = navController)
                            }

                           /* composable(WEIGHT) {
                                toolbarTitle = "Weight Monitor"
                                WeightScreen(navController = navController)
                            }*/
                        }

                    }

                    LaunchedEffect(key1 = Unit) {
                        mainViewModel.mainEvent.collect { event ->
                            when (event) {
                                is MainEvent.OnBackUpSuccess -> {
                                    val fileUri: Uri = FileProvider.getUriForFile(
                                        context,
                                        "com.velmurugan.personalnote.provider",
                                        event.file
                                    )
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_STREAM, fileUri)
                                        type =
                                            "application/json" // or "application/pdf" depending on the file type
                                    }

                                    // Create a chooser Intent to let the user select an app
                                    val chooserIntent =
                                        Intent.createChooser(sendIntent, "Share file using")

                                    // Grant read URI permission to the receiving apps
                                    val grantUriPermissions = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    chooserIntent.addFlags(grantUriPermissions)
                                    shareFileLauncher.launch(chooserIntent)
                                }

                                is MainEvent.OnError -> {
                                    // Handle error
                                }
                            }
                        }

                    }
                }

            }
        }
    }
}

private fun fileFromContentUri(context: Context, contentUri: Uri): File {

    val fileExtension = getFileExtension(context, contentUri)
    val fileName = "temporary_file" + if (fileExtension != null) ".$fileExtension" else ""

    val tempFile = File(context.cacheDir, fileName)
    tempFile.createNewFile()

    try {
        val oStream = FileOutputStream(tempFile)
        val inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let {
            copy(inputStream, oStream)
        }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? {
    val fileType: String? = context.contentResolver.getType(uri)
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(fileType)
}

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buf = ByteArray(8192)
    var length: Int
    while (source.read(buf).also { length = it } > 0) {
        target.write(buf, 0, length)
    }
}

const val HOME = "Home"
const val TASK = "Task"
const val ROUTINE = "Routine"
const val SHOPPING = "Shopping"
const val WEIGHT = "Weight"