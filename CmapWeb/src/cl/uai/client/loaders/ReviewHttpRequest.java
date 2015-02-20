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

import java.util.logging.Logger;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Class implementing load/save using HttpRequests
 * 
 * @author Jorge Villalon
 *
 */
public class ReviewHttpRequest implements ReviewLoader {

	private static Logger logger = Logger.getLogger(ReviewHttpRequest.class.getName());

	private String urlTmlServlet;
	private String documentId;
	private String reviewAuthor;
	private String reviewName;

	/**
	 * 
	 */
	public ReviewHttpRequest() {
	}


	/**
	 * @param urlTmlServlet URL of TML servlet
	 * @param documentId Id of the document
	 * @param reviewAuthor Id for the user
	 * @param reviewName name of the review (e.g. conceptmap)
	 */
	public ReviewHttpRequest(String urlTmlServlet, String documentId,
			String reviewAuthor, String reviewName) {
		this.urlTmlServlet = urlTmlServlet;
		this.documentId = documentId;
		this.reviewAuthor = reviewAuthor;
		this.reviewName = reviewName;
	}


	/**
	 * @return the reviewAuthor
	 */
	public String getReviewAuthor() {
		return reviewAuthor;
	}

	/**
	 * @return the documentId
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * @return the reviewName
	 */
	public String getReviewName() {
		return reviewName;
	}

	/**
	 * @return the urlTmlServlet
	 */
	public String getUrlTmlServlet() {
		return urlTmlServlet;
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.loaders.ReviewLoader#load()
	 */
	public void load(final AsyncCallback<String> callback) {

		String url = this.urlTmlServlet + 
			"?document=" + this.documentId + 
			"&review_author=" + this.reviewAuthor + 
			"&review_name=" + this.reviewName;
		RequestBuilder request = new RequestBuilder(RequestBuilder.GET, url);

		try {
			request.sendRequest(null, new RequestCallback() {			
				public void onError(Request request, Throwable exception) {
					logger.severe(exception.getMessage());
					callback.onFailure(new Throwable(exception));
				}			
				public void onResponseReceived(Request request, Response response) {
					if(response.getStatusCode()!=200) {
						logger.severe("Server returned an error " + response.getStatusCode());
						callback.onFailure(new Throwable("Server returned an error " + response.getStatusCode()));
						return;
					}
					callback.onSuccess(response.getText());
				}
			});
		} catch (RequestException e) {
			logger.severe(e.getMessage());
			callback.onFailure(new Throwable(e));
		}
	}

	/* (non-Javadoc)
	 * @see cl.uai.client.loaders.ReviewLoader#save(java.lang.String)
	 */
	public void save(String xml, final AsyncCallback<String> callback) {
		RequestBuilder request = new RequestBuilder(RequestBuilder.POST, this.urlTmlServlet);
		request.setHeader("Content-type", "application/x-www-form-urlencoded");
		String data = "review_author=" + this.reviewAuthor + 
		"&review_name=" + this.reviewName
		+ "&document_id=" + this.documentId + "&review_value=" + URL.encode(xml);
		try {
			request.sendRequest(data, new RequestCallback() {			
				public void onError(Request request, Throwable exception) {
					exception.printStackTrace();
					callback.onFailure(new Throwable("Error trying to save review data!"));
				}			
				public void onResponseReceived(Request request, Response response) {
					if(response.getStatusCode()!=200) {
						callback.onFailure(new Throwable("Server returned an error " + response.getStatusCode()));
						return;
					}
					callback.onSuccess("Saved!");
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
			callback.onFailure(e);
		}
	}
	/**
	 * @param reviewAuthor the author to set
	 */
	public void setReviewAuthor(String reviewAuthor) {
		this.reviewAuthor = reviewAuthor;
	}
	/**
	 * @param documentId the documentId to set
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * @param reviewName the reviewName to set
	 */
	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	/**
	 * @param urlTmlServlet the urlTmlServlet to set
	 */
	public void setUrlTmlServlet(String urlTmlServlet) {
		this.urlTmlServlet = urlTmlServlet;
	}

}
