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

import org.eclipse.swt.widgets.Composite;
import org.primaresearch.page.viewer.PageViewer;
import org.primaresearch.page.viewer.Document;


/**
 * Abstract document view
 *  
 * @author Christian Clausner
 *
 */
public abstract class DocumentView {
	
	//Display modes (bit flags)
	public static final int DISPLAYMODE_IMAGE 			= 64;
	public static final int DISPLAYMODE_BORDER 			= 16;
	public static final int DISPLAYMODE_PRINTSPACE 		= 32;
	public static final int DISPLAYMODE_REGION 			= 1;
	public static final int DISPLAYMODE_TEXTLINE 		= 2;
	public static final int DISPLAYMODE_WORD 			= 4;
	public static final int DISPLAYMODE_GLYPH 			= 8;
	public static final int DISPLAYMODE_READING_ORDER 	= 128;
	
	protected Document document;
	protected PageViewer pageViewer;
	protected Composite parent;
	protected int displayMode = DISPLAYMODE_IMAGE;
	
	/**
	 * Constructor
	 * @param pageViewer Page Viewer object
	 * @param document Document object
	 * @param parent Parent view container
	 */
	public DocumentView(PageViewer pageViewer, Document document, Composite parent) {
		this.pageViewer = pageViewer;
		this.document = document;
		this.parent = parent;
	}
	
	/**
	 * Returns the SWT view pane
	 */
	public abstract Composite getViewPane(); 

	/**
	 * Sets the document object
	 */
	public void setDocument(Document doc) {
		this.document = doc;
	}

	/**
	 * Returns the display mode (see DISPLAYMODE_... constants)
	 * @return Collection of bit flags used to determine which page content to show/hide
	 */
	public int getDisplayMode() {
		return displayMode;
	}

	/**
	 * Sets the display mode (see DISPLAYMODE_... constants)
	 * @param displayMode Collection of bit flags used to determine which page content to show/hide
	 */
	public void setDisplayMode(int displayMode) {
		this.displayMode = displayMode;
	}

	/**
	 * Refreshes this view
	 */
	public abstract void refresh();
}
