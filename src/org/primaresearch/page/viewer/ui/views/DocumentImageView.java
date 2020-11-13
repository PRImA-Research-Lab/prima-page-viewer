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
package org.primaresearch.page.viewer.ui.views;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.primaresearch.dla.page.layout.PageLayout;
import org.primaresearch.dla.page.layout.logical.Group;
import org.primaresearch.dla.page.layout.logical.GroupMember;
import org.primaresearch.dla.page.layout.logical.ReadingOrder;
import org.primaresearch.dla.page.layout.logical.RegionRef;
import org.primaresearch.dla.page.layout.physical.ContentIterator;
import org.primaresearch.dla.page.layout.physical.ContentObject;
import org.primaresearch.dla.page.layout.physical.Region;
import org.primaresearch.dla.page.layout.physical.shared.LowLevelTextType;
import org.primaresearch.dla.page.layout.physical.shared.RegionType;
import org.primaresearch.dla.page.layout.physical.text.impl.TextLine;
import org.primaresearch.maths.geometry.Point;
import org.primaresearch.maths.geometry.Polygon;
import org.primaresearch.page.viewer.Document;
import org.primaresearch.page.viewer.Document.DocumentListener;
import org.primaresearch.page.viewer.PageViewer;
import org.primaresearch.page.viewer.ui.PageElementTooltip;
import org.primaresearch.page.viewer.ui.render.DrawingHelper;
import org.primaresearch.page.viewer.ui.render.DrawingHelper.ArrowShape;
import org.primaresearch.page.viewer.ui.render.PageContentColors;

import uky.article.imageviewer.views.SWTImageCanvas;

/**
 * Document view implementation showing a page image with content object overlay
 * 
 * @author Christian Clausner
 *
 */
public class DocumentImageView extends DocumentView implements DocumentListener, PaintListener, MouseTrackListener, MouseMoveListener, MouseWheelListener {
	
	private Composite viewPane;
	private SWTImageCanvas imageCanvas;
	private PageLayout docLayout;
	private PageElementTooltip tooltip;
	private PageContentColors colors;
	
	/**
	 * Constructor
	 * @param pageViewer Page Viewer object
	 * @param document Document object
	 * @param parent Parent view container
	 */
	public DocumentImageView(PageViewer pageViewer, Document document, Composite parent) {
		super(pageViewer, document, parent);
		init();
	}
	
	/**
	 * Releases resources
	 */
	public void dispose() {
		colors.dispose();
		tooltip.dispose();
	}
	
	/**
	 * Creates the view pane, adds listeners, and initialises the tooltips.
	 */
	private void init() {
		
		viewPane = new Composite(parent, SWT.NONE);
		
		viewPane.setLayout(new FillLayout());
		
		viewPane.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		
	    imageCanvas = new SWTImageCanvas(viewPane);
	    
	    imageCanvas.addMouseTrackListener(this);
	    imageCanvas.addMouseMoveListener(this);
	    
		imageCanvas.addMouseWheelListener(imageCanvas);
		//imageCanvas.addMouseMoveListener(imageCanvas);
		imageCanvas.addMouseWheelListener(this);

	    tooltip = new PageElementTooltip(viewPane.getShell());
	    tooltip.activateHoverHelp(imageCanvas);
	    
	    colors = new PageContentColors(viewPane.getDisplay());
	}

	@Override
	public Composite getViewPane() {
		return viewPane;
	}
	
	@Override
	public void refresh() {
		org.eclipse.swt.graphics.Image source = pageViewer.getDocument().getImage().getImageObject();
		org.eclipse.swt.graphics.Image copy;
		
		if ((displayMode & DISPLAYMODE_IMAGE) != 0) {
			
			if (source.getImageData().depth > 8) { //Colour image
				copy = new org.eclipse.swt.graphics.Image(	viewPane.getDisplay(), 
															source, 
															SWT.IMAGE_COPY);
			}
			//B/W or grey scale image (we cannot copy, otherwise the internal image is not in colour and we can't draw the overlay in colour)
			else {
				int height = source.getImageData().height;
				int width = source.getImageData().width;
				
				copy = new org.eclipse.swt.graphics.Image(viewPane.getDisplay(), 
	        											width, height);
				
				GC gc = new GC(copy);
				gc.drawImage(source,  0,  0);
				gc.dispose();
				
				/*
				copy = new org.eclipse.swt.graphics.Image(viewPane.getDisplay(), 
	        											width, height);
				//Copy pixels
				int x,y;
				int buffer[] = new int[width];
				ImageData srcData = source.getImageData();
				ImageData targetData = copy.getImageData();
				int black = 0x00000000;
				int white = 0x00FFFFFF;
				for (y=0; y<height; y++) {
					//srcData.getPixels(0, y, width, buffer, 0);
					Arrays.fill(buffer, black);
					targetData.setPixels(0, y, width, buffer, 0);
				
					//CC: Too slow:
					//for (x=0; x<width; x++)
					//	copy.getImageData().setPixel(x, y, source.getImageData().getPixel(x, y) == 0 
					//												? SWT.COLOR_BLACK 
					//												: SWT.COLOR_WHITE);
				}*/
			}
		} else { //Empty image
			copy = new org.eclipse.swt.graphics.Image(viewPane.getDisplay(), pageViewer.getDocument().getImage().getWidth(), pageViewer.getDocument().getImage().getHeight());
		}
		GC gc = new GC(copy);
		drawEverything(gc);
		imageCanvas.setSourceImage(copy, false);
		gc.dispose();
	}
	
	/**
	 * Increases the zoom level
	 */
	public void zoomIn() {
		imageCanvas.zoomIn();
	}
	
	/**
	 * Decreases the zoom level
	 */
	public void zoomOut() {
		imageCanvas.zoomOut();
	}

	/**
	 * Resets the zoom to 100%
	 */
	public void resetZoom() {
		imageCanvas.resetZoom();
	}
	
	/**
	 * Change zoom to fit whole page image into window
	 */
	public void zoomToFit() {
		imageCanvas.fitCanvas();
	}

	/**
	 * Called when the document page image has changed
	 */
	public void imageChanged() {
		try {
			if (pageViewer.getDocument().getImage() != null)
			{
				final DocumentImageView view = this; 
				viewPane.getDisplay().asyncExec(new Runnable() {
		            public void run() {
		        		try {
		        			//org.eclipse.swt.graphics.Image source = pageViewer.getDocument().getImage().getImageObject();
		        			org.eclipse.swt.graphics.Image copy = null;
		        			
		        			//System.out.println(" Depth = "+source.getImageData().depth);
		        					
		        			//if (source.getImageData().depth > 1) { //Colour or grey scale image
		        				//Copy directly 
		        				copy = new org.eclipse.swt.graphics.Image(viewPane.getDisplay(), 
									 										pageViewer.getDocument().getImage().getImageObject(), 
									 										SWT.IMAGE_COPY);
		        			//} 
		        			//B/W image
		        			//else if (source.getImageData().depth == 1){
		        			//	copy = new org.eclipse.swt.graphics.Image(viewPane.getDisplay(), 
		        			//											source.getBounds().width, source.getBounds().height);
		        			//}
		        			
		        			imageCanvas.setSourceImage(copy);
		        			imageCanvas.addPaintListener(view);
		        		} catch (Exception exc) {
		        			exc.printStackTrace();
		        		}
		            }
		         });
			}
		} catch (Exception exc) {
			exc.printStackTrace();
		}
	}

	@Override
	public void setDocument(Document doc) {
		super.setDocument(doc);
		doc.addListener((DocumentListener)this);
		imageChanged();
	}

	public void paintControl(PaintEvent e) {
        e.gc.dispose();
	}
	
	/**
	 * Renders the page content overlays.
	 * @param gc Drawing canvas
	 */
	private void drawEverything(GC gc) {
		if (document != null && document.getPageLayout() != null) {
			docLayout = document.getPageLayout();
			
			int oldAntialias = gc.getAntialias();
			gc.setAntialias(SWT.ON);
			
			if ((displayMode & DISPLAYMODE_BORDER) != 0)
				drawBorder(gc);
			if ((displayMode & DISPLAYMODE_PRINTSPACE) != 0)
				drawPrintSpace(gc);
			if ((displayMode & DISPLAYMODE_REGION) != 0)
				drawRegions(gc);
			if ((displayMode & DISPLAYMODE_READING_ORDER) != 0)
				drawReadingOrder(gc);
			if ((displayMode & DISPLAYMODE_TEXTLINE) != 0)
				drawTextlines(gc);
			if ((displayMode & DISPLAYMODE_WORD) != 0)
				drawWords(gc);
			if ((displayMode & DISPLAYMODE_GLYPH) != 0)
				drawGlyphs(gc);
			
			gc.setAntialias(oldAntialias);
		}		
	}
	
	/**
	 * Draws all regions
	 */
	private void drawRegions(GC gc) {
		gc.setLineWidth(1);
		for (ContentIterator it = docLayout.iterator(null); it.hasNext(); ) {
			ContentObject region = it.next();
			if (region.getCoords() != null) {
				Polygon coords = region.getCoords();
				DrawingHelper.drawPolygon(gc, getContentObjectColor(region), coords);
			}
		}
	}
	
	/**
	 * Draws all text lines
	 */
	private void drawTextlines(GC gc) {
		gc.setLineWidth(1);
		for (ContentIterator it = docLayout.iterator(LowLevelTextType.TextLine); it.hasNext(); ) {
			ContentObject region = it.next();
			if (region instanceof TextLine &&
					((TextLine)region).getBaseline() != null) {
				Polygon baseline = ((TextLine)region).getBaseline();
				DrawingHelper.drawMultiline(gc, getContentObjectColor(region), baseline);
			}
			if (region.getCoords() != null) {
				Polygon coords = region.getCoords();
				DrawingHelper.drawPolygon(gc, getContentObjectColor(region), coords);
			}
		}

		/*for (int i=0; i<docLayout.getRegionCount(); i++) {
			Region region = docLayout.getRegion(i);
			if (region instanceof TextRegion) {
				TextRegion textReg = (TextRegion)region;
				if (!textReg.hasTextObjects())
					continue;
				for (int l=0; l<textReg.getTextObjectCount(); l++) {
					TextLine line = (TextLine)textReg.getTextObject(l);
					if (line.getCoords() != null) {
						Polygon coords = line.getCoords();
						drawPolygon(gc, getContentObjectColor(line), coords);
					}
				}
			}
		}*/
	}

	/**
	 * Draws all words
	 */
	private void drawWords(GC gc) {
		gc.setLineWidth(1);
		for (ContentIterator it = docLayout.iterator(LowLevelTextType.Word); it.hasNext(); ) {
			ContentObject region = it.next();
			if (region.getCoords() != null) {
				Polygon coords = region.getCoords();
				DrawingHelper.drawPolygon(gc, getContentObjectColor(region), coords);
			}
		}

		/*for (int i=0; i<docLayout.getRegionCount(); i++) {
			Region region = docLayout.getRegion(i);
			if (region instanceof TextRegion) {
				TextRegion textReg = (TextRegion)region;
				if (!textReg.hasTextObjects())
					continue;
				for (int l=0; l<textReg.getTextObjectCount(); l++) {
					TextLine line = (TextLine)textReg.getTextObject(l);
					if (!line.hasTextObjects())
						continue;
					for (int w=0; w<line.getTextObjectCount(); w++) {
						Word word = (Word)line.getTextObject(w);
						if (word.getCoords() != null) {
							Polygon coords = word.getCoords();
							drawPolygon(gc, getContentObjectColor(word), coords);
						}
					}
				}
			}
		}*/
	}
	
	/**
	 * Draws all glyphs
	 */
	private void drawGlyphs(GC gc) {
		gc.setLineWidth(1);
		for (ContentIterator it = docLayout.iterator(LowLevelTextType.Glyph); it.hasNext(); ) {
			ContentObject region = it.next();
			if (region.getCoords() != null) {
				Polygon coords = region.getCoords();
				DrawingHelper.drawPolygon(gc, getContentObjectColor(region), coords);
			}
		}

		/*for (int i=0; i<docLayout.getRegionCount(); i++) {
			Region region = docLayout.getRegion(i);
			if (region instanceof TextRegion) {
				TextRegion textReg = (TextRegion)region;
				if (!textReg.hasTextObjects())
					continue;
				for (int l=0; l<textReg.getTextObjectCount(); l++) {
					TextLine line = (TextLine)textReg.getTextObject(l);
					if (!line.hasTextObjects())
						continue;
					for (int w=0; w<line.getTextObjectCount(); w++) {
						Word word = (Word)line.getTextObject(w);
						if (!word.hasTextObjects())
							continue;
						for (int g=0; g<word.getTextObjectCount(); g++) {
							Glyph glyph = (Glyph)word.getTextObject(g);
							if (glyph.getCoords() != null) {
								Polygon coords = glyph.getCoords();
								drawPolygon(gc, getContentObjectColor(glyph), coords);
							}
						}
					}
				}
			}
		}*/
	}



	/**
	 * Draws the page border
	 */
	private void drawBorder(GC gc) {
		gc.setLineWidth(2);
		if (docLayout.getBorder() != null) {
			DrawingHelper.drawPolygon(gc, new Color(viewPane.getDisplay(), 255, 99, 71), docLayout.getBorder().getCoords());
		}
	}

	/**
	 * Draws the page print space
	 */
	private void drawPrintSpace(GC gc) {
		gc.setLineWidth(2);
		if (docLayout.getPrintSpace() != null) {
			DrawingHelper.drawPolygon(gc, new Color(viewPane.getDisplay(), 0, 100, 0), docLayout.getPrintSpace().getCoords());
		}
	}
	
	/**
	 * Draws the text region reading order (arrows, etc.)
	 * @param gc
	 */
	private void drawReadingOrder(GC gc) {
		ReadingOrder readingOrder = docLayout.getReadingOrder();

		if (readingOrder == null || readingOrder.getRoot() == null || readingOrder.getRoot().getSize() == 0)
			return;

		gc.setAlpha(255);
		gc.setLineWidth(3);

		//Connections
		drawReadingOrderElement(gc, readingOrder.getRoot(), 0);
	}

	/**
	 * Draws the given reading order element and all its child elements.
	 */
	private void drawReadingOrderElement(GC gc, GroupMember element, int level)
	{
		if (element == null) //Should not happen
			return;

		//Group
		if (element instanceof Group) {
			Group group = (Group)element;

			//Draw children recursively
			for (int i=0; i<group.getSize(); i++)
				drawReadingOrderElement(gc, group.getMember(i), level+1);

			//Draw Group:
			// Ordered Group (arrows from child to child)
			if (group.isOrdered())
			{
				GroupMember child1 = null;
				GroupMember child2 = null;
				Point center1 = null;
				Point center2 = null;
				ArrowShape arrow = new ArrowShape();
				arrow.fill = true;
				arrow.width = 15;
				arrow.theta = 0.78;

				for (int i=0; i<group.getSize(); i++) {
					child2 = group.getMember(i);
					if (child2 != null)	{
						center2 = getReadingOrderEndPoint(child2);
						if (center2 == null) //happens for empty groups
							continue;
					}
					if (child1 != null && center1 != null && center2 != null) {
						//Draw arrow from centre of child 1 to centre of child 2
						gc.setForeground(getReadingOrderGroupColor(level));
						gc.setBackground(getReadingOrderGroupColor(level));

						DrawingHelper.drawArrow(gc, center1, center2, arrow);

						//Dot
						gc.setForeground(colors.ReadingOrderCenterColor);
						gc.setBackground(colors.ReadingOrderCenterColor);
						gc.fillOval(center2.x-4, center2.y-4, 9, 9);
					}
					child1 = child2;
					center1 = getReadingOrderStartPoint(child1);
				}
			}
			//Unordered Group (star - lines from the group centre to the child centres)
			else { 
				Point groupCenter = getReadingOrderStartPoint(group);
				Point center2 = null;
				if (groupCenter != null) {
					gc.setForeground(getReadingOrderGroupColor(level));
					gc.setBackground(getReadingOrderGroupColor(level));

					//'Star'
					int x1, y1;
					x1 = groupCenter.x;
					y1 = groupCenter.y;
					for (int i=0; i<group.getSize(); i++) {
						center2 = getReadingOrderEndPoint(group.getMember(i));
						if (center2 != null) {
							gc.drawLine(x1,  y1,  center2.x, center2.y);
						}
					}
					//Circle
					gc.setForeground(colors.ReadingOrderCenterColor);
					gc.setBackground(colors.White);
					gc.fillOval(x1-7, y1-7, 15, 15);
					gc.drawOval(x1-7, y1-7, 15, 15);
				}
			}
		}
		//RegionRef (just draw a dot)
		else {
			//Dot
			Point center = getReadingOrderEndPoint(element);
			if (center != null) {
				gc.setForeground(colors.ReadingOrderCenterColor);
				gc.setBackground(colors.ReadingOrderCenterColor);
				gc.fillOval(center.x-4, center.y-4, 9, 9);
			}
		}
	}
	
	/**
	 * Returns the colour for the reading oder group connectors.
	 */
	private Color getReadingOrderGroupColor(int level) {
		switch(level%3)
		{
			case 0: return colors.ReadingOrderColor1;
			case 1: return colors.ReadingOrderColor2;
			default: return colors.ReadingOrderColor3;
		}
	}
	
	/**
	 * Returns the centre of the specified reading order element.
	 * For a RegionRef element this is simply the centre of the region.
	 * For groups it is the centre of all child centres.
	 */
	Point getReadingOrderStartPoint(GroupMember element) {
		if (element == null)
			return null;

		Point ret = new Point();

		//RegionRef
		if (element instanceof RegionRef) {
			Region region = (Region)docLayout.getRegion(((RegionRef)element).getRegionId());
			if (region != null)	{
				return region.getCoords().getBoundingBox().getCenter();
			}
		}
		//Group 
		else if (element instanceof Group) {
			Group group = (Group)element;
			if (group.getSize() == 0) { //No centre available -> skip the group
				return null;
			}
			if (group.isOrdered()) //ordered -> start point = centre of last child
			{
				for (int i=group.getSize()-1; i>=0; i--) {
					ret = getReadingOrderEndPoint(group.getMember(i));
					if (ret != null)
						return ret;
				}
			}
			else { //unordered -> start point = average of all child centres
				int x = 0, y = 0, count = 0;
				for (int i=0; i<group.getSize(); i++) {
					Point childCenter = getReadingOrderEndPoint(group.getMember(i));
					if (childCenter != null) {
						x += childCenter.x;
						y += childCenter.y;
						count++;
					}
				}	
				if (x == 0 && y == 0 || count == 0) //No centre available
					return null;
				x /= count;
				y /= count;
				ret.x = x;
				ret.y = y;
			}
		}
		return ret;		
	}

	
	/**
	 * Returns the centre of the specified reading order element.
	 * For a RegionRef element this is simply the centre of the region.
	 * For groups it is the centre of all child centres.
	 */
	Point getReadingOrderEndPoint(GroupMember element) {
		if (element == null)
			return null;

		Point ret = new Point();

		//RegionRef (centre = centre of bounding box)
		if (element instanceof RegionRef) {
			Region region = (Region)docLayout.getRegion(((RegionRef)element).getRegionId());
			if (region != null)	
				ret = region.getCoords().getBoundingBox().getCenter();
		}
		//Group 
		else if (element instanceof Group) {
			Group group = (Group)element;
			if (group.getSize() == 0) { //No centre available -> skip the group
				return null;
			}
			if (group.isOrdered()) //ordered -> end point = centre of first child
			{
				for (int i=0; i<group.getSize(); i++) {
					ret = getReadingOrderEndPoint(group.getMember(i));
					if (ret != null)
						return ret;
				}
			}
			else { //unordered (centre = average of all child centres)
				int x = 0, y = 0, count = 0;
				for (int i=0; i<group.getSize(); i++) {
					Point childCenter = getReadingOrderEndPoint(group.getMember(i));
					if (childCenter != null) {
						x += childCenter.x;
						y += childCenter.y;
						count++;
					}
				}	
				if (x == 0 && y == 0 || count == 0) { //No centre available
					return null;
				}
				x /= count;
				y /= count;
				ret.x = x;
				ret.y = y;
			}
		}
		return ret;
	}
	
	/**
	 * Returns the outline and fill colour that corresponds to the object type 
	 */
	private Color getContentObjectColor(ContentObject contentObj) {
		if (contentObj.getType() == LowLevelTextType.TextLine)
			return colors.TextLineColor;
		else if (contentObj.getType() == LowLevelTextType.Word)
			return colors.WordColor;
		else if (contentObj.getType() == LowLevelTextType.Glyph)
			return colors.GlyphColor;
		else if (contentObj.getType() == RegionType.TextRegion)
			return colors.TextRegionColor;
		else if (contentObj.getType() == RegionType.ChartRegion)
			return colors.ChartRegionColor;
		else if (contentObj.getType() == RegionType.GraphicRegion)
			return colors.GraphicRegionColor;
		else if (contentObj.getType() == RegionType.ImageRegion)
			return colors.ImageRegionColor;
		else if (contentObj.getType() == RegionType.LineDrawingRegion)
			return colors.LineDrawingRegionColor;
		else if (contentObj.getType() == RegionType.MathsRegion)
			return colors.MathsRegionColor;
		else if (contentObj.getType() == RegionType.NoiseRegion)
			return colors.NoiseRegionColor;
		else if (contentObj.getType() == RegionType.SeparatorRegion)
			return colors.SeparatorRegionColor;
		else if (contentObj.getType() == RegionType.TableRegion)
			return colors.TableRegionColor;
		else if (contentObj.getType() == RegionType.AdvertRegion)
			return colors.AdvertRegionColor;
		else if (contentObj.getType() == RegionType.ChemRegion)
			return colors.ChemRegionColor;
		else if (contentObj.getType() == RegionType.MusicRegion)
			return colors.MusicRegionColor;
		else if (contentObj.getType() == RegionType.MapRegion)
			return colors.MapRegionColor;
		else if (contentObj.getType() == RegionType.CustomRegion)
			return colors.CustomRegionColor;
		return colors.UnknownRegionColor;
	}

	@Override
	public void mouseEnter(MouseEvent e) {
	}

	@Override
	public void mouseExit(MouseEvent e) {
	}

	@Override
	public void mouseHover(MouseEvent e) {
		if (docLayout == null)
			return;
		
		//Calculate the position in document page coordinates
		java.awt.Point p = new java.awt.Point(e.x, e.y);
		AffineTransform transform = imageCanvas.getTransform();
		try {
			transform.inverseTransform(p, p);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
		
		//Find the region at the current position
		if ((displayMode & DISPLAYMODE_REGION) != 0
			|| (displayMode & DISPLAYMODE_TEXTLINE) != 0
			|| (displayMode & DISPLAYMODE_WORD) != 0
			|| (displayMode & DISPLAYMODE_GLYPH) != 0
			) {
			ContentObject obj = null;
			if ((displayMode & DISPLAYMODE_GLYPH) != 0)
				obj = docLayout.getObjectAt(p.x, p.y, LowLevelTextType.Glyph);
			if (obj == null && (displayMode & DISPLAYMODE_WORD) != 0)
				obj = docLayout.getObjectAt(p.x, p.y, LowLevelTextType.Word);
			if (obj == null && (displayMode & DISPLAYMODE_TEXTLINE) != 0)
				obj = docLayout.getObjectAt(p.x, p.y, LowLevelTextType.TextLine);
			if (obj == null && (displayMode & DISPLAYMODE_REGION) != 0)
				obj = docLayout.getRegionAt(p.x, p.y);
				
			//Show/hide tooltip
			if (obj != null) 
				tooltip.onMouseHover(e, obj);
			else 
				tooltip.setVisible(false);
		}
	}

	@Override
	public void mouseMove(MouseEvent e) {
		tooltip.setVisible(false);
	}

	@Override
	public void mouseScrolled(MouseEvent e) {
		// non-CTRL already handled by SWTImageCanvas
		if ((e.stateMask & (SWT.CTRL | SWT.COMMAND)) != 0) {
			if (e.count > 0)
				zoomIn();
			else
				zoomOut();
		}
	}
}

