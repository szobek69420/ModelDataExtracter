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
    private ImageView exportButton;

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

        initButtons();
    }

    private void initButtons()
    {
        exportButton.setImage(new Image(this.getClass().getResource("/sprites/export_icon.png").toString()));
        exportButton.setOnMouseClicked(new ExportButtonMouseHandler(renderSceneHandler));
    }

    public RenderSceneHandler getRenderSceneHandler()
    {
        return renderSceneHandler;
    }

    private class ExportButtonMouseHandler implements EventHandler<MouseEvent>{
        private RenderSceneHandler renderSceneHandler;
        private long lastClick=-1;

        public ExportButtonMouseHandler(RenderSceneHandler renderSceneHandler)
        {
            this.renderSceneHandler=renderSceneHandler;
        }

        @Override
        public void handle(MouseEvent event) {
            boolean doubleClicked=false;
            if(event.getEventType()==MouseEvent.MOUSE_CLICKED)
            {
                long time=System.currentTimeMillis();

                if(time-lastClick<700)
                    renderSceneHandler.exportModel();

                lastClick=time;
            }
        }
    }
}
