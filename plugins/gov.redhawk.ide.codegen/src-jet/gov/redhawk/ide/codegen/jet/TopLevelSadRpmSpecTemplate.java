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
package gov.redhawk.ide.codegen.jet;

import gov.redhawk.ide.codegen.RedhawkCodegenActivator;
import gov.redhawk.model.sca.util.ModelUtil;
import gov.redhawk.ide.codegen.util.ProjectCreator;
import java.util.List;
import mil.jpeojtrs.sca.util.ScaEcoreUtils;
import mil.jpeojtrs.sca.partitioning.ComponentFile;
import mil.jpeojtrs.sca.partitioning.PartitioningPackage;
import mil.jpeojtrs.sca.sad.SadPackage;
import mil.jpeojtrs.sca.sad.SoftwareAssembly;
import mil.jpeojtrs.sca.sad.SadComponentInstantiation;
import mil.jpeojtrs.sca.spd.SoftPkg;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * @generated
 */
public class TopLevelSadRpmSpecTemplate
{

  protected static String nl;
  public static synchronized TopLevelSadRpmSpecTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    TopLevelSadRpmSpecTemplate result = new TopLevelSadRpmSpecTemplate();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "# RPM package for ";
  protected final String TEXT_2 = NL + NL + "# By default, the RPM will install to the standard REDHAWK SDR root location (/var/redhawk/sdr)" + NL + "# You can override this at install time using --prefix /new/sdr/root when invoking rpm (preferred method, if you must)" + NL + "%{!?_sdrroot: %global _sdrroot /var/redhawk/sdr}" + NL + "%define _prefix %{_sdrroot}" + NL + "Prefix: %{_prefix}" + NL + "" + NL + "Name: ";
  protected final String TEXT_3 = NL + "Summary: Waveform ";
  protected final String TEXT_4 = NL + "Version: ";
  protected final String TEXT_5 = NL + "Release: 1%{?dist}" + NL + "License: None" + NL + "Group: REDHAWK/Waveforms" + NL + "Source: %{name}-%{version}.tar.gz" + NL + "# Require the controller whose SPD is referenced" + NL + "Requires: ";
  protected final String TEXT_6 = NL + "# Require each referenced component" + NL + "Requires:";
  protected final String TEXT_7 = " ";
  protected final String TEXT_8 = NL + "BuildArch: noarch" + NL + "BuildRoot: %{_tmppath}/%{name}-%{version}" + NL + "" + NL + "%description";
  protected final String TEXT_9 = NL;
  protected final String TEXT_10 = NL + NL + "%prep" + NL + "%setup" + NL + "" + NL + "%install" + NL + "%__rm -rf $RPM_BUILD_ROOT" + NL + "%__mkdir_p \"$RPM_BUILD_ROOT%{_prefix}";
  protected final String TEXT_11 = "\"" + NL + "%__install -m 644 ";
  protected final String TEXT_12 = " $RPM_BUILD_ROOT%{_prefix}";
  protected final String TEXT_13 = "/";
  protected final String TEXT_14 = NL + NL + "%files" + NL + "%defattr(-,redhawk,redhawk)";
  protected final String TEXT_15 = NL;
  protected final String TEXT_16 = NL + "%{_prefix}";
  protected final String TEXT_17 = "/";
  protected final String TEXT_18 = NL;

  public String generate(Object argument) throws CoreException
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
    final SoftwareAssembly sad = (SoftwareAssembly) argument;
    
    // The assembly controller isn't specified initially after project creation; ignore if it's not specified, throw
    // an error if it is and we can't get the assembly controller
    final SadComponentInstantiation instance = (SadComponentInstantiation) ScaEcoreUtils.getFeature(sad, 
    		SadPackage.Literals.SOFTWARE_ASSEMBLY__ASSEMBLY_CONTROLLER, 
    		SadPackage.Literals.ASSEMBLY_CONTROLLER__COMPONENT_INSTANTIATION_REF, 
    		PartitioningPackage.Literals.COMPONENT_INSTANTIATION_REF__INSTANTIATION
    		);
    if (instance == null) {
    	return null;
    }
    final  SoftPkg controller = (SoftPkg) ScaEcoreUtils.getFeature(instance, 
    		PartitioningPackage.Literals.COMPONENT_INSTANTIATION__PLACEMENT,
    		PartitioningPackage.Literals.COMPONENT_PLACEMENT__COMPONENT_FILE_REF,
    		PartitioningPackage.Literals.COMPONENT_FILE_REF__FILE,
    		PartitioningPackage.Literals.COMPONENT_FILE__SOFT_PKG
    		);
    if (controller == null){
	    throw new CoreException(new Status(IStatus.ERROR, RedhawkCodegenActivator.PLUGIN_ID, "Unable to get assembly controller. Check your SAD file and Target SDR."));
    }
    
    final List<ComponentFile> componentFiles = sad.getComponentFiles().getComponentFile();
    final String waveformSubDir = "/dom/waveforms/" + sad.getName().replace('.', '/');
    final String directoryBlock = ProjectCreator.createDirectoryBlock("%dir %{_prefix}/dom/waveforms/" + sad.getName().replace('.', '/'));

    stringBuffer.append(TEXT_1);
    stringBuffer.append(sad.getName());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(sad.getName());
    stringBuffer.append(TEXT_3);
    stringBuffer.append(sad.getName());
    stringBuffer.append(TEXT_4);
    stringBuffer.append((sad.getVersion() != null && sad.getVersion().trim().length() > 0) ? sad.getVersion() : "1.0.0");
    stringBuffer.append(TEXT_5);
    stringBuffer.append(controller.getName());
    stringBuffer.append(TEXT_6);
    
    for (ComponentFile compFile : componentFiles) {
        SoftPkg softPkg = compFile.getSoftPkg();
        if (softPkg != null) {
        
    stringBuffer.append(TEXT_7);
    stringBuffer.append(softPkg.getName());
    
        } else {
          throw new CoreException(new Status(IStatus.ERROR, RedhawkCodegenActivator.PLUGIN_ID, "Unable to locate component file. Check your SAD file and Target SDR."));
        }
    }

    stringBuffer.append(TEXT_8);
    
    if (sad.getDescription() != null) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(sad.getDescription());
    
    }

    stringBuffer.append(TEXT_10);
    stringBuffer.append(waveformSubDir);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(ModelUtil.getResource(sad).getName());
    stringBuffer.append(TEXT_12);
    stringBuffer.append(waveformSubDir);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(ModelUtil.getResource(sad).getName());
    stringBuffer.append(TEXT_14);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(directoryBlock);
    stringBuffer.append(TEXT_16);
    stringBuffer.append(waveformSubDir);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(ModelUtil.getResource(sad).getName());
    stringBuffer.append(TEXT_18);
    return stringBuffer.toString();
  }
}

// END GENERATED CODE