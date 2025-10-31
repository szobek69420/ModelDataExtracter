package main.java.org.element;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class IconButton extends Pane {
    private ImageView imageView;

    public IconButton()
    {
        imageView=new ImageView();
        imageView.setPreserveRatio(false);

        super.setWidth(20.0);
        super.setHeight(20.0);
    }
}
