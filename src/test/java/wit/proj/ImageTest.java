package wit.proj;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Klasa testu dla klasy Image
 * @author Bartosz Kowalczyk
 * @since 21.05.2024
 * @version 1.0
 */
class ImageTest {
    /**
     * Testowanie utworzenia daty na podstawie pliku
     */
    @Test
    void CreationDateTest() {
        try {
            //Arrange
            Image image = new Image("src/test/resources/testowy/Canon_PowerShot_S40.jpg");
            //Act & Assert
            assertNotNull(image.getCreationDate(), "Data utworzenia nie powinna być pusta");
            assertTrue(image.getCreationDate().matches("\\d{4}/\\d{2}/\\d{2}"), "Data utworzenia powinna być w formacie yyyy/MM/dd");
        } catch (Exception e) {
            fail("Nie powinien wystąpić wyjątek: " + e.getMessage());
        }
    }
    /**
     * Testowanie wyświetlenia w konsoli komunikatu o braku metadanych
     */
    @Test
    void withoutMetaDataTest() {
        //Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try{
            //Act
            Image img = new Image("src/test/resources/testowy/invalid/image01088.jpg");
            System.setOut(System.out);
            String output = out.toString().trim();
            //Assert
            assertEquals("Brak metadanych dla pliku: src/test/resources/testowy/invalid/image01088.jpg", output);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Testowanie wyświetlenia w konsoli komunikatu o odczytaniu EXIF dla danego pliku
     */
    @Test
    void dataEXIFTest() {
        //Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try{
            //Act
            Image img = new Image("src/test/resources/testowy/xmp/no_exif.jpg");
            System.setOut(System.out);
            String output = out.toString().trim();
            //Assert
            assertEquals("Data EXIF dla pliku src/test/resources/testowy/xmp/no_exif.jpg: 2014:09:22 10:56:35", output);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Testowanie wyświetlenia w konsoli komunikatu o braku pola TIFF_TAG_DATE_TIME dla danego pliku
     */
    @Test
    void withoutTIFF_TAG_DATE_TIMETest() {
        //Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        try{
            //Act
            Image img = new Image("src/test/resources/testowy/orientation/landscape_2.jpg");
            System.setOut(System.out);
            String output = out.toString().trim();
            //Assert
            assertEquals("Brak pola TIFF_TAG_DATE_TIME dla pliku: src/test/resources/testowy/orientation/landscape_2.jpg", output);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}