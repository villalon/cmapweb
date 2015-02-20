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

import org.vaadin.gwtgraphics.client.Line;
import org.vaadin.gwtgraphics.client.shape.Path;
import org.vaadin.gwtgraphics.client.shape.path.LineTo;

import cl.uai.client.cmap.Relationship;

/**
 * This class represents a relationship label in the view.
 * 
 * @author Jorge Villalon
 *
 */
public class RelationshipLabel extends ConceptLabel {
	
	// The relationship to which this label belongs
	private Relationship relationship;
	// The relationship source line
	private Line srcLine = null;
	// The relationship target line
	private Line tgtLine = null;
	// The arrow for the target Line
	private Path tgtArrow = null;
	// The concept map view to which the label belongs
	private ConceptMapView cmapview = null;

	/**
	 * @param label
	 */
	public RelationshipLabel(ConceptMapView cview, Relationship rel) {
		super(rel.getLinkingWord());
		this.relationship = rel;
		this.cmapview = cview;
		
		srcLine = new Line(0,0,0,0);
		srcLine.setStyleName("srcRelationship");
		tgtLine = new Line(0,0,0,0);
		tgtLine.setStyleName("tgtRelationship");
		tgtArrow = new Path(0, 0);
		tgtArrow.lineRelativelyTo(4, 16);
		tgtArrow.lineRelativelyTo(-8, 0);
		tgtArrow.close();
		tgtArrow.setStyleName("tgtRelationship");
	}
	/**
	 * @return the srcLine
	 */
	public Line getSrcLine() {
		return srcLine;
	}
	
	/**
	 * @return the tgtLine
	 */
	public Line getTgtLine() {
		return tgtLine;
	}
	
	public Path getTgtArrow() {
		return tgtArrow;
	}


	/**
	 * Re-calculates the coordinates for the lines from the source concept
	 * to the linking word and then to the target concept
	 */
	public void drawAllLines() {
		// Coordinates for label 1 corner and size (w,h)
		int x1 = this.relationship.getSource().getPosx();
		int y1 = this.relationship.getSource().getPosy();
		int w1 = this.cmapview.getConceptLabels().get(this.relationship.getSource().getId()).getOffsetWidth();
		int h1 = this.cmapview.getConceptLabels().get(this.relationship.getSource().getId()).getOffsetHeight();

		// Coordinates for label 2 corner and size (w,h)
		int x2 = this.relationship.getPosx();
		int y2 = this.relationship.getPosy();
		int w2 = super.getOffsetWidth();
		int h2 = super.getOffsetHeight();

		// Coordinates for label 2 corner and size (w,h)
		int x3 = this.relationship.getTarget().getPosx();
		int y3 = this.relationship.getTarget().getPosy();
		int w3 = this.cmapview.getConceptLabels().get(this.relationship.getTarget().getId()).getOffsetWidth();
		int h3 = this.cmapview.getConceptLabels().get(this.relationship.getTarget().getId()).getOffsetHeight();

		// Calculating centers for all labels
		int cx1 = x1 + w1 / 2;
		int cy1 = y1 + h1 / 2;
		int cx2 = x2 + w2 / 2;
		int cy2 = y2 + h2 / 2;
		int cx3 = x3 + w3 / 2;
		int cy3 = y3 + h3 / 2;

		// Calculating angles for arrow line and target concept proportion
		double lineAngle = Math.atan2(((double)cy3 - (double)cy2),((double)cx3-(double)cx2));
		double targetAngle = Math.atan2(h3, w3);

		int arrowx = 0;
		int arrowy = 0;

		// Depending on the quadrant calculate the arrow's coordinates
		if(lineAngle >= - targetAngle && lineAngle < targetAngle) {
			arrowx = x3;
			arrowy = (int) ((double)cy2 + ((double) x3 - (double) cx2) * Math.tan(lineAngle));
		} else if (lineAngle >= targetAngle && lineAngle < Math.PI - targetAngle) {
			arrowy = y3;
			arrowx = (int) ((double)cx2 + ((double) y3 - (double) cy2) / Math.tan(lineAngle));
		} else if ((lineAngle >= Math.PI - targetAngle && lineAngle <= Math.PI) || (lineAngle >= - Math.PI && lineAngle < - Math.PI + targetAngle)) {
			arrowx = x3 + w3;
			arrowy = (int) ((double)cy2 + ((double) x3 + (double) w3 - (double) cx2) * Math.tan(lineAngle));
		} else {
			arrowy = y3 + h3;
			arrowx = (int) ((double)cx2 + ((double) y3 + (double) h3 - (double) cy2) / Math.tan(lineAngle));
		}

		// Calculating coordinates for the arrow points based on size and angle (d and angle)
		double arrowSize = 8;
		double arrowAngle = Math.PI / 6;
		double h = Math.abs(arrowSize/Math.cos(arrowAngle));;
		double angle1 = lineAngle + Math.PI + arrowAngle;
		int topx = (int) (arrowx + Math.cos(angle1) * h);
		int topy = (int) (arrowy + Math.sin(angle1) * h);
		double angle2 = lineAngle + Math.PI - arrowAngle;
		int botx = (int) (arrowx + Math.cos(angle2) * h);
		int boty = (int) (arrowy + Math.sin(angle2) * h);

		

		// Draw line from source concept to relationship
		srcLine.setX1(cx1);
		srcLine.setY1(cy1);
		srcLine.setX2(cx2);
		srcLine.setY2(cy2);
		
		// Draw line from relationship to target concept
		tgtLine.setX1(cx2);
		tgtLine.setY1(cy2);
		tgtLine.setX2(arrowx);
		tgtLine.setY2(arrowy);
		
		// Draw arrow head on target relationship
		tgtArrow.setX(arrowx);
		tgtArrow.setY(arrowy);
		tgtArrow.setStep(1,new LineTo(false, topx, topy));
		tgtArrow.setStep(2,new LineTo(false, botx, boty));
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		drawAllLines();
	}
}
