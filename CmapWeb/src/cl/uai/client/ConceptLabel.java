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

import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * Represents a Label in the CM viewer. It is a piece of HTML
 * to have a better interface (round corners) for labels.
 * 
 * @author Jorge Villalon
 *
 */
public class ConceptLabel extends HTML {

	//	private static Logger logger = Logger.getLogger(ConceptLabel.class.getName());
	private static String htmlPrefix = "<table class=cmapLabelTable><tr><td class=cmCornerTopLeft></td><td class=cmTopCenter></td><td class=cmCornerTopRight></td></tr><tr><td class=cmLeft></td><td class=cmCenter>";
	private static String htmlSuffix = "</td><td class=cmRight></td></tr><tr><td class=cmCornerBottomLeft></td><td class=cmBottomCenter></td><td class=cmCornerBottomRight></td></tr></table>";
	private static boolean readonly = false;
	private static AbsolutePanel absolutePanel = null;
	private static ConceptLabel selectedLabel = null;
	private String label = null;

	public static ConceptLabel getSelectedLabel() {
		return selectedLabel;
	}

	public static void setReadonly(boolean readOnly) {
		readonly = readOnly;
	}

	public static void setAbsolutePanel(AbsolutePanel panel) {
		absolutePanel = panel;
	}

	/**
	 * Creates a Concept label and adds its handler
	 * @param label
	 */
	public ConceptLabel(String label) {
		super(htmlPrefix + label + htmlSuffix);

		this.setLabel(label);
		// Mouse over to show the edit label on top of the concept
		this.addMouseOverHandler(new MouseOverHandler() {
			public void onMouseOver(MouseOverEvent event) {
				selectedLabel = (ConceptLabel) event.getSource();

				if(absolutePanel!=null && !readonly) {
					ConceptMapView.getConceptEditButtons().setConceptLabel(selectedLabel);
					int x = getX() + getOffsetWidth() - ConceptMapView.getConceptEditButtons().getOffsetWidth();
					int y = getY() - ConceptMapView.getConceptEditButtons().getOffsetHeight();
					if(absolutePanel.getWidgetIndex(ConceptMapView.getConceptEditButtons())<0) {
						absolutePanel.add(ConceptMapView.getConceptEditButtons(), x, y);
					} else {
						absolutePanel.setWidgetPosition(ConceptMapView.getConceptEditButtons(), x, y);
					}
				}
			}
		});
	}

	@Override
	public void setText(String text) {
		super.setHTML(htmlPrefix + text + htmlSuffix);
	}
	
	public int getCenterX() {
		return this.getX() + this.getElement().getClientWidth() / 2;
	}
	
	public int getCenterY() {
		return this.getY() + this.getElement().getClientHeight() / 2;
	}
	
	public int getX() {
		return this.getAbsoluteLeft() - absolutePanel.getAbsoluteLeft();
	}
	
	public int getY() {
		return this.getAbsoluteTop() - absolutePanel.getAbsoluteTop();
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
		this.setText(label);
	}
}
