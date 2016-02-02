package org.yenbo.commonDemo;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.RandomUtils;

public class DrawImage {

	public static void main(String[] args) throws IOException {

		final int width = 250;
		final int height = 250;
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setColor(new Color(RandomUtils.nextInt(0, 256), RandomUtils.nextInt(0, 256),
				RandomUtils.nextInt(0, 256)));
		graphics2d.fillRect(0, 0, width, height);
		
		ImageIO.write(image, "png", new File("d:\\var\\temp.png"));
		
		graphics2d.dispose();
	}
}
