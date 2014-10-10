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

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.primaresearch.dla.page.MetaData;
import org.primaresearch.dla.page.Page;
import org.primaresearch.shared.variable.Variable;

/**
 * Dialogue for page attributes and metadata
 * 
 * @author Christian Clausner
 *
 */
public class PageDataDialog extends Dialog {

	private Font boldFont;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	/**
	 * Constructor
	 * @param parent
	 */
	public PageDataDialog(Shell parent) {
		super(parent);
	}

	/**
	 * Displays the dialog
	 */
    public void open(Page page) {
        Shell parent = getParent();
        Shell shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
        shell.setText(getText());
        shell.setSize(500, 500);
        shell.setText("Page Data");
        
        try {
        	boldFont = new Font(shell.getDisplay(),"Arial",12,SWT.BOLD);
    		GridLayout gridLayout = new GridLayout();
    		gridLayout.numColumns = 2;
    		gridLayout.marginWidth = 2;
    		gridLayout.marginHeight = 2;
    		shell.setLayout(gridLayout);
    		
    		addPageAttributes(page, shell);
    		addMetadata(page, shell);
        } catch(Exception exc) {
        	exc.printStackTrace();
        }
        
        //shell.pack();
        
        shell.open();
        Display display = parent.getDisplay();
        while (!shell.isDisposed()) {
        	if (!display.readAndDispatch()) display.sleep();
        }
        
        shell.dispose();
        boldFont.dispose();
    }
    
    /**
     * Adds page type and custom field
     */
    private void addPageAttributes(Page page, Shell shell) {
		Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, true, false, 2, 1));
		label.setFont(boldFont);
		label.setText("Page attributes");

		//Type
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Page type");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (page.getAttributes() != null) {
			Variable type = page.getAttributes().get("type");
			if (type != null && type.getValue() != null)
				label.setText(type.getValue().toString());
			else
				label.setText("[not set]");
		}
		
		//Custom
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Custom field");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (page.getAttributes() != null) {
			Variable custom = page.getAttributes().get("custom");
			if (custom != null && custom.getValue() != null)
				label.setText(custom.getValue().toString());
			else
				label.setText("[not set]");
		}
	}
    
    /**
     * Adds GtsId, comments, creator, etc.
     */
    private void addMetadata(Page page, Shell shell) {
    	MetaData metadata = page.getMetaData();
    	if (metadata == null)
    		return;
    	
		Label label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, true, false, 2, 1));
		label.setFont(boldFont);
		label.setText("Metadata");

		//GtsId
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("GTS-ID");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (page.getGtsId() != null)
			label.setText(page.getGtsId().toString());
		else
			label.setText("[not set]");
		
		//Creator
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Creator");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (metadata.getCreator() != null)
			label.setText(metadata.getCreator());
		else
			label.setText("[not set]");
		
		//Created
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Created");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (metadata.getCreationTime() != null)
			label.setText(dateFormat.format(metadata.getCreationTime()));
		else
			label.setText("[not set]");
		
		//Last change
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Last change");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (metadata.getLastModificationTime() != null)
			label.setText(dateFormat.format(metadata.getLastModificationTime()));
		else
			label.setText("[not set]");
		
		//Width
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Width");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (page.getLayout() != null)
			label.setText(""+page.getLayout().getWidth()+"px");
		else
			label.setText("[not set]");
		
		//Height
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		label.setText("Height");
		
		label = new Label(shell, SWT.NONE);
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		if (page.getLayout() != null)
			label.setText(""+page.getLayout().getHeight()+"px");
		else
			label.setText("[not set]");
		
		//Comments
		if (metadata.getComments() != null && !metadata.getComments().isEmpty()) {
			label = new Label(shell, SWT.NONE);
			label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, true, false, 2, 1));
			label.setText("Comments:");
	 
			label = new Label(shell, SWT.NONE);
			label.setLayoutData(new GridData(SWT.FILL, GridData.VERTICAL_ALIGN_CENTER, true, true, 2, 1));
			label.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
			label.setText(metadata.getComments());
		}
    }
}
