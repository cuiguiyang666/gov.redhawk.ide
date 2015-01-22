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

// BEGIN GENERATED CODE
package gov.redhawk.ide.debug.impl;

import gov.redhawk.ide.debug.LocalAbstractComponent;
import gov.redhawk.ide.debug.LocalLaunch;
import gov.redhawk.ide.debug.LocalScaComponent;
import gov.redhawk.ide.debug.ScaDebugPackage;
import gov.redhawk.model.sca.impl.ScaComponentImpl;
import gov.redhawk.sca.util.SilentJob;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import CF.LifeCyclePackage.ReleaseError;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Local Sca Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link gov.redhawk.ide.debug.impl.LocalScaComponentImpl#getLaunch <em>Launch</em>}</li>
 *   <li>{@link gov.redhawk.ide.debug.impl.LocalScaComponentImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link gov.redhawk.ide.debug.impl.LocalScaComponentImpl#getImplementationID <em>Implementation ID</em>}</li>
 *   <li>{@link gov.redhawk.ide.debug.impl.LocalScaComponentImpl#getExecParam <em>Exec Param</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LocalScaComponentImpl extends ScaComponentImpl implements LocalScaComponent {
	/**
	 * The default value of the '{@link #getLaunch() <em>Launch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLaunch()
	 * @generated
	 * @ordered
	 */
	protected static final ILaunch LAUNCH_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getLaunch() <em>Launch</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLaunch()
	 * @generated
	 * @ordered
	 */
	protected ILaunch launch = LAUNCH_EDEFAULT;
	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final String MODE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected String mode = MODE_EDEFAULT;
	/**
	 * The default value of the '{@link #getImplementationID() <em>Implementation ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplementationID()
	 * @generated
	 * @ordered
	 */
	protected static final String IMPLEMENTATION_ID_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getImplementationID() <em>Implementation ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImplementationID()
	 * @generated
	 * @ordered
	 */
	protected String implementationID = IMPLEMENTATION_ID_EDEFAULT;
	/**
	 * The default value of the '{@link #getExecParam() <em>Exec Param</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getExecParam()
	 * @generated
	 * @ordered
	 */
	protected static final String EXEC_PARAM_EDEFAULT = "";
	/**
	 * The cached value of the '{@link #getExecParam() <em>Exec Param</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getExecParam()
	 * @generated
	 * @ordered
	 */
	protected String execParam = EXEC_PARAM_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocalScaComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScaDebugPackage.Literals.LOCAL_SCA_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ILaunch getLaunch() {
		return launch;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLaunch(ILaunch newLaunch) {
		ILaunch oldLaunch = launch;
		launch = newLaunch;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH, oldLaunch, launch));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMode(String newMode) {
		String oldMode = mode;
		mode = newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getImplementationID() {
		return implementationID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setImplementationID(String newImplementationID) {
		String oldImplementationID = implementationID;
		implementationID = newImplementationID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID, oldImplementationID, implementationID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getExecParam() {
		return execParam;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setExecParam(String newExecParam) {
		String oldExecParam = execParam;
		execParam = newExecParam;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM, oldExecParam, execParam));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH:
				return getLaunch();
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE:
				return getMode();
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID:
				return getImplementationID();
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM:
				return getExecParam();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH:
				setLaunch((ILaunch)newValue);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE:
				setMode((String)newValue);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID:
				setImplementationID((String)newValue);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM:
				setExecParam((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH:
				setLaunch(LAUNCH_EDEFAULT);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE:
				setMode(MODE_EDEFAULT);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID:
				setImplementationID(IMPLEMENTATION_ID_EDEFAULT);
				return;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM:
				setExecParam(EXEC_PARAM_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH:
				return LAUNCH_EDEFAULT == null ? launch != null : !LAUNCH_EDEFAULT.equals(launch);
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE:
				return MODE_EDEFAULT == null ? mode != null : !MODE_EDEFAULT.equals(mode);
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID:
				return IMPLEMENTATION_ID_EDEFAULT == null ? implementationID != null : !IMPLEMENTATION_ID_EDEFAULT.equals(implementationID);
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM:
				return EXEC_PARAM_EDEFAULT == null ? execParam != null : !EXEC_PARAM_EDEFAULT.equals(execParam);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class< ? > baseClass) {
		if (baseClass == LocalLaunch.class) {
			switch (derivedFeatureID) {
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH:
				return ScaDebugPackage.LOCAL_LAUNCH__LAUNCH;
			case ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE:
				return ScaDebugPackage.LOCAL_LAUNCH__MODE;
			default:
				return -1;
			}
		}
		if (baseClass == LocalAbstractComponent.class) {
			switch (derivedFeatureID) {
				case ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID: return ScaDebugPackage.LOCAL_ABSTRACT_COMPONENT__IMPLEMENTATION_ID;
				case ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM: return ScaDebugPackage.LOCAL_ABSTRACT_COMPONENT__EXEC_PARAM;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class< ? > baseClass) {
		if (baseClass == LocalLaunch.class) {
			switch (baseFeatureID) {
			case ScaDebugPackage.LOCAL_LAUNCH__LAUNCH:
				return ScaDebugPackage.LOCAL_SCA_COMPONENT__LAUNCH;
			case ScaDebugPackage.LOCAL_LAUNCH__MODE:
				return ScaDebugPackage.LOCAL_SCA_COMPONENT__MODE;
			default:
				return -1;
			}
		}
		if (baseClass == LocalAbstractComponent.class) {
			switch (baseFeatureID) {
				case ScaDebugPackage.LOCAL_ABSTRACT_COMPONENT__IMPLEMENTATION_ID: return ScaDebugPackage.LOCAL_SCA_COMPONENT__IMPLEMENTATION_ID;
				case ScaDebugPackage.LOCAL_ABSTRACT_COMPONENT__EXEC_PARAM: return ScaDebugPackage.LOCAL_SCA_COMPONENT__EXEC_PARAM;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (launch: ");
		result.append(launch);
		result.append(", mode: ");
		result.append(mode);
		result.append(", implementationID: ");
		result.append(implementationID);
		result.append(", execParam: ");
		result.append(execParam);
		result.append(')');
		return result.toString();
	}

	@Override
	public void dispose() {
		// END GENERATED CODE
		Job job = new SilentJob("Local Component Release job") {

			@Override
			protected IStatus runSilent(IProgressMonitor monitor) {
				try {
					releaseObject();
				} catch (final ReleaseError e) {
					// PASS
				}
				return Status.OK_STATUS;
			}

		};
		job.setUser(false);
		job.setSystem(true);
		job.schedule();

		super.dispose();
		// BEGIN GENERATED CODE
	}

	@Override
	public void releaseObject() throws ReleaseError {
		// END GENERATED CODE
		final String tmpName = getName();
		super.releaseObject();
		if (this.launch != null) {
			final Job terminateJob = new TerminateJob(this, tmpName);
			terminateJob.schedule(5000);
		}
		// BEGIN GENERATED CODE
	}

	@Override
	public void unsetProfileURI() {
		// END GENERATED CODE
		// BEGIN GENERATED CODE

	}
	
	@Override
	public void unsetComponentInstantiation() {
		// END GENERATED CODE
		// BEGIN GENERATED CODE
		
	}
	
	/*
	 * @see gov.redhawk.model.sca.impl.ScaAbstractComponentImpl#unsetIdentifier()
	 */
	@Override
	public void unsetIdentifier() {
		// END GENERATED CODE
		// BEGIN GENERATED CODE
	}

} // LocalScaComponentImpl
