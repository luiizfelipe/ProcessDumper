package com.processdumper.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.processdumper.checkStoragePermission
import com.processdumper.components.LogScreen
import com.processdumper.getListProcess
import com.processdumper.model.ProcessInfo
import com.processdumper.repository.ProcessRepository
import com.processdumper.service.Dumper
import com.processdumper.utils.LogManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(name: String, modifier: Modifier = Modifier) {
    var itens by remember {
        mutableStateOf(listOf("a"))
    }
    var showModal by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf<ProcessInfo?>(null) }
    var processRepository by remember { mutableStateOf(ProcessRepository()) }
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
                                    selectedItem = processRepository.getProcessInfo(item);
                                    selectedItem.let {
                                        LogManager.log("Selected item: " + it?.packageName);
                                    }
                                    showModal = false;
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
        horizontalAlignment = Alignment.Start) {
        Button(onClick = {
            getListProcess(
                newList = { _processRepository->
                    if(_processRepository != null){
                        processRepository = _processRepository;
                        itens = processRepository.getAllFormated()
                    }
                }, context = context);
            lastUpdated = System.currentTimeMillis();
            showModal = true;

        }) {
            Text("Selecionar processo")
        }
        Button(onClick = {
            var dumper = Dumper(selectedItem)
        }) {

            Text("Dump")
        }
        LogScreen()

        Text(text="Atualizado: $lastUpdated")
        if(selectedItem != null) {
            Text(text="Processo selecionado: ${selectedItem?.packageName}")
        }
    }

}
