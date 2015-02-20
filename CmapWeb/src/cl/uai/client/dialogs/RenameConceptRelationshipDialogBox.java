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

/**
 * Dialog box to rename a concept or relationship.
 * 
 * @author Jorge Villalon
 *
 */
public class RenameConceptRelationshipDialogBox extends AddConceptDialogBox {

	private String oldValue = null;
	private boolean valueChanged = false;
	
	public String getOldValue() {
		return oldValue;
	}

	public boolean isValueChanged() {
		return valueChanged;
	}

	public RenameConceptRelationshipDialogBox(String concept) {
		super();
		oldValue = concept;
		txtNewValue.setText(oldValue);		

		this.setHTML(messages.getString("RenameConcept")); //$NON-NLS-1$
	}
	
	@Override
	protected void onValueChanged(String value) {
		newValue = value;
		valueChanged = !newValue.equals(oldValue);
		hide(true);
	}
}
