package com.processdumper

import android.Manifest
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.processdumper.repository.ProcessRepository
import com.processdumper.screens.HomeScreen
import com.processdumper.ui.theme.ProcessDumperTheme
import com.processdumper.utils.LogManager
import com.topjohnwu.superuser.Shell
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        enableEdgeToEdge()

        val builder = Shell.Builder.create()
            .setFlags(Shell.FLAG_MOUNT_MASTER)

        Shell.setDefaultBuilder(builder)

        setContent {
            ProcessDumperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HomeScreen(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



fun  Context.checkStoragePermission(): Boolean {
    var result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    if(result ==  PERMISSION_GRANTED) {
        return true;
    }

    result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    if(result ==  PERMISSION_GRANTED) {
        return true;
    }

    result = ContextCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
    if(result ==  PERMISSION_GRANTED) {
        return true;
    }
    return false;
}

fun getListProcess(newList: (processRepository: ProcessRepository?) -> Unit, context: Context) {
    var processRepository = ProcessRepository();
    val cmd : Shell.Result = Shell.cmd("ps").exec();
    if(!cmd.isSuccess()){
        newList(null);
        return;
    }

    val resultPs : List<String> = cmd.getOut();

    for ((index, process) in resultPs.withIndex()) {
        if(index == 0) continue;
        val processInfos: List<String> = process.split("\\s+".toRegex())
        val packageName: String = processInfos.get(processInfos.size - 1);
        var pid = processInfos.get(1).toIntOrNull();
        processRepository.add(context, packageName, pid)
    }
    for (process in processRepository.getAllFormated()){
        LogManager.log(process.name);
    }
    newList(processRepository);
}
