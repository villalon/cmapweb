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
 * This class represents a Concept in a CM.
 * 
 * @author Jorge Villalon
 *
 */
public class Concept {

	/** Concept id */
	private int id;
	/** Concept's label */
	private String label;
	/** X coordinate in the CM */
	private int posx;
	/** Y coordinate in the CM */
	private int posy;

	/**
	 * @return the concept label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the new concept label to set
	 */
	public void setLabel(String label) {
		this.label = label;
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
	 * @return the concept id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Creates a new concept
	 * 
	 * @param newId concept id
	 */
	public Concept(int newId) {
		this.id = newId;
	}

	/**
	 * Evaluates if two concepts are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Concept))
			return false;
		Concept c = (Concept) obj;
		if(c.getId() == this.getId()){
			if(c.getPosx() != this.getPosx()
					|| c.getPosy() != this.getPosy()
					|| !c.getLabel().equals(this.getLabel()))
				return false;
			else
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return label;
	}

	/**
	 * Exports a concept as XML.
	 * 
	 * @return an XML string
	 */
	public String exportXML() {
		String output = 
			"<concept id=\"" + this.id + 
			"\" label=\"" + this.label + 
			"\" posx=\"" + this.posx + 
			"\" posy=\"" + this.posy + 
			"\"/>";
		return output;
	}
}
