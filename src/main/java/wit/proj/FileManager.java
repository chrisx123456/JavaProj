package wit.proj;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileManager  {
     public static List<Image> imageList = new ArrayList<>();   //Lista klasy Image przechowywująca ścieżki do pliku oraz datę utworzenia
     public static String sourcePath;
    public String destinationPath; //Zmienne String przechowywujące ścieżki do folderu początkowego oraz wynikowego

    //Konstruktor 2 argumentowy przyjmujący ścieżki do folderu początkowego oraz wynikowego
    public  FileManager(String sourcePath, String destinationPath){
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    //Sposób wywołania metody: getImagePaths(new File(sourcePath),imageList);
    /**
     * Metoda przyjmująca folder oraz listę klasy Image.
     * Przechodzi ona zdjęcie po zdjęciu i folder po folderze pobierając ściezki znajdujących się tam zdjęć,
     *        po czym dodaje tworzy obiekt klast Image z podaną ścieżką i dodaje go do listy.
     * @param folder
     * @throws Exception
     */
    public static void getImagePaths(File folder) throws Exception
    {

        File[] files = folder.listFiles();
        if(files == null){
            return;
        }
        for (File file : files) {
            if(file.isDirectory()){
                getImagePaths(file);
            }
            else if(file.isFile() && file.getName().endsWith(".jpg")){
                try{
                    Image image = new Image(file.getAbsolutePath());
                    String date = image.getCreationDate();
                    if(date == null)
                    {
                        System.out.println("Brak daty: "+ file.getAbsolutePath());
                    }
                    else{imageList.add(new Image(file.getAbsolutePath()));}


                } catch(IOException e){
                    throw new Exception("Failed to add image to the list of images " );
                }

            }
        }
    }
    //Sposób wywołania Save(new File(destinationPath),imageList)
    public static void Save (File folder, List <Image> imageList) throws Exception{
        ChceckIfDirectoryExists();
        if(imageList == null){
            return;
        }

        for(Image image : imageList){
            String date = image.getCreationDate();
            if(date == null){
                System.out.println("Brak daty: ");
            }
            else {
                File subFolder = new File(folder.getAbsolutePath(), date);
                System.out.println(subFolder.getAbsolutePath());

                if (!subFolder.exists() && !subFolder.isDirectory()) {  //Jeśli folder nie istnieje i nie jest folderem
                        if(!subFolder.mkdirs()){
                            throw new Exception("Failed to create folder " + subFolder.getAbsolutePath());
                        }
                }

                //Kopiowanie pliku do nowego foldru
                long numberOfFilesInFolder = 0;
                if(subFolder.listFiles()!= null){
                    numberOfFilesInFolder = subFolder.listFiles().length;
                }



                Path imagePathWithName = Paths.get(subFolder.getAbsolutePath()).resolve(numberOfFilesInFolder + 1 +".jpg");
                try {
                    Files.copy(Paths.get(image.getSourcePath()), imagePathWithName);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to Copy file", e);
                }
            }

        }

    }
    private static void ChceckIfDirectoryExists()throws Exception{
        File folder = new File(sourcePath);
        if (!folder.exists() && !folder.isDirectory()) {  //Jeśli folder nie istnieje i nie jest folderem
            if(!folder.mkdirs()){
                throw new Exception("Failed to create folder " + folder.getAbsolutePath());
            }
        }
    }
    public static List<Image> getImageList() {
        return imageList;
    }




}
