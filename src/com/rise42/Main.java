package com.rise42;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rise42.module.Module;
import com.rise42.scheme.CircleScheme;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(
                Paths.get(
                        "/home/rise42/Projects/ComputerSystems/res/scheme1.json", ""
                )
        ).forEach(builder::append);
        //System.out.println(builder.toString());

        JsonObject module = new JsonParser().parse(builder.toString())
                .getAsJsonObject().get("module")
                .getAsJsonObject();

        CircleScheme circleScheme = new CircleScheme(7, module);

        int[][] matrix = circleScheme.buildSecondMatrix();

        printPrettyMatrix(matrix);

        System.out.println("Max is: "+findMaxInMatrix(matrix));
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
                if(Integer.toString(j).length() > 1)
                    System.out.print(j+" ");
                else
                    System.out.print(j+"  ");
            }
            System.out.println();
        }
    }

    public static int findMaxInMatrix(int[][] m){
        int max = -1;

        for (int[] row: m)
            for(int i: row){
                if(i == 0) break;

                if(i > max) max = i;
            }

        return max;
    }
}
