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
package org.primaresearch.page.viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.primaresearch.page.viewer.dla.XmlDocumentLayoutLoader;
import org.primaresearch.page.viewer.extra.Task;
import org.primaresearch.page.viewer.ui.MainWindow;
import org.primaresearch.page.viewer.ui.views.DocumentView;

/**
 * Entry point class for the Page Viewer tool
 * 
 * @author Christian Clausner
 *
 */
public class PageViewer {
	
	private MainWindow mainWindow; 
	private EventListener mainEventListener;
	private Task currentTask = null;
	private List<Task> taskQueue = new ArrayList<Task>();
	private XmlDocumentLayoutLoader xmlLoader;
	private String imageFilePath;
	private String resolveDir;
	private Document document;
	private Set<DocumentView> documentViews = new HashSet<DocumentView>();

	/**
	 * Main function
	 * @param args Argument 0 (optional): Page XML file; Argument 1 (optional): Image file; Options: --resolve-dir </path/to/img>
	 */
	public static void main(String[] args) {
		
		String pageFilePath = null;
		String imageFilePath = null;
		String resolveDir = null;
		for (int i=0; i<args.length; i++) {
			if ("--resolve-dir".equals(args[i])) {
				i++;
				resolveDir = args[i];
			}
			else if (pageFilePath == null)
				pageFilePath = args[i];
			else 
				imageFilePath = args[i];
		}
		
		//Resolve
		if (imageFilePath != null && resolveDir != null && ! new File(imageFilePath).isAbsolute()) 
			imageFilePath = resolveDir + (resolveDir.endsWith(File.separator) ? "" : File.separator) + imageFilePath;

        Display display = new Display();
        new PageViewer(display, pageFilePath, imageFilePath, resolveDir);
        display.dispose();
	}
	
	/**
	 * Constructor
	 * @param display Responsible for managing the connection between SWT and the underlying operating system
	 * @param pageFilePath Path to page content XML file (optional, use <code>null</code> if not used)
	 * @param imageFilePath Path to page image file (optional, use <code>null</code> if not used)
	 * @param resolveDir Root path for loading images (using relative image path)
	 */
	public PageViewer(Display display, String pageFilePath, String imageFilePath, String resolveDir) {
		this.imageFilePath = imageFilePath;
		this.resolveDir = resolveDir;
		Shell shell = new Shell();
		
		//Icon
		try {
	    	Image smallIcon = new Image(display, getClass().getResourceAsStream("/org/primaresearch/page/viewer/ui/res/shell_icon_16.png"));
	    	Image largeIcon = new Image(display, getClass().getResourceAsStream("/org/primaresearch/page/viewer/ui/res/shell_icon_32.png"));
	
	    	shell.setImages(new Image[] { smallIcon, largeIcon });
		} catch (Exception exc) {
			exc.printStackTrace();
		}
		
		try {
			mainEventListener = new EventListener(this); 
		    shell.addKeyListener(mainEventListener);
			
	 		mainWindow = new MainWindow(shell, this);
	 		mainWindow.init();
	 		
	 		//Load page content now?
	 		if (pageFilePath != null)
	 			openDocument(pageFilePath, imageFilePath);
	 
	        while (!shell.isDisposed()) {
	        	if (!display.readAndDispatch()) {
	        		display.sleep();
	        	}
	        }
		}
		catch (Exception exc) {
			exc.printStackTrace(); //TODO
		}
    }

	/**
	 * A central event listener (toolbar, load events, keyboard) 
	 * @return Listener object
	 */
	public EventListener getMainEventListener() {
		return mainEventListener;
	}
	
	/**
	 * Returns the main window of the Page Viewer
	 */
	public MainWindow getMainWindow() {
		return mainWindow;
	}
	
	/**
	 * Exits the tool and cleans up
	 */
	public void exit() {
		try {
			mainWindow.dispose();
			cleanUp();
		} 
		catch (Exception exc) {
			exc.printStackTrace(); //TODO
		}
		System.exit(0);
	}
	
	/**
	 * Releases resources 
	 */
	private void cleanUp() {
		if (document != null)
			document.dispose();
	}
	
	/**
	 * Queues a task to be run asynchronously  
	 * @param task Task object
	 */
	public synchronized void runTaskAsync(Task task) {
		taskQueue.add(task);
		processNextTask();
	}
	
	/**
	 * Starts the next task from the task queue (if not empty).
	 */
	public void processNextTask() {
		if (!taskQueue.isEmpty()) {
			currentTask = taskQueue.get(0);
			taskQueue.remove(0);
			try {
				currentTask.addListener(mainEventListener);
				currentTask.runAsync();
			} catch (Exception exc) {
				exc.printStackTrace(); //TODO
			}
		}
	}

	/**
	 * Loads the specified PAGE file.
	 * Resets image file path.
	 */
	public void openDocument(String filePath) {
		openDocument(filePath, null);
	}
	
	/**
	 * Loads the specified PAGE file
	 */
	public void openDocument(String xmlFilePath, String imageFilePath) {
		setImageFilePath(imageFilePath);
		Document doc = new Document();
		setDocument(doc);
		xmlLoader = new XmlDocumentLayoutLoader(xmlFilePath, resolveDir);
		runTaskAsync(xmlLoader);
		updateTitle(xmlFilePath);
	}
	
	private void updateTitle(String xmlFilePath) {
		String title = "Page Viewer";
		if (xmlFilePath != null) {
			title += " (" + xmlFilePath;

			if (imageFilePath != null) {
				title += ", " + imageFilePath;
			}
			title += ")";
		}
		mainWindow.setTitle(title);
	}
	
	/**
	 * Sets the current document
	 * @param document Document object
	 */
	public void setDocument(Document document) {
		if (this.document != null)
			this.document.removeListeners();
		this.document = document;
		
		for (Iterator<DocumentView> it = documentViews.iterator(); it.hasNext(); )
			it.next().setDocument(document);
	}
	
	/**
	 * Returns the current document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Adds the given document view
	 */
	public void registerDocumentView(DocumentView view) {
		documentViews.add(view);
	}

	/**
	 * Returns a set of all registered document views
	 */
	public Set<DocumentView> getDocumentViews() {
		return documentViews;
	}
	
	/**
	 * Enables/disables a specific action
	 * @param actionId ID of the action
	 * @param enable <code>true</code> to enable; <code>false</code> to disable
	 */
	public void enableAction(String actionId, boolean enable) {
		mainWindow.getToolbar().setEnabled(actionId, enable);
	}

	/**
	 * Checks the toolbar button that corresponds to the given action (checked usually means lowered) 
	 * @param actionId ID of the button action
	 * @param check <code>true</code> to check (lower); <code>false</code> for normal state (raised)
	 */
	public void checkButtonForAction(String actionId, boolean check) {
		mainWindow.getToolbar().setChecked(actionId, check);
	}

	/**
	 * Sets the display mode of all document views.
	 * @param mode New display mode (see DocumentView.DISPLAYMODE_... constants)
	 */
	public void setDisplayMode(int mode) {
		for (Iterator<DocumentView> it = documentViews.iterator(); it.hasNext(); ) {
			it.next().setDisplayMode(mode);
		}
	}

	/**
	 * Refreshes document views.
	 * @param mode New display mode (see DocumentView.DISPLAYMODE_... constants)
	 */
	public void refreshViews() {
		for (Iterator<DocumentView> it = documentViews.iterator(); it.hasNext(); ) {
			it.next().refresh();
		}
	}

	/**
	 * Returns the XML loader that is used for page XML files
	 * @return
	 */
	public XmlDocumentLayoutLoader getXmlLoader() {
		return xmlLoader;
	}

	/**
	 * Returns the current image file path
	 * @return File path or <code>null</code>
	 */
	public String getImageFilePath() {
		if (imageFilePath != null)
			return imageFilePath;
		return xmlLoader.getImageFilePath();
	}

	/**
	 * Sets the current image file path
	 * @param imageFilePath File path or <code>null</code>
	 */
	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	
}
