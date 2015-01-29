/*******************************************************************************
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 *
 * This file is part of REDHAWK IDE.
 *
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package gov.redhawk.ide.graphiti.ui.properties;

import gov.redhawk.ide.graphiti.ext.impl.RHContainerShapeImpl;
import gov.redhawk.ide.graphiti.ui.diagram.util.DUtil;
import mil.jpeojtrs.sca.partitioning.UsesPortStub;

import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter;

/**
 * 
 */
public class UsesPortFilter extends AbstractPropertySectionFilter {

	/**
	 * 
	 */
	public UsesPortFilter() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.ui.platform.AbstractPropertySectionFilter#accept(org.eclipse.graphiti.mm.pictograms.PictogramElement)
	 */
	@Override
	protected boolean accept(PictogramElement pictogramElement) {
		ContainerShape containerShape = (ContainerShape) DUtil.findContainerShapeParentWithProperty(pictogramElement,
			RHContainerShapeImpl.SHAPE_USES_PORT_RECTANGLE);
		Object obj = DUtil.getBusinessObject(containerShape);
		if (containerShape != null && obj != null && obj instanceof UsesPortStub) {
			return true;
		}
		return false;
	}

}