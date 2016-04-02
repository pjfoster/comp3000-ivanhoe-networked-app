package comp3004.ivanhoe.view.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageHandler {

	/**
	 * Returns a JLabel containing the image loaded from the given string
	 * @param imageName
	 * @return
	 */
	public static JLabel loadImage(String imageName) {
		try {
			BufferedImage image = ImageIO.read(ImageHandler.class.getResource("misc_images/" + imageName + ".png"));
			JLabel imageWrapper = new JLabel(new ImageIcon(image));
			return imageWrapper;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a JLabel containing the card loaded from the given string
	 * @param imageName
	 * @return
	 */
	public static JLabel loadCard(String imageName) {
		try {
			BufferedImage image = ImageIO.read(ImageHandler.class.getResource("cards/small/" + imageName + ".jpeg"));
			JLabel imageWrapper = new JLabel(new ImageIcon(image));
			return imageWrapper;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Returns a JLabel containing the image of a token of the given color
	 * @param imageName
	 * @return
	 */
	public static JLabel loadToken(String color) {
		System.out.println("Token color: " + color);
		try {
			BufferedImage image = ImageIO.read(ImageHandler.class.getResource("misc_images/token_" + color + ".png"));
			JLabel imageWrapper = new JLabel(new ImageIcon(image));
			return imageWrapper;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
