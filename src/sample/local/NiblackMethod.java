package sample.local;

import java.awt.image.BufferedImage;

public class NiblackMethod {

    public static void filter(int width, int height, int[][] rgbOldArray, BufferedImage image, Double k_, Integer radius) {
        int[][] newArr = new int[width][height];

        for (int i = radius; i < width - radius; i++) {
            for (int j = radius; j < height - radius; j++) {
                float avgLuminance = 0;
                double avgSqrLuminance = 0;

                for (int k = -radius; k <= radius; k++) {
                    for (int m = -radius; m <= radius; m++) {
                        int R = (rgbOldArray[i + k][j + m] >> 16) & 0xff;
                        int G = (rgbOldArray[i + k][j + m] >> 8) & 0xff;
                        int B = rgbOldArray[i + k][j + m] & 0xff;
                        float currLuminance = (R * 0.2126f + G * 0.7152f + B * 0.0722f) / 255;
                        avgLuminance += currLuminance;
                        avgSqrLuminance += (currLuminance * currLuminance);
                    }
                }

                avgLuminance /= (2 * radius + 1);
                avgSqrLuminance /= (2 * radius + 1);
                avgSqrLuminance = Math.sqrt(avgSqrLuminance);


                double probability = avgLuminance + k_ * avgSqrLuminance;
                newArr[i - radius][j - radius] = probability >= 0.5f ? 0xffffff : 0;
            }
        }
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, newArr[i][j]);
            }
        }

    }
}
