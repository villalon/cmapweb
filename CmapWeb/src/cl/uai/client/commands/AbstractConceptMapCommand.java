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

/**
 * Abstract class that is base for all commands.
 * 
 * @author Jorge Villalon
 *
 */
public abstract class AbstractConceptMapCommand {

	/** An id for the concept or relationship */
	protected int id = -1;
	/** X coordinate of concept/relationship in the CM */
	protected int posx;
	/** Y coordinate of concept/relationship in the CM */
	protected int posy;
	/** Humanly readable name of the command */
	protected String name;
	/** Parameters to execute the command */
	protected Map<String, Object> parameters;
	/** The interface in which the command is executed */
	protected ConceptMapView cmapview = null;
	
	/**
	 * Creates a new command.
	 * 
	 * @param name name of the command
	 * @param parameters parameters for execution
	 */
	public AbstractConceptMapCommand(String name, Map<String, Object> parameters) {
		this.name = name;
		this.parameters = parameters;
	}
	
	/**
	 * @return the name of the command
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Parameters that the command will use in execution
	 * @return map of parameters by name
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * 
	 * @return the interface in which the command is executed
	 */
	public ConceptMapView getCmapView() {
		return cmapview;
	}

	/**
	 * Execute the command
	 * @param cview interface for execution
	 */
	public abstract void execute(ConceptMapView cview);
	
	/**
	 * Undo the command
	 */
	public abstract void undo();
	
	public String exportXML(int index) {
		String output = "<command index=\"" + index + "\" name=\"" + name + "\">";
		for(String key : this.parameters.keySet()) {
			output += "<parameter key=\"" + key + "\" value=\"" + parameters.get(key) + "\"/>";
		}
		output += "</command>";
		return output;
	}
}
