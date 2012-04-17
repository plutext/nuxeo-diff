/*
 * (C) Copyright 2012 Nuxeo SA (http://nuxeo.com/) and contributors.
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
 */
package org.nuxeo.ecm.diff.model.impl;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.nuxeo.ecm.diff.model.DiffDisplayBlock;
import org.nuxeo.ecm.platform.forms.layout.api.LayoutDefinition;

/**
 * Handles...
 *
 * @author <a href="mailto:ataillefer@nuxeo.com">Antoine Taillefer</a>
 */
public class DiffDisplayBlockImpl implements DiffDisplayBlock {

    private static final long serialVersionUID = 5777784629522360126L;

    protected String label;

    protected Map<String, Map<String, Serializable>> leftValue;

    protected Map<String, Map<String, Serializable>> rightValue;

    // TODO: remove?
    protected Map<String, Map<String, Serializable>> detailedDiffValue;

    protected LayoutDefinition layoutDefinition;

    public DiffDisplayBlockImpl(String label,
            Map<String, Map<String, Serializable>> leftValue,
            Map<String, Map<String, Serializable>> rightValue,
            Map<String, Map<String, Serializable>> detailedDiffValue,
            LayoutDefinition layoutDefinition) {
        this.label = label;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.detailedDiffValue = detailedDiffValue;
        this.layoutDefinition = layoutDefinition;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, Map<String, Serializable>> getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(Map<String, Map<String, Serializable>> leftValue) {
        this.leftValue = leftValue;
    }

    public Map<String, Map<String, Serializable>> getRightValue() {
        return rightValue;
    }

    public void setRightValue(Map<String, Map<String, Serializable>> rightValue) {
        this.rightValue = rightValue;
    }

    public Map<String, Map<String, Serializable>> getDetailedDiffValue() {
        return detailedDiffValue;
    }

    public void setDetailedDiffValue(
            Map<String, Map<String, Serializable>> detailedDiffValue) {
        this.detailedDiffValue = detailedDiffValue;
    }

    public LayoutDefinition getLayoutDefinition() {
        return layoutDefinition;
    }

    public void setLayoutDefinition(LayoutDefinition layoutDefinition) {
        this.layoutDefinition = layoutDefinition;
    }

    public boolean isEmpty() {
        return MapUtils.isEmpty(this.detailedDiffValue)
                && (MapUtils.isEmpty(this.leftValue) || MapUtils.isEmpty(this.rightValue));
    }
}
