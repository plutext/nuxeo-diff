/*
 * (C) Copyright 2006-20012 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 *     Antoine Taillefer
 */
package org.nuxeo.ecm.diff.detaileddiff;

import java.util.List;

import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * Interface for the HTML Detailed diff DocumentModel adapter.
 *
 * @author Antoine Taillefer
 * @since 5.6
 */
public interface HtmlDetailedDiffAdapter {

    boolean cachable();

    String getFileDetailedDiffURL(DocumentModel otherDoc);

    String getFileDetailedDiffURL(DocumentModel otherDoc, String xpath);

    List<Blob> getFileDetailedDiffBlobs(DocumentModel otherDoc)
            throws DetailedDiffException;

    List<Blob> getFileDetailedDiffBlobs(DocumentModel otherDoc, String xpath)
            throws DetailedDiffException;

//    List<Blob> getFileDetailedDiffBlobs(DocumentModel otherDoc,
//            boolean postProcess) throws DetailedDiffException;
//
//    List<Blob> getFileDetailedDiffBlobs(DocumentModel otherDoc, String xpath,
//            boolean postProcess) throws DetailedDiffException;

    void setAdaptedDocument(DocumentModel doc);

    void cleanup();

}
