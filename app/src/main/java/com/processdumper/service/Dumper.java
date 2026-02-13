package com.processdumper.service;

import com.processdumper.model.ProcessInfo;
import com.processdumper.utils.LogManager;
import com.topjohnwu.superuser.Shell;
import com.topjohnwu.superuser.io.SuFile;

public class Dumper {

    ProcessInfo process;

    public Dumper(ProcessInfo process){
        this.process = process;
    }

    public void init(){
        createDirectory("output/");

    }



    private void dumpFile(){

    }

    private boolean dumpMemory(){



        return true;
    }

    private boolean createDirectory(String path){
        LogManager.log("Diretório criado:" + path );
        SuFile directory = new SuFile(path);

        return directory.mkdirs();
    }

}
