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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import cl.uai.client.cmap.Relationship.Drawing;
import cl.uai.client.resources.Messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Random;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

/**
 * ConceptMap
 * This class represents a Concept Map, including a list of concepts,
 * a list of relationships and a title.
 * This class also has all the methods to interact with a CM.
 * 
 * @author Jorge Villalon
 *
 */
public class ConceptMap {

	/** For logging purposes */
	private static Logger logger = Logger.getLogger(ConceptMap.class.getName());

	/** Map of concepts by their id */
	private Map<Integer, Concept> concepts;
	/** Map of relationships by their id */
	private Map<Integer, Relationship> relationships;
	/** A sequence for concept ids */
	private int conceptIdSequence = 1;
	/** A sequence for relationships ids */
	private int relationshipIdSequence = 1;
	/** The CM title */
	private String title;
	/** Localized messages */
	private static Messages messages = GWT.create(Messages.class);

	/**
	 * Default CM constructor, it creates an empty CM
	 * with 'Untitled' as title.
	 */
	public ConceptMap() {
		this(messages.getString("Untitled")); //$NON-NLS-1$
	}

	/**
	 * Creates an empty CM.
	 * 
	 * @param cmapTitle The title for the CM
	 */
	public ConceptMap(String cmapTitle) {
		this.title = cmapTitle;
		this.concepts = new TreeMap<Integer, Concept>();
		this.relationships = new TreeMap<Integer, Relationship>();
		this.conceptIdSequence = 1;
		this.relationshipIdSequence = 1;
	}

	/**
	 * Adds a concept, with a new id, to the CM.
	 * 
	 * @param label The concept label.
	 * @param posx The X coordinate of the concept position in the map
	 * @param posy The Y coordinate of the concept position in the map
	 * @return the newly created concept.
	 */
	public Concept addConcept(String label, int posx, int posy) {
		return insertConcept(this.conceptIdSequence, label, posx, posy);
	}

	/**
	 * Adds a relationship, with a new id, to the CM.
	 * 
	 * @param source Source (first) concept of the relationship.
	 * @param target Target (second) concept of the relationship.
	 * @param linkingWord The label that links both concepts.
	 * @param drawingType How the relatinoship will be drawn.
	 * @return the newly created relationship.
	 */
	public Relationship addRelationship(Concept source, Concept target, String linkingWord, Relationship.Drawing drawingType, int posx, int posy) {
		return insertRelationship(
				this.relationshipIdSequence, source, target, linkingWord, drawingType,
				posx,
				posy);
	}

	/**
	 * If a concept is contained in the CM.
	 * 
	 * @param concept The concept to check.
	 * @return If the concept belongs to the CM.
	 */
	public boolean containsConcept(Concept concept) {
		return this.concepts.containsKey(concept.getId());
	}

	/**
	 * Deletes a concept from the CM.
	 * 
	 * @param id the id of the concept to delete.
	 */
	public void deleteConcept(int id) {
		Concept concept = this.getConcept(id);
		for(Relationship rel : incomingRelationships(concept)) {
			deleteRelationship(rel.getId());
		}
		for(Relationship rel : outgoingRelationships(concept)) {
			deleteRelationship(rel.getId());
		}
		this.concepts.remove(concept.getId());
	}

	/**
	 * Deletes a relationship from the CM.
	 * 
	 * @param id the id of the relationship.
	 */
	public void deleteRelationship(int id) {
		if(this.relationships.containsKey(id)) {
			this.relationships.remove(id);
		}
	}

	/**
	 * Exports a CM in XML format.
	 * 
	 * @return the XML string.
	 */
	public String exportXML() {
		String output = ""; //$NON-NLS-1$
		output += "<conceptmap title=\"" + this.title + "\">"; //$NON-NLS-1$ //$NON-NLS-2$
		// First, all concepts
		for(Concept c : this.concepts.values()) {
			output += c.exportXML();
		}
		// Second, all relationships
		for(Relationship r : this.relationships.values()) {
			output += r.exportXML();
		}
		output += "</conceptmap>"; //$NON-NLS-1$
		return output;
	}

	/**
	 * Gets a concept by its id
	 * 
	 * @param id id of the concept
	 * @return the concept
	 */
	public Concept getConcept(int id) {
		return this.concepts.get(id);
	}

	/**
	 * Gets the sequence for concepts ids
	 * 
	 * @return the last number used
	 */
	public int getConceptIdSequence() {
		return conceptIdSequence;
	}

	/**
	 * All the concepts in the CM.
	 * 
	 * @return a map of concepts by id.
	 */
	public Map<Integer, Concept> getConcepts() {
		return concepts;
	}

	/**
	 * Gets a relationship by its id.
	 * 
	 * @param id id of the relationship
	 * @return a relationship
	 */
	public Relationship getRelationship(int id) {
		return this.relationships.get(id);
	}

	/**
	 * Gets the sequence for relationships ids
	 * 
	 * @return the last number used
	 */
	public int getRelationshipIdSequence() {
		return relationshipIdSequence;
	}

	/**
	 * All the relationships in the CM.
	 * 
	 * @return a map of the relationshps by id
	 */
	public Map<Integer, Relationship> getRelationships() {
		return relationships;
	}

	/**
	 * The CM title.
	 * 
	 * @return title.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * All the relationships that 'end' in a particular concept, for which
	 * the concept is the target.
	 * 
	 * @param concept the concept.
	 * @return a list of relationships.
	 */
	public List<Relationship> incomingRelationships(Concept concept) {
		List<Relationship> output = new ArrayList<Relationship>();
		for(Relationship rel : this.relationships.values()) {
			if(rel.getTarget().equals(concept))
				output.add(rel);
		}
		return output;
	}

	/**
	 * Inserts a concept with a specific id in the CM. It does not create a new instance.
	 * 
	 * @param id the concept id
	 * @param label the label
	 * @param posx the X coordinate of the concept's position in the CM
	 * @param posy the Y coordinate of the concept's position in the CM
	 * @return
	 */
	public Concept insertConcept(int id, String label, int posx, int posy) {
		Concept c = new Concept(id);
		c.setLabel(label);
		c.setPosx(posx);
		c.setPosy(posy);
		this.concepts.put(c.getId(),c);
		if(this.conceptIdSequence <= id)
			this.conceptIdSequence = id + 1;
		return c;		
	}

	/**
	 * Inserts a relationship with a specific id in the CM. It does not create a new instance.
	 * 
	 * @param id the relationship id
	 * @param source the source concept
	 * @param target the target concept
	 * @param linkingWord the linking word
	 * @param drawingType how the relationship will be drawn
	 * @param posx the X coordinate of the relationship's position in the CM
	 * @param posy the Y coordinate of the relationship's position in the CM
	 * @return
	 */
	public Relationship insertRelationship(int id, Concept source, Concept target, String linkingWord, Relationship.Drawing drawingType, int posx, int posy) {		
		if(source == null || target == null)
			return null;
		Relationship r = new Relationship(id);
		r.setSource(source);
		r.setTarget(target);
		r.setLinkingWord(linkingWord);
		r.setPosx(posx);
		r.setPosy(posy);

		// Checking consistency, that both concepts are in the CM.
		if(this.containsConcept(source) && this.containsConcept(target)) {
			relationships.put(r.getId(),r);
			if(this.relationshipIdSequence <= id)
				this.relationshipIdSequence = id + 1;
			return r;
		} else {
			logger.severe("Trying to add a relationship with no concepts in the map." //$NON-NLS-1$
					+ r);
		}
		return null;
	}

	/**
	 * @return if the Concept Map is empty (has no concepts nor relationships)
	 */
	public boolean isEmpty() {
		return (this.relationships == null 
				|| this.concepts == null 
				|| (this.concepts.size() == 0 && this.relationships.size() == 0));
	}
	
	/**
	 * Changes a concept position in the CM.
	 * 
	 * @param id the concept id
	 * @param posx the X coordinate of the new position
	 * @param posy the Y coordinate of the new position
	 */
	public void moveConcept(int id, int posx, int posy) {
		Concept c = this.concepts.get(id);
		c.setPosx(posx);
		c.setPosy(posy);
	}

	/**
	 * Changes a relationship position in the CM.
	 * 
	 * @param id the relationship id
	 * @param posx the X coordinate of the new position
	 * @param posy the Y coordinate of the new position
	 */
	public void moveRelationship(int id, int posx, int posy) {
		Relationship r = this.relationships.get(id);
		r.setPosx(posx);
		r.setPosy(posy);
	}

	/**
	 * All relationships that are 'originated' in a particular concept, for which
	 * the concept is the source.
	 * 
	 * @param concept the concept
	 * @return a list of relationships
	 */
	public List<Relationship> outgoingRelationships(Concept concept) {
		List<Relationship> output = new ArrayList<Relationship>();
		for(Relationship rel : this.relationships.values()) {
			if(rel.getSource().equals(concept))
				output.add(rel);
		}
		return output;
	}

	/**
	 * Reads a CM from an XML, the format is:
	 * 
	 * {@code
	 * <conceptmap title="Simple map">
	 * <concept id="1" label="A" posx="10" posy="10"/>
	 * <concept id="2" label="B" posx="50" posy="50"/>
	 * <relationship id="1" source="1" target="2" linkingWord = "is followed by" posx="30" posy="30"/>
	 * </conceptmap>
	 * }
	 * 
	 * @param cmapXML the XML string.
	 */
	public void readXML(String cmapXML) {
		if(cmapXML == null
				|| cmapXML.trim().length()==0)
			return;
		
		try {
			// Decodes string for URL and parses the XML document into a DOM
			logger.fine("Loading Concept Map");
			Document messageDom = XMLParser.parse(URL.decode(cmapXML));

			// Finds the CM title and sets it.
			NodeList conceptmapList = messageDom.getElementsByTagName("conceptmap"); //$NON-NLS-1$
			for(int i=0; i<conceptmapList.getLength(); i++) {
				Node conceptNode = conceptmapList.item(i);
				String title = ((Element) conceptNode).getAttribute("title");
				logger.fine("Found concept map with title:" + title);
				this.setTitle(title);
			}
			
			// Finds all the concepts in the DOM and inserts them in the CM.
			NodeList conceptsList = messageDom.getElementsByTagName("concept"); //$NON-NLS-1$
			for(int i=0; i<conceptsList.getLength(); i++) {
				Node conceptNode = conceptsList.item(i);
				int id = Integer.parseInt(((Element) conceptNode).getAttribute("id")); //$NON-NLS-1$
				String label = ((Element) conceptNode).getAttribute("label"); //$NON-NLS-1$
				logger.fine("Found concept:" + label);
				int posx = -1;
				int posy = -1;
				try {
					posx = (int) Float.parseFloat(((Element) conceptNode).getAttribute("posx").replace(",", ".")); //$NON-NLS-1$
					posy = (int) Float.parseFloat(((Element) conceptNode).getAttribute("posy").replace(",", ".")); //$NON-NLS-1$
				} catch (Exception e) {
					logger.severe("No position info in concept maps");
					posx = Random.nextInt(500);
					posy = Random.nextInt(500);
				}
				this.insertConcept(id, label, posx, posy);
			}

			// Finds all the relationships in the DOM and inserts them in the CM.
			NodeList relationshipsList = messageDom.getElementsByTagName("relationship"); //$NON-NLS-1$
			for(int i=0; i<relationshipsList.getLength(); i++) {
				Node relationshipNode = relationshipsList.item(i);
				int id = Integer.parseInt(((Element) relationshipNode).getAttribute("id")); //$NON-NLS-1$
				int srcId = Integer.parseInt(((Element) relationshipNode).getAttribute("source")); //$NON-NLS-1$
				int tgtId = Integer.parseInt(((Element) relationshipNode).getAttribute("target")); //$NON-NLS-1$
				String linkingWord = ((Element) relationshipNode).getAttribute("linkingWord"); //$NON-NLS-1$
				logger.fine("Found relationship:" + linkingWord);
				int posx = -1;
				int posy = -1;
				try {
					posx = (int) Float.parseFloat(((Element) relationshipNode).getAttribute("posx").replace(",", ".")); //$NON-NLS-1$
					posy = (int) Float.parseFloat(((Element) relationshipNode).getAttribute("posy").replace(",", ".")); //$NON-NLS-1$
				} catch (Exception e) {
					// XML comes with no position
					Concept source = this.getConcept(srcId);
					Concept target = this.getConcept(tgtId);
					posx = source.getPosx() + (int) (((double)target.getPosx() - (double)source.getPosx())/2);
					posy = source.getPosy() + (int) (((double)target.getPosy() - (double)source.getPosy())/2);
				}

				Concept source = this.concepts.get(srcId);
				Concept target = this.concepts.get(tgtId);
				this.insertRelationship(id, source, target, linkingWord, Drawing.LINE, posx, posy);			    	
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
		}
	}

	/**
	 * Sets a new title for the CM.
	 * 
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Finds all relationships that have a common source concept and also a common linking word.
	 * It is used for drawing purposes so only one linking word is shown instead of several.
	 *  
	 * @param relationship the relationship to check for sisters
	 * @return a list of relationships
	 */
	public List<Relationship> sisterRelationships(Relationship relationship) {
		logger.fine("Analyzing " + relationship);
		List<Relationship> output = new ArrayList<Relationship>();
		for(Relationship rel : outgoingRelationships(relationship.getSource())) {
			// Exclude the relationship in the parameter and check for identical linking word
			if(!rel.getTarget().equals(relationship.getTarget()) 
					&& rel.getLinkingWord().equals(relationship.getLinkingWord())) {
				logger.fine("Found " + rel);
				output.add(rel);
			}
		}
		return output;
	}
}
