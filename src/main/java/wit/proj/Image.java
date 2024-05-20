package wit.proj;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.TIFF_TAG_DATE_TIME;
/**
 * Klasa przechowuje ścieżkę źródłową pliku .jpg i na jej podstawie otrzymuje i zapisuje datę utworzenia tegoż pliku
 * @author Szymon Krukowski
 * @since 19.05.2024
 * @version 1.0
 */
public class Image {
    public Image(String sourcePath) throws IOException {
        this.sourcePath = sourcePath;
        this.creationDate = exifDate(sourcePath);
    }
    /**
     * stała ścieżka źródłowa obsługiwanego pliku w zmiennej String
     */
    private final String sourcePath;
    /**
     * stała data w formacie "yyyy/MM/dd" w zmiennej String
     */
    private final String creationDate;
    /**
     * uzyskuje metadane na podstawie Stringa ścieżki źródłowej pliku .jpg i zwraca pole TIFF_TAG_DATE_TIME
     * @param sourcePath ścieżka źródłowa pliku .jpg jako String
     * @return pole TIFF_TAG_DATE_TIME z metadanych EXIF pliku znajdującego sie pod ścieżką zawartą w parametrze sourcePath , format: "yyyy/MM/dd"
     * @throws IOException wyrzuca IOException, jeśli nie jest w stanie zwrócić oczekiwanej wartości
     */
    private String exifDate(String sourcePath) throws IOException {
        File file = new File(sourcePath);
        final ImageMetadata metadata = Imaging.getMetadata(file);
        if (metadata == null) {
            System.out.println("Brak metadanych dla pliku: " + sourcePath);
            return null;
        }
        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            if (exifMetadata == null) {
                System.out.println("Brak metadanych EXIF dla pliku: " + sourcePath);
                return null;
            }
            final TiffField field = exifMetadata.findField(TIFF_TAG_DATE_TIME);
            if (field != null) {
                String exifDateString = field.getStringValue();
                System.out.println("Data EXIF dla pliku " + sourcePath + ": " + exifDateString);
                try {
                    return convertDate(exifDateString);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("Brak pola TIFF_TAG_DATE_TIME dla pliku: " + sourcePath);
            }
        } else {
            System.out.println("Metadane nie są instancją JpegImageMetadata dla pliku: " + sourcePath);
        }
        return null;
    }

    /**
     *
     * @param exifDateString String daty z godziną wykonania obrazu w
     * @return String wejściowy sformatowany w samą datę
     * @throws Exception wyrzuca Eception, jeśli nie jest w stanie poprawnie sparsować i zwrócić Stringa wynikowego
     */
    private String convertDate(String exifDateString) throws Exception{
        SimpleDateFormat exifDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = exifDateFormat.parse(exifDateString);
            return desiredDateFormat.format(date);
        } catch (Exception e) {
            throw new Exception("Failed to convert date to new format");
        }
    }
    //-------------------------\\
    //---------GETTERS---------\\
    //-------------------------\\
    /**
     * getter zmiennej sourcePath
     * @return Ścieżka źródłowa pliku .jpg jako String
     */
    public String getSourcePath() {
        return sourcePath;
    }
    /**
     * getter zmiennej creationDate
     * @return data utworzenie pliku .jpg w formacie "yyyy/MM/dd" jako String
     */
    public String getCreationDate() {
        return creationDate;
    }
}
