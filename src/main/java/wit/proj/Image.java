package wit.proj;

import java.util.Date;

public class Image {
    public Image(String sourcePath, Date creationDate) {
        this.sourcePath = sourcePath;
        this.creationDate = creationDate;
    }
    private String sourcePath = null;
    private Date creationDate = null;
    @Override
    public String toString(){
        return creationDate.toString();
    }
    public String getSourcePath() {
        return sourcePath;
    }
    public Date getCreationDate() {
        return creationDate;
    }
}
