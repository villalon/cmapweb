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
package cl.uai.client.cmap;

/**
 * Class that represents a relationship in the CM
 * 
 * @author Jorge Villalon
 *
 */
public class Relationship {

	/**
	 * Possibilites for drawing relationships. 
	 */
	public enum Drawing {
		/** Draw a straight line */
		LINE,
		/** Draw a curve */
		CURVE
	}

	/** Relationship id */
	private int id;
	/** Source concept */
	private Concept source;
	/** Target concept */
	private Concept target;
	/** Linking word */
	private String linkingWord;
	/** How the relationship lines will be drawn */
	private Drawing drawingType;
	/** X coordinate in the CM */
	private int posx;
	/** Y coordinate in the CM */
	private int posy;
	
	
	/**
	 * @return the source concept
	 */
	public Concept getSource() {
		return source;
	}
	/**
	 * @param source the new source concept to set
	 */
	public void setSource(Concept source) {
		this.source = source;
	}
	/**
	 * @return the target concept
	 */
	public Concept getTarget() {
		return target;
	}
	/**
	 * @param target the target concept to set
	 */
	public void setTarget(Concept target) {
		this.target = target;
	}
	/**
	 * @return the linking word
	 */
	public String getLinkingWord() {
		return linkingWord;
	}
	/**
	 * @param linkingWord the new linking word to set
	 */
	public void setLinkingWord(String linkingWord) {
		this.linkingWord = linkingWord;
	}
	/**
	 * @return the drawing type for lines
	 */
	public Drawing getDrawingType() {
		return drawingType;
	}
	/**
	 * @param drawingType the new drawing type for lines
	 */
	public void setDrawingType(Drawing drawingType) {
		this.drawingType = drawingType;
	}
	/**
	 * @return the X coordinate in the CM
	 */
	public int getPosx() {
		return posx;
	}
	/**
	 * @param posx the new X coordinate in the CM
	 */
	public void setPosx(int posx) {
		this.posx = posx;
	}
	/**
	 * @return the Y coordinate in the CM
	 */
	public int getPosy() {
		return posy;
	}
	/**
	 * @param posy the new Y coordinate in the CM
	 */
	public void setPosy(int posy) {
		this.posy = posy;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Creates a new relationship
	 *  
	 * @param newId the id of the new relationship
	 */
	public Relationship(int newId) {
		this.id = newId;
	}
	
	/**
	 * Exports the relationship in XML format
	 * @return the XML string
	 */
	public String exportXML() {
		String output = 
			"<relationship id=\"" + this.id + 
			"\" source=\"" + this.getSource().getId() + 
			"\" target=\"" + this.getTarget().getId() + 
			"\" linkingWord=\"" + this.linkingWord + 
			"\" posx=\"" + this.posx + 
			"\" posy=\"" + this.posy + "\"/>";
		return output;
	}
	
	@Override
	public String toString() {
		return source.getLabel() + " -- " + linkingWord + " --> " + target.getLabel() + " [Id:" + id + "(" + posx + "," + posy + ")]";
	}
}
