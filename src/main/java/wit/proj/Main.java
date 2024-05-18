package wit.proj;

import java.io.File;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!!! TO ja Trakol112" );


        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i+234);
            System.out.println("i = " + i+11);
            System.out.println("i = " + i+1112);

        }
        String sourcePath = "C:\\Users\\alsmo\\Desktop\\obrazy do testowania projektu z javy";
        String destinationPath = "C:\\Users\\alsmo\\Desktop\\obrazy do wklejenia";
        FileManager filemanager = new FileManager(sourcePath,destinationPath);
        try {
            FileManager.getImagePaths(new File(sourcePath));
            System.out.println(filemanager.getImageList());
            System.out.println("Pobrano ścieżki");
            FileManager.Save(new File(destinationPath),filemanager.getImageList());
            System.out.println("Przekopiowano");

        } catch (Exception e) {
            System.out.println("Nie udało się");
            throw new RuntimeException(e);

        }

    }
}