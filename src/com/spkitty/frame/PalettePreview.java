package com.spkitty.frame;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.spkitty.palette.Palette;

/**
 * 
 * @author SpearKitty
 *
 */
public class PalettePreview extends JFrame {
	
	/**
	 * D:
	 */
	private static final long serialVersionUID = -5427132134525698962L;
	private BufferedImage img;

	public PalettePreview(Palette palette, int[] args){
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setTitle("Preview (" + args[0] + "," + args[1] + "," + args[2] + ")");
		this.setSize(50*palette.size(), 100);
		try {
			this.setIconImage(ImageIO.read(ClassLoader.getSystemResource("palette.jpeg")));
		}catch(Exception e) {}
		this.setLayout(new GridLayout(1, palette.size()));
		for(int i = 0; i < palette.size(); i++) {
			JLabel lbl = new JLabel();
			lbl.setText(""+i);
			lbl.setBackground(new Color(palette.getColor(i).getRed(), palette.getColor(i).getGreen(), palette.getColor(i).getBlue()));
			lbl.setForeground(palette.getColor(i));
			lbl.setOpaque(true);
			this.add(lbl);
		}
	}
	
	public void run() {
		this.setVisible(true);
	}
	
	public BufferedImage getImage() {
		return img;
	}
}
