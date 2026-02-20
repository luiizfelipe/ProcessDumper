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

    public MemoryManagement memoryManagement;
    public ProcessInfo process;
    String fileName = "";

    public Dumper(ProcessInfo process, String fileName){
        this.process = process;
        this.fileName = fileName;
        this.memoryManagement = new MemoryManagement();
    }

    public DumpAction init(){
        List<MapInfo> filesName = this.memoryManagement.getAllMaps(process, fileName);
        int amountFiles = filesName.size();
        if(amountFiles == 0){
            return DumpAction.NOT_FOUND_FILE;
        }
        if(amountFiles == 1){
            return DumpAction.PROCEED;
        }
        return DumpAction.ASK_USER_FILE;
    }

    public void start(){
        if(fileName.isEmpty()) return;

        Shell.Result cmd = Shell.cmd("dd if=/system/lib/libjavacrypto.so of=/data/local/tmp/output bs=1 skip=${}").exec();

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
