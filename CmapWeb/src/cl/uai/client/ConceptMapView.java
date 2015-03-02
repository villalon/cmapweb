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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.Line;

import cl.uai.client.cmap.Concept;
import cl.uai.client.cmap.ConceptMap;
import cl.uai.client.cmap.Relationship;
import cl.uai.client.cmap.Relationship.Drawing;
import cl.uai.client.commands.AddConceptCommand;
import cl.uai.client.commands.AbstractConceptMapCommand;
import cl.uai.client.commands.AddRelationshipCommand;
import cl.uai.client.commands.DeleteConceptCommand;
import cl.uai.client.commands.DeleteRelationshipCommand;
import cl.uai.client.commands.RenameConceptCommand;
import cl.uai.client.commands.RenameRelationshipCommand;
import cl.uai.client.dialogs.AddConceptDialogBox;
import cl.uai.client.dialogs.AddRelationshipDialogBox;
import cl.uai.client.dialogs.HelpDialogBox;
import cl.uai.client.dialogs.RenameConceptRelationshipDialogBox;
import cl.uai.client.loaders.HtmlInput;
import cl.uai.client.loaders.ReviewLoader;
import cl.uai.client.resources.Messages;

import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Concept Map viewer. A class that allows to interact with a CM
 * through double clicking and drag and drop.
 * 
 * @author Jorge Villalon
 *
 */
public class ConceptMapView extends Composite {

	/** For logging purposes */
	private static Logger logger = Logger.getLogger(ConceptMapView.class.getName());
	protected static Messages messages = GWT.create(Messages.class);

	/** The canvas that draws lines */
	private DrawingArea canvas;
	/** Drag and drop controller */
	private PickupDragController dragController;
	/** Boundary panel for the CM */
	private AbsolutePanel boundaryPanel;
	/** Focus panel on top of boundary to access double clicks */
	private FocusPanel focusPanel;
	/** Header of the interface */
	private ConceptMapHeader cmapHeader;
	/** Main panel that holds the whole interface */
	private VerticalPanel vMainPanel;
	/** If a concept was clicked within a double click */
	private boolean conceptClicked = false;
	/** If the viewer is in read only mode */
	private boolean readOnly = false;
	/** List of commands executed by the interface (for Undo-Redo) */
	private List<AbstractConceptMapCommand> commands;
	/** Index of the last executed command */
	private int lastCommand;
	/** The CM */
	private ConceptMap cmap = null;
	/** All labels for concepts */
	private Map<Integer, ConceptLabel> conceptLabels;
	/** All labels for relationships */
	private Map<Integer, RelationshipLabel> relationshipLabels;
	private static ConceptEditButtons conceptEditButtons = null;
	static {
		conceptEditButtons = new ConceptEditButtons();
	}
	private ReviewLoader loader;
	private String name = "conceptmap";
	private boolean inAddConceptMode = false;
	private boolean inAddRelationshipMode = false;
	/** Relationship line */
	private static Line addRelationshipLine = null;
	static {
		addRelationshipLine = new Line(0,0,0,0);
		addRelationshipLine.setStrokeColor("#CCCCCC");
		addRelationshipLine.setStrokeWidth(2);
	}
	private Concept addRelationshipSourceConcept = null;

	public static ConceptEditButtons getConceptEditButtons() {
		return conceptEditButtons;
	}
	/**
	 * Initializes the viewer. It has to indicate if the interface
	 * is read only or not, and the HTML input to store the CM's
	 * data in XML format.
	 * 
	 * @param isReadOnly if the interface will be in read only mode
	 */
	public ConceptMapView(int width, int height, boolean isReadOnly) {

		ConceptLabel.setReadonly(isReadOnly);
		
		readOnly = isReadOnly;
		
		// Initializing Concept Map object
		this.cmap = new ConceptMap();

		// Initializing command history for Undo/Redo
		this.commands = new ArrayList<AbstractConceptMapCommand>();

		// Initializing drag and drop canvas
		canvas = new DrawingArea(width, height);
		focusPanel = new FocusPanel();
		focusPanel.setStylePrimaryName("cmapFocusPanel");
		boundaryPanel = new AbsolutePanel();
		boundaryPanel.setWidth(width + "px");
		boundaryPanel.setHeight(height + "px");

		if(!isReadOnly) {
		dragController = new PickupDragController(boundaryPanel, true);
		dragController.setBehaviorDragStartSensitivity(1);
		dragController.setBehaviorScrollIntoView(false);
		dragController.addDragHandler(new ConceptMapDragHandler(this));
		}
		// Linking concepts with the panel, the edit label will open a rename dialog
		ConceptLabel.setAbsolutePanel(boundaryPanel);

		ConceptMapView.getConceptEditButtons().getEditButton().addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				ConceptLabel lbl = ConceptLabel.getSelectedLabel();
				if(lbl==null)
					return;
				if(lbl instanceof RelationshipLabel)
					showRenameRelationshipDialog(lbl, lbl.getText());
				else
					showRenameConceptDialog(lbl, lbl.getText());
			}
		});

		ConceptMapView.getConceptEditButtons().getDeleteButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ConceptLabel lbl = ConceptLabel.getSelectedLabel();
				boolean confirmDelete = Window.confirm(messages.ConfirmDelete() + " " + lbl.getLabel());
				if(!confirmDelete)
					return;
				if(lbl instanceof RelationshipLabel) {
					Relationship rel = getRelationshipFromLabel(lbl);
					Map<String, Object> params = new TreeMap<String, Object>();
					params.put("id", rel.getId());
					DeleteRelationshipCommand command = new DeleteRelationshipCommand(params);
					executeCommand(command);
				} else {
					Map<String, Object> params = new TreeMap<String, Object>();
					Concept c = getConceptFromLabel(lbl);
					params.put("id",c.getId());
					params.put("label",c.getLabel());
					params.put("posx",c.getPosx());
					params.put("posy",c.getPosy());
					DeleteConceptCommand command = new DeleteConceptCommand(params);
					executeCommand(command);
				}
			}
		});

		ConceptMapView.getConceptEditButtons().getLinkButton().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				conceptClicked = true;
				ConceptLabel lbl = ConceptLabel.getSelectedLabel();
				setInAddRelationshipMode(true);
				addRelationshipLine.setX1(lbl.getCenterX());
				addRelationshipLine.setY1(lbl.getCenterY());
				addRelationshipSourceConcept = getConceptFromLabel(lbl);
			}
		});

		focusPanel.add(boundaryPanel);

		// If the panel is not readonly then a double click must add a concept
		if(!readOnly) {
			focusPanel.addClickHandler(new ClickHandler() {				
				@Override
				public void onClick(ClickEvent event) {
					if(isInAddConceptMode()) {
						// Calculate position for the dialog and the concept
						int x = event.getRelativeX(event.getRelativeElement()) + event.getRelativeElement().getAbsoluteLeft();
						int y = event.getRelativeY(event.getRelativeElement()) + event.getRelativeElement().getAbsoluteTop();

						addConceptDialog(x,y);

						setInAddConceptMode(false);
					} else if(isInAddRelationshipMode() && !conceptClicked) {
						setInAddRelationshipMode(false);
					} else {
						conceptClicked = false;
					}
				}
			});
			focusPanel.addDoubleClickHandler(new DoubleClickHandler() {			
				public void onDoubleClick(DoubleClickEvent event) {
					// If the double click was on a concept or relationship do nothing
					if(conceptClicked)
						return;

					// Calculate position for the dialog and the concept
					int x = event.getRelativeX(event.getRelativeElement()) + event.getRelativeElement().getAbsoluteLeft();
					int y = event.getRelativeY(event.getRelativeElement()) + event.getRelativeElement().getAbsoluteTop();

					addConceptDialog(x,y);
				}
			});
			focusPanel.addMouseMoveHandler(new MouseMoveHandler() {				
				@Override
				public void onMouseMove(MouseMoveEvent event) {
					if(isInAddRelationshipMode()) {
						addRelationshipLine.setX2(event.getRelativeX(focusPanel.getElement()));
						addRelationshipLine.setY2(event.getRelativeY(focusPanel.getElement()));
					}
				}
			});
			focusPanel.addKeyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent event) {
					if(KeyCodes.KEY_ESCAPE==event.getUnicodeCharCode()) {
						Window.alert("Escape");
						return;
					}
					String msg = "";
					if(event.isControlKeyDown())
						msg += "Ctrl-";
					msg += event.getCharCode();
					msg += " " + event.getUnicodeCharCode();
					Window.alert(msg);
				}
			});
		}

		cmapHeader = new ConceptMapHeader(this, isReadOnly);

		// Layout parts initialization
		vMainPanel = new VerticalPanel();
		vMainPanel.setStylePrimaryName("cmapedPanel"); //$NON-NLS-1$

		ScrollPanel spanel = new ScrollPanel(focusPanel);
		spanel.setSize(width + "px", height + "px");
		spanel.addStyleName("cmapPanel");
		vMainPanel.add(cmapHeader);
		vMainPanel.add(spanel);

		initWidget(vMainPanel);
	}

	private void addConceptDialog(final int x, final int y) {
		// Ask user for the concept name
		AddConceptDialogBox dbox = new AddConceptDialogBox();
		dbox.setPopupPosition(x, y);
		dbox.addCloseHandler(new CloseHandler<PopupPanel>() {
			public void onClose(CloseEvent<PopupPanel> event) {
				AddConceptDialogBox dbox = (AddConceptDialogBox) event.getSource();
				// If word for concept is valid, create the concept
				if(dbox.getNewValue() != null && dbox.getNewValue().trim().length() > 0) {
					Map<String, Object> params = new TreeMap<String, Object>();
					params.put("label", dbox.getNewValue());
					params.put("posx", x - boundaryPanel.getAbsoluteLeft());
					params.put("posy", y - boundaryPanel.getAbsoluteTop());
					// Execute command
					AddConceptCommand command = new AddConceptCommand(params);
					executeCommand(command);
				}
			}
		});
		dbox.show();
		dbox.getTxtNewValue().setFocus(true);		
	}

	/**
	 * Adds a concept to the CM and a label to the viewer.
	 * 
	 * @param label label of the concept
	 * @param posx X coordinate of position in the CM
	 * @param posy Y coordinate of position in the CM
	 * @return the newly created concept
	 */
	public Concept addConceptAndLabel(String label, int posx, int posy) {
		Concept c = cmap.addConcept(label, posx, posy);
		addConceptLabel(c);
		return c;
	}

	/**
	 * Adds a label to the viewer for an existing concept in the CM.
	 * 
	 * @param concept the concept
	 */
	private void addConceptLabel(final Concept concept) {
		final ConceptLabel lblConcept = new ConceptLabel(concept.getLabel());
		lblConcept.setStylePrimaryName("concept");
		if(!readOnly) {
			lblConcept.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					conceptClicked = true;
					final ConceptLabel lbl = (ConceptLabel) event.getSource();
					final Concept target = getConceptFromLabel(lbl);
					if(isInAddRelationshipMode() && addRelationshipSourceConcept != null
							&& !addRelationshipSourceConcept.equals(target)) {
						AddRelationshipDialogBox dbox = new AddRelationshipDialogBox();
						dbox.addCloseHandler(new CloseHandler<PopupPanel>() {							
							public void onClose(CloseEvent<PopupPanel> event) {
								AddRelationshipDialogBox dbox = (AddRelationshipDialogBox) event.getSource();
								if(dbox.getNewValue() != null && dbox.getNewValue().trim().length() > 0) {
									Map<String, Object> params = new TreeMap<String, Object>();
									params.put("sourceId", addRelationshipSourceConcept.getId());
									params.put("targetId", target.getId());
									params.put("linkingWord", dbox.getNewValue());
									AddRelationshipCommand command = new AddRelationshipCommand(params);
									executeCommand(command);
								}
							}
						});
						dbox.showRelativeTo(getConceptLabels().get(target.getId()));
						dbox.getTxtNewValue().setFocus(true);						
					}
				}
			});
			
			lblConcept.addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					conceptClicked = true;
					final ConceptLabel lbl = (ConceptLabel) event.getSource();
					showRenameConceptDialog(lbl, concept.getLabel());
				}
			});
		}

		if(!readOnly)
			dragController.makeDraggable(lblConcept);
		boundaryPanel.add(lblConcept, concept.getPosx(), concept.getPosy());
		conceptLabels.put(concept.getId(), lblConcept);
	}

	/**
	 * Adds a relationship to the CM and the viewer.
	 * 
	 * @param source Source concept
	 * @param target Target concept
	 * @param linkingWord linking word
	 * @param drawingType drawing type
	 * @return the newly created relationship
	 */
	public Relationship addRelationshipAndLabel(Concept source, Concept target, String linkingWord, Drawing drawingType) {
		// Calculating middle point between two concepts
		int x = source.getPosx() + (int) (((double)target.getPosx() - (double)source.getPosx())/2);
		int y = source.getPosy() + (int) (((double)target.getPosy() - (double)source.getPosy())/2);

		// Creating the relationship in the CM
		Relationship rel = cmap.addRelationship(
				source, 
				target, 
				linkingWord, 
				drawingType, 
				x, 
				y);

		// In case there's something wrong return
		if(rel == null) {
			logger.severe("Relationship " + rel + " couldn't be created.");
			return null;
		}

		// Add label for relatinoship in viewer
		addRelationshipLabel(rel);

		return rel;
	}

	/**
	 * Adds a label for an existing relationship in the CM.
	 * 
	 * @param relationship the relationship
	 */
	private void addRelationshipLabel(final Relationship relationship) {
		final RelationshipLabel lblRelationship = new RelationshipLabel(this, relationship);
		lblRelationship.setStylePrimaryName("relationship");
		if(!readOnly) {
			lblRelationship.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					conceptClicked = true;
				}
			});
			lblRelationship.addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					conceptClicked = true;
					final ConceptLabel lbl = (ConceptLabel) event.getSource();
					showRenameRelationshipDialog(lbl, relationship.getLinkingWord());
				}
			});
		}

		if(!readOnly)
			dragController.makeDraggable(lblRelationship);
		boundaryPanel.add(lblRelationship, relationship.getPosx(), relationship.getPosy());
		relationshipLabels.put(relationship.getId(), lblRelationship);
		canvas.add(lblRelationship.getSrcLine());
		canvas.add(lblRelationship.getTgtLine());
		canvas.add(lblRelationship.getTgtArrow());

		// Adjust sister relationships
		adjustSisterRelationships(relationship);		
	}

	private void adjustSisterRelationships(Relationship rel) {
		for(Relationship r : cmap.sisterRelationships(rel)) {
			logger.fine("Found sister relationship " + rel);
			RelationshipLabel lblRel = relationshipLabels.get(r.getId());
			if(lblRel == null) {
				logger.warning("Label with id " + r.getId() + " not found in map");
				return;
			}
			cmap.moveRelationship(r.getId(), rel.getPosx(), rel.getPosy());
			boundaryPanel.setWidgetPosition(lblRel, rel.getPosx(), rel.getPosy());
			lblRel.drawAllLines();
		}		
	}

	/**
	 * Deletes a concept and its label in the viewer.
	 * 
	 * @param conceptId the id of the concept to delete
	 */
	public void deleteConceptAndLabel(int conceptId) {
		Concept concept = this.cmap.getConcept(conceptId);
		for(Relationship rel : cmap.incomingRelationships(concept)) {
			deleteRelationshipAndLabel(rel.getId());
		}
		for(Relationship rel : cmap.outgoingRelationships(concept)) {
			deleteRelationshipAndLabel(rel.getId());
		}
		boundaryPanel.remove(conceptLabels.get(concept.getId()));
		conceptLabels.remove(concept.getId());
		cmap.deleteConcept(concept.getId());
		removeConceptEditButtons();
	}

	/**
	 * Deletes a relationship and its label in the viewer.
	 * 
	 * @param id the id of the relationship to delete
	 */
	public void deleteRelationshipAndLabel(int id) {
		RelationshipLabel lbl = relationshipLabels.get(id);
		boundaryPanel.remove(relationshipLabels.get(id));
		relationshipLabels.remove(id);
		cmap.deleteRelationship(id);
		canvas.remove(lbl.getSrcLine());
		canvas.remove(lbl.getTgtLine());
		removeConceptEditButtons();
	}

	public void removeConceptEditButtons() {
		boundaryPanel.remove(ConceptMapView.getConceptEditButtons());		
	}
	/**
	 * Execute a command on the viewer.
	 * 
	 * @param command the command to be executed
	 */
	public void executeCommand(AbstractConceptMapCommand command) {
		command.execute(this);
		logger.fine("Executing " + command);

		// Checks if it is a new command in the history
		// or if a history reset is required
		if(this.lastCommand == this.commands.size()) {
			this.commands.add(command);
			this.lastCommand = this.commands.size();
		} else {
			for(int i=this.commands.size()-1; i>=this.lastCommand; i--) {
				this.commands.remove(i);
			}
			this.commands.add(command);
			this.lastCommand = this.commands.size();			
		}
		this.cmapHeader.setRedoEnabled(false);
		this.cmapHeader.setUndoEnabled(true);
		
		this.setInAddConceptMode(false);
		this.setInAddRelationshipMode(false);

		this.loader.save(this.exportXml(), new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Error saving data!" + caught);
			}
			@Override
			public void onSuccess(String result) {
			}
		});
	}
	public String exportXml() {
		String xml = "<?xml version=\"1.0\" ?>"; 
		xml += cmap.exportXML();
		xml = URL.encode(xml);
		return xml;
	}
	/**
	 * @return the boundaryPanel
	 */
	public AbsolutePanel getBoundaryPanel() {
		return boundaryPanel;
	}

	/**
	 * The CM in the viewer.
	 * 
	 * @return a CM
	 */
	public ConceptMap getCmap() {
		return cmap;
	}

	/**
	 * @return the cmapHeader
	 */
	public ConceptMapHeader getCmapHeader() {
		return cmapHeader;
	}

	/**
	 * For a paticular label, finds its corresponding concept.
	 * 
	 * @param lbl the label
	 * @return the corresponding concept
	 */
	public Concept getConceptFromLabel(ConceptLabel lbl) {
		if(!this.conceptLabels.containsValue(lbl))
			return null;

		for(int key : this.conceptLabels.keySet()) {
			ConceptLabel l = this.conceptLabels.get(key);
			if(l.equals(lbl)) {
				return this.cmap.getConcept(key);
			}
		}
		return null;
	}

	/**
	 * @return the conceptLabels
	 */
	public Map<Integer, ConceptLabel> getConceptLabels() {
		return conceptLabels;
	}

	/**
	 * @return the loader
	 */
	public ReviewLoader getLoader() {
		return loader;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * For a paticular label, finds its corresponding relationship.
	 * @param lbl the label
	 * @return the corresponding relationship
	 */
	public Relationship getRelationshipFromLabel(ConceptLabel lbl) {
		if(!this.relationshipLabels.containsValue(lbl))
			return null;

		for(int key : this.relationshipLabels.keySet()) {
			ConceptLabel l = this.relationshipLabels.get(key);
			if(l.equals(lbl)) {
				return this.cmap.getRelationship(key);
			}
		}
		return null;
	}

	/**
	 * @return the relationshipLabels
	 */
	public Map<Integer, RelationshipLabel> getRelationshipLabels() {
		return relationshipLabels;
	}

	/**
	 * Initializes interface
	 */
	private void initView() {
		conceptLabels = new TreeMap<Integer, ConceptLabel>();
		relationshipLabels = new TreeMap<Integer, RelationshipLabel>();

		boundaryPanel.clear();
		canvas.clear();
		boundaryPanel.add(canvas,0,0);
		canvas.add(addRelationshipLine);
	
	}

	/**
	 * Inserts an existing concept (or previously existing) and its label to the viewer.
	 * 
	 * @param id the concept id
	 * @param label the concept's label
	 * @param posx X coordinate in the CM
	 * @param posy Y coordinate in the CM
	 * @return the new concept instance
	 */
	public Concept insertConceptAndLabel(int id, String label, int posx, int posy) {
		Concept c = cmap.insertConcept(id, label, posx, posy);
		addConceptLabel(c);
		return c;
	}

	/**
	 * Inserts and existing (or previously existing) relationship and its label in the viewer.
	 * 
	 * @param id the relationship id
	 * @param source the source concept
	 * @param target the target concept
	 * @param linkingWord the linking word
	 * @param drawingType drawing type
	 * @param posx X coordinate in the CM
	 * @param posy Y coordinate in the CM
	 * @return
	 */
	public Relationship insertRelationshipAndLabel(int id, Concept source, Concept target, String linkingWord, Drawing drawingType, int posx, int posy) {
		Relationship rel = cmap.insertRelationship(id, source, target, linkingWord, drawingType, posx, posy);
		if(rel != null) {
			addRelationshipLabel(rel);
		}
		return rel;
	}
	public boolean isInAddConceptMode() {
		return inAddConceptMode;
	}

	public boolean isInAddRelationshipMode() {
		return inAddRelationshipMode;
	}

	/**
	 * Viewer in read only mode.
	 * 
	 * @return true if the viewer is in read only mode.
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	private void load() throws RequestException {
		// Invokes the loader
		loader.load(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				logger.severe("Error loading review data. Server returned an error " + caught.getMessage());
			}
			public void onSuccess(String result) {
				// Creates a new Concept Map with the XML returned
				String msg = "Loading cmap from";
				if(loader instanceof HtmlInput) {
					HtmlInput input = (HtmlInput) loader;
					msg += input.getInputName();
				}
				logger.fine(msg + " " + result);
				ConceptMap cmap = new ConceptMap();
				cmap.readXML(result);
				setCmap(cmap);
			}
		});
	}

	/**
	 * Loads the CM in the viewer.
	 */
	private void loadMap() {
		if(this.cmap == null)
			return;

		for(Concept concept : this.cmap.getConcepts().values()) {
			this.addConceptLabel(concept);
		}

		for(Relationship relationship : this.cmap.getRelationships().values()) {
			this.addRelationshipLabel(relationship);
		}

		this.commands = new ArrayList<AbstractConceptMapCommand>();
		this.lastCommand = this.commands.size();
	}

	/**
	 * Moves a concept in the CM and the viewer.
	 * 
	 * @param id concept id
	 * @param posx new X coordinate in the CM
	 * @param posy new Y coordinate in the CM
	 */
	public void moveConcept(int id, int posx, int posy) {		
		ConceptLabel lbl = this.conceptLabels.get(id);
		cmap.moveConcept(id, posx, posy);
		boundaryPanel.setWidgetPosition(lbl, posx, posy);
		for(Relationship rel : cmap.outgoingRelationships(cmap.getConcept(id)))
		{
			this.getRelationshipLabels().get(rel.getId()).drawAllLines();
		}
		for(Relationship rel : cmap.incomingRelationships(cmap.getConcept(id)))
		{
			this.getRelationshipLabels().get(rel.getId()).drawAllLines();			
		}
	}

	/**
	 * Moves a relationship in the CM and the viewer.
	 * 
	 * @param id relationship id
	 * @param posx new X coordinate in the CM
	 * @param posy new Y coordinate in the CM
	 */
	public void moveRelationship(int id, int posx, int posy) {
		RelationshipLabel lbl = this.relationshipLabels.get(id);
		cmap.moveRelationship(id, posx, posy);
		boundaryPanel.setWidgetPosition(lbl, posx, posy);
		Relationship r = cmap.getRelationship(id);
		adjustSisterRelationships(r);

		lbl.drawAllLines();
	}

	@Override
	protected void onLoad() {
		super.onLoad();

		try {
			load();
		} catch (RequestException e) {
			e.printStackTrace();
			Window.alert(e.getMessage());
		}

		//initView();
	}

	/**
	 * Finds a concept that is overlapping another. It is used to check
	 * for drag and drop interface to create relationships.
	 * 
	 * @param concept the concept the check
	 * @return an overlapping concept or null
	 */
	public Concept overlappingConcept(Concept concept) {
		ConceptLabel lblConcept = this.conceptLabels.get(concept.getId());
		if(lblConcept == null)
			return null;
		for(Concept c : this.cmap.getConcepts().values()) {
			if(concept.equals(c))
				continue;
			ConceptLabel lblTarget = this.conceptLabels.get(c.getId());
			if(lblTarget == null)
				continue;

			if(lblConcept.getAbsoluteLeft() < lblTarget.getAbsoluteLeft() + lblTarget.getOffsetWidth()
					&& lblConcept.getAbsoluteLeft() + lblConcept.getOffsetWidth() >= lblTarget.getAbsoluteLeft()
					&& lblConcept.getAbsoluteTop() + lblConcept.getOffsetHeight() >= lblTarget.getAbsoluteTop()
					&& lblConcept.getAbsoluteTop() < lblTarget.getAbsoluteTop() + lblTarget.getOffsetHeight()
					) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Redo the last undone command.
	 */
	public void redo() {
		if(this.lastCommand == this.commands.size())
			return;
		AbstractConceptMapCommand lastCmCommand = this.commands.get(this.lastCommand);
		lastCmCommand.execute(this);
		logger.fine("Redoing " + lastCmCommand);
		this.lastCommand++;
		this.cmapHeader.setRedoEnabled(this.lastCommand < this.commands.size());
		this.cmapHeader.setUndoEnabled(true);
	}

	/**
	 * Changes the label of a concept.
	 * 
	 * @param id concept id
	 * @param label new label
	 */
	public void renameConcept(int id, String label) {
		cmap.getConcept(id).setLabel(label);
		conceptLabels.get(id).setText(label);
	}

	/**
	 * Changes the linking word in a relationship.
	 * 
	 * @param id relationshp id
	 * @param linkingWord new linking word
	 */
	public void renameRelationship(int id, String linkingWord) {
		Relationship rel = cmap.getRelationship(id);
		rel.setLinkingWord(linkingWord);
		relationshipLabels.get(id).setText(linkingWord);
		adjustSisterRelationships(rel);
	}

	/**
	 * Sets a new CM for the viewer.
	 * 
	 * @param cmap
	 */
	public void setCmap(ConceptMap cmap) {
		this.cmap = cmap;
		initView();
		loadMap();
		cmapHeader.setConceptMap(cmap);
	}

	public void setInAddConceptMode(boolean inAddConceptMode) {
		this.inAddConceptMode = inAddConceptMode;
		if(inAddConceptMode) {
			this.setInAddRelationshipMode(false);
			focusPanel.addStyleName("addConceptCursor");
		} else {
			focusPanel.removeStyleName("addConceptCursor");
		}
	}

	public void setInAddRelationshipMode(boolean inAddRelationshipMode) {
		this.inAddRelationshipMode = inAddRelationshipMode;
		if(inAddRelationshipMode) {
			this.setInAddConceptMode(false);
			focusPanel.addStyleName("addRelationshipCursor");
			// DOM.setStyleAttribute(focusPanel.getElement(), "cursor", "url(addrelationship.cur),auto");
		} else {
			focusPanel.removeStyleName("addRelationshipCursor");
			addRelationshipLine.setX1(0);
			addRelationshipLine.setY1(0);
			addRelationshipLine.setX2(0);
			addRelationshipLine.setY2(0);
		}
	}

	/**
	 * @param loader the loader to set
	 */
	public void setLoader(ReviewLoader loader) {
		this.loader = loader;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Shows the help dialog box under a widget.
	 * 
	 * @param widget the widget under which the dialog box will be shown.
	 */
	public void showHelp() {
		HelpDialogBox dbox = new HelpDialogBox();
		dbox.setModal(true);
		dbox.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + cmapHeader.getOffsetHeight());
		dbox.show();
	}

	private void showRenameConceptDialog(final ConceptLabel lbl, String label) {
		RenameConceptRelationshipDialogBox dbox = new RenameConceptRelationshipDialogBox(label);
		dbox.setPopupPosition(lbl.getAbsoluteLeft(), lbl.getAbsoluteTop());
		dbox.show();
		dbox.getTxtNewValue().setFocus(true);
		dbox.addCloseHandler(new CloseHandler<PopupPanel>() {				
			public void onClose(CloseEvent<PopupPanel> event) {
				RenameConceptRelationshipDialogBox dbox = (RenameConceptRelationshipDialogBox) event.getSource();
				if(dbox.isValueChanged()) {
					Map<String, Object> params = new TreeMap<String, Object>();
					params.put("id", getConceptFromLabel(lbl).getId());
					params.put("label", dbox.getNewValue());
					RenameConceptCommand command = new RenameConceptCommand(params);
					executeCommand(command);
				}
				conceptClicked = false;
			}
		});		
	}

	private void showRenameRelationshipDialog(final ConceptLabel lbl, String label) {
		RenameConceptRelationshipDialogBox dbox = new RenameConceptRelationshipDialogBox(label);
		dbox.setPopupPosition(lbl.getAbsoluteLeft(), lbl.getAbsoluteTop());
		dbox.show();
		//		dbox.showRelativeTo(lbl);
		dbox.getTxtNewValue().setFocus(true);
		dbox.addCloseHandler(new CloseHandler<PopupPanel>() {				
			public void onClose(CloseEvent<PopupPanel> event) {
				RenameConceptRelationshipDialogBox dbox = (RenameConceptRelationshipDialogBox) event.getSource();
				if(dbox.isValueChanged()) {
					Relationship rel = getRelationshipFromLabel(lbl);
					Map<String, Object> params = new TreeMap<String, Object>();
					params.put("id", rel.getId());
					params.put("linkingWord", dbox.getNewValue());
					RenameRelationshipCommand command = new RenameRelationshipCommand(params);
					executeCommand(command);
				}
				conceptClicked = false;
			}
		});		
	}

	/**
	 * Undo the last command executed.
	 */
	public void undo() {
		if(this.commands.size() == 0 || this.lastCommand == 0)
			return;
		AbstractConceptMapCommand lastCmCommand = this.commands.get(this.lastCommand-1);
		lastCmCommand.undo();
		this.lastCommand--;
		this.cmapHeader.setRedoEnabled(true);
		this.cmapHeader.setUndoEnabled(this.lastCommand > 0);
	}
}
