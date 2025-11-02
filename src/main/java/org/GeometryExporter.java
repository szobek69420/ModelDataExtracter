package main.java.org;

import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

public class GeometryExporter {
    public static boolean exportModel(TriangleMesh mesh, File directory, String name)
    {
        if(!directory.isDirectory())
        {
            System.err.println("Invalid directory "+directory.getAbsolutePath());
            return false;
        }

        File file=new File(directory, name+".geometry");

        //export data
        try(OutputStreamWriter writer=new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.US_ASCII))
        {
            ObservableFloatArray vertices=mesh.getPoints();
            ObservableFloatArray uvs=mesh.getTexCoords();
            ObservableIntegerArray faces=mesh.getFaces();

            if(mesh.getVertexFormat()==VertexFormat.POINT_NORMAL_TEXCOORD)
            {
                writer.write("vertex count: "+faces.size()/3+""+(char)10);

                for(int i=0;i<faces.size();i+=3)
                {
                    int vertexIndex=faces.get(i)*3;
                    int uvIndex=faces.get(i+2)*2;

                    writer.write(
                            vertices.get(vertexIndex)+" " +
                                vertices.get(vertexIndex+1)+" "+
                                vertices.get(vertexIndex+2)+" "+
                                uvs.get(uvIndex)+" "+
                                uvs.get(uvIndex+1)+""+
                                (char)10
                            );
                }
            }
            else
            {
                writer.write("vertex count: "+faces.size()/2+""+(char)10);

                for(int i=0;i<faces.size();i+=2)
                {
                    int vertexIndex=faces.get(i)*3;
                    int uvIndex=faces.get(i+1)*2;

                    writer.write(
                            vertices.get(vertexIndex)+" " +
                                    vertices.get(vertexIndex+1)+" "+
                                    vertices.get(vertexIndex+2)+" "+
                                    uvs.get(uvIndex)+" "+
                                    uvs.get(uvIndex+1)+""+
                                    (char)10
                    );
                }
            }
        }
        catch (Exception ex){return false;}


        return true;
    }
}
