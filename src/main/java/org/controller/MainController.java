package main.java.org.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.java.org.handler.FileBrowserHandler;

public class MainController {
    @FXML
    private FlowPane buttonContainer;

    @FXML
    private Pane fileBrowser;
    @FXML
    private AnchorPane fileBrowserParent;
    @FXML
    private Text currentDirPath;
    private FileBrowserHandler fileBrowserHandler;

    public void init()
    {
        Text text = new Text("nigga");
        buttonContainer.getChildren().add(text);
        fileBrowserHandler=new FileBrowserHandler(fileBrowser, fileBrowserParent, currentDirPath);
    }
}
