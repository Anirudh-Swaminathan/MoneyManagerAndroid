package com.anirudh.anirudhswami.personalassistant;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anirudh Swami on 16-07-2016 for the project PersonalAssistant.
 */
public class FileHelper {

    Context context;

    public FileHelper(Context c) {
        this.context = c;
    }

    /**
     * @param fileName fileName
     * @return rets
     */
    public List<Double> read(String fileName) {
        List<Double> rets = new ArrayList<>();
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String output;

            while ((output = bufferedReader.readLine()) != null) {
                if(output!="") {
                    rets.add(Double.parseDouble(output));
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rets;
    }

    /**
     * @param fileName Name of file to delete
     */
    public void deleteFile(String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            boolean deleted = file.delete();
            if (deleted) {
                //Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Could not delete file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName Name of file to delete
     * @param msg      Data to write
     */
    public void write(String fileName, String msg) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
            //Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName Name of file to delete last line from
     */
    public void delLastLine(String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rw");

            byte b;
            long length = 0;

            length = randomAccessFile.length();

            if (length != 0) {
                do {
                    length -= 1;
                    randomAccessFile.seek(length);
                    b = randomAccessFile.readByte();
                } while (b != 10 && length > 0);
                randomAccessFile.setLength(length);
                randomAccessFile.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param fileName Name of file to append info
     * @param input    Data to append at end of the file
     */
    public void appendLine(String fileName, String input) {
        String msg = System.lineSeparator();
        msg += input;
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_APPEND);
            fileOutputStream.write(msg.getBytes());
            fileOutputStream.close();
            //Toast.makeText(context, "File Appended", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
