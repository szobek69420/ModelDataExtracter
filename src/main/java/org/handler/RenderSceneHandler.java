package main.java.org.handler;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.org.GeometryExporter;
import main.java.org.controller.ExportMenuController;
import main.java.org.controller.MainController;

import java.io.File;

public class RenderSceneHandler {
    private SubScene scene;
    private Group sceneRoot;
    private AnchorPane sceneParent;
    private Button exportButton;

    private PerspectiveCamera camera;
    private double pitch=-30.0;
    private double yaw=135.0;
    private double distance=10.0;

    private MeshView importedModel;
    private File importedModelPath;

    public RenderSceneHandler(AnchorPane sceneParent)
    {
        this.sceneParent=sceneParent;

        this.sceneRoot=new Group();
        this.scene=new SubScene(sceneRoot, sceneParent.getWidth(), sceneParent.getHeight(), true, SceneAntialiasing.BALANCED);
        this.sceneParent.getChildren().add(this.scene);

        //setup camera
        this.camera=new PerspectiveCamera(true);
        camera.setVerticalFieldOfView(true);
        camera.setFieldOfView(60.0);
        camera.getTransforms().add(new Translate(0,0,-15));

        //import the default model
        importedModel=new MeshView();
        importedModel.getTransforms().add(new Translate(0,0,0));
        importedModel.setOpacity(1.0);
        importedModel.setMaterial(new PhongMaterial(new Color(1.0,1.0,1.0,1)));

        sceneRoot.getChildren().add(importedModel);

        importModel(new File(this.getClass().getResource("/models/default_model/kocsi.obj").getFile()));

        //create the export button
        exportButton=new Button("Export");
        sceneParent.getChildren().add(exportButton);
        AnchorPane.setBottomAnchor(exportButton, 10.0);
        AnchorPane.setRightAnchor(exportButton, 10.0);
        exportButton.setOnMouseClicked(event->this.exportModel());

        //final touches
        this.scene.setCamera(this.camera);
        this.scene.setFill(Color.BLUEVIOLET);
        System.out.println(this.scene.isDepthBuffer());

        RenderSceneDragHandler dragHandler=new RenderSceneDragHandler(this);
        this.scene.setOnMouseDragged(dragHandler);
        this.scene.setOnMousePressed(dragHandler);
        this.scene.setOnMouseReleased(dragHandler);

        this.scene.setOnScroll(new RenderSceneScrollHandler(this));

        this.sceneParent.layoutBoundsProperty().addListener(
                (obs, oldVal, newVal)->{
                    this.scene.setWidth(newVal.getWidth());
                    this.scene.setHeight(newVal.getHeight());
                }
        );

        updateCum();
    }

    public boolean importModel(File file)
    {
        if(!file.isFile())
            return false;

        if(file.getName().endsWith(".obj"))
        {
            importObj(file);
            importedModelPath=file;
            return true;
        }
        if(file.getName().endsWith(".stl"))
        {
            importStl(file);
            importedModelPath=file;
            return true;
        }
        if(file.getName().endsWith(".geometry"))
        {
            importGeometry(file);
            importedModelPath=file;
            return true;
        }

        return false;
    }

    public void exportModel()
    {

        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Export.fxml"));
        Parent exportRoot=null;
        try{ exportRoot = loader.load(); }
        catch(Exception ex){ }
        ExportMenuController exportController=loader.getController();

        Stage stage=new Stage();
        Scene scene=new Scene(exportRoot);
        stage.setScene(scene);

        stage.initOwner(this.scene.getScene().getWindow());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Export "+importedModelPath.getName());
        stage.show();

        exportController.init(stage, (TriangleMesh) importedModel.getMesh(), importedModelPath.getParentFile());
    }

    private void importObj(File file)
    {
        ObjModelImporter importer = new ObjModelImporter();
        importer.read(file);
        importedModel.setMesh(importer.getImport()[0].getMesh());
        importer.close();
    }

    private void importStl(File file)
    {
        StlMeshImporter importer = new StlMeshImporter();
        importer.read(file);
        TriangleMesh importedMesh =importer.getImport();
        importer.close();

        importedModel.setMesh(importedMesh);
    }

    private void importGeometry(File file)
    {
        TriangleMesh importedMesh = GeometryExporter.importModel(file);
        importedModel.setMesh(importedMesh);
    }

    private void updateCum()
    {
        camera.getTransforms().clear();
        camera.getTransforms().add(new Rotate(yaw, new Point3D(0,1,0)));
        camera.getTransforms().add(new Rotate(pitch, new Point3D(1, 0, 0)));
        camera.getTransforms().add(new Translate(0,0,-distance));
    }

    private class RenderSceneScrollHandler implements EventHandler<ScrollEvent>{
        private static final double SENSITIVITY=0.02;

        private RenderSceneHandler handler;

        public RenderSceneScrollHandler(RenderSceneHandler handler)
        {
            this.handler=handler;
        }

        @Override
        public void handle(ScrollEvent event) {
            this.handler.distance-=SENSITIVITY*event.getDeltaY();
            if(this.handler.distance<0.1) this.handler.distance=0.1;

            this.handler.updateCum();
        }
    }

    private class RenderSceneDragHandler implements EventHandler<MouseEvent>{
        private static final double SENSITIVITY_X = 0.1;
        private static final double SENSITIVITY_Y = 0.2;

        private RenderSceneHandler handler;
        private double lastX, lastY;
        private boolean pressed;

        public RenderSceneDragHandler(RenderSceneHandler handler)
        {
            this.handler=handler;

            lastX=0.0;
            lastY=0.0;
            pressed=false;
        }

        @Override
        public void handle(MouseEvent event) {
            EventType<MouseEvent> eventType=(EventType<MouseEvent>)event.getEventType();

            if (!pressed&&eventType==MouseEvent.MOUSE_PRESSED) {
                pressed=true;
                lastX=event.getSceneX();
                lastY=event.getSceneY();
            }
            else if(eventType==MouseEvent.MOUSE_RELEASED)
                pressed=false;
            else if(pressed&&eventType==MouseEvent.MOUSE_DRAGGED)
            {
                double deltaX=event.getSceneX()-lastX;
                double deltaY=event.getSceneY()-lastY;
                lastX=event.getSceneX();
                lastY=event.getSceneY();

                handler.yaw+=SENSITIVITY_X*deltaX;
                if(handler.yaw>180) handler.yaw-=360;
                else if(handler.yaw<-180) handler.yaw+=360;

                handler.pitch-=SENSITIVITY_Y*deltaY;
                if(handler.pitch>89.0) handler.pitch=89.0;
                if(handler.pitch<-89.0) handler.pitch=-89.0;

                handler.updateCum();
            }
        }
    }
}
