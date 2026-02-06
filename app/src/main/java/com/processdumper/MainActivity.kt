package com.processdumper

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import com.processdumper.model.ProcessInfo
import com.processdumper.model.ProcessManager
import com.processdumper.ui.theme.ProcessDumperTheme
import com.topjohnwu.superuser.Shell
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Main Activity Init");
        enableEdgeToEdge()
        setContent {
            ProcessDumperTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var itens by remember {
        mutableStateOf(listOf("Item 1", "Item 2", "Item 3"))
    }
    var showModal by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<String?>(null) }


    var lastUpdated by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current
    if(!context.checkStoragePermission()) {
        Toast.makeText(context, "PERMISSÃO DE ARMAZENAMENTO NÃO CONCEDIDA", Toast.LENGTH_SHORT).show()
    }
    if (showModal) {
        Dialog(onDismissRequest = { showModal = false }) {

            Box(
                modifier = Modifier
                    .size(280.dp) // quadrado
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp)
            ) {
                LazyColumn {
                    items(itens) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    selectedItem = item
                                    showModal = false
                                }
                        )
                    }
                }

            }
        }
    }




    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text="Atualizado: $lastUpdated")
        Button(
            onClick = { getListProcess( newList = { list ->
                itens = list
            }, context = context); lastUpdated = System.currentTimeMillis()}
        ){
            Text("Recarregar processos")
        }

        Button(onClick = { showModal = true }) {
            Text("Selecionar processo")
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

fun getListProcess(newList: (List<String>) -> Unit, context: Context){
    var processManager = ProcessManager();
    val cmd : Shell.Result = Shell.cmd("ps").exec();
    if(!cmd.isSuccess()){
        newList(listOf("Error"));
        return;
    }

    val resultPs : List<String> = cmd.getOut();

    for ((index, process) in resultPs.withIndex()) {
        if(index == 0) continue;
        val processInfos: List<String> = process.split(" ");
        val packageName: String = processInfos.get(processInfos.size - 1);
        var pid : Int? = processInfos.get(1).toIntOrNull();
        processManager.add(ProcessInfo(context, packageName, pid))
    }

    newList(processManager.getAllFormated());
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProcessDumperTheme {
        Greeting("Android")
    }
}
