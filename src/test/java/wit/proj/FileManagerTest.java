package wit.proj;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class FileManagerTest {

    @Test
    void getImagesPathsTest() {
        //Arrange
        FileManager fm = new FileManager();
        File testFolder = new File("src/test/resources/testowy");

        //Act
        FileManager.GetImagesPaths(testFolder);

        //Assert
        assertNotNull(fm.pathsList);
        assertTrue(fm.pathsList.size() >= 57);

    }

    @Test
    void createImageListForValidFilesTest() throws Exception{
        //Przypadek dla poprawnych obrazków
        //Arrange
        List<File> validFiles = new ArrayList<>();
        validFiles.add(new File("src/test/resources/testowy/Archer.(Fate.stay.night).full.1801264.jpg"));
        validFiles.add(new File("src/test/resources/testowy/caster-fate-grand-order-solomon-goetia-anime-34552-resized.jpg"));

        //Act
        List<Image> img = FileManager.CreateImageList(validFiles);


        //Assert
        assertNotNull(img);
        assertEquals(2, img.size());
    }

    @Test
    void createImageListForInValidFilesTest() throws Exception{
        //Przypadek gdzie lista plików zawiera pliki bez daty wykonania
        //Arrange
        List<File> invalidFiles = new ArrayList<>();
        invalidFiles.add(new File("src/test/resources/testowy/Archer.(Fate.stay.night).full.1801264.jpg"));
        invalidFiles.add(new File("src/test/resources/testowy/caster-fate-grand-order-solomon-goetia-anime-34552-resized.jpg"));
        invalidFiles.add(new File("src/test/resources/testowy/78932960_2576492565801783_3461086214226968576_n.jpg"));


        //Act
        List<Image> img = FileManager.CreateImageList(invalidFiles);


        //Assert
        assertNotNull(img);
        assertEquals(2, img.size());

    }

    @Test
    void createImageListEmptyListTest() throws Exception{
        //Przypadek gdzie lista plików jest pusta
        //Arrange
        List<File> empty = new ArrayList<>();

        //Act
        Exception exception = assertThrows(Exception.class, () -> FileManager.CreateImageList(empty));

        //Assert
        assertEquals("Paths list is null or empty", exception.getMessage());

    }

    @Test
    void createImageListNullListTest() throws Exception{
        //Przypadek gdzie lista plików jest nullem
        //Arrange & Act
        Exception exception = assertThrows(Exception.class, () -> FileManager.CreateImageList(null));

        //Assert
        assertEquals("Paths list is null or empty", exception.getMessage());

    }

    @Test
    void saveTest() throws Exception{
        //Arrange
        File folder = new File("src/test/resources/testowy");
        Image img1 = new Image("src/test/resources/testowy/Archer.(Fate.stay.night).full.1801264.jpg");
        Image img2 = new Image("src/test/resources/testowy/caster-fate-grand-order-solomon-goetia-anime-34552-resized.jpg");
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

    @Test
    void saveNullPointerExceptionForNullListTest() throws Exception{
        //Arrange
        File folder = new File("src/test/resources/testowy");
        List<Image> images = null;

        //Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> FileManager.Save(folder, images));
        assertEquals("Image list is null or empty", exception.getMessage());
    }

    @Test
    void saveNullPointerExceptionForEmptyListTest() throws Exception{
        //Arrange
        File folder = new File("src/test/resources/testowy");
        List<Image> images = Collections.emptyList();

        //Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> FileManager.Save(folder, images));
        assertEquals("Image list is null or empty", exception.getMessage());
    }

    @Test
    void saveIOExceptionNonExistedDirectoryTest() throws Exception{
        //Arrange
        File folder = new File("Z:/nieistniejacykatalog");
        Image img1 = new Image("src/test/resources/testowy/Archer.(Fate.stay.night).full.1801264.jpg");
        List<Image> images = Arrays.asList(img1);

        //Act & Assert
        Exception exception = assertThrows(IOException.class, () -> FileManager.Save(folder, images));
        assertEquals("Failed to create folder Z:\\nieistniejacykatalog\\2014\\11\\17", exception.getMessage());
    }

    @Test
    void saveIOExceptionFileAsDirectoryTest() throws Exception{
        //Arrange
        Path file = Files.createTempFile("temp", ".jpg");
        File folder = file.toFile();
        Image img1 = new Image("src/test/resources/testowy/Archer.(Fate.stay.night).full.1801264.jpg");
        List<Image> images = Arrays.asList(img1);

        //Act & Assert
        Exception exception = assertThrows(IOException.class, () -> FileManager.Save(folder, images));
        assertTrue(exception.getMessage().contains("Failed to create folder"));
    }
}