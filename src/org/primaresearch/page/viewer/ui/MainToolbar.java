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
package org.primaresearch.page.viewer.ui;

import org.primaresearch.swt.ui.ImageToolbar;

/**
 * Toolbar for the main window of the Page Viewer
 * 
 * @author Christian Clausner
 *
 */
public class MainToolbar extends ImageToolbar{

	public MainToolbar(MainWindow mainWindow) {
		super(mainWindow.getShell(), mainWindow.getEventListener());
		
		//Enable only the open for now (the rest is enabled later)
		
		//addPushButton("New", "NEW_DOCUMENT", "/org/primaresearch/aletheia/ui/res/icon_new.png", "Create new document");
		addPushButton("Open", "OPEN_DOCUMENT", "/org/primaresearch/page/viewer/ui/res/icon_open.png", "Open document");
		addPushButton("Page data", "SHOW_PAGE_DATA", "/org/primaresearch/page/viewer/ui/res/icon_metadata.png", "Show page attributes and metadata");
		setEnabled("SHOW_PAGE_DATA", false);
		addSeparator();
		addPushButton("Zoom in", "ZOOM_IN", "/org/primaresearch/page/viewer/ui/res/icon_zoomin.png", "Zoom in");
		setEnabled("ZOOM_IN", false);
		addPushButton("Zoom out", "ZOOM_OUT", "/org/primaresearch/page/viewer/ui/res/icon_zoomout.png", "Zoom out");
		setEnabled("ZOOM_OUT", false);
		addPushButton("100%", "ZOOM_100", "/org/primaresearch/page/viewer/ui/res/icon_zoom_100.png", "Reset zoom to 100%");
		setEnabled("ZOOM_100", false);
		addPushButton("Fit", "ZOOM_FIT", "/org/primaresearch/page/viewer/ui/res/icon_zoom_fit.png", "Zoom to show whole image");
		setEnabled("ZOOM_FIT", false);
		addSeparator();
		ImageToolbarButton btn = addCheckButton("Image", 	"DISPLAY_IMAGE", 	"/org/primaresearch/page/viewer/ui/res/icon_displaymode_image.png", 	"Display Image");
		btn.getItem().setSelection(true);
		setEnabled("DISPLAY_IMAGE", false);
		addCheckButton("Border", "DISPLAY_BORDER", 	"/org/primaresearch/page/viewer/ui/res/icon_displaymode_border.png", 	"Display Border");
		setEnabled("DISPLAY_BORDER", false);
		addCheckButton("Printsp.", "DISPLAY_PRINTSPACE", "/org/primaresearch/page/viewer/ui/res/icon_displaymode_printspace.png", "Display Print Space");
		setEnabled("DISPLAY_PRINTSPACE", false);
		addCheckButton("Regions", "DISPLAY_REGIONS", 	"/org/primaresearch/page/viewer/ui/res/icon_displaymode_region.png", 	"Display Regions");
		setEnabled("DISPLAY_REGIONS", false);
		addCheckButton("Order", "DISPLAY_READING_ORDER", 	"/org/primaresearch/page/viewer/ui/res/icon_reading_order.png", 	"Display Reading Order");
		setEnabled("DISPLAY_READING_ORDER", false);
		addCheckButton("Lines", 	"DISPLAY_TEXTLINES", "/org/primaresearch/page/viewer/ui/res/icon_displaymode_textline.png", "Display Text Lines");
		setEnabled("DISPLAY_TEXTLINES", false);
		addCheckButton("Words", 	"DISPLAY_WORDS", 	"/org/primaresearch/page/viewer/ui/res/icon_displaymode_word.png", 	"Display Words");
		setEnabled("DISPLAY_WORDS", false);
		addCheckButton("Glyphs", 	"DISPLAY_GLYPHS", 	"/org/primaresearch/page/viewer/ui/res/icon_displaymode_glyph.png", 	"Display Glyphs");
		setEnabled("DISPLAY_GLYPHS", false);
	}
	
}
