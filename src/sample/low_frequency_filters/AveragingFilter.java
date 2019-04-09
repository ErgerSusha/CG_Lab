package sample.low_frequency_filters;

import java.awt.image.BufferedImage;

public class AveragingFilter {

    public static void filter(int width, int height, int[][] rgbOldArray, BufferedImage image, Integer radius) {
        int[][] newArr = new int[width - radius * 2][height - radius * 2];

        for (int i = radius; i < width - radius; i++) {
            for (int j = radius; j < height - radius; j++) {
                int newR = 0, newG = 0, newB = 0;

                for (int k = -radius; k <= radius; k++) {
                    for (int m = -radius; m <= radius; m++) {
                        newR += (rgbOldArray[i + k][j + m] >> 16) & 0xff;
                        newG += (rgbOldArray[i + k][j + m] >> 8) & 0xff;
                        newB += rgbOldArray[i + k][j + m] & 0xff;
                    }
                }

                int n = (2 * radius + 1) * (2 * radius + 1);
                newR /= n;
                newG /= n;
                newB /= n;

                newArr[i - radius][j - radius] = (newR << 16) | (newG << 8) | newB;
            }
        }
        for (int i = radius; i < width - radius; i++) {
            for (int j = radius; j < height - radius; j++) {
                image.setRGB(i, j, newArr[i - radius][j - radius]);
            }
        }

    }
}
