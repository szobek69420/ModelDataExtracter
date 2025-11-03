package main.java.org.handler;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import main.java.org.controller.MainController;
import main.java.org.element.FileButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class FileBrowserHandler {
    private AnchorPane containerParent;
    private Pane container;
    private Text currentDirText;

    public final MainController controller;

    public FileBrowserHandler(MainController controller, Pane container, AnchorPane containerParent, Text currentDirText)
    {
        this.controller=controller;
        this.container=container;
        this.containerParent=containerParent;
        this.currentDirText=currentDirText;

        String tempPath=new File(".").getAbsolutePath();
        update(new File(tempPath.substring(0, tempPath.length()-2)));

        this.containerParent.widthProperty().addListener((obs, oldVal, newVal)->{
            this.container.setMinWidth((double)newVal-15);
            this.container.setMaxWidth((double)newVal-15);
            this.container.setPrefWidth((double)newVal-15);
        });
    }

    public void update(File currentDir)
    {
        ArrayList<File> files=new ArrayList<>();
        files.add(currentDir.getParentFile()==null? currentDir:currentDir.getParentFile());  //add parent
        for(File file : Arrays.stream(currentDir.listFiles()).filter((file)->file.isDirectory()).toList()) //add directories
            files.add(file);
        for(File file : Arrays.stream(currentDir.listFiles()).filter(this::is3DModel).toList()) //add 3d files
            files.add(file);

        container.getChildren().clear();

        double width = containerParent.getWidth();
        double height=files.size()*20.0f;

        container.setMinHeight(height);
        container.setMaxHeight(height);
        container.setPrefHeight(height);

        ArrayList<FileButton> buttons = new ArrayList<>();
        for(int i=0;i<files.size();i++)
        {
            FileButton fb= new FileButton(this, files.get(i), i==0?"..": files.get(i).getName());
            fb.setTranslateX(0);
            fb.setTranslateY(i*20.0);
            container.getChildren().add(fb);
        }

        currentDirText.setText(currentDir.getPath());
    }

    private boolean is3DModel(File file)
    {
        if(!file.isFile())
            return false;

        if(file.getName().endsWith(".obj"))
            return true;
        if(file.getName().endsWith(".stl"))
            return true;
        if(file.getName().endsWith(".geometry"))
            return true;

        return false;
    }
}
