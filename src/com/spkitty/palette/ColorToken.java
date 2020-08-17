package com.spkitty.palette;

import java.awt.Color;

/**
 * 
 * @author SpearKitty
 *
 */
public class ColorToken{
	
	private Color base;
	private Color lowLim;
	private Color hiLim;
	private double tolerance;
	private int count;
	
	public ColorToken(Color init_base, double init_tolerance) {
		base = init_base;
		
		tolerance = init_tolerance;
		count = 1;
		
		lowLim = new Color((int) Math.max(base.getRed() - tolerance*255, 0), (int) Math.max(base.getGreen() - tolerance*255, 0), (int) Math.max(base.getBlue() - tolerance*255, 0));
		hiLim = new Color((int) Math.min(base.getRed() + tolerance*255, 255), (int) Math.min(base.getGreen() + tolerance*255, 255), (int) Math.min(base.getBlue() + tolerance*255, 255));
	}
	
	public Color[] getColorRange() {
		return new Color[] {lowLim, hiLim};
	}
	
	public void increment() {
		count++;
	}
	
	public double redRatio() {
		return (double) base.getRed() / (double) (base.getRed()+base.getGreen()+base.getBlue());
	}
	
	public double greenRatio() {
		return (double) base.getGreen() / (double) (base.getRed()+base.getGreen()+base.getBlue());
	}
	
	public double blueRatio() {
		return (double) base.getBlue() / (double) (base.getRed()+base.getGreen()+base.getBlue());
	}
	
	public String getHighestRGB() {
		return redRatio() > blueRatio() ? redRatio() > greenRatio() ? "Red" : "Green" : blueRatio() > greenRatio() ? "Blue" : "Green";
	}
	
	public boolean passesColorCheck(double threshold) {
		return getHighestRGBValue() > threshold;
	}
	
	public double getHighestRGBValue() {
		return redRatio() > blueRatio() ? redRatio() > greenRatio() ? redRatio() : greenRatio() : blueRatio() > greenRatio() ? blueRatio() : greenRatio();
	}
	
	public Color getColor() {
		return base;
	}
	public int getCount() {
		return count;
	}
	
	public boolean inRange(Color input) {
		return input.getRed() > lowLim.getRed() && input.getGreen() > lowLim.getGreen() && input.getBlue() > lowLim.getBlue() && input.getRed() < hiLim.getRed() && input.getGreen() < hiLim.getGreen() && input.getBlue() < hiLim.getBlue();
	}
	
	public boolean equals(Object obj) {
		return inRange(((ColorToken) obj).getColor()); // ((ColorToken) obj).getDistanceFromBlack() == this.getDistanceFromBlack();
	}
	
	public String toString() {
		return "Token Info:\nRGB: " + base.getRed() + ", " + base.getGreen() + ", " + base.getBlue() + " | " + Palette.distanceFromBlack(base) + " | " + (passesColorCheck(0.4) ? getHighestRGB() : "Mixed") + " (" + this.redRatio() + ", " + this.greenRatio() + ", " + this.blueRatio() + ")" + "\nCount: " + count;
	}
}
