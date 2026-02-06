package com.processdumper.model

class ProcessManager {
    private val processes: MutableList<ProcessInfo> = mutableListOf()

    fun add(process: ProcessInfo) {
        processes.add(process)
    }

    fun getAll(): List<ProcessInfo> = processes

    fun getAllFormated(): MutableList<String>{
        var processFormated : MutableList<String> = mutableListOf();
        for (process in this.processes) {
            processFormated.add(process.PackageName);
        }
        return processFormated;
    }
}