package com.rise42;


import com.google.gson.JsonParser;
import com.rise42.module.Module;
import com.rise42.scheme.CircleScheme;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(
                Paths.get(
                        "/home/rise42/Projects/ComputerSystems/res/scheme1.json", ""
                )
        ).forEach(builder::append);
        System.out.println(builder.toString());

        JsonParser jsonParser = new JsonParser();

        Module module = new Module(
                0,
                jsonParser.parse(builder.toString())
                        .getAsJsonObject().get("module")
                        .getAsJsonObject()
        );

        Module module1 = new Module(
                0,
                jsonParser.parse(builder.toString())
                        .getAsJsonObject().get("module")
                        .getAsJsonObject()
        );

        Module module2 = new Module(
                0,
                jsonParser.parse(builder.toString())
                        .getAsJsonObject().get("module")
                        .getAsJsonObject()
        );

        System.out.println(module);

        int[][] matrix1 = module.buildFirstMatrix();
        System.out.println("Matrix1");

        printPrettyMatrix(matrix1);

        CircleScheme circleScheme = new CircleScheme();
        circleScheme.addModule(module);
        //circleScheme.addModule(module1);
        circleScheme.addModule(module2);

        int[][] matrix11 = circleScheme.buildFirstMatrix();
        System.out.println("Matrix11");

        printPrettyMatrix(matrix11);
    }

    public static void printPrettyMatrix(int[][] matrix){
        System.out.print("#  ");
        for (int i = 0; i < matrix.length; i++) {
            if(Integer.toString(i).length() > 1)
                System.out.print(i+" ");
            else
                System.out.print(i+"  ");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {

            int[] row = matrix[i];
            if(Integer.toString(i).length() > 1)
                System.out.print(i+" ");
            else
                System.out.print(i+"  ");
            for (int j : row) {
                System.out.print(j+"  ");
            }
            System.out.println();
        }
    }
}
