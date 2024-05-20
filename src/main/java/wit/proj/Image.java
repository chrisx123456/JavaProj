package wit.proj;

import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;


public class Image {
    public Image(String sourcePath) throws IOException {
        this.sourcePath = sourcePath;
        this.creationDate = exifDate(sourcePath);
    }
    private final String sourcePath;
    private final String creationDate;

    @Override
    public String toString(){
        if(creationDate != null)
            return creationDate;
        return "brak daty";
    }
    private String exifDate(String sourcePath) throws IOException, ImagingException {
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
            final TiffField field = jpegMetadata.findExifValueWithExactMatch(TiffTagConstants.TIFF_TAG_DATE_TIME);
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
    public String getSourcePath() {
        return sourcePath;
    }
    public String getCreationDate() {
        return creationDate;
    }

}