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
     String sourcePath, destinationPath; //Zmienne String przechowywujące ścieżki do folderu początkowego oraz wynikowego

    //Konstruktor 2 argumentowy przyjmujący ścieżki do folderu początkowego oraz wynikowego
    public  FileManager(String sourcePath, String destinationPath){
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    //Sposób wywołania metody: getImagePaths(new File(sourcePath),imageList);
    /**
     * Metoda przyjmująca folder oraz listę klasy Image.
     * Przechodzi ona zdjęcie po zdjęciu i folder po folderze pobierając ściezki znajdujących się tam zdjęć,
     *      po czym dodaje tworzy obiekt klast Image z podaną ścieżką i dodaje go do listy.
     * @param folder
     * @param imageList
     */
    public static void getImagePaths(File folder, List <Image> imageList) throws Exception
    {
        File[] files = folder.listFiles();
        if(files == null){
            return;
        }
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        for (File file : files) {
            if(file.isDirectory()){
                getImagePaths(file, imageList);
            }
            else if(file.isFile() && file.getName().endsWith(".png")){
                try{
                    imageList.add(new Image(file.getAbsolutePath()));
                    /*
                    Albo w ten sposób odrazu jeśli szympek doda konstruktor z zmienną data odrazu
                     BasicFileAttributes atributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                     String creationTime = dateFormat.format(new Date(atributes.creationTime().toMillis()));
                     imageList.add(new Image(file.getAbsolutePath(), creationTime));
                     */

                } catch(IOException e){
                    throw new Exception("Failed to add image to the list of images " );
                }

            }
        }
    }
    //Sposób wywołania Save(new File(destinationPath),imageList)
    public static void Save (File folder, List <Image> imageList) throws Exception{
        File[] files = folder.listFiles();
        if(imageList == null){
            return;
        }

        for(Image image : imageList){
            String date = image.getCreationDate();
            if(date == null){
                return;
            }
            File subFolder = new File(folder.getAbsolutePath(),date);

            if(!subFolder.exists() && subFolder.isDirectory()){  //Jeśli nie istnieje i jest folderem

                if(!subFolder.mkdir()){ //Probuje utworzyć folder. Jeśli nie uda się wyrzuca błąd

                    throw new Exception("Failed to create folder " + subFolder.getAbsolutePath());
                }
            }

            //Kopiowanie pliku do nowego foldru
           // File[] filesInSubFolder = subFolder.listFiles();
            long numberOfFilesInFolder = subFolder.listFiles().length;
            Path imagePathWithName = Paths.get(subFolder.getAbsolutePath(),Long.toString(numberOfFilesInFolder+1));
            try{
                Files.copy(Paths.get(image.getSourcePath()),imagePathWithName);

            }catch(IOException e){
                throw new Exception("Failed to Copy file");

            }


        }

    }




}
