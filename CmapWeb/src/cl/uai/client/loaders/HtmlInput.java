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
package cl.uai.client.loaders;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Class implementing loading and saving from an HTML input tag (usually hidden).
 * 
 * @author Jorge Villalon
 *
 */
public class HtmlInput implements ReviewLoader {

	/** Name of the HTML input tag to load/save data from */
	private String inputName;
	
	/**
	 * Default constructor
	 */
	public HtmlInput() {
	}
	
	/**
	 * @param inputName Name of the HTML input tag to load/save data from
	 */
	public HtmlInput(String inputName) {
		this.inputName = inputName;
	}

	/**
	 * @return the inputName
	 */
	public String getInputName() {
		return inputName;
	}

	/**
	 * Loads a concept map in xml format from a loader.
	 */
	public void load(final AsyncCallback<String> callback) {
		Element element = DOM.getElementById(inputName);
		if(element == null) {
			callback.onFailure(new Throwable("Invalid name of the input tag."));
			return;
		}
		
		String xml = null;
		if(element.getTagName().toLowerCase().equals("textarea"))
			xml = element.getInnerText();
		else if(element.getTagName().toLowerCase().equals("input"))
			xml = element.getAttribute("value");
		else {
			callback.onFailure(new Throwable("Invalid tag for the input. It should be textarea or input., it was " + element.getTagName()));
			return;	
		}
		callback.onSuccess(xml);
	}

	/**
	 * Saves the concept map in xml format into a loader.
	 */
	public void save(String xml, final AsyncCallback<String> callback) {
		Element element = DOM.getElementById(inputName);
		
		if(element == null) {
			callback.onFailure(new Throwable("Invalid name of the input tag."));
			return;
		}
		
		if(element.getTagName().toLowerCase().equals("textarea"))
			element.setInnerText(xml);
		else if(element.getTagName().toLowerCase().equals("input"))
			element.setAttribute("value", xml);
		else {
			callback.onFailure(new Throwable("Invalid tag for the input. It should be textarea or input., it was " + element.getTagName()));
			return;			
		}
		
		callback.onSuccess("Data saved");
	}
	
	/**
	 * @param inputName the inputName to set
	 */
	public void setInputName(String inputName) {
		this.inputName = inputName;
	}
}
