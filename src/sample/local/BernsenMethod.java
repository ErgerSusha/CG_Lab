package sample.local;

import java.awt.image.BufferedImage;

public class BernsenMethod {

    public static BufferedImage filter(int width, int height, int[][] rgbOldArray, BufferedImage image, Integer radius) {
        int[][] newArr = new int[width - radius][height - radius];

        for (int i = radius; i < width - 2 * radius - 1; i+=radius) {
            for (int j = radius; j < height - 2 * radius - 1; j+=radius) {
                float minLuminance = Float.MAX_VALUE, maxLuminance = Float.MIN_VALUE;

                for (int k = -radius; k <= radius; k++) {
                    for (int m = -radius; m <= radius; m++) {
                        int R = (rgbOldArray[i + k][j + m] >> 16) & 0xff;
                        int G = (rgbOldArray[i + k][j + m] >> 8) & 0xff;
                        int B = rgbOldArray[i + k][j + m] & 0xff;
                        float currLuminance = (R * 0.2126f + G * 0.7152f + B * 0.0722f) / 255;
                        if (currLuminance < minLuminance) {
                            minLuminance = currLuminance;
                        }
                        if (currLuminance > maxLuminance) {
                            maxLuminance = currLuminance;
                        }
                    }
                }

                float luminance = (minLuminance + maxLuminance) / 2;
                for (int k = -radius; k <= radius; k++) {
                    for (int m = -radius; m <= radius; m++) {
                        newArr[i + k][j + m] = luminance >= 0.5f ? 0xffffff : 0;
                    }
                }
            }
        }
        for (int i = radius; i < width - radius; i++) {
            for (int j = radius; j < height - radius; j++) {
                image.setRGB(i, j, newArr[i - radius][j - radius]);
            }
        }

        return image;
    }
}
