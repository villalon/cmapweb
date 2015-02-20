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
package cl.uai.client.dialogs;

import cl.uai.client.resources.Messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Interface to ask for a name for a new concept.
 * 
 * @author Jorge Villalon
 *
 */
public class AddConceptDialogBox extends DialogBox {
	
	protected String newValue = null;
	protected VerticalPanel vMainPanel = null;
	protected TextBox txtNewValue;
	protected Button btnCancel;
	protected Button btnAccept;
	protected HorizontalPanel hButtonsPanel;
	protected static Messages messages = GWT.create(Messages.class);

	public TextBox getTxtNewValue() {
		return txtNewValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public AddConceptDialogBox() {
		super(true, true);
		vMainPanel = new VerticalPanel();
		btnCancel = new Button(messages.getString("Cancel")); //$NON-NLS-1$
		btnCancel.setStylePrimaryName("btnCancel"); //$NON-NLS-1$
		btnAccept = new Button(messages.getString("Accept")); //$NON-NLS-1$
		btnAccept.setStylePrimaryName("btnAccept"); //$NON-NLS-1$
		txtNewValue = new TextBox();
		txtNewValue.setStylePrimaryName("txtNewValue"); //$NON-NLS-1$

		this.setHTML(messages.getString("AddConcept")); //$NON-NLS-1$
		vMainPanel.add(txtNewValue);
		hButtonsPanel = new HorizontalPanel();
		hButtonsPanel.setStylePrimaryName("hpanelAddConceptButtons"); //$NON-NLS-1$
		hButtonsPanel.add(btnAccept);
		hButtonsPanel.add(btnCancel);
		vMainPanel.add(hButtonsPanel);
		vMainPanel.setCellHorizontalAlignment(hButtonsPanel, HasAlignment.ALIGN_RIGHT);
		this.add(vMainPanel);
		
		txtNewValue.addValueChangeHandler(new ValueChangeHandler<String>() {			
			public void onValueChange(ValueChangeEvent<String> event) {
				if(!validateNewValue()) {
					Window.alert(messages.getString("InvalidConceptName")); //$NON-NLS-1$
				}
				onValueChanged(event.getValue());
			}
		});
		
		btnCancel.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				hide(true);
			}
		});
		
		btnAccept.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				onValueChanged(txtNewValue.getText());
			}
		});
	}
	
	protected void onValueChanged(String value) {
		newValue = value;
		hide(true);		
	}
	
	private boolean validateNewValue() {
		if(txtNewValue.getText() != null
				&& txtNewValue.getText().trim().length() > 0)
			return true;
		return false;
	}
}
