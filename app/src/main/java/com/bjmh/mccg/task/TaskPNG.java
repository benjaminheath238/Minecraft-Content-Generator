package com.bjmh.mccg.task;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class TaskPNG extends Task {
    public BufferedImage newImage(String size) {
        if (!size.matches("\\d*x\\d*"))
            return null;

        return new BufferedImage(Integer.parseInt(size.split("x")[0]),
                Integer.parseInt(size.split("x")[1]),
                BufferedImage.TYPE_4BYTE_ABGR);
    }

    public void saveImage(BufferedImage image, String path) {
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            System.err.println("An exception occurred while saving image to: " + path);
            e.printStackTrace();
        }
    }

    public BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println("An exception occurred while loading image from: " + path);
            e.printStackTrace();
            return null;
        }
    }

    public BufferedImage writeLayer(BufferedImage base, BufferedImage layer, String transparent) {
        return writeRange(base, layer, 0, 0, base.getWidth(), base.getHeight(), transparent);
    }

    public BufferedImage writeRange(BufferedImage base, BufferedImage layer, int x0, int y0, int x1, int y1,
            String transparent) {
        for (int x = 0; x < (x1 - x0); x++) {
            for (int y = 0; y < (y1 - y0); y++) {
                base = writePixel(base, layer, x, y, transparent);
            }
        }

        return base;
    }

    public BufferedImage writePixel(BufferedImage base, BufferedImage layer, int x, int y, String transparent) {
        Color lc = new Color(layer.getRGB(x, y), true);

        if (lc.getRGB() >= 0)
            return base;

        if (transparent.equals("true")) {
            Color bc = new Color(base.getRGB(x, y), true);
            base.setRGB(x, y, new Color(
                    (lc.getRed() + bc.getRed()) / 2,
                    (lc.getGreen() + bc.getGreen()) / 2,
                    (lc.getBlue() + bc.getBlue()) / 2,
                    (lc.getAlpha() + bc.getAlpha()) / 2).getRGB());
        } else {
            base.setRGB(x, y, lc.getRGB());
        }

        return base;
    }
}