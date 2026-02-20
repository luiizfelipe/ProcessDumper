package com.processdumper.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.processdumper.components.MapItem
import com.processdumper.getListProcess
import com.processdumper.model.ProcessInfo
import com.processdumper.model.enums.dumper.DumpAction
import com.processdumper.model.mapinfo.MapInfo
import com.processdumper.repository.ProcessRepository
import com.processdumper.service.Dumper

enum class TypeList {
    LIST_PROCESS,
    LIST_FILES
}


@Composable
fun HomeScreen(name: String, modifier: Modifier = Modifier) {
    var items by remember {
        mutableStateOf(listOf<MapItem>())
    }
    var fileName by remember { mutableStateOf("") }
    var showModal by remember { mutableStateOf(false) }
    var selectedProcess by remember { mutableStateOf<ProcessInfo?>(null) }
    var selectedFilename by remember { mutableStateOf<MapItem?>(null) }
    var processRepository by remember { mutableStateOf(ProcessRepository()) }
    var lastUpdated by remember { mutableStateOf(System.currentTimeMillis()) }
    val context = LocalContext.current
    var typeList: TypeList by remember { mutableStateOf<TypeList>(TypeList.LIST_PROCESS) }

    fun refreshProcessList() {
        lastUpdated = System.currentTimeMillis()
    }

    fun openModal() {
        showModal = true
    }

    fun openProcessSelector() {
        typeList = TypeList.LIST_PROCESS;
        refreshProcessList();
        getListProcess(
            newList = { _processRepository ->
                if (_processRepository != null) {
                    processRepository = _processRepository;
                    items = processRepository.getAllFormated()
                }
            }, context = context
        );
        openModal();
    }

    fun openFileSelector() {
        typeList = TypeList.LIST_FILES;
        refreshProcessList();
        openModal();
    }

    val onClickSelected : () -> Unit = {
        typeList = TypeList.LIST_PROCESS;
        openProcessSelector();
    }

    val onClickDump : () -> Unit = {
        if(fileName == selectedFilename?.name){
            var dumper = Dumper(selectedProcess, fileName)
            var action: DumpAction = dumper.init();
        }
        var dumper = Dumper(selectedProcess, fileName)
        var action: DumpAction = dumper.init();
        if(action == DumpAction.ASK_USER_FILE){
            showModal = true;
            val filesNames = mutableListOf<MapItem>()
            var hashMap : HashMap<String, List<MapInfo>> = dumper.memoryManagement.listMapsData;
            val listMaps: List<MapInfo> = hashMap.values.flatten()
            for (map in listMaps) {
                filesNames.add(MapItem(
                    id = map.Id,
                    name = map.fileName
                ));
            }
            items = filesNames;
            openFileSelector();
        }
        if (action == DumpAction.NOT_FOUND_FILE){

        }
        if(action == DumpAction.PROCEED){

        }
        if(action == DumpAction.ERROR){

        }
    }


    ListModal(showModal, onDismiss = { showModal = false }, clickableLine = { line, id ->
        if(typeList == TypeList.LIST_PROCESS){
            selectedProcess = processRepository.getProcessInfo(line);
            showModal = false;
        }
        else {
            fileName =  line;
            selectedFilename = MapItem(id,line);
            showModal = false;
        }
    }, items);


    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Button(onClick = onClickSelected) {
            Text("Selecionar processo")
        }

        TextField(
            value = fileName,
            onValueChange = { fileName = it },
            placeholder = { Text("Enter file name") },
            singleLine = true
        )

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
        if (fileName != null) {
            Text(text = "Arquivo selecionado: ${fileName}")
        }
    }

}
