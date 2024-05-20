package wit.proj;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Klasa zajmująca się obsługą plików posiadająca 2 główne metody: getImagePaths oraz Save do pobierania ścieżek i przekopiowywania
 * @author Aleksander Traczyk, Maciej Lacek
 * @since 14.05.2024
 * @version 1.1
 */
public class FileManager {
    /**Lista obiektów klasy Image przechowywująca ścieżki do pliku oraz datę utworzenia */
    public static List<File> pathsList = new ArrayList<>();
    /**Liczba skopiowanych plików*/
    public static int copiedFiles = 0;

    /**
     * Metoda odpowiadająca za podzielenie zdjęć na równe przedziały które są następnie przetwarzane wielowątkowo
     * @param srcFolder Folder źródłowy
     * @param dstFolder Folder docelowy
     * @throws Exception Wyjątek z metod GetImagesPaths/Save/CreateImageList
     */
    public static void RunMultiThread(File srcFolder, File dstFolder) throws Exception {
        pathsList.clear();
        copiedFiles = 0;
        GetImagesPaths(srcFolder);
        int cores = Runtime.getRuntime().availableProcessors();
        int listSize = pathsList.size();
        int sublistSize = listSize / cores;
        int startIndex = 0;
        List<Callable<Void>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        for (int i = 0; i < cores; i++) {
            int endIndex = startIndex + sublistSize;
            if (i == cores - 1) {
                endIndex = listSize;
            }
            List<File> subList = new ArrayList<>(pathsList.subList(startIndex, endIndex));

            Callable<Void> task = () -> {
                List<Image> images = CreateImageList(subList);
                Save(dstFolder, images);
                return null;
            };
            tasks.add(task);

            startIndex = endIndex;
        }
        List<Future<Void>> futures = executorService.invokeAll(tasks);
        //Czekanie na zakończenie wszystkich zadań
        for (Future<Void> future : futures) {
            future.get();
        }
        executorService.shutdown();
        UI.DisplayMessage("Copied Files: " + copiedFiles, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @param folder Folder źródłowy
     * @throws Exception ?
     */
    public static void GetImagesPaths(File folder) throws Exception{
        File[] files = folder.listFiles();
        /**Jeśli folder jest pusty przerywa działanie*/
        //To jest źle bo może być pusty folder w sumie
        if(files == null){
            throw new Exception("No dir found");
        }
        for(File file : files){
            if(file.isDirectory()){
                GetImagesPaths(file);
            } else if(file.isFile() && (file.getName().endsWith(".JPG") || file.getName().endsWith(".JPEG") ||
                    file.getName().endsWith(".jpg")|| file.getName().endsWith(".jpeg"))){
                pathsList.add(file);
            }
        }
    }

    /**
     *
     * @param paths Ścieżki zdjęć
     * @return List<Image> - lista zdjęć
     * @throws Exception Wyjątek wyrzucany gdy paths jest puste/null
     */
    public static List<Image> CreateImageList(List<File> paths) throws Exception{
        List<Image> images = new ArrayList<>();
        if(paths==null || paths.isEmpty()) throw new Exception("No images at given dir");
        for(File file : paths){
            if(file.isFile() && (file.getName().endsWith(".JPG") || file.getName().endsWith(".JPEG") ||
                    file.getName().endsWith(".jpg")|| file.getName().endsWith(".jpeg") )){
                Image image = new Image(file.getAbsolutePath());
                String date = image.getCreationDate();
                if(date != null && !date.isEmpty())
                    images.add(image);
            }
        }
        return images;
    }
    //Sposób wywołania Save(new File(destinationPath),imageList)
    /**
     * Pobiera obiekt klasy image z listy i przechodzi po każdym pokoleji. Pobiera datę utworzenia tego pliku i
     *      sprawdza czy istnieje folder o takiej dacie jeśli nie to go tworzy.Następnie przekopjowywuje pliki z
     *      miejsa początkowego do docleowego z zmienioną nazwą
     * @param folder Przyjmuje Plik o zadanej ścieżce
     * @param imageList Lista zdjęć do zapisania
     * @throws Exception Nie udało się utworzyć folderu
     */
    public static void Save (File folder, List<Image> imageList) throws Exception{
       // ChceckIfDirectoryExists();
        /**Jeśli lista obiektów jest pusta to przerywa działanie*/
        if(imageList == null || imageList.isEmpty()){
            System.out.println("No correct files found to copy ");
            return;
        }
        int HowManyHaveBeenCopied = 0;
        /**Przechodzi przez każdy obiekt w liście*/
        synchronized(FileManager.class) {
            for (Image image : imageList) {
                String date = image.getCreationDate();
                File subFolder = new File(folder.getAbsolutePath(), date);

                /**Jeśli folder nie istnieje i nie jest folderem*/
                if (!subFolder.exists() || !subFolder.isDirectory()) {
                    /**Próbuje utworzyć foldery.Jeśli się nie uda wyrzuca błąd*/
                    if (!subFolder.mkdirs()) {
                        //Sprawdza czy inny wątek w międzyczasie nie stowrzył folderu
                        if (!subFolder.exists() || !subFolder.isDirectory()) {
                            throw new Exception("Failed to create folder " + subFolder.getAbsolutePath());
                        }
                    }
                }
                //Kopiowanie pliku do nowego foldru
                long numberOfFilesInFolder = 0;
                /**Jeśli folder nie jest pusty przypisuje liczbę plików w nim do zmiennej*/
                if (subFolder.listFiles() != null) {
                    numberOfFilesInFolder = subFolder.listFiles().length;
                }
                /**Tworzenie nowej ścieżki dla pliku*/
                Path imagePathWithName = Paths.get(subFolder.getAbsolutePath()).resolve(numberOfFilesInFolder + 1 + ".jpg");
                try {
                    /**Kopjuje plik*/
                    Files.copy(Paths.get(image.getSourcePath()), imagePathWithName);
                    HowManyHaveBeenCopied++;
                } catch (IOException e) {
                    throw new IOException("Failed to copy file " + e.getMessage());
                }

            }
        }
        copiedFiles+=HowManyHaveBeenCopied;
    }


}
