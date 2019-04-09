package sample.low_frequency_filters;

import java.awt.image.BufferedImage;

public class AveragingFilterModificated {

    public static BufferedImage filter(int width, int height, int[][] rgbOldArray, BufferedImage image, Integer a) {
        int[][] newArr = new int[width - 2][height - 2];

        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                int newR = 0, newG = 0, newB = 0;

                for (int k = -1; k <= 1; k++) {
                    for (int m = -1; m <= 1; m++) {
                        if (m == 0 && k == 0) {
                            for (int _a = 0; _a < a; _a++) {
                                newR += (rgbOldArray[i][j] >> 16) & 0xff;
                                newG += (rgbOldArray[i][j] >> 8) & 0xff;
                                newB += rgbOldArray[i][j] & 0xff;
                            }
                        } else {
                            newR += (rgbOldArray[i + k][j + m] >> 16) & 0xff;
                            newG += (rgbOldArray[i + k][j + m] >> 8) & 0xff;
                            newB += rgbOldArray[i + k][j + m] & 0xff;
                        }
                    }
                }

                newR /= 8 + a;
                newG /= 8 + a;
                newB /= 8 + a;

                newArr[i - 1][j - 1] = (newR << 16) | (newG << 8) | newB;
            }
        }
        for (int i = 1; i < width - 1; i++) {
            for (int j = 1; j < height - 1; j++) {
                image.setRGB(i, j, newArr[i - 1][j - 1]);
            }
        }

        return image;
    }
}
