package com.rise42;


import com.google.gson.JsonParser;
import com.rise42.module.Module;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException {
        StringBuilder builder = new StringBuilder();
        Files.lines(
                Paths.get(
                        "/home/rise42/Projects/ComputerSystems/res/scheme2.json", ""
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

        System.out.println(module);

        int[][] matrix1 = module.buildFirstMatrix();
        System.out.println("Matrix1");

        for (int[] row: matrix1) {
            for (int i : row) {
                System.out.print(i+" ");
            }
            System.out.println();
        }

        int[][] matrix2 = module.buildSecondMatrix();
        System.out.println("\nMatrix2");

        for (int[] row: matrix2) {
            for (int i : row) {
                System.out.print(i+" ");
            }
            System.out.println();
        }
    }
}
