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
package org.primaresearch.page.viewer.dla;

import java.io.File;

import org.primaresearch.dla.page.Page;
import org.primaresearch.dla.page.io.xml.PageXmlInputOutput;
import org.primaresearch.page.viewer.extra.Task;

/**
 * XML loading task.
 * 
 * @author Christian Clausner
 *
 */
public class XmlDocumentLayoutLoader extends Task {
	private String filePath;
	private Page page = null;
	private String imageFilePath = null;

	/**
	 * Constructor
	 * @param filePath File path of the XML to be loaded
	 */
	public XmlDocumentLayoutLoader(String filePath) {
		this.filePath = filePath;
	}

	@Override
	protected boolean doRun() {
		
		try {
			page = PageXmlInputOutput.readPage(filePath);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return page != null;
	}

	/**
	 * Returns the loaded page
	 * @return The page object or <code>null</code> if the loading failed
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * Returns the image file path that was specified in the XML file
	 * @return The file path or an empty string if no image was specified
	 */
	public String getImageFilePath() {
		//Get root folder from XML file path
		String rootFolder = "";
		if (filePath.contains(File.separator)) {
			rootFolder = filePath.substring(0, filePath.lastIndexOf(File.separator));
		}
		if (!rootFolder.isEmpty())
			imageFilePath = rootFolder + File.separator + page.getImageFilename();
		else
			imageFilePath = page.getImageFilename();
		return imageFilePath;
	}
	
	/**
	 * Post-processing (e.g. for coordinate conversion)
	 */
	public void postProcess(Page page, int imageWidth, int imageHeight, double resX, double resY) {
		PageXmlInputOutput.postProcessPage(page, imageWidth, imageHeight, resX, resY);
	}
}
