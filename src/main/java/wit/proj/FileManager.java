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

    public static List<File> pathsList = new ArrayList<>();//Lista przechowywująca ścieżki do pliku
    public static int copiedFiles = 0;//Liczba skopiowanych plików

    /**
     * Metoda odpowiadająca za podzielenie zdjęć na równe przedziały które są następnie przetwarzane wielowątkowo
     * @param srcFolder Folder źródłowy
     * @param dstFolder Folder docelowy
     * @throws Exception Wyjątek z metod GetImagesPaths/Save/CreateImageList
     */
    public static void RunMultiThread(File srcFolder, File dstFolder) throws Exception {
        pathsList.clear(); //Czyszczenie listy przy każdym naciśnięciu przycisku
        copiedFiles = 0;
        GetImagesPaths(srcFolder);
        int cores = Runtime.getRuntime().availableProcessors(); //Pobiera liczbę dostępnych rdzeni procesora
        int listSize = pathsList.size();
        int sublistSize = listSize / cores; //Ile obiektów ma być w podlistach dla każdego wątku
        int startIndex = 0;
        List<Callable<Void>> tasks = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        for (int i = 0; i < cores; i++) {
            int endIndex = startIndex + sublistSize;
            if (i == cores - 1) {
                endIndex = listSize;
            }
            List<File> subList = new ArrayList<>(pathsList.subList(startIndex, endIndex)); //Tworzenie podlist dla wątkow

            Callable<Void> task = () -> {
                List<Image> images = CreateImageList(subList); //Wywołanie metody tworzenia listy obiektów klasy image
                Save(dstFolder, images); //Wywołanie metody kopiowania zdjęć
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
        UI.DisplayMessage("Copied Files: " + copiedFiles, JOptionPane.INFORMATION_MESSAGE); //Wypiswywanie okienka z liczbą łącznie przekopiowanych zdjęć
    }

    /**
     * Metoda pobierająca ścieżki do plików z podanego folderu i jego podfolderów poczym dodaje je do listy pathsList
     * @param folder Folder źródłowy
     * @throws Exception ?
     */
    public static void GetImagesPaths(File folder) throws Exception{
        File[] files = folder.listFiles();
        //Jeśli folder jest pusty przerywa działanie
        //To jest źle bo może być pusty folder w sumie
        if(files == null){
            throw new Exception("No dir found");
        }
        for(File file : files){    //Przechodzi przez wszystkie pliki w folderze
            if(file.isDirectory()){     //Jeśli aktualny plik jest folderem to wywołuje ponownie tą funkcję z nową ścieżką
                GetImagesPaths(file);
            } else if(file.isFile() && (file.getName().endsWith(".JPG") || file.getName().endsWith(".JPEG") ||  //Jeśli plik jest zdjęciem JPG to dodaje do listy
                    file.getName().endsWith(".jpg")|| file.getName().endsWith(".jpeg"))){
                pathsList.add(file);
            }
        }
    }

    /**
     * Metoda zwracająca listę obiektów klasy Image
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
        if(imageList == null || imageList.isEmpty()){//Jeśli lista obiektów jest pusta to przerywa działanie
            throw new Exception("No correct files found to copy");
        }
        int HowManyHaveBeenCopied = 0;
        synchronized(FileManager.class) {
            for (Image image : imageList) {//Przechodzi przez każdy obiekt w liście
                String date = image.getCreationDate();
                File subFolder = new File(folder.getAbsolutePath(), date);


                if (!subFolder.exists() || !subFolder.isDirectory()) {//Jeśli folder nie istnieje lub nie jest folderem
                    if (!subFolder.mkdirs()) { //Próbuje utworzyć foldery.Jeśli się nie uda wyrzuca błąd
                        if (!subFolder.exists() || !subFolder.isDirectory()) {//Sprawdza czy inny wątek w międzyczasie nie stowrzył folderu
                            throw new Exception("Failed to create folder " + subFolder.getAbsolutePath());
                        }
                    }
                }
                //Kopiowanie pliku do nowego foldru
                long numberOfFilesInFolder = 0;
                if (subFolder.listFiles() != null) { //Jeśli folder nie jest pusty przypisuje liczbę plików w nim do zmiennej
                    numberOfFilesInFolder = subFolder.listFiles().length;
                }
                Path imagePathWithName = Paths.get(subFolder.getAbsolutePath()).resolve(numberOfFilesInFolder + 1 + ".jpg");//Tworzenie nowej ścieżki dla pliku
                try {
                    Files.copy(Paths.get(image.getSourcePath()), imagePathWithName);//Kopjuje plik
                    HowManyHaveBeenCopied++;
                } catch (IOException e) {
                    throw new IOException("Failed to copy file " + e.getMessage());
                }

            }
        }
        copiedFiles+=HowManyHaveBeenCopied;
    }


}
