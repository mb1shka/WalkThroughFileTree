package com.javarush.task.task31.task3101;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/* 
Проход по дереву файлов
*/

public class Solution {
    public static void main(String[] args) {
        String path = args[0];
        String resultFileAbsolutePath = args[1];
        try {
            File resultFile = new File(resultFileAbsolutePath);
            File dest = new File(resultFile.getParentFile() + "/allFilesContent.txt");
            if (FileUtils.isExist(dest)) {
                FileUtils.deleteFile(dest);
            }
            FileUtils.renameFile(resultFile, dest);

            List<String> fileTree = getFileTree(path); //получаем все файлы

            List<File> sortedFiles = sort(fileTree); //сортируем по имени файла в возрастающем порядке

            writeAllFiles(sortedFiles, dest);

        } catch (IOException e) {

        }
    }

    public static List<String> getFileTree(String root) throws IOException {
        File rootDir = new File(root); //директория как файл

        ArrayList<String> result = new ArrayList<>();
        Queue<File> fileQueue = new PriorityQueue<>();  //все файлы в директории rootDir

        Collections.addAll(fileQueue, rootDir.listFiles());

        while (!fileQueue.isEmpty()) {
            File currentFile = fileQueue.remove();
            if (currentFile.isDirectory()) {
                Collections.addAll(fileQueue, currentFile.listFiles());
            } else {
                result.add(currentFile.getAbsolutePath());
            }
        }
        return result;
    }

    public static List<File> sort(List<String> fileTree) {
        ArrayList<File> sortedFiles = new ArrayList<>();

        for (String fileName : fileTree) {
            File file = new File(fileName);
            if (file.length() <= 50) {
                sortedFiles.add(file);
            }
        }
        Collections.sort(sortedFiles, Comparator.comparing(File::getName));
        return sortedFiles;
    }

    public static void writeAllFiles(List<File> fileList, File result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
            for (File file : fileList) {
                String str;
                BufferedReader reader = new BufferedReader(new FileReader(file));

                while ((str = reader.readLine()) != null) {
                    writer.write(str);
                    writer.write("\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
