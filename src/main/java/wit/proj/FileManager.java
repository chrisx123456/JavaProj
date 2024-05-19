package wit.proj;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa zajmująca się obsługą plików posiadająca 2 główne metody: getImagePaths oraz Save do pobierania ścieżek i przekopiowywania
 * @author Aleksander Traczyk
 * @since 14.05.2024
 * @version 1.0
 */
public class FileManager  {
    /**Lista obiektów klasy Image przechowywująca ścieżki do pliku oraz datę utworzenia */
     public static List<Image> imageList = new ArrayList<>();
    /**Zmienne String przechowywujące ścieżki do folderu początkowego oraz wynikowego*/
     public static String sourcePath,destinationPath;


    /**
     *Konstruktor 2 argumentowy przyjmujący ścieżki do folderu początkowego oraz wynikowego
     * @param sourcePath  ścieżka do folderu początkowego
     * @param destinationPath   ścieżka do folderu końcowego
     */
    public  FileManager(String sourcePath, String destinationPath){
        this.sourcePath = sourcePath;
        this.destinationPath = destinationPath;
    }

    //Sposób wywołania metody: getImagePaths(new File(sourcePath),imageList);
    /**
     * Metoda przyjmująca folder z ścieżką sourcePath
     * Przechodzi ona zdjęcie po zdjęciu i folder po folderze pobierając ściezki znajdujących się tam zdjęć,
     *        po czym dodaje tworzy obiekt klast Image z podaną ścieżką i dodaje go do listy.
     * @param folder Przyjmuje Plik o zadanej ścieżce
     * @throws Exception Nie udało się dodać obiektu Image do listy
     */
    public static void getImagePaths(File folder) throws Exception
    {
        /**Pobranie listy plików w folderze docelowym*/
        File[] files = folder.listFiles();
        /**Jeśli folder jest pusty przerywa działanie*/
        if(files == null){
            System.out.println("No files found");
            return;
        }
        /**Przechodzi przez wszystkie pliki w folderze po koleji*/
        for (File file : files) {
            /**Jeśli dany plik jest folderem to wywołuję tą metodę ponownie z nową ścieżką*/
            if(file.isDirectory()){
                getImagePaths(file);
            }
            /**Jeśli dany plik jest folderem i jest typu jpg*/
            else if(file.isFile() && file.getName().endsWith(".jpg")){
                try{
                    Image image = new Image(file.getAbsolutePath());
                    String date = image.getCreationDate();
                    /**Jeśli dany plik nie ma daty EXIF to nie dodaje go do listy*/
                    if(date == null)
                    {
                        System.out.println("Missing date: "+ file.getAbsolutePath());
                    }
                    else{imageList.add(new Image(file.getAbsolutePath()));}


                } catch(IOException e){
                    throw new Exception("Failed to add image to the list of images " );
                }

            }
        }
    }
    //Sposób wywołania Save(new File(destinationPath),imageList)
    /**
     * Pobiera obiekt klasy image z listy i przechodzi po każdym pokoleji. Pobiera datę utworzenia tego pliku i
     *      sprawdza czy istnieje folder o takiej dacie jeśli nie to go tworzy.Następnie przekopjowywuje pliki z
     *      miejsa początkowego do docleowego z zmienioną nazwą
     * @param folder Przyjmuje Plik o zadanej ścieżce
     * @throws Exception Nie udało się utworzyć folderu
     */
    public static void Save (File folder) throws Exception{
       // ChceckIfDirectoryExists();
        /**Jeśli lista obiektów jest pusta to przerywa działanie*/
        if(imageList == null){
            System.out.println("No correct files found to copy ");
            return;
        }
        int HowManyHaveBeenCopied = 0;
        /**Przechodzi przez każdy obiekt w liście*/
        for(Image image : imageList){
            String date = image.getCreationDate();
            File subFolder = new File(folder.getAbsolutePath(), date);

            /**Jeśli folder nie istnieje i nie jest folderem*/
            if (!subFolder.exists() && !subFolder.isDirectory()) {
                     /**Próbuje utworzyć foldery.Jeśli się nie uda wyrzuca błąd*/
                    if(!subFolder.mkdirs()){
                        throw new Exception("Failed to create folder " + subFolder.getAbsolutePath());
                    }
            }

            //Kopiowanie pliku do nowego foldru
            long numberOfFilesInFolder = 0;
            /**Jeśli folder nie jest pusty przypisuje liczbę plików w nim do zmiennej*/
            if(subFolder.listFiles()!= null){
                numberOfFilesInFolder = subFolder.listFiles().length;
            }
            /**Tworzenie nowej ścieżki dla pliku*/
            Path imagePathWithName = Paths.get(subFolder.getAbsolutePath()).resolve(numberOfFilesInFolder + 1 +".jpg");
            try {
                /**Kopjuje plik*/
                Files.copy(Paths.get(image.getSourcePath()), imagePathWithName);
                HowManyHaveBeenCopied++;
            } catch (IOException e) {
                throw new RuntimeException("Failed to Copy file", e);
            }

        }
        System.out.println(HowManyHaveBeenCopied);   //To trzeba wyświetlać na koniec

    }
//    private static void ChceckIfDirectoryExists()throws Exception{
//        File folder = new File(sourcePath);
//        if (!folder.exists() && !folder.isDirectory()) {  //Jeśli folder nie istnieje i nie jest folderem
//            if(!folder.mkdirs()){
//                throw new Exception("Failed to create folder " + folder.getAbsolutePath());
//            }
//        }
//    }}


    /**
     * Zwraca sourcePath
     * @return sourcePath
     */
    public static String getSourcePath() {
        return sourcePath;
    }




}
