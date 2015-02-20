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

import cl.uai.client.resources.Messages;
import cl.uai.client.resources.Resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;

/**
 * The set of buttons shown when the mouse is over a concept or linking word for editing them.
 * 
 * @author Jorge Villalon
 *
 */
public class ConceptEditButtons extends HorizontalPanel {
	
	private Messages messages = GWT.create(Messages.class);
	private PushButton btnEdit;
	private PushButton btnDelete;
	private PushButton btnLink;
	
	/**
	 * 
	 */
	public ConceptEditButtons() {
		this.btnDelete = new PushButton(new Image(Resources.INSTANCE.delete()));
		this.btnEdit = new PushButton(new Image(Resources.INSTANCE.edit()));
		this.btnLink = new PushButton(new Image(Resources.INSTANCE.newrelationship()));
		
		this.btnDelete.setTitle(messages.Delete());
		this.btnEdit.setTitle(messages.Edit());
		this.btnLink.setTitle(messages.AddRelationship());
		
		this.btnEdit.setStyleName("btnEditConcept");
		this.btnDelete.setStyleName("btnEditConcept");
		this.btnLink.setStyleName("btnEditConcept");
		
		this.add(this.btnEdit);
		this.add(this.btnLink);
		this.add(this.btnDelete);
	}

	public PushButton getEditButton() {
		return btnEdit;
	}

	public PushButton getDeleteButton() {
		return btnDelete;
	}

	public PushButton getLinkButton() {
		return btnLink;
	}
	
	public void setConceptLabel(ConceptLabel lbl) {
		if(lbl instanceof RelationshipLabel) {
			btnLink.setVisible(false);
		} else {
			btnLink.setVisible(true);
		}
	}
}
