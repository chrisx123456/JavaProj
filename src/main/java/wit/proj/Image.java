package wit.proj;

//import java.util.Date;
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
import static org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants.TIFF_TAG_DATE_TIME;

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
        return "";
    }
    private String exifDate(String sourcePath) throws IOException, ImagingException {
        File file = new File(sourcePath);
        final ImageMetadata metadata = Imaging.getMetadata(file);
        if (metadata instanceof JpegImageMetadata jpegMetadata) {
            final TiffImageMetadata exifMetadata = jpegMetadata.getExif();
            final TiffField field = exifMetadata.findField(TIFF_TAG_DATE_TIME);
            if(field != null){
                try {
                    return convertDate(field.getStringValue());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
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
