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
package org.primaresearch.page.viewer.image;

import org.eclipse.swt.widgets.Display;
import org.primaresearch.page.viewer.extra.Task;

/**
 * Image loading task
 * 
 * @author Christian Clausner
 *
 */
public class ImageLoader extends Task {
	private Display display;
	private String filePath;
	private Image image = null;

	/**
	 * Constructor
	 * @param display SWT display object
	 * @param filePath Image file path
	 */
	public ImageLoader(Display display, String filePath) {
		this.display = display;
		this.filePath = filePath;
	}

	@Override
	protected boolean doRun() {
		image = new Image(display, filePath);
		image.getImageObject().getImageData(); //Triggers loading
		return true;
	}
	
	/**
	 * Returns the loaded image
	 * @return
	 */
	public Image getImage() {
		return image;
	}
}
