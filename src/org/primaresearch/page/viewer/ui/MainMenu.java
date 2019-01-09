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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * Menu for the main window of the Page Viewer
 * 
 * @author Christian Clausner
 *
 */
public class MainMenu {

	/**
	 * Constructor
	 * @param mainWindow Main window where the menu is to be attached
	 */
	public MainMenu(MainWindow mainWindow) {
		Shell shell = mainWindow.getShell();
		
        Menu menuBar = new Menu(shell, SWT.BAR);
        
        //File menu
        MenuItem cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&File");
        
        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        MenuItem item;
        
        // New
//        item = new MenuItem(fileMenu, SWT.PUSH);
//        item.setText("New");
//        item.setData("NEW_DOCUMENT");
//        item.addSelectionListener(mainWindow.getEventListener());
        
        // New
        item = new MenuItem(fileMenu, SWT.PUSH);
        item.setText("Open");
        item.setData("OPEN_DOCUMENT");
        item.addSelectionListener(mainWindow.getEventListener());
       
        // Exit
        item = new MenuItem(fileMenu, SWT.PUSH);
        item.setText("&Exit Page Viewer");
        item.setData("EXIT_ALETHEIA");
        item.addSelectionListener(mainWindow.getEventListener());
        
        
        //View menu
        /*cascadeFileMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeFileMenu.setText("&View");
        
        fileMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeFileMenu.setMenu(fileMenu);

        // Zoom in
        item = new MenuItem(fileMenu, SWT.PUSH);
        item.setText("Zoom in\t+");
        item.setData("ZOOM_IN");
        item.addSelectionListener(mainWindow.getEventListener());

        // Zoom out
        item = new MenuItem(fileMenu, SWT.PUSH);
        item.setText("Zoom out\t-");
        item.setData("ZOOM_OUT");
        item.addSelectionListener(mainWindow.getEventListener());*/
        
        //Help menu
        MenuItem cascadeHelpMenu = new MenuItem(menuBar, SWT.CASCADE);
        cascadeHelpMenu.setText("&Help");
        
        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        cascadeHelpMenu.setMenu(helpMenu);

        // About
        item = new MenuItem(helpMenu, SWT.PUSH);
        item.setText("About Page Viewer");
        item.setData("HELP_ABOUT");
        item.addSelectionListener(mainWindow.getEventListener());

        shell.setMenuBar(menuBar);
	}
}
