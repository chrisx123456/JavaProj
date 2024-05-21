package wit.proj;

//import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.TIFF_TAG_DATE_TIME;

public class Image {
    public Image(String sourcePath) throws Exception {
        this.sourcePath = sourcePath;
        this.creationDate = exifDate(sourcePath);
    }
    private final String sourcePath;
    private final String creationDate;

    private String exifDate(String sourcePath) throws Exception {
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

            TiffField field = null;
            TiffField field1 = exifMetadata.findField(TIFF_TAG_DATE_TIME);
            TiffField field2 = jpegMetadata.findExifValueWithExactMatch(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
            if (field1 != null) {
                field = field1;
            } else if (field2 != null) {
                field = field2;
            }

            if (field != null) {
                String exifDateString = field.getStringValue();
                System.out.println("Data EXIF dla pliku " + sourcePath + ": " + exifDateString);
                try {
                    return convertDate(exifDateString);
                } catch (ParseException e) {
                    throw new Exception(e.getMessage() + e.getErrorOffset());
                }
            } else {
                System.out.println("Brak pola TIFF_TAG_DATE_TIME dla pliku: " + sourcePath);
            }
        } else {
            System.out.println("Metadane nie są instancją JpegImageMetadata dla pliku: " + sourcePath);
        }
        return null;
    }

    private String convertDate(String exifDateString) throws ParseException {
        SimpleDateFormat exifDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = exifDateFormat.parse(exifDateString);
            return desiredDateFormat.format(date);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
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
