/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.davidellner.shottracker.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
/**
 *
 * @author davidellner
 */
public class ErrorLogger {
    private final static String ERROR_LOG_FILEPATH = "/Users/davidellner/logs/";
    /**
     *
     * @param errorDescription
     */
    public static void log(String errorDescription) {
		
		try {
                        File file = new File(ERROR_LOG_FILEPATH + StringUtilities.dateToFilename("txt"));
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.println(String.valueOf(new Date()) + "| " + errorDescription);
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
