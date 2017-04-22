package com.rise42;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rise42.module.Module;
import com.rise42.scheme.CircleScheme;
import com.rise42.scheme.Scheme;
import com.rise42.scheme.StarScheme;
import com.rise42.scheme.TreeScheme;

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
                        "/home/rise42/Projects/ComputerSystems/res/scheme3.json", ""
                )
        ).forEach(builder::append);
        //System.out.println(builder.toString());

        JsonObject file = new JsonParser().parse(builder.toString()).getAsJsonObject();
        JsonObject module = file.get("module").getAsJsonObject();
        Scheme scheme;

        switch (file.get("topology").getAsString()){

            case "circle":
                scheme = new CircleScheme(13, module);
                break;

            case "star":
                scheme = new StarScheme(12, module);
                break;

            case "tree":
                scheme = new TreeScheme(15, module);
                break;

            default:scheme = null;
        }

        int[][] matrix = scheme.buildSecondMatrix();

        printPrettyMatrix(matrix);

        System.out.println("Max is: "+findMaxInMatrix(matrix));
    }

    public static void printPrettyMatrix(int[][] matrix){
        System.out.print("#  ");
        for (int i = 0; i < matrix.length; i++) {
            if(Integer.toString(i).length() == 3)
                System.out.print(Integer.toString(i).substring(1)+" ");
            else if(Integer.toString(i).length() == 2)
                System.out.print(i+" ");
            else
                System.out.print(i+"  ");
        }
        System.out.println();

        for (int i = 0; i < matrix.length; i++) {

            int[] row = matrix[i];
            if(Integer.toString(i).length() == 3)
                System.out.print(Integer.toString(i).substring(1)+" ");
            else if(Integer.toString(i).length() == 2)
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
