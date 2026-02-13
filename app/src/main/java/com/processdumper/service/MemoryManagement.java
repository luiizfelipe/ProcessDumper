package com.processdumper.service;

import android.util.Log;

import com.processdumper.model.MapInfo;
import com.processdumper.model.MemoryInfo;
import com.processdumper.model.ProcessInfo;
import com.topjohnwu.superuser.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class MemoryManagement {
    private HashMap<String, String> listMapsData;

    private List<MapInfo> getAllMaps(ProcessInfo process, String fileName) {

        List<MapInfo> listMaps = new ArrayList<>();

        int PID = -1;
        for (int pid : process.getPids()) {

            StringBuilder mapsString = new StringBuilder();

            Shell.Result cmd = Shell.cmd("cat /proc/" + pid + "/maps").exec();

            if (!cmd.isSuccess()) {
                return listMaps;
            }

            List<MapInfo> createListMaps = new ArrayList<>();

            List<String> output = cmd.getOut();

            for (int i = 0; i < (output.size()); i++) {

                String lines = output.get(i).replaceAll("\\s+", " ");

                if (!lines.contains("USER") && !lines.contains("PID") && !lines.contains("PPID") && !lines.contains("NAME")) {

                    if(lines.toString().contains(fileName)){
                        Timber.d("[+] MAP FILES [+] %s", lines);
                        listMapsData.put("Maps-" + pid + ".txt",lines + "\n");
                        createListMaps.add(new MapInfo(pid, lines));
                    }
                }
            }

            if (PID == -1) {
                PID = pid;
            }

        }
        return listMaps;
    }
}
