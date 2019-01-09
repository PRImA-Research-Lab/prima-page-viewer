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
package org.primaresearch.swt.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Toolbar with image buttons
 * 
 * @author Christian Clausner
 *
 */
public class ImageToolbar {
	ToolBar toolBar;
	SelectionListener selectionListener;
	Map<String, ImageToolbarButton> buttons = new HashMap<String, ImageToolbarButton>();

	/**
	 * Constructor
	 * @param parent Parent view container
	 * @param selectionListener Button listener
	 */
	public ImageToolbar(Composite parent, SelectionListener selectionListener) {
		this.selectionListener = selectionListener;
	    toolBar = new ToolBar(parent, SWT.HORIZONTAL);
	    toolBar.setLayoutData(new GridData(GridData.FILL, GridData.BEGINNING, true, false));
	}
	
	/**
	 * Releases resources
	 */
	public void dispose() {
		for (Iterator<ImageToolbarButton> it = buttons.values().iterator(); it.hasNext(); )
			it.next().dispose();
	}
	
	/**
	 * Adds a toolbar item
	 * @param type SWT item type (e.g. SWT.PUSH, SWT.RADIO, ...)
	 * @param text Label text
	 * @param actionCommand Action ID
	 * @param imageResPath Image source
	 * @param toolTipText Tooltip
	 */
	public void addItem(int type, String text, String actionCommand, String imageResPath, String toolTipText) {
		buttons.put(actionCommand, new ImageToolbarButton(toolBar, selectionListener, type, text, actionCommand, imageResPath, toolTipText));
	}
	
	/**
	 * Adds a push button (SWT.PUSH)
	 * @param text Label text
	 * @param actionCommand Action ID
	 * @param imageResPath Image source
	 * @param toolTipText Tooltip
	 */
	public void addPushButton(String text, String actionCommand, String imageResPath, String toolTipText) {
		buttons.put(actionCommand, new ImageToolbarButton(toolBar, selectionListener, SWT.PUSH, text, actionCommand, imageResPath, toolTipText));
	}

	/**
	 * Adds a check button (SWT.CHECK)
	 * @param text Label text
	 * @param actionCommand Action ID
	 * @param imageResPath Image source
	 * @param toolTipText Tooltip
	 */
	public ImageToolbarButton addCheckButton(String text, String actionCommand, String imageResPath, String toolTipText) {
		ImageToolbarButton btn = new ImageToolbarButton(toolBar, selectionListener, SWT.CHECK, text, actionCommand, imageResPath, toolTipText);
		buttons.put(actionCommand, btn);
		return btn;
	}

	/**
	 * Adds a radio button (SWT.RADIO)
	 * @param radioGroupId Radio group ID
	 * @param text Label text
	 * @param actionCommand Action ID
	 * @param imageResPath Image source
	 * @param toolTipText Tooltip
	 */
	public void addRadioButton(String radioGroupId, String text, String actionCommand, String imageResPath, String toolTipText) {
		ImageToolbarButton btn = new ImageToolbarButton(toolBar, selectionListener, SWT.RADIO, text, actionCommand, imageResPath, toolTipText);
		buttons.put(actionCommand, btn);
	}
	
	/**
	 * Adds a separator with a fixed width of 16 pixels.
	 */
	public void addSeparator() {
		ToolItem item = new ToolItem(toolBar, SWT.SEPARATOR);
		item.setWidth(16);
	}

	/**
	 * Enables/disables a button
	 */
	public void setEnabled(String buttonId, boolean enable) {
		if (buttons.containsKey(buttonId)) 
			buttons.get(buttonId).setEnabled(enable);
	}

	/**
	 * Checks(lowers)/unchecks(raises) a button
	 */
	public void setChecked(String buttonId, boolean check) {
		if (buttons.containsKey(buttonId)) 
			buttons.get(buttonId).getItem().setSelection(check);
	}

	/**
	 * Image button
	 * 
	 * @author Christian Clausner
	 *
	 */
	public static class ImageToolbarButton {
		private Image image = null;
		ToolItem item;
	
		public ImageToolbarButton(ToolBar toolBar, SelectionListener selectionListener, int type, String text, String actionCommand, 
								  String imageResPath, String toolTipText) {
			
		    try {
		    	image = new Image(toolBar.getDisplay(), getClass().getResourceAsStream(imageResPath));
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		    
		    item = new ToolItem(toolBar, type);
		    item.setText(text);
		    item.setImage(image);
		    item.setHotImage(null);
		    item.setToolTipText(toolTipText);
		    item.setData(actionCommand);
		    item.addSelectionListener(selectionListener);
		}
		
		public void dispose() {
			if (image != null)
				image.dispose();
		}

		public ToolItem getItem() {
			return item;
		}
		
		public void setEnabled(boolean enable) {
			item.setEnabled(enable);
		}
	}

}
