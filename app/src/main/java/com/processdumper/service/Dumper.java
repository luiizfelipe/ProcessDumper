package com.processdumper.service;

import android.util.Log;

import com.processdumper.components.MapItem;
import com.processdumper.model.enums.dumper.DumpAction;
import com.processdumper.model.mapinfo.MapInfo;
import com.processdumper.model.ProcessInfo;
import com.processdumper.utils.LogManager;
import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.io.SuFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    public void start(MapItem selectedFilename){
        if(fileName.isEmpty()) return;

        List<MapInfo> filesName = this.memoryManagement.getAllMaps(process, fileName);
        MapInfo selectedMap = filesName
                .stream()
                .filter(map -> map.Id.equals(selectedFilename.getId()))
                .findFirst()
                .orElse(null);
        if(selectedMap == null){
            // TO DO: TRATAMENTO DE ERRO
            return;
        }
        LogManager.clear();
        //  public String Id;
        //    private final int PID;
        //    private String address = "";
        //    private String perms = "";
        //    private String offset = "";
        //    private String dev = "";
        //    private String inode = "";
        //    private String path = "";
        //    private String fileName = "";
        LogManager.log("-------- Dumping Start --------");
        LogManager.log("File Id: " + selectedMap.Id);
        LogManager.log("File PID: " + selectedMap.getPID());
        LogManager.log("File Start Address: " + selectedMap.getStartAddress());
        LogManager.log("File End Address: " + selectedMap.getEndAddress());
        LogManager.log("File Perms: " + selectedMap.getPerms());
        LogManager.log("File OffSet: " + selectedMap.getOffset());
        LogManager.log("File Path: " + selectedMap.getPath());
        LogManager.log("File Filename: " + selectedMap.getFileName());

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
