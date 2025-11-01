package main.java.org.controller;

import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import main.java.org.handler.FileBrowserHandler;
import main.java.org.handler.RenderSceneHandler;

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

    @FXML
    private AnchorPane renderSceneParent;
    private RenderSceneHandler renderSceneHandler;

    public void init()
    {
        fileBrowserHandler=new FileBrowserHandler(fileBrowser, fileBrowserParent, currentDirPath);
        renderSceneHandler=new RenderSceneHandler(renderSceneParent);
    }
}
