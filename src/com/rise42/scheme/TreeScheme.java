package com.rise42.scheme;

import com.google.gson.JsonObject;
import com.rise42.module.TreeModule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by rise42 on 08/04/17.
 */
public class TreeScheme extends Scheme {
    int moduleCount;
    int nodesInModule;

    Map<Integer, String> tree;

    TreeModule sampleModule;
    int[][] secondMatrix;

    int leftToRight, leftToParent, rightToParent;

    public TreeScheme(int moduleCount, JsonObject moduleJson) {
        this.moduleCount = moduleCount;

        sampleModule = new TreeModule(7, moduleJson);
        secondMatrix = sampleModule.buildSecondMatrix();
        nodesInModule = sampleModule.getIncludedNodes().size();

        leftToRight = secondMatrix[sampleModule.getLeftChild().getIndex()][sampleModule.getRightChild().getIndex()];
        leftToParent = secondMatrix[sampleModule.getLeftChild().getIndex()][sampleModule.getParent().getIndex()];
        rightToParent = secondMatrix[sampleModule.getRightChild().getIndex()][sampleModule.getParent().getIndex()];

        buildHierarchy();
    }

    private void buildHierarchy() {
        /*
                      Le Failling Tree
                       ______7_______
                      |             |
                 ____3____      ___11___
                |        |     |       |
             __1__    __5__  __9__   _13__
            |    |   |    | |    |  |    |
            0    2   4   6  8   10 12   14
         */

        boolean stop = false;
        int remaining = moduleCount;

        Map<Float, String> floatMap = new HashMap<>(moduleCount + 1);

        for (int i = 1, pow=0; !stop; i *= 2, pow++) {
            int path = 0;
            for (int j = 1; j < i * 2; j += 2) {
                float key = (float) moduleCount / ((float) i * 2) * j;

                String s = "";
                int p = path;
                for (int k = 0; k < pow; k++) {
                    s = ((p & 1) == 1 ? 'r' : 'l') + s;
                    p = p >> 1;
                }

                floatMap.put(key, s);

                remaining--;
                if (remaining == 0) {
                    stop = true;
                    break;
                }

                path++;
            }
        }

        float[] keys = new float[floatMap.size()];
        Iterator<Float> iterator = floatMap.keySet().iterator();
        for (int i = 0; i < floatMap.size(); i++) {
            keys[i] = iterator.next();
            System.out.println(i+": "+keys[i]);
        }
        System.out.println();

        Arrays.sort(keys);

        tree = new HashMap<>(moduleCount+1);
        for (int i = 0; i < keys.length; i++) {
            tree.put(i, floatMap.get(keys[i]));
        }

        System.out.println("Count: " + tree.size());
        for (int i : tree.keySet()) {
            System.out.println(i + " path: " + tree.get(i));
        }
    }

    @Override
    public int[][] buildSecondMatrix() {

        int totalSize = nodesInModule * moduleCount;
        int[][] m = new int[totalSize][totalSize];

        for (int i = 0; i < totalSize; i++)
            for (int j = 0; j < totalSize; j++)
                m[i][j] = -1;

        int[][] inner = sampleModule.buildSecondMatrix();

        for (int eight = 0; eight < totalSize; eight += nodesInModule)
            for (int i = 0; i < nodesInModule; i++)
                for (int j = 0; j < nodesInModule; j++)
                    m[eight + i][eight + j] = inner[i][j];


        for (int i = 1; i < totalSize; i++) {

            for (int j = 0; j < totalSize; j++) {

                if (m[i][j] == 0)
                    break;

                if (m[i][j] != -1)
                    continue;

                m[i][j] = findDistance(i, j);
                m[j][i] = m[i][j];
            }

        }


        return m;
    }

    private int findDistance(int a, int b) {
        int aModule = a / nodesInModule, aIndex = a % nodesInModule;
        int bModule = b / nodesInModule, bIndex = b % nodesInModule;

        String aPath = new StringBuilder(tree.get(aModule)).reverse().toString();
        String bPath = new StringBuilder(tree.get(bModule)).reverse().toString();

        while (aPath.length() > 0 && bPath.length() > 0 && aPath.charAt(aPath.length() - 1) == bPath.charAt(bPath.length() - 1)) {
            aPath = aPath.substring(0, aPath.length() - 1);
            bPath = bPath.substring(0, bPath.length() - 1);
        }

        aPath = new StringBuilder(aPath).reverse().toString();
        bPath = new StringBuilder(bPath).reverse().toString();

        int distance = 0;

        if (aPath.length() == 0) {
            distance += secondMatrix[aIndex][
                    bPath.charAt(0) == 'r' ? sampleModule.getRightChild().getIndex() : sampleModule.getLeftChild().getIndex()
                    ];
            distance += secondMatrix[bIndex][sampleModule.getParent().getIndex()];

            for (int i = 1; i < bPath.length(); i++) {
                distance += bPath.charAt(i) == 'r' ? rightToParent : leftToParent;
            }
            distance += bPath.length();
        } else if (bPath.length() == 0) {
            distance += secondMatrix[bIndex][
                    aPath.charAt(0) == 'r' ? sampleModule.getRightChild().getIndex() : sampleModule.getLeftChild().getIndex()
                    ];
            distance += secondMatrix[aIndex][sampleModule.getParent().getIndex()];
            for (int i = 1; i < aPath.length(); i++) {
                distance += aPath.charAt(i) == 'r' ? rightToParent : leftToParent;
            }
            distance += aPath.length();
        } else {
            distance += leftToRight;
            distance += secondMatrix[bIndex][sampleModule.getParent().getIndex()];
            for (int i = 1; i < bPath.length(); i++) {
                distance += bPath.charAt(i) == 'r' ? rightToParent : leftToParent;
            }
            distance += bPath.length();
            distance += secondMatrix[aIndex][sampleModule.getParent().getIndex()];
            for (int i = 1; i < aPath.length(); i++) {
                distance += aPath.charAt(i) == 'r' ? rightToParent : leftToParent;
            }
            distance += aPath.length();
        }

        return distance;
    }

}
