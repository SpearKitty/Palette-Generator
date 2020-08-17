package com.spkitty.palette;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 
 * @author SpearKitty
 *
 */
public class Palette {
	
	private ArrayList<Color> colors;
	
	public Palette() {
		colors = new ArrayList<Color>();
	}
	
	public boolean addColor(Color c) {
		return colors.contains(c) ? false : colors.add(c);
	}
	
	public Color getColor(int index) {
		return colors.get(index);
	}
	
	public void sort(boolean isAscending) {
		int[] rawColors = new int[colors.size()];
		for(int i = 0; i < colors.size(); i++)
			rawColors[i] = colors.get(i).getRGB();
		for(int i = 0; i < rawColors.length - 1; i++) {
			if(isAscending ? rawColors[i] > rawColors[i+1] : rawColors[i] < rawColors[i+1]) {
				int temp = rawColors[i];
				Color tempC = colors.get(i);
				rawColors[i] = rawColors[i+1];
				colors.set(i, colors.get(i+1));
				rawColors[i+1] = temp;
				colors.set(i+1, tempC);
			}
		}
	}
	
	public void createPalette(BufferedImage img, double scalingFactor, double tolerance, double cutoffPercent) {
		this.colors = new ArrayList<Color>();
		int[] whiteSide = new int[] {0, 0, 0};
		int[] blackSide = new int[] {255, 255, 255};
		
		
		for(int i = 0; i < img.getHeight(); i++) 
			for(int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				whiteSide = whiteSide[0] < c.getRed() && whiteSide[1] < c.getGreen() && whiteSide[2] < c.getBlue() ? new int[] {c.getRed(), c.getGreen(), c.getBlue()} : whiteSide;
				blackSide = blackSide[0] > c.getRed() && blackSide[1] > c.getGreen() && blackSide[2] > c.getBlue() ? new int[] {c.getRed(), c.getGreen(), c.getBlue()} : blackSide;
			}
		
		BufferedImage smol = resize(img, (int) (img.getWidth() * scalingFactor), (int) (img.getHeight() * scalingFactor));
		ArrayList<ColorToken> unique = new ArrayList<ColorToken>();
		
		for(int i = 0; i < smol.getHeight(); i++)
			for(int j = 0; j < smol.getWidth(); j++) {
				ColorToken token = new ColorToken(new Color(smol.getRGB(j, i)), tolerance);
				if(unique.contains(token)) {
					unique.get(unique.indexOf(token)).increment();
					smol.setRGB(j, i, unique.get(unique.indexOf(token)).getColor().getRGB());
				}else
					unique.add(token);
			}
		
		colors.add(new Color(blackSide[0], blackSide[1], blackSide[2]));
		colors.add(new Color((whiteSide[0] + blackSide[0]) / 2, (whiteSide[1] + blackSide[1]) / 2, (whiteSide[2] + blackSide[2]) / 2));
		colors.add(new Color(whiteSide[0], whiteSide[1], whiteSide[2]));
		for(int i = 0; i < unique.size(); i++)
			if(unique.get(i).passesColorCheck(cutoffPercent))
				colors.add(unique.get(i).getColor());
	}
	
	public int size() {
		return colors.size();
	}
	
	public static int distanceFromBlack(Color c) {
		return 1000000*c.getRed() + 1000*c.getGreen() + c.getBlue();
	}
	
	public static int colorCompare(Color color1, Color color2) {
		return Math.round(Math.signum(distanceFromBlack(color1) - distanceFromBlack(color2)));
	}
	
	public BufferedImage getImage() {
		BufferedImage img = new BufferedImage(100*colors.size(), 100, BufferedImage.TYPE_INT_RGB);
		for(int i = 0; i < colors.size(); i++) {
			for(int x = i*100; x < (i+1)*100; x++) {
				for(int y = 0; y < 100; y++) {
					img.setRGB(x, y, colors.get(i).getRGB());
				}
			}
		}
		return img;
	}
	
	/**
	 * @param img Image to display
	 * @param scale Scalar value to adjust image by
	 */
	
	public static void displayImage(BufferedImage img, double scale) {
		BufferedImage rImage = resize(img, (int) ((double) img.getWidth() * scale), (int) ((double) img.getHeight() * scale));
		displayImage(rImage);
	}
	
	/**
	 * @param img Image to display
	 */
	
	public static void displayImage(BufferedImage img) {
		
		JFrame frame = new JFrame("Image");
		JLabel lbl = new JLabel();
			
		frame.setSize((int) (img.getWidth()*1.5), (int) (img.getHeight()*1.5));
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		lbl.setOpaque(true);
		lbl.setIcon(new ImageIcon(img));
			
		frame.setLocationRelativeTo(null);
			
		frame.add(lbl, BorderLayout.CENTER);
		frame.setVisible(true);
	}
	
	public static BufferedImage resize(BufferedImage img, int nWid, int nHgt) {
		BufferedImage ret = new BufferedImage(nWid, nHgt, BufferedImage.TYPE_INT_ARGB);
		Image temp = img.getScaledInstance(nWid, nHgt, Image.SCALE_SMOOTH);
		
		ret.getGraphics().drawImage(temp, 0, 0, null);
		
		return ret;
	}
	
	public String toString() {
		String str = "Palette:\n";
		for(Color c : colors)
			str += c.getRed() + "  " + c.getGreen() + "  " + c.getBlue() + "  |  " + distanceFromBlack(c) + "\n";
		return str;
	}
}
