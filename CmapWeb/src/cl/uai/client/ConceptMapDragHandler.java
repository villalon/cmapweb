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

import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import cl.uai.client.cmap.Concept;
import cl.uai.client.cmap.Relationship;
import cl.uai.client.commands.AddRelationshipCommand;
import cl.uai.client.commands.MoveConceptCommand;
import cl.uai.client.commands.MoveRelationshipCommand;
import cl.uai.client.dialogs.AddRelationshipDialogBox;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Class implementing the drag and drop behavior of Concepts and Relationships
 * @author Jorge Villalon
 *
 */
public class ConceptMapDragHandler implements DragHandler {

	private static Logger logger = Logger.getLogger(ConceptMapDragHandler.class.getName());
	private ConceptMapView cmapview;
	
	public ConceptMapDragHandler(ConceptMapView conceptMapView) {
		this.cmapview = conceptMapView;
	}

	/**
	 * When an object is dropped
	 */
	public void onDragEnd(DragEndEvent event) {
		//Get the label that was dropped and find if it is a concept or relationship
		ConceptLabel conceptLabel = (ConceptLabel) event.getSource();
		Concept concept = cmapview.getConceptFromLabel(conceptLabel);
		Relationship relationship = cmapview.getRelationshipFromLabel(conceptLabel);

		if(concept == null && relationship == null) {
			logger.severe("Problems! Label with no concept or relationship found");
			return;
		}

		int x = conceptLabel.getAbsoluteLeft() - cmapview.getBoundaryPanel().getAbsoluteLeft();
		int y = conceptLabel.getAbsoluteTop() - cmapview.getBoundaryPanel().getAbsoluteTop();
		
		// Adjust to grid
		/*x = x - (x % gridSize) + 1;
		y = y - (y % gridSize) + 1;*/

		// If it a concept being moved
		if(concept != null) {
			int dx = Math.abs(x - concept.getPosx());
			int dy = Math.abs(y - concept.getPosy());
			if(dx < 5 && dy < 5) {
				cmapview.getBoundaryPanel().setWidgetPosition(conceptLabel, x, y);
				return;
			}
			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("id", concept.getId());
			params.put("posx", x);
			params.put("posy", y);
			MoveConceptCommand command = new MoveConceptCommand(params);
			cmapview.executeCommand(command);
		} else {
			// Otherwise it is a relationship
			int dx = Math.abs(x - relationship.getPosx());
			int dy = Math.abs(y - relationship.getPosy());
			if(dx < 5 && dy < 5) {
				cmapview.getBoundaryPanel().setWidgetPosition(conceptLabel, x, y);
				return;
			}
			Map<String, Object> params = new TreeMap<String, Object>();
			params.put("id", relationship.getId());
			params.put("posx", x);
			params.put("posy", y);
			MoveRelationshipCommand command = new MoveRelationshipCommand(params);
			cmapview.executeCommand(command);
		}
	}

	/**
	 * When the user starts dragging something, the edit label should dissapear.
	 */
	public void onDragStart(DragStartEvent event) {
		cmapview.removeConceptEditButtons();
	}

	/**
	 * Before an object is dropped, we need to check if there's a concept
	 * being dropped on top of another concept, in which case
	 * a relationship will be created.
	 */
	public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
		// Concept being dragged (source concept)
		ConceptLabel conceptLabel = (ConceptLabel) event.getSource();
		final Concept concept = cmapview.getConceptFromLabel(conceptLabel);
		if(concept == null)
			return;
		
		// Concept on top of which we are dropping the source (target concept)
		final Concept concept2 = cmapview.overlappingConcept(concept);
		
		if(concept2 != null) {
			AddRelationshipDialogBox dbox = new AddRelationshipDialogBox();
			dbox.addCloseHandler(new CloseHandler<PopupPanel>() {							
				public void onClose(CloseEvent<PopupPanel> event) {
					AddRelationshipDialogBox dbox = (AddRelationshipDialogBox) event.getSource();
					if(dbox.getNewValue() != null) {
						Map<String, Object> params = new TreeMap<String, Object>();
						params.put("sourceId", concept.getId());
						params.put("targetId", concept2.getId());
						params.put("linkingWord", dbox.getNewValue());
						AddRelationshipCommand command = new AddRelationshipCommand(params);
						cmapview.executeCommand(command);
					}
				}
			});
			dbox.showRelativeTo(cmapview.getConceptLabels().get(concept2.getId()));
			dbox.getTxtNewValue().setFocus(true);
			throw new VetoDragException();
		}
	}

	public void onPreviewDragStart(DragStartEvent event)
	throws VetoDragException {
	}

}
