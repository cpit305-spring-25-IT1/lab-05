package cpit305.fcit.kau.edu.sa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class MultiThreadedWordCounterTest {

    @TempDir
    Path tempDir;

    private Path sampleFile1;
    private Path sampleFile2;
    private Path largeFile;
    private Path emptyFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create test files
        sampleFile1 = tempDir.resolve("sample1.txt");
        sampleFile2 = tempDir.resolve("sample2.txt");
        largeFile = tempDir.resolve("large.txt");
        emptyFile = tempDir.resolve("empty.txt");

        // Write content to files
        Files.writeString(sampleFile1, "This is a test file with eight words.");
        Files.writeString(sampleFile2, "Another file with five words");

        // Create a larger file with repeated content for multi-threading tests
        StringBuilder largeContent = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            largeContent.append("This is line ").append(i).append(" with some test words. ");
        }
        Files.writeString(largeFile, largeContent.toString());

        Files.writeString(emptyFile, "");
    }

    @Test
    void testCountWordsInMultipleFiles() throws IOException, InterruptedException {
        String[] filePaths = {sampleFile1.toString(), sampleFile2.toString(), emptyFile.toString()};
        long[] wordCounts = MultiThreadedWordCounter.countWordsInFiles(filePaths);

        assertEquals(3, wordCounts.length, "Should return count for all files");
        assertEquals(8, wordCounts[0], "First file should have 8 words");
        assertEquals(5, wordCounts[1], "Second file should have 5 words");
        assertEquals(0, wordCounts[2], "Empty file should have 0 words");
    }

    @Test
    void testCountWordsInSingleFileWithMultipleThreads() throws IOException, InterruptedException {
        // Expected word count in the large file
        long expectedCount = 8000; // 8 words per line * 1000 lines

        // Test with different numbers of threads
        for (int numThreads = 1; numThreads <= 8; numThreads *= 2) {
            long count = MultiThreadedWordCounter.countWords(largeFile.toString(), numThreads);
            assertEquals(expectedCount, count,
                    "Word count should be consistent regardless of thread count (" + numThreads + ")");
        }
    }

    @Test
    void testWithEmptyFile() throws IOException, InterruptedException {
        long count = MultiThreadedWordCounter.countWords(emptyFile.toString(), 4);
        assertEquals(0, count, "Word count in empty file should be 0");
    }

    @Test
    void testWithInvalidFilePath() {
        Exception exception = assertThrows(IOException.class, () -> {
            MultiThreadedWordCounter.countWords("non_existent_file.txt", 2);
        });

        assertTrue(exception.getMessage().contains("non_existent_file.txt") ||
                        exception instanceof IOException,
                "Should throw IOException for non-existent file");
    }

    @Test
    void testWithInvalidThreadCount() {
        assertThrows(IllegalArgumentException.class, () -> {
            MultiThreadedWordCounter.countWords(sampleFile1.toString(), 0);
        }, "Should throw IllegalArgumentException for non-positive thread count");

        assertThrows(IllegalArgumentException.class, () -> {
            MultiThreadedWordCounter.countWords(sampleFile1.toString(), -1);
        }, "Should throw IllegalArgumentException for negative thread count");
    }

    @Test
    void testWithNullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            MultiThreadedWordCounter.countWords(null, 2);
        }, "Should throw IllegalArgumentException for null file path");
    }

    @Test
    void testWithNullFilePathsArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            MultiThreadedWordCounter.countWordsInFiles(null);
        }, "Should throw IllegalArgumentException for null file paths array");
    }

    @Test
    void testConcurrencyConsistency() throws IOException, InterruptedException {
        // Compare results between single-threaded and multi-threaded implementations
        long singleThreadResult = MultiThreadedWordCounter.countWords(largeFile.toString(), 1);
        long multiThreadResult = MultiThreadedWordCounter.countWords(largeFile.toString(), 4);

        assertEquals(singleThreadResult, multiThreadResult,
                "Results should be consistent between single and multi-threaded execution");
    }
}