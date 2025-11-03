package main.java.org.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.org.GeometryExporter;
import main.java.org.handler.FileBrowserHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ExportMenuController {
    private Stage stage;
    private TriangleMesh model;
    private File currentDir;
    @FXML
    private Label currentDirLabel;

    @FXML
    private AnchorPane fileBrowserParent;
    @FXML
    private Pane fileBrowser;

    @FXML
    private Button exportButton;
    @FXML
    private TextField exportNameTextField;

    public void init(Stage stage, TriangleMesh model, File dir)
    {
        if(!dir.isDirectory())
            return;
        this.currentDir=dir;
        this.model=model;
        this.stage=stage;

        stage.setMinWidth(10.0);
        stage.setMaxWidth(400.0);
        stage.setMinHeight(10.0);
        stage.setMaxHeight(400.0);

        fileBrowser.setMinWidth(200.0);
        fileBrowser.setMaxWidth(200.0);
        fileBrowser.setPrefWidth(200.0);

        exportButton.setOnMouseClicked(event->export());

        updateCurrentDir(this.currentDir);
    }

    private void updateCurrentDir(File dir)
    {
        if(!dir.isDirectory())
            return;
        this.currentDir=dir;

        ArrayList<File> files=new ArrayList<>();
        files.add(dir.getParentFile()==null?dir:dir.getParentFile());
        for(File file : Arrays.stream(dir.listFiles()).filter(File::isDirectory).toList())
            files.add(file);

        fileBrowser.setMinHeight(files.size()*20);
        fileBrowser.setMaxHeight(files.size()*20);
        fileBrowser.setPrefHeight(files.size()*20);
        fileBrowser.getChildren().clear();
        for(int i=0;i<files.size();i++)
        {
            ExportFileButton button = new ExportFileButton(this, files.get(i), i==0?"..":files.get(i).getName());
            button.setTranslateX(0);
            button.setTranslateY(i*20.0);
            fileBrowser.getChildren().add(button);
        }

        currentDirLabel.setText(this.currentDir.getAbsolutePath());
    }

    private void export()
    {
        GeometryExporter.exportModel(this.model, this.currentDir, exportNameTextField.getText().endsWith(".geometry")? exportNameTextField.getText().substring(0,exportNameTextField.getText().length()-9):exportNameTextField.getText());
        stage.close();
    }

    private class ExportFileButton extends Pane {
        private static final long DOUBLE_CLICK_THRESHOLD=700;

        private static Image folderIcon=null;
        private static Image fileIcon=null;
        private static boolean iconsImported=false;

        private ExportMenuController emc;
        private File file;
        private ImageView image;
        private Text text;

        private long lastClicked=-1;

        public ExportFileButton(ExportMenuController emc, File file, String name)
        {
            if(iconsImported==false)
            {
                folderIcon=new Image(getClass().getResource("/sprites/folder_icon.png").toString());
                fileIcon=new Image(getClass().getResource("/sprites/file_icon.png").toString());
            }

            this.emc=emc;
            this.file=file;

            super.setHeight(15.0);
            super.setWidth(200.0);
            super.setOnMouseClicked((event)->{
                if(event.getEventType()!= MouseEvent.MOUSE_CLICKED)
                    return;
                if(System.currentTimeMillis()-DOUBLE_CLICK_THRESHOLD<lastClicked)
                    emc.updateCurrentDir(file);
                lastClicked=System.currentTimeMillis();
            });

            text=new Text(31,11,name+"                   ");
            text.setScaleX(1.1);
            text.setScaleY(1.1);
            super.getChildren().add(text);

            image=new ImageView(folderIcon);
            image.setFitWidth(13);
            image.setFitHeight(13);
            image.setTranslateX(6.0);
            image.setTranslateY(1.0);
            super.getChildren().add(image);
        }

    }
}
