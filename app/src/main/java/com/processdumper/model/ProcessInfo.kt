package com.processdumper.model

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class ProcessInfo(){

    var AppName: String = ""
    var PackageName: String = ""
    var Pid: Int? = -1

    constructor(context: Context, packageName: String, pid: Int?) : this(){
        var applicationInfo : ApplicationInfo? = null;
        try {

            applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);

        } catch (e: PackageManager.NameNotFoundException ) {
            e.printStackTrace();
        }
        finally
        {
            PackageName = packageName;

            Pid = pid;

            if(applicationInfo != null){
                AppName = context.getPackageManager().getApplicationLabel(applicationInfo).toString();
            }
            else {
                AppName = "App Name not found";
            }
        }
    }
}