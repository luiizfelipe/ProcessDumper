package com.processdumper.model;

import android.util.Log;

import java.util.Arrays;

public class MapInfo {
    private final int PID;
    private String address = "";
    private String perms = "";
    private String offset = "";
    private String dev = "";
    private String inode = "";
    private String path = "";
    private String fileName = "";

    public MapInfo(int PID, String mapData) {
        this.PID = PID;
        mapData = mapData.replaceAll("\\s+", " ");
        String[] dataString = mapData.split(" ");
        if(dataString.length < 6) {
            throw new IllegalArgumentException("Erro no mapeamento: formato inválido");

        }
        address = dataString[0];
        perms = dataString[1];
        offset = dataString[2];
        dev = dataString[3];
        inode = dataString[4];
        path = this.getFullStringStartingFromSplit(mapData, " ", 5);
        if (!path.contains("[")) {
            if (path.contains(":")) {
                fileName = "";
            } else {
                if (path.contains(" ")) {
                    String[] stringData = path.split(" ");
                    if (path.contains("/")) {

                        String[] stringData2 = stringData[0].split("/");
                        fileName = stringData2[stringData2.length - 1];
                    } else {
                        fileName = "";
                    }
                } else {
                    if (path.contains("/")) {
                        fileName = path.split("/")[path.split("/").length - 1];
                    } else {
                        fileName = "";
                    }
                }
            }
        } else {
           fileName = "";
        }
    }
    private String getFullStringStartingFromSplit(String input, String find, int startIndex) {

        if(input.contains(find))
        {
            // Split the input string into an array of substrings based on the whitespace delimiter
            String[] substrings = input.split(find);

            // Define the index of the substring to start concatenating from startIndex
            // Concatenate the substrings starting from the startIndex
            return String.join(" ", Arrays.copyOfRange(substrings, startIndex, substrings.length));
        }
        return input;
    }
}

