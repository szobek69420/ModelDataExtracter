package main.java.org.element;

import javafx.scene.text.Text;

import java.io.File;

public class FileButton extends Text{
    private File file;

    public FileButton(File file, String name)
    {
        this.file=file;
        super.setText(name);
    }
}
