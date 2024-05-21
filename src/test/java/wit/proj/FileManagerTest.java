package wit.proj;

import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Klasa testu dla klasy FileManager
 * @author Bartosz Kowalczyk
 * @since 20.05.2024
 * @version 1.1
 */
class FileManagerTest {
    /**
     * Testowanie metody GetImagesPaths
     */
    @Test
    public void getImagesPathsTest() {
        //Arrange
        FileManager fm = new FileManager();
        File testFolder = new File("src/test/resources/testowy");

        //Act
        FileManager.GetImagesPaths(testFolder);

        //Assert
        assertNotNull(fm.pathsList);
        assertTrue(fm.pathsList.size() >= 76);

    }
    /**
     * Testowanie metody CreateImageList dla prawidłowych plików
     */
    @Test
    public void createImageListForValidFilesTest() throws Exception{
        //Przypadek dla poprawnych obrazków
        //Arrange
        List<File> validFiles = new ArrayList<>();
        validFiles.add(new File("src/test/resources/testowy/Canon_PowerShot_S40.jpg"));
        validFiles.add(new File("src/test/resources/testowy/Canon_DIGITAL_IXUS_400.jpg"));

        //Act
        List<Image> img = FileManager.CreateImageList(validFiles);


        //Assert
        assertNotNull(img);
        assertEquals(2, img.size());
    }
    /**
     * Testowanie metody CreateImageList dla nieprawidłowych plików
     */
    @Test
    public void createImageListForInValidFilesTest() throws Exception{
        //Przypadek gdzie lista plików zawiera pliki błędne
        //Arrange
        List<File> invalidFiles = new ArrayList<>();
        invalidFiles.add(new File("src/test/resources/testowy/Canon_PowerShot_S40.jpg"));
        invalidFiles.add(new File("src/test/resources/testowy/Canon_DIGITAL_IXUS_400.jpg"));
        invalidFiles.add(new File("src/test/resources/testowy/invalid/image01088.jpg"));


        //Act
        List<Image> img = FileManager.CreateImageList(invalidFiles);


        //Assert
        assertNotNull(img);
        assertEquals(2, img.size());

    }
    /**
     * Testowanie metody CreateImageList dla pustej listy
     */
    @Test
    public void createImageListEmptyListTest() throws Exception{
        //Przypadek gdzie lista plików jest pusta
        //Arrange
        List<File> empty = new ArrayList<>();

        //Act
        Exception exception = assertThrows(Exception.class, () -> FileManager.CreateImageList(empty));

        //Assert
        assertEquals("Paths list is null or empty", exception.getMessage());

    }
    /**
     * Testowanie metody CreateImageList dla nullowej listy
     */
    @Test
    public void createImageListNullListTest() throws Exception{
        //Przypadek gdzie lista plików jest nullem
        //Arrange & Act
        Exception exception = assertThrows(Exception.class, () -> FileManager.CreateImageList(null));

        //Assert
        assertEquals("Paths list is null or empty", exception.getMessage());

    }
    /**
     * Testowanie metody Save
     */
    @Test
    public void saveTest() throws Exception{
        //Arrange
        File folder = new File("src/test/resources/testowy");
        Image img1 = new Image("src/test/resources/testowy/Canon_PowerShot_S40.jpg");
        Image img2 = new Image("src/test/resources/testowy/Canon_DIGITAL_IXUS_400.jpg");
        List<Image> images = Arrays.asList(img1, img2);

        //Act
        FileManager.Save(folder, images);

        //Assert
        File subFolder1 = new File(folder, img1.getCreationDate());
        File subFolder2 = new File(folder, img2.getCreationDate());
        //
        assertTrue(subFolder1.exists());
        assertTrue(subFolder1.isDirectory());
        assertTrue(subFolder1.listFiles().length >= 1);
        //
        assertTrue(subFolder2.exists());
        assertTrue(subFolder2.isDirectory());
        assertTrue(subFolder2.listFiles().length >= 1);
    }
    /**
     * Testowanie metody Save dla wyjątku NullPointerException
     */
    @Test
    public void saveNullPointerExceptionForNullListTest() throws Exception{
        //Arrange
        File folder = new File("src/test/resources/testowy");
        List<Image> images = null;

        //Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> FileManager.Save(folder, images));
        assertEquals("Image list is null or empty", exception.getMessage());
    }
    /**
     * Testowanie metody Save dla wyjątku IOException w przypadku nieistniejacego katalogu
     */
    @Test
    public void saveIOExceptionNonExistedDirectoryTest() throws Exception{
        //Arrange
        File folder = new File("Z:/nieistniejacykatalog");
        Image img1 = new Image("src/test/resources/testowy/Canon_PowerShot_S40.jpg");
        List<Image> images = Arrays.asList(img1);

        //Act & Assert
        Exception exception = assertThrows(IOException.class, () -> FileManager.Save(folder, images));
        assertEquals("Failed to create folder Z:\\nieistniejacykatalog\\2003\\12\\14", exception.getMessage());
    }
    /**
     * Testowanie metody Save dla wyjątku IOException w przypadku pliku jako katalogu
     */
    @Test
    public void saveIOExceptionFileAsDirectoryTest() throws Exception{
        //Arrange
        Path file = Files.createTempFile("temp", ".jpg");
        File folder = file.toFile();
        Image img1 = new Image("src/test/resources/testowy/Canon_PowerShot_S40.jpg");
        List<Image> images = Arrays.asList(img1);

        //Act & Assert
        Exception exception = assertThrows(IOException.class, () -> FileManager.Save(folder, images));
        assertTrue(exception.getMessage().contains("Failed to create folder"));
    }
}