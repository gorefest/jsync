package net.mantucon.jsync.actions.steps;

import net.mantucon.jsync.Configuration;
import net.mantucon.jsync.actions.*;
import net.mantucon.jsync.util.JSyncLogger;

import java.io.*;

/**
 * Created by marcus on 15.04.14.
 */
public class CopyFileStep implements Step {

    private final File sourceFile;
    private final File destFile;

    public CopyFileStep(File sourceFile, File destFile) {
        this.sourceFile = sourceFile;
        this.destFile = destFile;
    }

    @Override
    public void perform() {
        performCopy();
    }

    private void performCopy() {
        JSyncLogger logger = Configuration.getLogger();
        if (logger.isDebugEnabled()) {
            logger.info(Thread.currentThread().getName() + ": COPY "+sourceFile.getAbsolutePath());
        }

        try {
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destFile);
            byte[] buffer = new byte[4096];
            int bytesRead = 0;

            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer,0,bytesRead);
            }

            inputStream.close();
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "CopyFileAction{" +
                "sourceFile=" + sourceFile.getName() +
                ", destFile=" + destFile.getName() +
                '}';
    }
}
