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

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.MessageBox;
import org.primaresearch.dla.page.layout.PageLayout;
import org.primaresearch.dla.page.layout.physical.shared.LowLevelTextType;
import org.primaresearch.page.viewer.dla.XmlDocumentLayoutLoader;
import org.primaresearch.page.viewer.extra.Task;
import org.primaresearch.page.viewer.extra.TaskListener;
import org.primaresearch.page.viewer.image.Image;
import org.primaresearch.page.viewer.image.ImageLoader;
import org.primaresearch.page.viewer.ui.AboutDialog;
import org.primaresearch.page.viewer.ui.PageDataDialog;
import org.primaresearch.page.viewer.ui.views.DocumentImageView;
import org.primaresearch.page.viewer.ui.views.DocumentView;

/**
 * Event listener/handler
 * 
 * @author Christian Clausner
 * @author Jake Sebright
 *
 */
public class EventListener implements SelectionListener, TaskListener, KeyListener {
	
	private PageViewer pageViewer;

	/**
	 * Constructor
	 * 
	 * @param pageViewer Page Viewer main class
	 */
	public EventListener(PageViewer pageViewer) {
		this.pageViewer = pageViewer;
	}

	//SelectionListener
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/**
	 * Called when a widget has been selected (e.g. button pressed)
	 */
	public void widgetSelected(SelectionEvent e) {
		//Menu items
		if (e.getSource() instanceof Item) {
			Item item = (Item)e.getSource();
			//New
			if (item.getData() == "NEW_DOCUMENT") {
				OnNewDocument();
			}
			//Open
			else if (item.getData() == "OPEN_DOCUMENT") {
				OnOpenDocument();
			}
			//Exit
			else if (item.getData() == "EXIT_ALETHEIA") {
                pageViewer.exit();
			}
			//About
			else if (item.getData() == "HELP_ABOUT") {
                showAboutDialog();
			}
			//Zoom
			else if (item.getData() == "ZOOM_IN") {
				if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
					((DocumentImageView)pageViewer.getMainWindow().getView()).zoomIn();
			}
			else if (item.getData() == "ZOOM_OUT") {
				if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
					((DocumentImageView)pageViewer.getMainWindow().getView()).zoomOut();
			}
			else if (item.getData() == "ZOOM_100") {
				if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
					((DocumentImageView)pageViewer.getMainWindow().getView()).resetZoom();
			}
			else if (item.getData() == "ZOOM_FIT") {
				if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
					((DocumentImageView)pageViewer.getMainWindow().getView()).zoomToFit();
			}
			//Display modes
			else if (item.getData() == "DISPLAY_IMAGE") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_IMAGE);
			}
			else if (item.getData() == "DISPLAY_BORDER") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_BORDER);
			}
			else if (item.getData() == "DISPLAY_PRINTSPACE") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_PRINTSPACE);
			}
			else if (item.getData() == "DISPLAY_REGIONS") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_REGION);
			}
			else if (item.getData() == "DISPLAY_READING_ORDER") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_READING_ORDER);
			}
			else if (item.getData() == "DISPLAY_TEXTLINES") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_TEXTLINE);
			}
			else if (item.getData() == "DISPLAY_WORDS") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_WORD);
			}
			else if (item.getData() == "DISPLAY_GLYPHS") {
				toggleDisplayMode(DocumentView.DISPLAYMODE_GLYPH);
			}
			//Page data (attributes and metadata)
			else if (item.getData() == "SHOW_PAGE_DATA") {
				showPageDataDialog();
			}
		}
	}
	
	@Deprecated
	private void OnNewDocument() {
        FileDialog fd = new FileDialog(pageViewer.getMainWindow().getShell(), SWT.OPEN);
        fd.setText("Select Document Image");
        String[] filterExt = { "*.tif", "*.tiff" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
		if ( selected != null) {
			Document doc = new Document();
			pageViewer.setDocument(doc);
			ImageLoader imgLoader = new ImageLoader(pageViewer.getMainWindow().getShell().getDisplay(), selected);
			pageViewer.runTaskAsync(imgLoader);
		}
	}
	
	/**
	 * Opens a file selection dialog and creates a new document from the selected XML file
	 */
	private void OnOpenDocument() {
        FileDialog fd = new FileDialog(pageViewer.getMainWindow().getShell(), SWT.OPEN);
        fd.setText("Select Document File");
        String[] filterExt = { "*.xml" };
        fd.setFilterExtensions(filterExt);
        String selected = fd.open();
		if ( selected != null) {
			pageViewer.openDocument(selected);
		}
	}

	@Override
	public void taskFinished(final Task task) {
		try {
			// Task Succeeded
			if (task.isSuccessfull()) {
				//Image Loader
				if (task instanceof ImageLoader) {
					onImageLoaderFinished((ImageLoader)task);
				}
				//Xml Loader
				else if (task instanceof XmlDocumentLayoutLoader) {
					onXmlLoaderFinished((XmlDocumentLayoutLoader)task);
				}
			}
			// Task Failed
			else
			{
				//Xml Loader
				if (task instanceof XmlDocumentLayoutLoader) {
					onXmlLoaderFailed((XmlDocumentLayoutLoader)task);
				}
			}
		} catch (Exception exc) {
			exc.printStackTrace(); //TODO
		}
		
		//Process next task
		pageViewer.processNextTask();
	}
	


	/**
	 * Called when an image has been loaded 
	 */
	private void onImageLoaderFinished(ImageLoader imageLoader) {
		try {
			final Image image = imageLoader.getImage();
			pageViewer.getDocument().setImage(image);
			
			pageViewer.getMainWindow().getShell().getDisplay().asyncExec(new Runnable() {
	            public void run() {
	        		try {
	        			PageLayout pageLayout = pageViewer.getDocument().getPageLayout();
	        			
	        			//Post-process
	        			pageViewer.getXmlLoader().postProcess(pageViewer.getDocument().getPage(), image.getWidth(), image.getHeight(), 0, 0);
	        			
	        			//Init display mode
	        			int displayMode = DocumentView.DISPLAYMODE_IMAGE;
	        			// If there are regions - activate region mode
	        			if (pageLayout.getRegionCount() > 0)
	        				displayMode |= DocumentView.DISPLAYMODE_REGION; 
	        			pageViewer.setDisplayMode(displayMode);
	        			
	        			//Enable/disable toolbar buttons
						pageViewer.enableAction("SHOW_PAGE_DATA", true);
						pageViewer.enableAction("ZOOM_IN", true);
						pageViewer.enableAction("ZOOM_OUT", true);
						pageViewer.enableAction("ZOOM_100", true);
						pageViewer.enableAction("ZOOM_FIT", true);
						pageViewer.enableAction("DISPLAY_IMAGE", true);
						pageViewer.enableAction("DISPLAY_BORDER", 
								pageLayout.getBorder() != null);
						pageViewer.enableAction("DISPLAY_PRINTSPACE",
								pageLayout.getPrintSpace() != null);
						pageViewer.enableAction("DISPLAY_REGIONS", 
												pageViewer.getDocument().getPageLayout().getRegionCount() > 0);
						pageViewer.enableAction("DISPLAY_READING_ORDER", 
								pageViewer.getDocument().getPageLayout().getReadingOrder() != null 
								&& pageViewer.getDocument().getPageLayout().getReadingOrder().getRoot().getSize() > 0);
						pageViewer.enableAction("DISPLAY_TEXTLINES", 
								pageLayout.hasLowLevelTextObject(LowLevelTextType.TextLine));
						pageViewer.enableAction("DISPLAY_WORDS", 
								pageLayout.hasLowLevelTextObject(LowLevelTextType.Word));
						pageViewer.enableAction("DISPLAY_GLYPHS", 
								pageLayout.hasLowLevelTextObject(LowLevelTextType.Glyph));
						
						//Check/uncheck toolbar buttons
						pageViewer.checkButtonForAction("DISPLAY_BORDER", 
								(displayMode & DocumentView.DISPLAYMODE_BORDER) != 0);
						pageViewer.checkButtonForAction("DISPLAY_PRINTSPACE",
								(displayMode & DocumentView.DISPLAYMODE_PRINTSPACE) != 0);
						pageViewer.checkButtonForAction("DISPLAY_REGIONS", 
								(displayMode & DocumentView.DISPLAYMODE_REGION) != 0);
						pageViewer.checkButtonForAction("DISPLAY_READING_ORDER", 
								(displayMode & DocumentView.DISPLAYMODE_READING_ORDER) != 0);
						pageViewer.checkButtonForAction("DISPLAY_TEXTLINES", 
								(displayMode & DocumentView.DISPLAYMODE_TEXTLINE) != 0);
						pageViewer.checkButtonForAction("DISPLAY_WORDS", 
								(displayMode & DocumentView.DISPLAYMODE_WORD) != 0);
						pageViewer.checkButtonForAction("DISPLAY_GLYPHS", 
								(displayMode & DocumentView.DISPLAYMODE_GLYPH) != 0);
						
						//Refresh view?
						if (displayMode != DocumentView.DISPLAYMODE_IMAGE)
							pageViewer.refreshViews();
	        		} catch (Exception exc) {
	        			exc.printStackTrace();
	        		}
	            }
	         });
		} catch (Exception exc) {
			exc.printStackTrace(); //TODO
		}
	}
	
	/**
	 * Called when an XML file has been loaded 
	 */
	private void onXmlLoaderFinished(final XmlDocumentLayoutLoader xmlLoader) {
		try {
			pageViewer.getDocument().setPage(xmlLoader.getPage());
			//Load image
			final String imageFilePath = pageViewer.getImageFilePath();
			if (imageFilePath != null) {
				
				Display.getDefault().asyncExec(new Runnable()
				{
					 @Override
					 public void run()
					 {
						String filePath = imageFilePath;
						
						//File exists?
						if (filePath == null || !(new File(filePath)).exists()) {
							//Select image manually
						    FileDialog fd = new FileDialog(pageViewer.getMainWindow().getShell(), SWT.OPEN);
						    fd.setText("Select Image");
						    String[] filterExt = { "*.tif", "*.jpg", "*.png"/*, "*.jp2"*/ };
						    fd.setFilterExtensions(filterExt);
						    filePath = fd.open();
						}
						
						if (filePath != null) {
							ImageLoader imgLoader = new ImageLoader(pageViewer.getMainWindow().getShell().getDisplay(), filePath);
							pageViewer.runTaskAsync(imgLoader);
						}
					 }
				});
			}
		} catch (Exception exc) {
			exc.printStackTrace(); //TODO
		}
	}
	
	/**
	 * Called when an XML file has failed
	 */
	private void onXmlLoaderFailed(XmlDocumentLayoutLoader task) {
		Display.getDefault().asyncExec(new Runnable()
		{
			 @Override
			 public void run()
			 {
				// create a dialog with an OK button
				 MessageBox dialog =
				     new MessageBox(Display.getDefault().getActiveShell(), SWT.OK);
				 dialog.setText("XML Load Error");
				 dialog.setMessage("An XML loading error occured. Please ensure XML validity and try again.");

				 // open dialog and await confirmation
				 dialog.open();
			 }
		});
		
		
	}
	
	/**
	 * Toggles a flag to display (or hide) specific page content 
	 * @param mode Collection of bit flags (see DocumentView.DISPLAYMODE_... constants)
	 */
	private void toggleDisplayMode(int mode) {
		pageViewer.getMainWindow().getView().setDisplayMode(pageViewer.getMainWindow().getView().getDisplayMode() ^ mode);
		pageViewer.getMainWindow().getView().refresh();
	}

	/**
	 * Handles key press events
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.character == '+')	{
			if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
				((DocumentImageView)pageViewer.getMainWindow().getView()).zoomIn();
		} else if (e.character == '-')	{
			if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
				((DocumentImageView)pageViewer.getMainWindow().getView()).zoomOut();
		} else if (e.character == '/')	{
			if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
				((DocumentImageView)pageViewer.getMainWindow().getView()).zoomToFit();
		} else if (e.character == '*')	{
			if (pageViewer.getMainWindow().getView() instanceof DocumentImageView)
				((DocumentImageView)pageViewer.getMainWindow().getView()).resetZoom();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	/**
	 * Opens the about dialog
	 */
	private void showAboutDialog() {
		AboutDialog dlg = new AboutDialog(pageViewer.getMainWindow().getShell());
		dlg.open();
	}
	
	/**
	 * Opens dialogue with page attributes and metadata
	 */
	private void showPageDataDialog() {
		PageDataDialog dlg = new PageDataDialog(pageViewer.getMainWindow().getShell());
		dlg.open(pageViewer.getDocument().getPage());
	}

}
