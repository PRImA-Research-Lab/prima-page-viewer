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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.primaresearch.dla.page.layout.physical.ContentObject;
import org.primaresearch.dla.page.layout.physical.text.TextObject;
import org.primaresearch.shared.variable.VariableMap;
import org.primaresearch.shared.variable.VariableValue;

/**
 * Tooltip for page content objects (shows ID, type, and attributes) 
 * 
 * @author Christian Clausner
 *
 */
public class PageElementTooltip {
	private Shell tipShell;
	private Label tipHeadline;
	private Label tipLeftColumn;
	private Label tipRightColumn;
	private Label tipBottomHeading;
	private Label tipBottom;
	private Point tipPosition; // the position being hovered over
	
	private static Font textContentFont = null;
	private static Font smallFont = null;
	private static Font headlineFont = null;
	
	
	/**
	 * Creates a new tooltip handler
	 *
	 * @param parent the parent Shell
	 */
	public PageElementTooltip(Shell parent) {
		final Display display = parent.getDisplay();
		//this.parentShell = parent;
		tipShell = new Shell(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 2;
		gridLayout.marginHeight = 2;
		tipShell.setLayout(gridLayout);

		tipShell.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

		tipHeadline = new Label(tipShell, SWT.NONE);
		tipHeadline.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipHeadline.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipHeadline.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, false, false, 2, 1));
		tipHeadline.setFont(getHeadlineFont(parent));

		tipLeftColumn = new Label(tipShell, SWT.NONE);
		tipLeftColumn.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipLeftColumn.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipLeftColumn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		
		tipRightColumn = new Label(tipShell, SWT.NONE);
		tipRightColumn.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipRightColumn.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipRightColumn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		
		tipBottomHeading = new Label(tipShell, SWT.NONE);
		tipBottomHeading.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		tipBottomHeading.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		tipBottomHeading.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, false, false, 2, 1));

		tipBottom = new Label(tipShell, SWT.NONE);
		tipBottom.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		tipBottom.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		tipBottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL , GridData.VERTICAL_ALIGN_CENTER, false, false, 2, 1));
		tipBottom.setFont(getTextContentFont(parent));
	}
	
	public void dispose() {
		tipShell.dispose();
	}
	
	/**
	 * Returns font object (creates it if not done already)
	 */
	private synchronized Font getSmallFont(Shell parent) {
		if (smallFont == null) {
			smallFont = new Font(parent.getDisplay(),"Arial",2,SWT.BOLD); 
		}
		return smallFont;
	}
	
	/**
	 * Returns font object (creates it if not done already)
	 */
	private synchronized Font getHeadlineFont(Shell parent) {
		if (headlineFont == null) {
			headlineFont = new Font(parent.getDisplay(),"Arial",12,SWT.BOLD); 
		}
		return headlineFont;
	}

	/**
	 * Returns font object using 'Aletheia Sans' (creates it if not done already)
	 */
	private synchronized Font getTextContentFont(Shell parent) {
		if (textContentFont == null) {
			//Get font resource and save to temp file
			
			try {
				String filePath = System.getProperty("java.io.tmpdir") + File.separator + "aletheiaSans" + Long.toString(System.nanoTime())+".ttf";
				InputStream in = this.getClass().getResourceAsStream("/org/primaresearch/page/viewer/ui/res/AletheiaSans.ttf");
				
				File f = new File(filePath);
				try {
					Files.copy(in, f.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
				finally {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				//Use temp file for SWT font
				parent.getDisplay().loadFont(filePath);
				f.deleteOnExit();
				textContentFont = new Font(parent.getDisplay(),"Aletheia Sans",12,SWT.NORMAL); 
			} catch(Exception exc) {
				exc.printStackTrace();
			}
		}
		return textContentFont;
	}
	
//	private Font getAletheiaSans() {
//		if (aletheiaSans == null) {
//			aletheiaSans = Font.
//		}
//		return aletheiaSans;
//	}
	
	/**
	 * Displays the tooltip at the mouse cursor position
	 * @param event Mouse hover event 
	 * @param pageElement Page element underneath the mouse cursor
	 */
	public void onMouseHover(MouseEvent event, ContentObject pageElement) {
		Point pt = new Point (event.x, event.y);
		Widget widget = event.widget;
		if (widget == null) {
			tipShell.setVisible(false);
			return;
		}

		tipPosition = ((Control)event.getSource()).toDisplay(pt);
		
		String headline = "";
		String left = "";
		String right = "";
		if (pageElement != null) {
			headline += /*pageElement.getName()*/ pageElement.getType().getName() + " " + pageElement.getId();
			VariableMap attrs = pageElement.getAttributes();
			if (attrs != null && attrs.getSize()>0) {
				//attrs.sort();
				for (int i=0; i<attrs.getSize(); i++) {
					VariableValue val = attrs.get(i).getValue();
					if (val != null) {
						String name = attrs.get(i).getName();
						left += "\n" + name + ":";
						right += "\n" + val.toString();
					}
				}
			}
		}
		String textContent = "";
		if (pageElement instanceof TextObject) {
			textContent = ((TextObject)pageElement).getText();
		}
		
		tipHeadline.setText(headline);
		tipLeftColumn.setText(left);
		tipRightColumn.setText(right);
		boolean hasText = textContent != null && !textContent.isEmpty();
		tipBottomHeading.setText(hasText ? "Text content:" : "");
		tipBottomHeading.setFont(hasText ? tipLeftColumn.getFont() : getSmallFont(tipShell)); //CC: Workaround to hide the label when empty
		tipBottom.setText(textContent != null ? textContent : "");
		tipBottom.setFont(hasText ? getTextContentFont(tipShell) : getSmallFont(tipShell));  //CC: Workaround to hide the label when empty
		tipShell.pack();

		setHoverLocation(tipShell, tipPosition);
		tipShell.setVisible(true);
	}

	/**
	 * Enables customised hover help for a specified control
	 *
	 * @control the control on which to enable hoverhelp
	 */
	public void activateHoverHelp(final Control control) {
	}

	/**
	 * Sets the location for a hovering shell
	 * @param shell the object that is to hover
	 * @param position the position of a widget to hover over
	 * @return the top-left location for a hovering box
	 */
	private void setHoverLocation(Shell shell, Point position) {
		Rectangle displayBounds = shell.getDisplay().getBounds();
		Rectangle shellBounds = shell.getBounds();
		shellBounds.x = Math.max(Math.min(position.x, displayBounds.width - shellBounds.width), 0);
		shellBounds.y = Math.max(Math.min(position.y + 16, displayBounds.height - shellBounds.height), 0);
		shell.setBounds(shellBounds);
	}
	
	/**
	 * Shows/hides the tooltip
	 */
	public void setVisible(boolean vis) {
		tipShell.setVisible(vis);
	}
}