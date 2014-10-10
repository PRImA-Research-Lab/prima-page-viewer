/*
 * Copyright 2014 PRImA Research Lab, University of Salford, United Kingdom
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

import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 'About' dialog with HTML content
 * 
 * @author Christian Clausner
 *
 */
public class AboutDialog extends Dialog {

	/**
	 * Constructor
	 * @param parent Parent window
	 */
	public AboutDialog(Shell parent) {
		super(parent);
	}

	/**
	 * Displays the dialog
	 */
    public void open() {
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setText(getText());
        shell.setSize(512, 400);
        shell.setText("About");
        
        // Your code goes here (widget creation, set result, etc).
        shell.setLayout(new FillLayout());
	    Browser browser;
	    try {
	        browser = new Browser(shell, SWT.NONE);
	         
	        //CC: This did not work with html inside a jar file
            //browser.setUrl(getClass().getResource("/org/primaresearch/page/viewer/ui/res/about.htm").toExternalForm());
	        
	        //Workaround:
	        InputStream in = getClass().getResourceAsStream("/org/primaresearch/page/viewer/ui/res/about.htm");
	        Scanner scanner = new Scanner(in);
	        StringBuffer buffer = new StringBuffer();
	        while(scanner.hasNextLine()) {
	            buffer.append(scanner.nextLine());
	        }
	        scanner.close();
	        browser.setText(buffer.toString());

	    } catch (Exception e) {
	    	e.printStackTrace(); //TODO
	    }
	    
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
        }
    }
}
