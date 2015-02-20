// This file is part of Moodle - http://moodle.org/
//
// Moodle is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// Moodle is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with Moodle.  If not, see <http://www.gnu.org/licenses/>.

/**
 * Strings for component 'block_news_items', language 'en', branch 'MOODLE_20_STABLE' 
*
* @package   block_news_items
* @copyright 2011 onwards Jorge Villalon {@link http://villalon.cl}
* @license   http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
*/
package cl.uai.client;

import cl.uai.client.cmap.ConceptMap;
import cl.uai.client.resources.Messages;
import cl.uai.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * @author Jorge Villalon
 *
 */
public class ConceptMapHeader extends Composite {
	/** The CM interactive title */
	private ConceptMapTitle lblConceptMapTitle;	
	private boolean readOnly = false;
	private Messages messages = GWT.create(Messages.class);
	private ConceptMapView cmapView = null;
	private HorizontalPanel hButtonsPanel;
	private HorizontalPanel hTitlePanel;
	private boolean undoEnabled = false;
	private boolean redoEnabled = false;
	private PushButton btnExportXml;
	private PushButton btnUndo;
	private PushButton btnRedo;
	private PushButton btnNewConceptMap;
	private PushButton btnHelp;
	private PushButton btnAddConcept;
	private PushButton btnAddRelationship;
	
	/**
	 * @return the undoEnabled
	 */
	public boolean isUndoEnabled() {
		return undoEnabled;
	}

	/**
	 * @param undoEnabled the undoEnabled to set
	 */
	public void setUndoEnabled(boolean undoEnabled) {
		this.undoEnabled = undoEnabled;
		this.btnUndo.setEnabled(undoEnabled);
	}

	/**
	 * @return the redoEnabled
	 */
	public boolean isRedoEnabled() {
		return redoEnabled;
	}

	/**
	 * @param redoEnabled the redoEnabled to set
	 */
	public void setRedoEnabled(boolean redoEnabled) {
		this.redoEnabled = redoEnabled;
		this.btnRedo.setEnabled(redoEnabled);
	}

	public ConceptMapHeader(ConceptMapView cview, boolean isReadOnly) {
		this.cmapView = cview;
		this.readOnly = isReadOnly;
		
		btnAddConcept = new PushButton(new Image(Resources.INSTANCE.newconcept()));
		//btnExportXml.setStylePrimaryName("btnExportXml"); //$NON-NLS-1$
		btnAddConcept.setTitle(messages.getString("AddConcept")); //$NON-NLS-1$
		btnAddConcept.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				cmapView.setInAddConceptMode(true);
			}
		});

		btnAddRelationship = new PushButton(new Image(Resources.INSTANCE.newrelationship()));
		//btnExportXml.setStylePrimaryName("btnExportXml"); //$NON-NLS-1$
		btnAddRelationship.setTitle(messages.getString("AddRelationship")); //$NON-NLS-1$
		btnAddRelationship.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				cmapView.setInAddRelationshipMode(true);
			}
		});

		btnExportXml = new PushButton(new Image(Resources.INSTANCE.exportxml()));
		//btnExportXml.setStylePrimaryName("btnExportXml"); //$NON-NLS-1$
		btnExportXml.setTitle(messages.getString("ExportXML")); //$NON-NLS-1$
		btnExportXml.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				DialogBox box = new DialogBox(true, true);
				box.setText(URL.decode(cmapView.exportXml()));
				box.setTitle("XML");
				box.setWidth("640px");
				box.setHeight("480px");
				box.show();
			}
		});

		btnUndo = new PushButton(new Image(Resources.INSTANCE.undo()));
		//btnUndo.setStylePrimaryName("btnUndo"); //$NON-NLS-1$
		btnUndo.setTitle(messages.getString("Undo")); //$NON-NLS-1$
		btnUndo.setEnabled(false);
		btnUndo.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				cmapView.undo();
			}
		});

		btnRedo = new PushButton(new Image(Resources.INSTANCE.redo()));
		//btnRedo.setStylePrimaryName("btnRedo"); //$NON-NLS-1$
		btnRedo.setTitle(messages.getString("Redo")); //$NON-NLS-1$
		btnRedo.setEnabled(false);
		btnRedo.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				cmapView.redo();
			}
		});

		btnNewConceptMap = new PushButton(new Image(Resources.INSTANCE.newmap()));
		//btnNewConceptMap.setStylePrimaryName("btnNewConceptMap"); //$NON-NLS-1$
		btnNewConceptMap.setTitle(messages.getString("NewConceptMap")); //$NON-NLS-1$
		btnNewConceptMap.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				if(!Window.confirm(messages.getString("ChangesWillBeLost"))) //$NON-NLS-1$
					return;
				ConceptMap conceptMap = new ConceptMap();
				cmapView.setCmap(conceptMap);
				lblConceptMapTitle.setCmap(conceptMap);
			}
		});

		btnHelp = new PushButton(new Image(Resources.INSTANCE.help()));
		//btnHelp.setStylePrimaryName("btnHelp"); //$NON-NLS-1$
		btnHelp.setTitle(messages.getString("HelpButton")); //$NON-NLS-1$
		btnHelp.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				cmapView.showHelp();
			}
		});

		lblConceptMapTitle = new ConceptMapTitle(cview.getCmap(), isReadOnly);
		
		hTitlePanel = new HorizontalPanel();
		hTitlePanel.setStylePrimaryName("titlePanel"); //$NON-NLS-1$
		hTitlePanel.add(lblConceptMapTitle);

		hButtonsPanel = new HorizontalPanel();
		hButtonsPanel.add(btnNewConceptMap);
		hButtonsPanel.add(btnAddConcept);
		//hButtonsPanel.add(btnAddRelationship);
		hButtonsPanel.add(btnUndo);
		hButtonsPanel.add(btnRedo);
		//hButtonsPanel.add(btnExportXml);
		hButtonsPanel.add(btnHelp);

		hTitlePanel.add(hButtonsPanel);
		hTitlePanel.setCellHorizontalAlignment(hButtonsPanel, HasAlignment.ALIGN_RIGHT);
		// If it is not int readonly mode, add the buttons.
		hButtonsPanel.setVisible(!readOnly);
		
		initWidget(hTitlePanel);
	}

	public void setConceptMap(ConceptMap cmap) {
		this.lblConceptMapTitle.setCmap(cmap);
	}
	
	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
		hButtonsPanel.setVisible(!readOnly);		
		lblConceptMapTitle.setReadOnly(readOnly);
	}

	/**
	 * @return the lblConceptMapTitle
	 */
	public ConceptMapTitle getLblConceptMapTitle() {
		return lblConceptMapTitle;
	}
}
