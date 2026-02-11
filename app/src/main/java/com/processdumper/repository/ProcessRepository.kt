package com.processdumper.repository

import android.content.Context
import com.processdumper.model.ProcessInfo

class ProcessRepository  {
    private val processes: MutableList<ProcessInfo> = mutableListOf()

    fun add(context: Context, packageName: String, pid: Int) {
        if(pid <= 0){
            return;
        }
        val existing = processes.find { it ->
            it.PackageName === packageName
        }
        if(existing != null){
            existing.Pids.add(pid)
        }
        else {
            processes.add(
                ProcessInfo(
                    context,
                    packageName,
                    mutableListOf(pid)
                )
            )
        }
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