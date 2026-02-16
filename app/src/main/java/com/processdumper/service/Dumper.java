package com.processdumper.service;

import android.util.Log;

import com.processdumper.model.enums.dumper.DumpAction;
import com.processdumper.model.mapinfo.MapInfo;
import com.processdumper.model.ProcessInfo;
import com.processdumper.utils.LogManager;
import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.io.SuFile;

import java.util.ArrayList;
import java.util.List;


public class Dumper {

    MemoryManagement memoryManagement;
    ProcessInfo process;
    String fileName = "";


    public Dumper(ProcessInfo process){
        this.process = process;
        this.memoryManagement = new MemoryManagement();
    }

    public DumpAction init(){
        List<String> filesName = this.getFileNames();
        int amountFiles = filesName.size();
        if(amountFiles == 0){
            return DumpAction.NOT_FOUND_FILE;
        }
        if(amountFiles == 1){
            return DumpAction.PROCEED;
        }
        return DumpAction.ASK_USER_FILE;
    }

    private List<String> getFileNames(){
        List<MapInfo> maps = this.memoryManagement.getAllMaps(process, fileName);
        List<String> filesName = new ArrayList<>();
        for (MapInfo map : maps) {
            String fileName = map.getFileName();
            if(fileName.isEmpty()) continue;
            filesName.add(fileName);
            LogManager.log(fileName);
        }
        return filesName;
    }

    private void dumpFile(){

    }

    private boolean dumpMemory() {


        return true;
    }

    private boolean createDirectory(String path){
        SuFile directory = new SuFile(path);
        return directory.mkdirs();
    }

}
