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
import com.processdumper.components.ListModal
import com.processdumper.components.LogScreen
import com.processdumper.getListProcess
import com.processdumper.model.ProcessInfo
import com.processdumper.model.enums.dumper.DumpAction
import com.processdumper.repository.ProcessRepository
import com.processdumper.service.Dumper

@Composable
fun HomeScreen(name: String, modifier: Modifier = Modifier) {
    var items by remember {
        mutableStateOf(listOf(""))
    }
    var showModal by remember { mutableStateOf(false) }
    var selectedProcess by remember { mutableStateOf<ProcessInfo?>(null) }
    var selectedFileName by remember { mutableStateOf<String?>(null) }
    var processRepository by remember { mutableStateOf(ProcessRepository()) }
    var lastUpdated by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current


    val onClickSelected : () -> Unit = {
        getListProcess(
            newList = { _processRepository ->
                if (_processRepository != null) {
                    processRepository = _processRepository;
                    items = processRepository.getAllFormated()
                }
            }, context = context
        );
        lastUpdated = System.currentTimeMillis();
        showModal = true;
    }

    val onClickDump : () -> Unit = {
        var dumper = Dumper(selectedProcess)
        var action: DumpAction = dumper.init();
        if(action == DumpAction.ASK_USER_FILE){
            showModal = true;
            items = dumper.getFilesName();
        }
        if (action == DumpAction.NOT_FOUND_FILE){

        }
        if(action == DumpAction.PROCEED){

        }
        if(action == DumpAction.ERROR){

        }
    }

    
    ListModal(showModal, onDismiss = { showModal = false }, clickableLine = { line ->
        selectedProcess = processRepository.getProcessInfo(line);
        showModal = false;
    }, items);


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = onClickSelected) {
            Text("Selecionar processo")
        }

        if (selectedProcess != null) {
            Button(onClick = onClickDump) {
                Text("Dump")
            }
        }

        LogScreen()

        Text(text = "Atualizado: $lastUpdated")
        if (selectedProcess != null) {
            Text(text = "Processo selecionado: ${selectedProcess?.packageName}")
        }
    }

}
