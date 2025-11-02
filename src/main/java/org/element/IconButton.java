package main.java.org.element;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class IconButton extends Pane {
    private ImageView imageView;

    public IconButton()
    {
        super.setWidth(20.0);
        super.setHeight(20.0);

        imageView=new ImageView();
        imageView.setPreserveRatio(false);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);
    }

    public void setImage(Image image)
    {
        imageView.setImage(image);
    }

    public void setOnClick(EventHandler<MouseEvent> onClick)
    {
        imageView.setOnMouseClicked(onClick);
    }
}
