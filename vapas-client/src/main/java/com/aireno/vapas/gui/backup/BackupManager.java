package com.aireno.vapas.gui.backup;

import com.aireno.base.ClassBase;
import com.aireno.utils.ADateUtils;

import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Airenas
 * Date: 13.2.11
 * Time: 22.43
 * To change this template use File | Settings | File Templates.
 */
public class BackupManager extends ClassBase {
    public void backupDb() {
        getLog().info("Starting backup");
        backupDb("vapas.script");
        backupDb("vapas.log");
        backupDb("vapas.properties");
        getLog().info("Done");
    }

    private void backupDb(String file) {
        String fileName = String.format("kopija/%s_%s", file, ADateUtils.dateToString(new Date()));
        String fileNameFrom = String.format("db/%s", file);
        copyFile(fileNameFrom, fileName);
    }

    private void copyFile(String fileNameFrom, String fileName) {
        try{
            File f1 = new File(fileNameFrom);
            File f2 = new File(fileName);
            InputStream in = new FileInputStream(f1);

            //For Append the file.
//  OutputStream out = new FileOutputStream(f2,true);
            //For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            getLog().info("File '{}' copied.", fileName);
        }
        catch(FileNotFoundException ex){
            getLog().error("Can not copy file.", ex);
            //System.exit(0);
        }
        catch(IOException e){
            getLog().error("Can not copy file.", e);
        }
    }
}
