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
package gov.redhawk.ide.graphiti.ui.diagram.features.custom;

import gov.redhawk.ide.graphiti.ext.RHContainerShape;

import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.services.Graphiti;

public class CollapseShapeFeature extends AbstractCustomFeature {

	/**
	 * Constructor
	 */
	public CollapseShapeFeature(IFeatureProvider fp) {
		super(fp);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.AbstractCustomFeature#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Collapse Shape"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.graphiti.features.impl.AbstractFeature#getName()
	 */
	@Override
	public String getName() {
		return "&Collapse Shape"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.AbstractCustomFeature#canExecute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public boolean canExecute(ICustomContext context) {
		PictogramElement[] pes = context.getPictogramElements();
		boolean foundRHContainerShape = false;
		if (pes != null) {
			for (PictogramElement p: pes) {
				if (!(p instanceof RHContainerShape)) {
					return false;
				} else if (((RHContainerShape) p).isCollapsed()) {
					return false;
				} else {
					foundRHContainerShape = true;
				}
			}
		}
		
		return foundRHContainerShape;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.graphiti.features.custom.ICustomFeature#execute(org.eclipse.graphiti.features.context.ICustomContext)
	 */
	@Override
	public void execute(ICustomContext context) {
		//collapse all selected shapes
		for (PictogramElement p: context.getPictogramElements()) {
			RHContainerShape rhContainerShape = (RHContainerShape) p;
			rhContainerShape.setCollapsed(true);
			updatePictogramElement(rhContainerShape);

			// Force the layout to use the minimum width and height
			Graphiti.getGaLayoutService().setSize(rhContainerShape.getGraphicsAlgorithm(), 0, 0);
			layoutPictogramElement(rhContainerShape);
		}

		updatePictogramElement(getDiagram());
	}
	
	@Override
	public String getImageId() {
		return gov.redhawk.ide.graphiti.ui.diagram.providers.ImageProvider.IMG_COLLAPSE;
	}
}
