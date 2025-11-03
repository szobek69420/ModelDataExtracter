package main.java.org;

import com.sun.javafx.collections.ObservableFloatArrayImpl;
import com.sun.javafx.scene.shape.ObservableFaceArrayImpl;
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

    public static TriangleMesh importModel(File file)
    {
        try(BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file))))
        {
            int vertexCount;
            float[] vertices;
            float[] uvs;
            int[] indices;

            String temp;
            String[] temps;

            temp=br.readLine();
            vertexCount=Integer.parseInt(temp.substring(14));

            vertices=new float[3*vertexCount];
            uvs=new float[2*vertexCount];
            indices=new int[vertexCount];

            for(int i=0;i<vertexCount;i++)
            {
                temp=br.readLine();
                temps=temp.split(" ");

                vertices[3*i]=Float.parseFloat(temps[0]);
                vertices[3*i+1]=Float.parseFloat(temps[1]);
                vertices[3*i+2]=Float.parseFloat(temps[2]);

                uvs[2*i]=Float.parseFloat(temps[3]);
                uvs[2*i+1]=Float.parseFloat(temps[4]);

                indices[i]=i;
            }

            TriangleMesh mesh = new TriangleMesh(VertexFormat.POINT_TEXCOORD);
            mesh.getPoints().clear();
            mesh.getPoints().addAll(vertices,0, vertices.length);
            mesh.getTexCoords().clear();
            mesh.getTexCoords().addAll(uvs,0, uvs.length);
            mesh.getFaces().clear();
            mesh.getFaces().addAll(indices, 0, indices.length);

            return mesh;
        }
        catch(Exception ex)
        {
            return null;
        }
    }
}
