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
package org.primaresearch.page.viewer.image;

import org.eclipse.swt.widgets.Display;

/**
 * Wrapper for an SWT image
 * 
 * @author Christian Clausner
 *
 */
public class Image {
	private org.eclipse.swt.graphics.Image imageObject;
	
	/**
	 * Constructor
	 * @param display SWT display object
	 * @param filePath Image file path
	 */
	public Image(Display display, String filePath) {
		imageObject = new org.eclipse.swt.graphics.Image(display, filePath);
	}

	/**
	 * Releases the image resource
	 */
	public void dispose() {
		imageObject.dispose();
	}
	
	/**
	 * Returns the SWT image
	 */
	public org.eclipse.swt.graphics.Image getImageObject() {
		return imageObject;
	}

	/**
	 * Image width in pixels
	 */
	public int getWidth() {
		return imageObject != null ? imageObject.getBounds().width : 0;
	}

	/**
	 * Image height in pixels
	 */
	public int getHeight() {
		return imageObject != null ? imageObject.getBounds().height : 0;
	}
}
