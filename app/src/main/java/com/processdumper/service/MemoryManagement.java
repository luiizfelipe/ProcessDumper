package com.processdumper.service;

import com.processdumper.model.mapinfo.InvalidMapFormatException;
import com.processdumper.model.mapinfo.MapInfo;
import com.processdumper.model.ProcessInfo;
import com.processdumper.utils.LogManager;
import com.topjohnwu.superuser.Shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import timber.log.Timber;

public class MemoryManagement {
    private HashMap<String, List<MapInfo>> listMapsData = new HashMap<>();

    public HashMap<String, List<MapInfo>> getListMapsData() {
        return listMapsData;
    }

    public List<MapInfo> getAllMaps(ProcessInfo process, String fileName) {

        List<MapInfo> createListMaps = new ArrayList<>();

        int PID = -1;
        for (Integer pid : process.pids) {

            StringBuilder mapsString = new StringBuilder();

            Shell.Result cmd = Shell.cmd("cat /proc/" + pid + "/maps").exec();

            if (!cmd.isSuccess()) {
                return createListMaps;
            }


            List<String> output = cmd.getOut();

            for (int i = 0; i < (output.size()); i++) {
                try {
                    String lines = output.get(i).replaceAll("\\s+", " ");

                    if (!lines.contains("USER") && !lines.contains("PID") && !lines.contains("PPID") && !lines.contains("NAME")) {

                        if(lines.toString().contains(fileName)){
                            createListMaps.add(new MapInfo(pid, lines));
                        }
                    }
                }
                catch (InvalidMapFormatException e) {
                    Timber.tag("MapParser").w("Linha ignorada: %s", output.get(i));
                }


            }
            listMapsData.put(pid.toString(), createListMaps);

            if (PID == -1) {
                PID = pid;
            }

        }
        return createListMaps;
    }
}
