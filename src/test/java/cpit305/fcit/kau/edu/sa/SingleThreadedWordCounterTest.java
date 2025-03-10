package cpit305.fcit.kau.edu.sa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class SingleThreadedWordCounterTest {

    @TempDir
    Path tempDir;

    private Path sampleFile1;
    private Path sampleFile2;
    private Path emptyFile;

    @BeforeEach
    void setUp() throws IOException {
        // Create test files
        sampleFile1 = tempDir.resolve("sample1.txt");
        sampleFile2 = tempDir.resolve("sample2.txt");
        emptyFile = tempDir.resolve("empty.txt");

        // Write content to files
        Files.writeString(sampleFile1, "This is a test file with eight words.");
        Files.writeString(sampleFile2, "Another file with five words");
        Files.writeString(emptyFile, "");
    }

    @Test
    void testCountWordsInEmptyFile() throws IOException {
        long count = SingleThreadedWordCounter.countWords(emptyFile.toString());
        assertEquals(0, count, "Word count in empty file should be 0");
    }

    @Test
    void testCountWordsInFile() throws IOException {
        long count = SingleThreadedWordCounter.countWords(sampleFile1.toString());
        assertEquals(8, count, "Word count should match expected value");
    }

    @Test
    void testCountWordsInMultipleFiles() throws IOException {
        String[] filePaths = {sampleFile1.toString(), sampleFile2.toString(), emptyFile.toString()};
        long[] wordCounts = SingleThreadedWordCounter.countWordsInFiles(filePaths);

        assertEquals(3, wordCounts.length, "Should return count for all files");
        assertEquals(8, wordCounts[0], "First file should have 8 words");
        assertEquals(5, wordCounts[1], "Second file should have 5 words");
        assertEquals(0, wordCounts[2], "Empty file should have 0 words");
    }

    @Test
    void testWithInvalidFilePath() {
        Exception exception = assertThrows(IOException.class, () -> {
            SingleThreadedWordCounter.countWords("non_existent_file.txt");
        });

        assertTrue(exception.getMessage().contains("non_existent_file.txt") ||
                        exception instanceof IOException,
                "Should throw IOException for non-existent file");
    }

    @Test
    void testWithNullFilePath() {
        assertThrows(IllegalArgumentException.class, () -> {
            SingleThreadedWordCounter.countWords(null);
        }, "Should throw IllegalArgumentException for null file path");
    }

    @Test
    void testWithNullFilePathsArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            SingleThreadedWordCounter.countWordsInFiles(null);
        }, "Should throw IllegalArgumentException for null file paths array");
    }
}