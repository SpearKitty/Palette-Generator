package com.spkitty.frame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.spkitty.palette.Palette;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 
 * @author SpearKitty
 *
 */
public class Frame extends JFrame {
	
	/**
	 * uwu
	 */
	private static final long serialVersionUID = -7244082044004798852L;

	private JPanel optionsPanel;
	
	private JLabel displayLabel;
	
	private JSlider scalingSlider;
	private JSlider toleranceSlider;
	private JSlider cutoffSlider;
	
	private JLabel scalingLabel;
	private JLabel toleranceLabel;
	private JLabel cutoffLabel;

	private JButton createButton;
	private JButton openButton;
	private JButton saveButton;
	
	private BufferedImage loadedImage;
	private Palette palette;
	
	public Frame() {
		this.setTitle("Palette Generator");
		this.setSize(600, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		try {
			this.setIconImage(ImageIO.read(ClassLoader.getSystemResource("palette.jpeg")));
		}catch(Exception e) {}
	}
	
	public void run() {
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(10, 1));
		
		scalingLabel = new JLabel("Scaling [15%]");
		scalingSlider = new JSlider(0, 100, 15);
		toleranceLabel = new JLabel("Tolerance [25%]");
		toleranceSlider = new JSlider(0, 100, 25);
		cutoffLabel = new JLabel("Cutoff [40%]");
		cutoffSlider = new JSlider(0, 100, 40);
		createButton = new JButton("View Palette");
		openButton = new JButton("Open");
		saveButton = new JButton("Export");

		scalingSlider.addChangeListener((ChangeEvent e) -> {
			scalingLabel.setText("Scaling [" + scalingSlider.getValue() + "%]");
		});
		
		toleranceSlider.addChangeListener((ChangeEvent e) -> {
			toleranceLabel.setText("Tolerance [" + toleranceSlider.getValue() + "%]");
		});
		
		cutoffSlider.addChangeListener((ChangeEvent e) -> {
			cutoffLabel.setText("Cutoff [" + cutoffSlider.getValue() + "%]");
		});
		
		createButton.addActionListener((ActionEvent e) -> {
			palette.createPalette(loadedImage, (double) scalingSlider.getValue() / 100, (double) toleranceSlider.getValue() / 100, (double) cutoffSlider.getValue() / 100); 
			PalettePreview p = new PalettePreview(palette, new int[] {scalingSlider.getValue(), toleranceSlider.getValue(), cutoffSlider.getValue()});
			p.run();
		});	
		
		openButton.addActionListener((ActionEvent e) -> {
			try {
				JFileChooser opt = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "png", "jpg", "jpeg");
				opt.setFileFilter(filter);
				if(opt.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					this.loadedImage = ImageIO.read(opt.getSelectedFile());
					double imageScalar = Math.max((double) loadedImage.getWidth() / displayLabel.getWidth(), (double) loadedImage.getHeight() / displayLabel.getHeight());
					displayLabel.setIcon(new ImageIcon(Palette.resize(loadedImage, (int) (loadedImage.getWidth() / imageScalar), (int) (loadedImage.getHeight() / imageScalar))));
					palette.createPalette(loadedImage, (double) scalingSlider.getValue() / 100, (double) toleranceSlider.getValue() / 100, (double) cutoffSlider.getValue() / 100); 
				}
			}catch(Exception err) {
				JOptionPane.showMessageDialog(this, "Error opening file.\n" + err.toString());
				err.printStackTrace();
			}
		});	
		
		saveButton.addActionListener((ActionEvent e) -> {
			try {
				JFileChooser opt = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", ".png");
				opt.setFileFilter(filter);
				if(opt.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
					ImageIO.write(palette.getImage(), "png", new File(opt.getSelectedFile().getPath() + ".png"));
				}
			}catch(Exception err) {
				JOptionPane.showMessageDialog(this, "Error saving file.\n" + err.toString());
				err.printStackTrace();
			}
		});
		
		optionsPanel.add(scalingLabel);
		optionsPanel.add(scalingSlider);
		optionsPanel.add(toleranceLabel);
		optionsPanel.add(toleranceSlider);
		optionsPanel.add(cutoffLabel);
		optionsPanel.add(cutoffSlider);
		optionsPanel.add(new JLabel());
		optionsPanel.add(createButton);
		optionsPanel.add(openButton);
		optionsPanel.add(saveButton);
		
		displayLabel = new JLabel();
		
		palette = new Palette();
		
		this.add(optionsPanel, BorderLayout.EAST);
		this.add(displayLabel, BorderLayout.CENTER);
		this.setVisible(true);
	}
	
}
