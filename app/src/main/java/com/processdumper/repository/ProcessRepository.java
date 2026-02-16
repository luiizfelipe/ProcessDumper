package com.processdumper.repository;

import android.content.Context;
import android.util.Log;

import com.processdumper.model.ProcessInfo;
import com.processdumper.utils.LogManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessRepository {
    private final HashMap<String,ProcessInfo> processes = new HashMap<>();

    public HashMap<String, ProcessInfo> getProcesses() {
        return processes;
    }

    public ProcessInfo getProcessInfo(String packageName){
        return processes.get(packageName);
    }

    public List<String> getAllFormated(){
        List<String> processFormatted = new ArrayList<>();

        for (ProcessInfo process : this.processes.values()) {
            if(process.appName != "App Name not found"){
                processFormatted.add(process.packageName);
            }
        }

        return processFormatted;
    }

    public boolean add(Context context, String packageName, Integer pid){
        if(pid <= 0){
            return false;
        }
        ProcessInfo info = processes.get(packageName);
        if(info != null){
            info.pids.add(pid);
        }else {
            processes.put(packageName,
                    new ProcessInfo(context, packageName, new ArrayList<>(List.of(pid))));
            ;
        }
        return true;
    }
}
