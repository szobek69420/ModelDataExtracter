package main.java.org.handler;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import main.java.org.element.FileButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBrowserHandler {
    private AnchorPane containerParent;
    private Pane container;

    public FileBrowserHandler(Pane container, AnchorPane containerParent)
    {
        this.container=container;
        this.containerParent=containerParent;

        update(new File("."));

        this.containerParent.widthProperty().addListener((obs, oldVal, newVal)->{
            this.container.setMinWidth((double)newVal-2.0);
            this.container.maxWidth((double)newVal-2.0);
            this.container.prefWidth((double)newVal-2.0);
            System.out.println(this.container.getWidth());
        });
    }

    public void update(File currentDir)
    {
        ArrayList<File> files=new ArrayList<>();
        files.add(currentDir.getParentFile());
        for(File file : currentDir.listFiles())
            files.add(file);

        container.getChildren().clear();

        double width = containerParent.getWidth();
        double height=files.size()*20.0f;

        container.minHeight(container.maxHeight(container.prefHeight(height)));

        ArrayList<FileButton> buttons = new ArrayList<>();
        for(int i=0;i<files.size();i++)
        {
            FileButton fb= new FileButton(files.get(i), i==0?"..": files.get(i).getName());
            fb.setX(0);
            fb.setY(i*20.0f);
            container.getChildren().add(fb);
        }
    }
}
