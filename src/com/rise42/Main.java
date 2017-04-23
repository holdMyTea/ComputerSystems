package com.rise42;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rise42.module.Module;
import com.rise42.scheme.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(
                Paths.get(
                        "/home/rise42/Projects/ComputerSystems/res/scheme3.json", ""
                )
        ).forEach(builder::append);

        JsonObject file = new JsonParser().parse(builder.toString()).getAsJsonObject();
        JsonObject module = file.get("module").getAsJsonObject();

        System.out.print("Module count: ");
        int count = new Scanner(System.in).nextInt();

        int[][] matrix;

        if(count == 1) {
            matrix = new Module(0, module).buildSecondMatrix();
        } else if (count > 1){
            Scheme scheme;
            switch (file.get("topology").getAsString()){

                case "line":
                    System.out.println("LINE");
                    scheme = new LinearScheme(count, module);
                    break;

                case "circle":
                    scheme = new CircleScheme(count, module);
                    break;

                case "star":
                    scheme = new StarScheme(count, module);
                    break;

                case "tree":
                    scheme = new TreeScheme(count, module);
                    break;

                default:scheme = null;
            }

            //matrix = scheme.buildSecondMatrix();
        } else return;

        /*printPrettyMatrix(matrix);

        int s = findS(matrix);
        int d = findD(matrix);
        float meanD = findMeanD(matrix);

        System.out.println("S: "+s);
        System.out.println("D: "+ d);
        System.out.println("Mean D: "+ meanD);
        System.out.println("C: "+d * matrix.length * s);
        System.out.println("T: "+2 * meanD / s);*/
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

    public static int findS(int[][] m){
        int local, max = 0;
        for (int[] row: m){
            local = 0;
            for (int i: row){
                if(i==1){
                    local++;
                }
            }
            if(local > max){
                max = local;
            }
        }
        return  max;
    }

    public static int findD(int[][] m){
        int max = -1;

        for (int[] row: m)
            for(int i: row){
                if(i == 0) break;

                if(i > max) max = i;
            }

        return max;
    }

    public static float findMeanD(int[][] m){
        int totalSum = 0;

        for (int[] row: m)
            for(int i: row)
                totalSum += i;

        return (float) totalSum / (m.length * (m.length-1));
    }
}
