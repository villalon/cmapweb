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
package cl.uai.client.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cl.uai.client.ConceptMapView;
import cl.uai.client.cmap.Concept;
import cl.uai.client.cmap.Relationship;

/**
 * Delete concept command
 * 
 * @author Jorge Villalon
 *
 */
public class DeleteConceptCommand extends AbstractConceptMapCommand {

	private String label;
	private List<Relationship> incomingRelationships;
	private List<Relationship> outgoingRelationships;
	/**
	 * @param name
	 * @param parameters
	 * @param cmap
	 */
	public DeleteConceptCommand(Map<String, Object> parameters) {
		super("Delete concept", parameters);
		this.id = (Integer) parameters.get("id");
		this.label = (String) parameters.get("label");
		this.posx = (Integer) parameters.get("posx");
		this.posy = (Integer) parameters.get("posy");
		this.outgoingRelationships = new ArrayList<Relationship>();
		this.incomingRelationships = new ArrayList<Relationship>();
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.ConceptMapCommand#execute()
	 */
	@Override
	public void execute(ConceptMapView cview) {
		this.cmapview = cview;
		Concept c = this.cmapview.getCmap().getConcept(id);
		this.incomingRelationships.addAll(this.cmapview.getCmap().incomingRelationships(c));
		this.outgoingRelationships.addAll(this.cmapview.getCmap().outgoingRelationships(c));
		this.cmapview.deleteConceptAndLabel(this.id);
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.ConceptMapCommand#undo()
	 */
	@Override
	public void undo() {
		Concept c = this.cmapview.insertConceptAndLabel(id, label, posx, posy);
		for(Relationship rel : this.incomingRelationships) {
			this.cmapview.insertRelationshipAndLabel(rel.getId(), rel.getSource(), c, rel.getLinkingWord(), rel.getDrawingType(), rel.getPosx(), rel.getPosy());
		}
		for(Relationship rel : this.outgoingRelationships) {
			this.cmapview.insertRelationshipAndLabel(rel.getId(), c, rel.getTarget(), rel.getLinkingWord(), rel.getDrawingType(), rel.getPosx(), rel.getPosy());
		}
	}
	
	@Override
	public String toString() {
		return name + " " + label + " " + id + " (" + posx + "," + posy + ")";
	}
}
