package main.java.org.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.org.element.IconButton;
import main.java.org.handler.FileBrowserHandler;
import main.java.org.handler.RenderSceneHandler;

public class MainController {
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
        fileBrowserHandler=new FileBrowserHandler(this, fileBrowser, fileBrowserParent, currentDirPath);
        renderSceneHandler=new RenderSceneHandler(renderSceneParent);
    }

    public RenderSceneHandler getRenderSceneHandler()
    {
        return renderSceneHandler;
    }
}
