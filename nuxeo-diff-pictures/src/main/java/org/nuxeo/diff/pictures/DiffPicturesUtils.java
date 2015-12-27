/*
 * (C) Copyright 2015 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     thibaud
 */
package org.nuxeo.diff.pictures;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.core.api.Blob;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.platform.picture.api.ImageInfo;
import org.nuxeo.ecm.platform.picture.api.ImagingService;
import org.nuxeo.runtime.api.Framework;

/**
 * @since 7.4
 */
public class DiffPicturesUtils {

    /*
     * Centralize handling of the targetFileName (used in at least 3 operations => less code in the operation itself)
     */
    public static String updateTargetFileName(Blob inBlob, String inTargetFileName, String inTargetFileSuffix) {

        String updatedName = "";

        if (inTargetFileName == null || inTargetFileName.isEmpty()) {
            updatedName = inBlob.getFilename();
        } else {
            updatedName = inTargetFileName;
        }

        if (inTargetFileSuffix != null && !inTargetFileSuffix.isEmpty()) {
            updatedName = DiffPicturesUtils.addSuffixToFileName(updatedName, inTargetFileSuffix);
        }

        return updatedName;
    }

    /*
     * Adds the suffix before the file extension, if any
     */
    public static String addSuffixToFileName(String inFileName, String inSuffix) {
        if (inFileName == null || inFileName.isEmpty() || inSuffix == null || inSuffix.isEmpty()) {
            return inFileName;
        }

        int dotIndex = inFileName.lastIndexOf('.');
        if (dotIndex < 0) {
            return inFileName + inSuffix;
        }

        return inFileName.substring(0, dotIndex) + inSuffix + inFileName.substring(dotIndex);
    }

    /**
     * Check if the 2 blobs have the same format and same size. If yes, then the quick-compare ImageMagick command can
     * be used.
     * <p>
     * If blobs are null or are not pictures, we do nothing, it will fails with later (or here with a null pointer
     * exception)
     * 
     * @param inB1
     * @param inB2
     * @return true if the 2 blobs are pictures with same format and dimensions
     * @since 7.10
     */
    public static boolean sameFormatAndDimensions(Blob inB1, Blob inB2) {

        boolean result = true;

        String mt1 = inB1.getMimeType().toLowerCase();
        String mt2 = inB2.getMimeType().toLowerCase();
        if (!StringUtils.equals(mt1, mt2)) {
            result = false;
        } else {
            // Mime types are the same, check dimensions
            ImagingService imagingService = Framework.getService(ImagingService.class);
            ImageInfo info1 = imagingService.getImageInfo(inB1);
            ImageInfo info2 = imagingService.getImageInfo(inB2);
            if (info1.getWidth() != info2.getWidth() || info1.getHeight() != info2.getHeight()) {
                result = false;
            }
        }

        return result;
    }
    
    public static Blob getDocumentBlob(DocumentModel inDoc, String inXPath) {
        
       Blob b;
       
       if (StringUtils.isBlank(inXPath) || "null".equals(inXPath) || "default".equals(inXPath)) {
            b = (Blob) inDoc.getPropertyValue(DiffPictures.DEFAULT_XPATH);
        } else {
            b = (Blob) inDoc.getPropertyValue(inXPath);
        }
       
       return b;
    }

}
