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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Class representing the CM's title, that allows to interact
 * by changing its value with a double click.  
 * 
 * @author Jorge Villalon
 *
 */
public class ConceptMapTitle extends Composite {

	/** The panel for the title */
	private HorizontalPanel hTitlePanel;
	/** The label for the title */
	private Label lblTitle;
	/** Textbox to change the title */
	private TextBox txtTitle;
	/** The CM the title is being shown */
	private ConceptMap cmap;
	/** If the interface is read only */
	private boolean readOnly = false;
	/** Localized messages */
	private Messages messages = GWT.create(Messages.class);
	private Button btnSave;
	private Button btnCancel;
	private Label lblChangeTitle;

	/**
	 * Changes the CM title
	 */
	private void setCmTitle(String title) {
		this.lblTitle.setText(title);
		this.lblTitle.setVisible(true);
		this.lblChangeTitle.setVisible(true);
		this.txtTitle.setVisible(false);
		this.btnSave.setVisible(false);
		this.btnCancel.setVisible(false);
		this.cmap.setTitle(title);
	}

	/**
	 * Creates a new CM title interface.
	 * 
	 * @param conceptmap the CM
	 * @param isReadOnly if the interface will be read only
	 */
	public ConceptMapTitle(ConceptMap conceptmap, boolean isReadOnly) {
		this.readOnly = isReadOnly;

		this.cmap = conceptmap;
		if(conceptmap == null)
			return;

		hTitlePanel = new HorizontalPanel();

		lblTitle = new Label(cmap.getTitle());
		lblTitle.setStylePrimaryName("cmapTitle"); //$NON-NLS-1$
		if(!isReadOnly)
		lblTitle.setTitle(messages.getString("DoubleClickChangeTitle")); //$NON-NLS-1$
		lblTitle.addDoubleClickHandler(new DoubleClickHandler() {			
			public void onDoubleClick(DoubleClickEvent event) {
				if(!readOnly)
					editTitle();
			}
		});

		lblChangeTitle = new Label(messages.getString("Change"));
		lblChangeTitle.setStylePrimaryName("lblChangeTitle");
		lblChangeTitle.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				editTitle();
			}
		});

		txtTitle = new TextBox();
		txtTitle.setVisible(false);
		txtTitle.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				setCmTitle(txtTitle.getText());
			}
		});

		btnSave = new Button(messages.getString("Save"));
		btnSave.setVisible(false);
		btnSave.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setCmTitle(txtTitle.getText());				
			}
		});

		btnCancel = new Button(messages.getString("Cancel"));
		btnCancel.setVisible(false);
		btnCancel.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				setCmTitle(lblTitle.getText());				
			}
		});


		hTitlePanel.add(lblTitle);
		hTitlePanel.add(lblChangeTitle);
		lblChangeTitle.setVisible(!isReadOnly);
		hTitlePanel.add(txtTitle);
		hTitlePanel.add(btnSave);
		hTitlePanel.add(btnCancel);

		hTitlePanel.setCellVerticalAlignment(btnSave, HasAlignment.ALIGN_MIDDLE);
		hTitlePanel.setCellVerticalAlignment(btnCancel, HasAlignment.ALIGN_MIDDLE);
		hTitlePanel.setCellVerticalAlignment(lblChangeTitle, HasAlignment.ALIGN_BOTTOM);

		initWidget(hTitlePanel);
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
		lblChangeTitle.setVisible(!readOnly);
	}

	/**
	 * Changes interface for editing mode
	 */
	private void editTitle() {
		lblTitle.setVisible(false);
		lblChangeTitle.setVisible(false);
		txtTitle.setVisible(true);
		txtTitle.setText(lblTitle.getText());
		txtTitle.setFocus(true);
		txtTitle.selectAll();
		btnSave.setVisible(true);
		btnCancel.setVisible(true);
	}

	/**
	 * @return the cmap
	 */
	public ConceptMap getCmap() {
		return cmap;
	}

	/**
	 * @param cmap the cmap to set
	 */
	public void setCmap(ConceptMap cmap) {
		this.cmap = cmap;
		this.lblTitle.setText(this.cmap.getTitle());
	}
}
