package sample.adaptive;

import java.awt.image.BufferedImage;

public class AdaptiveMethod {

    public static void filter(int width, int height, int[][] rgbOldArray, BufferedImage image, float alpha) {
        int[][] newArr = new int[width - 2][height - 2];

        for (int i = 3; i < width - 3; i++) {
            for (int j = 3; j < height - 3; j++) {
                float avgLuminance = 0f;
                float minLuminance = Float.MAX_VALUE, maxLuminance = Float.MIN_VALUE;
                float deltaMax, deltaMin;
                float t = 0;
                int currWindowSize = 1;

                while (true) {
                    for (int k = -currWindowSize; k <= currWindowSize; k++) {
                        for (int m = -currWindowSize; m <= currWindowSize; m++) {
                            int R = (rgbOldArray[i + k][j + m] >> 16) & 0xff;
                            int G = (rgbOldArray[i + k][j + m] >> 8) & 0xff;
                            int B = rgbOldArray[i + k][j + m] & 0xff;
                            float currLuminance = (R * 0.2126f + G * 0.7152f + B * 0.0722f) / 255;
                            avgLuminance += currLuminance;
                            if (currLuminance < minLuminance) {
                                minLuminance = currLuminance;
                            }
                            if (currLuminance > maxLuminance) {
                                maxLuminance = currLuminance;
                            }
                        }
                    }

                    deltaMax = Math.abs(maxLuminance - avgLuminance);
                    deltaMin = Math.abs(minLuminance - avgLuminance);
                    avgLuminance /= (2 * currWindowSize + 1) * (2 * currWindowSize + 1);

                    int R = (rgbOldArray[i][j] >> 16) & 0xff;
                    int G = (rgbOldArray[i][j] >> 8) & 0xff;
                    int B = rgbOldArray[i][j] & 0xff;
                    float currLuminance = (R * 0.2126f + G * 0.7152f + B * 0.0722f) / 255;

                    if (deltaMax != deltaMin || maxLuminance == minLuminance) {
                        if (maxLuminance == minLuminance) {
                            t = alpha * avgLuminance;
                        } else if (deltaMax > deltaMin) {
                            t = alpha * ((2 * minLuminance) / 3 + avgLuminance / 3);
                        } else if (deltaMax < deltaMin) {
                            t = alpha * (minLuminance / 3 + (2 * avgLuminance) / 3);
                        }
                        int blackOrWhite = 0;
                        for (int m = i - 1; m <= i + 1; m++) {
                            for (int l = j - 1; l <= j + 1; l++) {
                                if (!(m == i && l == j)) {
                                    float avgLum = 0;
                                    for (int q = m - 1; q <= m + 1; q++) {
                                        for (int w = l - 1; w <= l + 1; w++) {
                                            int _R = (rgbOldArray[q][w] >> 16) & 0xff;
                                            int _G = (rgbOldArray[q][w] >> 8) & 0xff;
                                            int _B = rgbOldArray[q][w] & 0xff;
                                            avgLum += (_R * 0.2126f + _G * 0.7152f + _B * 0.0722f) / 255;
                                        }
                                    }
                                    avgLum /= (2 * currWindowSize + 1) * (2 * currWindowSize + 1);
                                    if (Math.abs(avgLum - currLuminance) > t) {
                                        ++blackOrWhite;
                                    }
                                }
                            }
                        }
                        if (blackOrWhite == 8) {
                            newArr[i - 1][j - 1] = 0xffffff;
                        } else {
                            newArr[i - 1][j - 1] = 0;
                        }
                        break;
                    } else {
                        ++currWindowSize;
                    }
                }
            }
        }

        for (int i = 3; i < width - 3; i++) {
            for (int j = 3; j < height - 3; j++) {
                image.setRGB(i, j, newArr[i - 1][j - 1]);
            }
        }

    }
}
