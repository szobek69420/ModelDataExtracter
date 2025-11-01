package main.java.org.element;

import javafx.event.EventType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import main.java.org.handler.FileBrowserHandler;

import java.io.File;

public class FileButton extends Pane {
    private static final long DOUBLE_CLICK_THRESHOLD=700;

    private static Image folderIcon=null;
    private static Image fileIcon=null;
    private static boolean iconsImported=false;

    private FileBrowserHandler fb;
    private File file;
    private ImageView image;
    private Text text;

    private long lastClicked=-1;

    public FileButton(FileBrowserHandler fb, File file, String name)
    {
        if(iconsImported==false)
        {
            folderIcon=new Image(getClass().getResource("/sprites/folder_icon.png").toString());
            fileIcon=new Image(getClass().getResource("/sprites/file_icon.png").toString());
        }

        this.fb=fb;
        this.file=file;

        super.setHeight(15.0);
        super.setWidth(200.0);
        if(file.isDirectory())
        {
            super.setOnMouseClicked((event)->{
                if(event.getEventType()!=MouseEvent.MOUSE_CLICKED)
                    return;

                if(System.currentTimeMillis()-DOUBLE_CLICK_THRESHOLD<lastClicked)
                    fb.update(file);
                else
                    lastClicked=System.currentTimeMillis();
            });
        }
        else
        {
            super.setOnMouseClicked((event)->{
                if(event.getEventType()!=MouseEvent.MOUSE_CLICKED)
                    return;

                if(System.currentTimeMillis()-DOUBLE_CLICK_THRESHOLD<lastClicked)
                    System.out.println(file.getName()+" imported");
                else
                    lastClicked=System.currentTimeMillis();
            });
        }

        text=new Text(31,11,name+"                   ");
        text.setScaleX(1.1);
        text.setScaleY(1.1);
        super.getChildren().add(text);

        image=new ImageView(file.isDirectory()?folderIcon:fileIcon);
        image.setFitWidth(13);
        image.setFitHeight(13);
        image.setTranslateX(6.0);
        image.setTranslateY(1.0);
        super.getChildren().add(image);
    }

}
