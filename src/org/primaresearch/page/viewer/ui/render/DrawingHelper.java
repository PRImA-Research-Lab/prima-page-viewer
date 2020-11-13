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
import org.eclipse.swt.graphics.GC;
import org.primaresearch.maths.geometry.Point;
import org.primaresearch.maths.geometry.Polygon;

/**
 * Several drawing functions
 * 
 * @author Christian Clausner
 *
 */
public class DrawingHelper {

	/**
	 * Draws the given polygon (filled)
	 */
	public static void drawPolygon(GC gc, Color color, Polygon coords) {
		int polygon[] = new int[coords.getSize()*2];
		
		for (int i=0; i<coords.getSize(); i++) {
			Point p = coords.getPoint(i); 
			polygon[i*2] = p.x;
			polygon[i*2+1] = p.y;
		}
		
		gc.setAlpha(30);
		gc.setForeground(color);
		gc.setBackground(color);
		gc.fillPolygon(polygon);
		gc.setAlpha(150);
		gc.drawPolygon(polygon);
	}
	
	/**
	 * Draws the given line string
	 */
	public static void drawMultiline(GC gc, Color color, Polygon coords) {
		int polyline[] = new int[coords.getSize()*2];

		for (int i=0; i<coords.getSize(); i++) {
			Point p = coords.getPoint(i);
			polyline[i*2] = p.x;
			polyline[i*2+1] = p.y;
		}

		gc.setForeground(color);
		gc.setBackground(color);
		gc.setAlpha(150);
		int w = gc.getLineWidth();
		gc.setLineWidth(w * 2);
		gc.drawPolyline(polyline);
		gc.setLineWidth(w);
	}

	/**
	 * Draws a line with arrow end
	 */
	public static void drawArrow(GC gc, int x1, int y1, int x2, int y2, ArrowShape arrow) {
		drawArrow(gc, new Point(x1,y1), new Point(x2,y2), arrow);
	}
	
	/** 
	 * Draws the given polygon (filled)
	 * Adapted from http://www.codeproject.com/KB/GDI/arrows.aspx
	 */
	public static void drawArrow(GC gc, Point from, Point to, ArrowShape arrow) {
		if (from == null || to == null)
			return;

		Point base = new Point();
		int[] aptPoly = new int[6];
		double[] vecLine = new double[2];
		double[] vecLeft = new double[2];
		double length;
		double th;
		double ta;

		// set to point
		aptPoly[0] = to.x;
		aptPoly[1] = to.y;

		// build the line vector
		vecLine[0] = (double) aptPoly[0] - from.x;
		vecLine[1] = (double) aptPoly[1] - from.y;

		// build the arrow base vector - normal to the line
		vecLeft[0] = -vecLine[1];
		vecLeft[1] = vecLine[0];

		// setup length parameters
		length = (double) Math.sqrt(vecLine[0] * vecLine[0] + vecLine[1] * vecLine[1]);
		th = arrow.width / (2.0 * length);
		ta = arrow.width / (2.0 * (Math.tan(arrow.theta) / 2.0) * length);

		// find the base of the arrow
		base.x = (int) (aptPoly[0] + -ta * vecLine[0]);
		base.y = (int) (aptPoly[1] + -ta * vecLine[1]);

		// build the points on the sides of the arrow
		aptPoly[2] = (int) (base.x + th * vecLeft[0]);
		aptPoly[3] = (int) (base.y + th * vecLeft[1]);
		aptPoly[4] = (int) (base.x + -th * vecLeft[0]);
		aptPoly[5] = (int) (base.y + -th * vecLeft[1]);

		// draw we're fillin'...
		if(arrow.fill) {
			gc.drawLine(from.x, from.y, aptPoly[0], aptPoly[1]);
			gc.fillPolygon(aptPoly);
		}
		// ... or even jes chillin'...
		else {
			gc.drawLine(from.x, from.y, base.x, base.y);
			gc.drawLine(base.x, base.y, aptPoly[2], aptPoly[3]);
			gc.drawLine(aptPoly[2], aptPoly[3], aptPoly[0], aptPoly[1]);
			gc.drawLine(aptPoly[0], aptPoly[1], aptPoly[4], aptPoly[5]);
			gc.drawLine(aptPoly[4], aptPoly[5], base.x, base.y);
		}
	}
	
	/**
	 * Data structure for defining an arrow
	 * 
	 * @author Christian Clausner
	 *
	 */
	public static final class ArrowShape {
		public boolean fill = true;
		public int width = 10;
		public double theta = 0.8;
	}
}
