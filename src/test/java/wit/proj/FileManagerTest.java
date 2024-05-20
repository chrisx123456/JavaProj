package wit.proj;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    @Test
    void getImagesPathsTest() throws Exception {
        //Arrange
        FileManager fm = new FileManager();
        File testFolder = new File("src/test/resources/testowy");

        //Act
        FileManager.GetImagesPaths(testFolder);

        //Assert
        assertNotNull(fm.pathsList);
        assertTrue(fm.pathsList.size() == 57);

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
        //Przypadek gdzie lista plików zawiera pliki bez daty wykonania
        //Arrange
        List<File> empty = new ArrayList<>();

        //Act
        Exception exception = assertThrows(Exception.class, () -> FileManager.CreateImageList(empty));

        //Assert
        assertEquals("No images at given dir", exception.getMessage());

    }

    @Test
    void save() {

    }
}