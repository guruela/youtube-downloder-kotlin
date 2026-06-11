package `in`.microkid.youtubedownloader

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yausername.ffmpeg.FFmpeg
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import `in`.microkid.youtubedownloader.ui.theme.YoutubeDownloaderTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

private val ytdlpUpdateChecked = AtomicBoolean(false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        try {
            YoutubeDL.getInstance().init(applicationContext)
            FFmpeg.getInstance().init(applicationContext)
        } catch (e: YoutubeDLException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to initialize yt-dlp", Toast.LENGTH_LONG).show()
        }
        setContent {
            YoutubeDownloaderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DownloaderScreen()
                }
            }
        }
    }
}

@Composable
fun DownloaderScreen() {
    var url by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val appContext = LocalContext.current.applicationContext

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Place YouTube URL here")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        )   {
            Button(onClick = {
                coroutineScope.launch {
                    startDownload(appContext, url, isAudioOnly = false)
                }
            })  {
                Text("Download Video")
            }

            Button(onClick = {
                coroutineScope.launch {
                    startDownload(appContext, url, isAudioOnly = true)
                }
            })  {
                Text("Download Audio")
            }
        }
    }
}

suspend fun startDownload(appContext: Context, url: String, isAudioOnly: Boolean)    {
    withContext(Dispatchers.IO) {
        try {
            updateYtDlpIfNeeded(appContext)

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val request = YoutubeDLRequest(url)

            request.addOption("--no-update")
            request.addOption("-o", "${downloadsDir.absolutePath}/%(title)s.%(ext)s")

            if (isAudioOnly)    {
                request.addOption("-x")
                request.addOption("--audio-format", "mp3")
            } else {
                request.addOption("-f", "best[ext=mp4]/best")
            }

            YoutubeDL.getInstance().execute(request)    { progress, etaInSeconds, _ ->
                println("Progress: $progress%, ETA: $etaInSeconds seconds")
            }

            println("Download Complete!")
        } catch (e: Exception)  {
            e.printStackTrace()
            println("Download Failed: ${e.message}")
        }
    }
}

private fun updateYtDlpIfNeeded(appContext: Context) {
    if (!ytdlpUpdateChecked.compareAndSet(false, true)) return

    try {
         val status = YoutubeDL.getInstance().updateYoutubeDL(
            appContext,
            YoutubeDL.UpdateChannel.STABLE
        )
        println("yt-dlp update status: $status")
    } catch (e: Exception) {
        ytdlpUpdateChecked.set(false)
        e.printStackTrace()
        println("yt-dlp update check failed: ${e.message}")
    }
}
