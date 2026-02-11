package com.processdumper.model

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class ProcessInfo(){

    var AppName: String = ""
    var PackageName: String = ""

    var Pids: MutableList<Int> = mutableListOf()



    constructor(context: Context, packageName: String, pids: MutableList<Int>) : this(){
        var applicationInfo : ApplicationInfo? = null;
        try {

            applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);

        } catch (e: PackageManager.NameNotFoundException ) {
            e.printStackTrace();
        }
        finally
        {
            PackageName = packageName;

            Pids = pids;

            if(applicationInfo != null){
                AppName = context.getPackageManager().getApplicationLabel(applicationInfo).toString();
            }
            else {
                AppName = "App Name not found";
            }
        }
    }
}