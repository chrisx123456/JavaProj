package wit.proj;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String sourcePath = "C:\\Users\\alsmo\\Desktop\\obrazy do testowania projektu z javy";
        String destinationPath = "C:\\Users\\alsmo\\Desktop\\obrazy do wklejenia";
        FileManager filemanager = new FileManager(sourcePath,destinationPath);
        try {
            FileManager.getImagePaths(new File(FileManager.getSourcePath()));
            //System.out.println(filemanager.getImageList());
            System.out.println("Pobrano ścieżki");
            FileManager.Save(new File(destinationPath));
            System.out.println("Przekopiowano");

        } catch (Exception e) {
            System.out.println("Nie udało się");
            throw new RuntimeException(e);

        }

    }
}