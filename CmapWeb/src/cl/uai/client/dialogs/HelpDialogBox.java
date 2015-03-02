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
package cl.uai.client.dialogs;

import cl.uai.client.resources.Messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Jorge Villalon
 *
 */
public class HelpDialogBox extends DialogBox {

	private Messages messages = GWT.create(Messages.class);
	private VerticalPanel vpanel;
	
	public HelpDialogBox() {
		super(true, false);
		vpanel = new VerticalPanel();
		

		String lblCopyright = "<br/><div align=\"center\"><font size=1>Copyright (c) 2011-2015 <a href=\"http://www.villalon.cl\">Jorge Villalon</a></font></div><br/>";
		
		this.setHTML(messages.getString("HelpTitle")); //$NON-NLS-1$
		vpanel.add(new HTML(messages.getString("HelpAddConcepts")));
		vpanel.add(new HTML(messages.getString("HelpAddRelationships"))); //$NON-NLS-1$
		vpanel.add(new HTML(messages.getString("HelpRenameDelete")));
		vpanel.add(new HTML(lblCopyright)); //$NON-NLS-1$
		
		Button btnClose = new Button(messages.getString("Close"));
		btnClose.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				hide();
			}
		});
			
		vpanel.add(btnClose);
		vpanel.setCellHorizontalAlignment(btnClose, HasAlignment.ALIGN_CENTER);
		this.add(vpanel);
	}
}
