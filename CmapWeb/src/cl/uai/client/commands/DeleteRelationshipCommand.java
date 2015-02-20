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

import java.util.Map;

import cl.uai.client.ConceptMapView;
import cl.uai.client.cmap.Concept;
import cl.uai.client.cmap.Relationship;
import cl.uai.client.cmap.Relationship.Drawing;

/**
 * @author Jorge Villalon
 *
 */
public class DeleteRelationshipCommand extends AbstractConceptMapCommand {

	private int id=-1;
	private String linkingWord;
	private int posx;
	private int posy;
	private int sourceId;
	private int targetId;
	
	/**
	 * @param name
	 * @param parameters
	 * @param cmap
	 */
	public DeleteRelationshipCommand(Map<String, Object> parameters) {
		super("Delete relationship", parameters);
		this.id = (Integer) parameters.get("id");
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.ConceptMapCommand#execute()
	 */
	@Override
	public void execute(ConceptMapView cview) {
		this.cmapview = cview;
		Relationship rel = this.cmapview.getCmap().getRelationship(id);
		this.linkingWord = rel.getLinkingWord();
		this.posx = rel.getPosx();
		this.posy = rel.getPosy();
		this.sourceId = rel.getSource().getId();
		this.targetId = rel.getTarget().getId();
		this.cmapview.deleteRelationshipAndLabel(id);
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.ConceptMapCommand#undo()
	 */
	@Override
	public void undo() {
		Concept c = this.cmapview.getCmap().getConcept(sourceId);
		Concept c2 = this.cmapview.getCmap().getConcept(targetId);
		this.cmapview.insertRelationshipAndLabel(id, c, c2, linkingWord, Drawing.LINE, posx, posy);
	}
	
	@Override
	public String toString() {
		return name + " " + id + " " + sourceId + "-" + linkingWord + "->" + targetId + " (" + posx + "," + posy + ")";
	}
}
