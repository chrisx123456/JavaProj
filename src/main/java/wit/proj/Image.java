package wit.proj;

//import java.util.Date;
import java.io.File;
import java.io.IOException;

/** nieprzydały się - nie czytało commons może sam to musze ogarnąć sb
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.common.ImageMetadata.ImageMetadataItem;
import org.apache.commons.imaging.common.RationalNumber;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.GpsTagConstants;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.apache.commons.imaging.formats.tiff.taginfos.TagInfo;
 **/
// alternatywnie można dodać zależność w maven do tego metadata extcractora ale miała być minimalna liczba zależności
import javax.imageio.ImageIO;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

public class Image {
    public Image(String sourcePath) {
        this.sourcePath = sourcePath;
        this.exifDate();
    }
    private String sourcePath = null;
    private String creationDate = null;


    @Override
    public String toString(){
        if(creationDate != null)
            return creationDate.toString();
        return "";
    }
    //podjebane całkowicie z chatu jeszcze do ogarnięcia
    public void exifDate(){
        if(sourcePath != null) {
            try {
                File imageFile = new File(sourcePath);
                ImageInputStream iis = ImageIO.createImageInputStream(imageFile);
                // Getting the reader for the image format
                javax.imageio.ImageReader imageReader = ImageIO.getImageReaders(iis).next();
                // Set input stream
                imageReader.setInput(iis, true);
                // Getting metadata
                IIOMetadata metadata = imageReader.getImageMetadata(0);
                // Get the Exif directory
                org.w3c.dom.Node exifNode = metadata.getAsTree("javax_imageio_1.0");
                creationDate = getTagValue(exifNode, "DateTimeOriginal");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static String getTagValue(org.w3c.dom.Node exifNode, String tagName) {
        org.w3c.dom.NodeList nodes = exifNode.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            if (node.getNodeName().equals("TIFFField")) {
                String name = node.getAttributes().getNamedItem("name").getNodeValue();
                if (name.equals(tagName)) {
                    return node.getAttributes().getNamedItem("value").getNodeValue();
                }
            }
        }
        return null;
    }
    //-------------------------\\
    //---------SETTERS---------\\
    //-------------------------\\
    public String getSourcePath() {
        return sourcePath;
    }
    public String getCreationDate() {
        return creationDate;
    }

}
