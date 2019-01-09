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

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;
import org.primaresearch.page.viewer.PageViewer;
import org.primaresearch.page.viewer.EventListener;
import org.primaresearch.page.viewer.ui.views.DocumentImageView;

/**
 * Main window for the Page Viewer (containing menu, toolbar, and document view)
 * 
 * @author Christian Clausner
 *
 */
public class MainWindow {
	
	private Shell shell;
	private PageViewer pageViewer;
	private DocumentImageView view;
	private MainToolbar toolbar = null;
	
	/**
	 * Constructor
	 * @param shell SWT window shell
	 * @param pageViewer Page Viewer object
	 */
	public MainWindow(Shell shell, PageViewer pageViewer) {
		this.shell = shell;
		this.pageViewer = pageViewer;
	}
	
	/**
	 * Initialises the window (title, size, etc., views)
	 */
	public void init() {
		shell.setText("Page Viewer");
		shell.setSize(1024, 768);

        centerToScreen();
        
	    shell.setLayout(new GridLayout(1,false));

        new MainMenu(this);
        toolbar = new MainToolbar(this);
        
        view = new DocumentImageView(pageViewer, null, shell);
        pageViewer.registerDocumentView(view);
        
 		shell.open();
	}
	
	/**
	 * Returns the SWT shell object of the window
	 * @return
	 */
	public Shell getShell() {
		return shell;
	}
	
	/**
	 * Centre window
	 */
    public void centerToScreen() {
        Rectangle bds = shell.getDisplay().getMonitors()[0].getBounds();
        Point p = shell.getSize();
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;
        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
    
    /**
     * Returns the page viewer's event listener
     */
	public EventListener getEventListener() {
		return pageViewer.getMainEventListener();
	}

	/**
	 * Releases resources and closes the window
	 */
	public void dispose() {
		toolbar.dispose();
		shell.getDisplay().dispose();
		view.dispose();
	}

	/**
	 * Returns the main document view
	 */
	public DocumentImageView getView() {
		return view;
	}

	/**
	 * Returns the main toolbar
	 */
	public MainToolbar getToolbar() {
		return toolbar;
	}
	
}
