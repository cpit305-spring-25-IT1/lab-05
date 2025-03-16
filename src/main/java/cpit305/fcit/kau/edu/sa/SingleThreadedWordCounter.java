package cpit305.fcit.kau.edu.sa;

import java.io.*;


public class SingleThreadedWordCounter {
    /**
     * Counts the number of words in the given file using a single thread
     * @param filePath Path to the file to count words in
     * @return the number of words in the file
     */
    public static long countWords(String filePath) throws IOException, IllegalArgumentException {
        if(filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }
        long totalWords = 0;



        return totalWords;
    }

    /**
     * Counts words in multiple files using a single thread (sequentially)
     * @param filePaths Array of file paths to count words in
     * @return Array of word counts corresponding to each file
     */
    public static long[] countWordsInFiles(String[] filePaths) throws IOException, IllegalArgumentException {
        if(filePaths == null || filePaths.length == 0) {
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        long[] wordCounts = new long[filePaths.length];



        return wordCounts;
    }

    /**
     * Helper method that counts words in a given text
     */
    private static long countWordsInText(String text) {
        if (text == null || text.isEmpty()) {
            return 0;
        }




        return 0;
    }
}