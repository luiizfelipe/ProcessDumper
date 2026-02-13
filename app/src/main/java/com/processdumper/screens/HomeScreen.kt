package com.processdumper.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.processdumper.components.LogScreen
import com.processdumper.components.ProcessListModal
import com.processdumper.getListProcess
import com.processdumper.model.ProcessInfo
import com.processdumper.repository.ProcessRepository
import com.processdumper.service.Dumper
import com.processdumper.utils.LogManager

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

    ProcessListModal(showModal, onDismiss = { showModal = false }, clickableLine = { line ->
        selectedItem = processRepository.getProcessInfo(line);
        showModal = false;
    }, itens);


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = {
            getListProcess(
                newList = { _processRepository ->
                    if (_processRepository != null) {
                        processRepository = _processRepository;
                        itens = processRepository.getAllFormated()
                    }
                }, context = context
            );
            lastUpdated = System.currentTimeMillis();
            showModal = true;

        }) {
            Text("Selecionar processo")
        }
        Button(onClick = {
            var dumper = Dumper(selectedItem)
            dumper.init();
        }) {

            Text("Dump")
        }
        LogScreen()

        Text(text = "Atualizado: $lastUpdated")
        if (selectedItem != null) {
            Text(text = "Processo selecionado: ${selectedItem?.packageName}")
        }
    }

}
