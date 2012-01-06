/*
 * (C) Copyright 20012 Nuxeo SAS (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     ataillefer
 *
 */

package org.nuxeo.ecm.webapp.clipboard;

import static org.jboss.seam.ScopeType.CONVERSATION;

import java.io.Serializable;
import java.util.List;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.VersionModel;
import org.nuxeo.ecm.platform.diff.model.DocumentDiff;
import org.nuxeo.ecm.platform.diff.service.DocumentDiffService;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.runtime.api.Framework;

/**
 * Handles document diff actions.
 * 
 * @author <a href="mailto:ataillefer@nuxeo.com">Antoine Taillefer</a>
 */
@Name("documentDiffActions")
@Scope(CONVERSATION)
public class DocumentDiffActionsBean implements Serializable {

    private static final long serialVersionUID = -5507491210664361778L;

    private static final String DOC_DIFF_VIEW = "view_doc_diff";

    @In(create = true)
    protected transient DocumentsListsManager documentsListsManager;

    @In(create = true, required = false)
    protected transient NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    protected DocumentDiff documentDiff;

    /**
     * Checks if the diff action is available for the current document selection
     * working list.
     * <p>
     * Condition: the working list has exactly 2 documents.
     * 
     * @return true if can copy
     */
    public boolean getCanDiffCurrentSelection() {

        List<DocumentModel> currentSelectionWorkingList = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
        return currentSelectionWorkingList != null
                && currentSelectionWorkingList.size() == 2;

    }

    /**
     * Makes a diff of the current selection.
     * 
     * @return the view id
     * @throws ClientException the client exception
     */
    public String diffCurrentSelection() throws ClientException {

        List<DocumentModel> currentSelectionWorkingList = getCurrentSelectionWorkingList();

        DocumentDiff documentDiff = getDocumentDiffService().diff(
                documentManager, currentSelectionWorkingList.get(0),
                currentSelectionWorkingList.get(1));
        setDocumentDiff(documentDiff);

        return DOC_DIFF_VIEW;

    }

    /**
     * Refreshes the diff of the current selection.
     * 
     * @throws ClientException the client exception
     */
    public void refreshCurrentSelectionDiff() throws ClientException {

        // Get docs from current selection list
        List<DocumentModel> currentSelectionWorkingList = getCurrentSelectionWorkingList();
        DocumentModel leftDoc = currentSelectionWorkingList.get(0);
        DocumentModel rightDoc = currentSelectionWorkingList.get(1);

        // Fetch docs from repository
        leftDoc = documentManager.getDocument(leftDoc.getRef());
        rightDoc = documentManager.getDocument(rightDoc.getRef());

        DocumentDiff documentDiff = getDocumentDiffService().diff(
                documentManager, leftDoc, rightDoc);
        setDocumentDiff(documentDiff);

    }

    /**
     * Makes a diff of selected version with the live doc.
     * 
     * @param version the version
     * @return the string
     * @throws ClientException the client exception
     */
    public String diffCurrentVersion(VersionModel selectedVersion)
            throws ClientException {

        DocumentModel currentDocument = navigationContext.getCurrentDocument();
        if (currentDocument == null) {
            throw new ClientException(
                    "Cannot make a diff between selected version and current document since current document is null.");
        }

        DocumentModel docVersion = documentManager.getDocumentWithVersion(
                currentDocument.getRef(), selectedVersion);
        if (docVersion == null) {
            throw new ClientException(
                    "Cannot make a diff between selected version and current document since selected version document is null.");
        }

        DocumentDiff documentDiff = getDocumentDiffService().diff(
                documentManager, currentDocument, docVersion);
        setDocumentDiff(documentDiff);

        return DOC_DIFF_VIEW;

    }

    public DocumentDiff getDocumentDiff() {
        return documentDiff;
    }

    public void setDocumentDiff(DocumentDiff docDiff) {
        this.documentDiff = docDiff;
    }

    /**
     * Gets the current selection working list.
     * 
     * @return the current selection working list
     * @throws ClientException the client exception
     */
    protected final List<DocumentModel> getCurrentSelectionWorkingList()
            throws ClientException {

        List<DocumentModel> currentSelectionWorkingList = documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);

        if (currentSelectionWorkingList == null
                || currentSelectionWorkingList.size() != 2) {
            throw new ClientException(
                    "Cannot make a diff of the current selection: need to have exactly 2 documents in the working list");
        }
        return currentSelectionWorkingList;
    }

    /**
     * Gets the document diff service.
     * 
     * @return the document diff service
     * @throws ClientException if cannot get the document diff service
     */
    protected final DocumentDiffService getDocumentDiffService()
            throws ClientException {

        DocumentDiffService documentDiffService;

        try {
            documentDiffService = Framework.getService(DocumentDiffService.class);
        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
        if (documentDiffService == null) {
            throw new ClientException("DocumentDiffService is null.");
        }
        return documentDiffService;

    }

}
