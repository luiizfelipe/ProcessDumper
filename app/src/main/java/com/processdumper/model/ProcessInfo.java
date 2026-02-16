package com.processdumper.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.processdumper.utils.LogManager;

import java.util.List;

public class ProcessInfo {
    public String appName;
    public String packageName;
    public List<Integer> pids;

    public ProcessInfo(Context context, String packageName, List<Integer> pids){
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = context.getPackageManager().getApplicationInfo(packageName,0);

        } catch (Exception e) {

        }
        finally {
            this.packageName = packageName;
            this.pids = pids;

            this.appName = (applicationInfo != null ? context.getPackageManager().getApplicationLabel(applicationInfo).toString() : "App Name not found");
        }
    }

}
