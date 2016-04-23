/*
 * Copyright 2015 PRImA Research Lab, University of Salford, United Kingdom
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
package org.primaresearch.page.viewer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.primaresearch.dla.page.Page;
import org.primaresearch.dla.page.layout.PageLayout;
import org.primaresearch.page.viewer.image.Image;

/**
 * Document with image and page content
 * 
 * @author Christian Clausner
 *
 */
public class Document {

	/** Page image */
	private Image image;
	
	/** Page content (layout, text, ...) */
	private Page page;
	
	private Set<DocumentListener> listeners = new HashSet<DocumentListener>();
	
	/**
	 * Sets a new document page image and notifies all document listeners.
	 */
	public void setImage(Image image) {
		if (this.image != null)
			this.image.dispose();
		this.image = image;
		
		//Notify listener
		for (Iterator<DocumentListener> it = listeners.iterator(); it.hasNext(); ) {
			it.next().imageChanged();
		}
	}

	/**
	 * Returns the current document page image
	 */
	public Image getImage() {
		return image;
	}
	
	/**
	 * Disposes this document (including the current image)
	 */
	public void dispose() {
		if (this.image != null)
			this.image.dispose();
	}
	
	/**
	 * Adds the given document listener
	 */
	public void addListener(DocumentListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Removes the given document listener
	 */
	public void removeListeners() {
		listeners.clear();
	}
	
	/**
	 * Sets the page content
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * Returns the page content
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Returns the page layout (part of page)
	 */
	public PageLayout getPageLayout() {
		return page.getLayout();
	}

	/**
	 * Listener interface for document related events
	 * 
	 * @author Christian Clausner
	 *
	 */
	public static interface DocumentListener {
		/**
		 * Notification that the document page image has been changed
		 */
		public void imageChanged();
	}
}

