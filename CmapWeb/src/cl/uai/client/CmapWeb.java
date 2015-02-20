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

import java.util.logging.Logger;

import cl.uai.client.loaders.HtmlInput;
import cl.uai.client.loaders.ReviewLoader;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point class for Concept Map Question Type.
 * 
 * @author Jorge Villalon
 *
 */
public class CmapWeb implements EntryPoint {

	/** For logging purposes */
	private static Logger logger = Logger.getLogger(CmapWeb.class.getName());

	/** The concept map view that will be displayed */
	private ConceptMapView cmapview;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		logger.fine("Loading testing interface");

		// Get id for CmapWeb's DIV tag
		String cmapwebDivId = "conceptmap";
		if(RootPanel.get(cmapwebDivId)==null) {
			Window.alert("Can not initalize. CmapWeb requires an existing DIV tag with id: conceptmap.");
			return;
		}
		
		int width = 0;
		int height = 0;

		try {
			// Reading width and height from HTML
			width = Integer.parseInt(RootPanel.get(cmapwebDivId).getElement().getAttribute("width"));
			height = Integer.parseInt(RootPanel.get(cmapwebDivId).getElement().getAttribute("height"));
		} catch (Exception e) {
			Window.alert("Error in HTML for CmapWeb can not initalize. Invalid width or height values (must be integers).");
			return;			
		}

		// The name of the HTML input in which the CM data will be stored
		String input = RootPanel.get(cmapwebDivId).getElement().getAttribute("input");
		if(RootPanel.get(input) == null) {
			Window.alert("Error in HTML for CmapWeb can not initalize. Invalid input id for saving data.");
			return;			
		}
			

		// Read div attribute for readonly
		boolean readOnly = false;
		if(RootPanel.get(cmapwebDivId).getElement().getAttribute("readonly") != null)
			readOnly = RootPanel.get(cmapwebDivId).getElement().getAttribute("readonly").equals("true");
		logger.fine("Read only mode: " + readOnly);

		// Set the client's width and height
		cmapview = new ConceptMapView(width, height, readOnly);
		cmapview.setLoader(getLoader(input));

		// Add the view to the div in the page
		RootPanel.get(cmapwebDivId).add(cmapview);

		// Start interface showing the help
		if(!readOnly && this.cmapview.getCmap() != null && this.cmapview.getCmap().isEmpty())
			cmapview.showHelp();
	}

	private ReviewLoader getLoader(String input) {
		ReviewLoader loader = null;

		HtmlInput request = new HtmlInput();
		request.setInputName(input);
		loader = request;
		return loader;
	}
}
