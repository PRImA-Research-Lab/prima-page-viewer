/*
 * Copyright 2019 PRImA Research Lab, University of Salford, United Kingdom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primaresearch.page.viewer.ui.render;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

/**
 * Collection of colours for the Page Viewer
 * 
 * @author Christian Clausner
 *
 */
public class PageContentColors {

	public Color TextLineColor;
	public Color WordColor;
	public Color GlyphColor;
	public Color TextRegionColor;
	public Color ChartRegionColor;
	public Color GraphicRegionColor;
	public Color ImageRegionColor;
	public Color LineDrawingRegionColor;
	public Color MathsRegionColor;
	public Color NoiseRegionColor;
	public Color SeparatorRegionColor;
	public Color TableRegionColor;
	public Color AdvertRegionColor;
	public Color ChemRegionColor;
	public Color MusicRegionColor;
	public Color MapRegionColor;
	public Color UnknownRegionColor;
	public Color CustomRegionColor;
	
	public Color ReadingOrderColor1;
	public Color ReadingOrderColor2;
	public Color ReadingOrderColor3;
	public Color ReadingOrderCenterColor;

	public Color White;

	public PageContentColors(Display display) {
		 TextLineColor = new Color(display, 50, 205, 50);
		 WordColor = new Color(display, 178, 34, 34);
		 GlyphColor = new Color(display, 46, 139, 8);
		 TextRegionColor = new Color(display, 0, 0, 255);
		 ChartRegionColor = new Color(display, 128, 0, 128);
		 GraphicRegionColor = new Color(display, 0,128,0);
		 ImageRegionColor = new Color(display, 0,206,209);
		 LineDrawingRegionColor = new Color(display, 184, 134, 11);
		 MathsRegionColor = new Color(display, 0, 191, 255);
		 NoiseRegionColor = new Color(display, 255, 0, 0);
		 SeparatorRegionColor = new Color(display, 255, 0, 255);
		 TableRegionColor = new Color(display, 139, 69, 19);
		 AdvertRegionColor = new Color(display, 70, 130, 180);
		 ChemRegionColor = new Color(display, 255, 140,   0);
		 MusicRegionColor = new Color(display, 148,   0, 211);
		 MapRegionColor = new Color(display, 154, 205, 50);
		 UnknownRegionColor = new Color(display, 100, 100, 100);
		 CustomRegionColor = new Color(display, 99, 124, 129);
		 
		 ReadingOrderColor1 = new Color(display, 220, 20, 60);
		 ReadingOrderColor2 = new Color(display, 148, 0, 211);
		 ReadingOrderColor3 = new Color(display, 0, 0, 139);
		 ReadingOrderCenterColor = new Color(display, 139, 0, 0);
		 
		 White = new Color(display, 255, 255, 255);
	}
	
	/**
	 * Releases resources
	 */
	public void dispose() {
		 TextLineColor.dispose();
		 WordColor.dispose();
		 GlyphColor.dispose();
		 TextRegionColor.dispose();
		 ChartRegionColor.dispose();
		 GraphicRegionColor.dispose();
		 ImageRegionColor.dispose();
		 LineDrawingRegionColor.dispose();
		 MathsRegionColor.dispose();
		 NoiseRegionColor.dispose();
		 SeparatorRegionColor.dispose();
		 TableRegionColor.dispose();
		 AdvertRegionColor.dispose();
		 ChemRegionColor.dispose();
		 MusicRegionColor.dispose();
		 MapRegionColor.dispose();
		 UnknownRegionColor.dispose();
		 CustomRegionColor.dispose();
		 
		 ReadingOrderColor1.dispose();
		 ReadingOrderColor2.dispose();
		 ReadingOrderColor3.dispose();
		 ReadingOrderCenterColor.dispose();
		 White.dispose();
	}
}
