/**
 * This file is protected by Copyright. 
 * Please refer to the COPYRIGHT file distributed with this source distribution.
 * 
 * This file is part of REDHAWK IDE.
 * 
 * All rights reserved.  This program and the accompanying materials are made available under 
 * the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 *
 */
package gov.redhawk.ide.graphiti.dcd.internal.ui.editor;

import gov.redhawk.ide.graphiti.dcd.ui.DCDUIGraphitiPlugin;
import gov.redhawk.ide.graphiti.dcd.ui.diagram.DcdDiagramUtilHelper;
import gov.redhawk.ide.graphiti.dcd.ui.diagram.GraphitiDcdDiagramEditor;
import gov.redhawk.ide.graphiti.dcd.ui.diagram.providers.DCDDiagramTypeProvider;
import gov.redhawk.ide.graphiti.ui.diagram.util.DUtil;
import gov.redhawk.ide.internal.ui.handlers.CleanUpComponentFilesAction;
import gov.redhawk.model.sca.util.ModelUtil;
import gov.redhawk.ui.editor.SCAFormEditor;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import mil.jpeojtrs.sca.dcd.DcdPackage;
import mil.jpeojtrs.sca.dcd.DeviceConfiguration;
import mil.jpeojtrs.sca.dcd.provider.DcdItemProviderAdapterFactory;
import mil.jpeojtrs.sca.prf.provider.PrfItemProviderAdapterFactory;
import mil.jpeojtrs.sca.scd.provider.ScdItemProviderAdapterFactory;
import mil.jpeojtrs.sca.spd.provider.SpdItemProviderAdapterFactory;

import org.eclipse.core.commands.operations.DefaultOperationHistory;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.provider.EcoreItemProviderAdapterFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalCommandStack;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.impl.TransactionalEditingDomainImpl;
import org.eclipse.emf.workspace.IWorkspaceCommandStack;
import org.eclipse.emf.workspace.WorkspaceEditingDomainFactory;
import org.eclipse.gef.EditPart;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramLink;
import org.eclipse.graphiti.mm.pictograms.PictogramsFactory;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.ui.editor.DiagramEditor;
import org.eclipse.graphiti.ui.editor.DiagramEditorInput;
import org.eclipse.graphiti.ui.internal.editor.GFWorkspaceCommandStackImpl;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageSelectionProvider;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

public class GraphitiDcdMultipageEditor extends SCAFormEditor implements ITabbedPropertySheetPageContributor, IViewerProvider {

	public static final String ID = "gov.redhawk.ide.graphiti.dcd.ui.editor.DcdEditor";

	public static final String EDITING_DOMAIN_ID = "mil.jpeojtrs.sca.dcd.diagram.EditingDomain";
	
	/**
	 * This is used to manually override the dirty state. It can be used to avoid marking the editor as dirty on trivial
	 * or hidden actions, such as linking the diagram to the sad.xml
	 */
	private boolean isDirtyAllowed = true;

	/**
	 * The graphical diagram editor embedded into this editor.
	 */
	private DiagramEditor diagramEditor;

	/**
	 * This keeps track of the active content viewer, which may be either one of
	 * the viewers in the pages or the content outline viewer.
	 */
	private Viewer currentViewer;

	/**
	 * This selection provider coordinates the selections of the various editor
	 * parts.
	 */
	private final MultiPageSelectionProvider selectionProvider;

	private GraphitiDcdOverviewPage overviewPage;

	private ResourceListener nameListener;

	private GraphitiDcdDevicesPage devicesPage;

	private IEditorPart textEditor;

	private int dcdSourcePageNum;

	private class ResourceListener extends AdapterImpl {
		private DeviceConfiguration dcd;
		private final Resource dcdResource;

		public ResourceListener(final Resource dcdResource) {
			this.dcdResource = dcdResource;
			if (this.dcdResource != null) {
				this.dcdResource.eAdapters().add(this);
				this.dcd = getDeviceConfiguration();
				if (this.dcd != null) {
					this.dcd.eAdapters().add(this);
					updateTitle();
				}
			}
		}

		/**
		 * Gets the soft pkg.
		 * 
		 * @return the soft pkg
		 */
		private DeviceConfiguration getDeviceConfiguration() {
			return ModelUtil.getDeviceConfiguration(this.dcdResource);
		}

		public void dispose() {
			if (this.dcd != null) {
				this.dcd.eAdapters().remove(this);
			}
			if (this.dcdResource != null) {
				this.dcdResource.eAdapters().remove(this);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void notifyChanged(final Notification msg) {
			if (msg.getNotifier() instanceof Resource) {
				switch (msg.getFeatureID(Resource.class)) {
				case Resource.RESOURCE__IS_LOADED:
					if (this.dcd != null) {
						this.dcd.eAdapters().remove(this);
						this.dcd = null;
					}
					if (this.dcdResource.isLoaded()) {
						this.dcd = getDeviceConfiguration();
						if (this.dcd != null) {
							this.dcd.eAdapters().add(this);

							updateTitle();
						}
					}
					break;
				default:
					break;
				}
			} else if (msg.getNotifier() instanceof DeviceConfiguration) {
				final int featureID = msg.getFeatureID(DeviceConfiguration.class);

				if (featureID == DcdPackage.DEVICE_CONFIGURATION__NAME) {
					if (msg.getEventType() == Notification.SET) {
						updateTitle();
					}
				}
			}
		}
	}

	public GraphitiDcdMultipageEditor() {
		super();
		this.selectionProvider = new MultiPageSelectionProvider(this);
		this.selectionProvider.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(final SelectionChangedEvent event) {
				setStatusLineManager(event.getSelection());
			}
		});
	}

	/**
	 * This sets the selection into whichever viewer is active.
	 */
	@Override
	public void setSelectionToViewer(final Collection< ? > collection) {
		final Collection< ? > theSelection = collection;
		// Make sure it's okay.
		//
		if (theSelection != null && !theSelection.isEmpty()) {
			// I don't know if this should be run this deferred
			// because we might have to give the editor a chance to process the
			// viewer update events
			// and hence to update the views first.
			//
			//
			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					// Try to select the items in the current content viewer of
					// the editor.
					//
					if (GraphitiDcdMultipageEditor.this.currentViewer != null) {
						GraphitiDcdMultipageEditor.this.currentViewer.setSelection(new StructuredSelection(theSelection.toArray()), true);
					}
				}
			};
			runnable.run();
		}
	}

	/**
	 * This makes sure that one content viewer, either for the current page or
	 * the outline view, if it has focus, is the current one.
	 */
	public void setCurrentViewer(final Viewer viewer) {
		// If it is changing...
		//
		if (this.currentViewer != viewer) {
			// Remember it.
			//
			this.currentViewer = viewer;
		}
	}

	/**
	 * This returns the viewer as required by the {@link IViewerProvider} interface.
	 */
	@Override
	public Viewer getViewer() {
		return this.currentViewer;
	}

	/**
	 * This is how the framework determines which interfaces we implement.
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(final Class key) {
		// TODO: do we not need a property sheet page?
//		if (key.equals(IPropertySheetPage.class)) {
//			return getPropertySheetPage();
//		} else if (key.equals(IGotoMarker.class)) {
//			return this;
//		} else {
//			return super.getAdapter(key);
//		}

		if (key.equals(IGotoMarker.class)) {
			return this;
		} else {
			return super.getAdapter(key);
		}
	}

	@Override
	public void gotoMarker(final IMarker marker) {
		try {
			if (marker.getType().equals(EValidator.MARKER)) {
				final String uriAttribute = marker.getAttribute(EValidator.URI_ATTRIBUTE, null);
				if (uriAttribute != null) {
					final URI uri = URI.createURI(uriAttribute);
					final EObject eObject = getEditingDomain().getResourceSet().getEObject(uri, true);
					if (eObject != null) {
						setSelectionToViewer(Collections.singleton(getWrapper(eObject)));
					}
				}
			}
		} catch (final CoreException exception) {
			StatusManager.getManager().handle(new Status(IStatus.ERROR, DCDUIGraphitiPlugin.PLUGIN_ID, "Failed to go to marker.", exception),
				StatusManager.LOG | StatusManager.SHOW);
		}
	}

	private Object getWrapper(final EObject eObject) {
		return AdapterFactoryEditingDomain.getWrapper(eObject, getEditingDomain());
	}

	public void setStatusLineManager(final ISelection selection) {
		final IStatusLineManager statusLineManager;

		statusLineManager = getActionBars().getStatusLineManager();

		if (statusLineManager != null) {
			if (selection instanceof IStructuredSelection) {
				final Collection< ? > collection = ((IStructuredSelection) selection).toList();
				switch (collection.size()) {
				case 0:
					statusLineManager.setMessage("Selected Nothing");
					break;
				case 1:
					final String text = new AdapterFactoryItemDelegator(getAdapterFactory()).getText(collection.iterator().next());
					statusLineManager.setMessage(MessageFormat.format("Selected Object: {0}", text));
					break;
				default:
					statusLineManager.setMessage(MessageFormat.format("Selected {0} Objects", Integer.toString(collection.size())));
					break;
				}
			} else {
				statusLineManager.setMessage("");
			}
		}
	}

	@Override
	public boolean isDirty() {
		if (this.getMainResource() != null && !this.getMainResource().getURI().isPlatform()) {
			return false;
		}
		return super.isDirty();
	}
	
	@Override
	protected boolean computeDirtyState() {
		if (!isDirtyAllowed()) {
			setDirtyAllowed(true);
			return false;
		}
		int activePage = getActivePage();
		if (activePage == -1) {
			return false;
		} else if (activePage == getPageCount() - 1) {
			return textEditor.isDirty();
		}
		BasicCommandStack commandStack = (BasicCommandStack) diagramEditor.getEditingDomain().getCommandStack();
		return commandStack.isSaveNeeded();
	}

	/**
	 * This implements {@link org.eclipse.jface.action.IMenuListener} to help
	 * fill the context menus with contributions from the Edit menu.
	 */
	@Override
	public void menuAboutToShow(final IMenuManager menuManager) {
		((IMenuListener) getEditorSite().getActionBarContributor()).menuAboutToShow(menuManager);
	}

	@Override
	public void dispose() {

		if (this.nameListener != null) {
			this.nameListener.dispose();
			this.nameListener = null;
		}
		super.dispose();
	}

	@Override
	public String getContributorId() {
		if (this.diagramEditor == null) {
			return null;
		}
		return this.diagramEditor.getContributorId();
	}

	@Override
	public String getTitle() {
		String name = null;
		final DeviceConfiguration dcd = getDeviceConfiguration();
		if (dcd != null) {
			name = dcd.getName();
			if (name == null) {
				name = getEditorInput().getName();
			}
		}
		if (name != null) {
			return name;
		} else {
			return super.getTitle();
		}
	}

	private DeviceConfiguration getDeviceConfiguration() {
		return ModelUtil.getDeviceConfiguration(getMainResource());
	}

	@Override
	protected void addPages() {
		// Only creates the other pages if there is something that can be edited
		//
		if (!getEditingDomain().getResourceSet().getResources().isEmpty()
			&& !(getEditingDomain().getResourceSet().getResources().get(0)).getContents().isEmpty()) {
			try {
				int pageIndex = 0;

				final Resource dcdResource = getMainResource();

				this.nameListener = new ResourceListener(dcdResource);

				this.overviewPage = new GraphitiDcdOverviewPage(this);
				this.overviewPage.setInput(dcdResource);
				this.addPage(this.overviewPage);

				this.devicesPage = new GraphitiDcdDevicesPage(this);
				this.devicesPage.setInput(dcdResource);
				addPage(this.devicesPage);

				// This is the page for the graphical diagram viewer
				final DiagramEditor editor = new GraphitiDcdDiagramEditor((TransactionalEditingDomain) getEditingDomain());
				setDiagramEditor(editor);
				final IEditorInput diagramInput = createDiagramInput(dcdResource);
				pageIndex = addPage(editor, diagramInput);
				setPageText(pageIndex, "Diagram");

				// TODO: Do we need to set layout like in SAD Diagram?
				// set layout for target-sdr editors
				// DUtil.layout(editor);

				// StructuredTextEditors only work on workspace entries because
				// org.eclipse.wst.sse.core.FileBufferModelManager:bufferCreated()
				// assumes that the editor input is in the workspace.
				if (getEditorInput() instanceof FileEditorInput) {
					try {
						textEditor = new StructuredTextEditor();
						this.dcdSourcePageNum = addPage(textEditor, this.getEditorInput());
					} catch (final NoClassDefFoundError e) {
						textEditor = new TextEditor();
						this.dcdSourcePageNum = addPage(textEditor, this.getEditorInput());
					}
				} else {
					textEditor = new TextEditor();
					this.dcdSourcePageNum = addPage(textEditor, this.getEditorInput());
				}
				this.setPageText(this.dcdSourcePageNum, this.getEditorInput().getName());

			} catch (final PartInitException e) {
				StatusManager.getManager().handle(new Status(IStatus.ERROR, DCDUIGraphitiPlugin.PLUGIN_ID, "Failed to add pages.", e));
			} catch (final IOException e) {
				StatusManager.getManager().handle(new Status(IStatus.ERROR, DCDUIGraphitiPlugin.PLUGIN_ID, "Failed to add pages.", e));
			} catch (final CoreException e) {
				StatusManager.getManager().handle(new Status(IStatus.ERROR, DCDUIGraphitiPlugin.PLUGIN_ID, "Failed to add pages.", e));
			}
		}
	}

	protected IEditorInput createDiagramInput(final Resource dcdResource) throws IOException, CoreException {
		final URI diagramURI = DUtil.getDiagramResourceURI(DcdDiagramUtilHelper.INSTANCE, dcdResource);

		DUtil.initializeDiagramResource(DcdDiagramUtilHelper.INSTANCE, DCDDiagramTypeProvider.DIAGRAM_TYPE_ID, DCDDiagramTypeProvider.PROVIDER_ID, diagramURI,
			dcdResource);

		Resource diagramResource = getEditingDomain().getResourceSet().getResource(diagramURI, true);

		// load diagram from resource
		final Diagram diagram = (Diagram) diagramResource.getContents().get(0);

		// load dcd from resource
		final DeviceConfiguration dcd = DeviceConfiguration.Util.getDeviceConfiguration(dcdResource);

		// link diagram with DeviceConfiguration
		TransactionalCommandStack stack = (TransactionalCommandStack) getEditingDomain().getCommandStack();
		stack.execute(new RecordingCommand((TransactionalEditingDomain) getEditingDomain()) {
			@Override
			protected void doExecute() {
				// Evade dirty check - we DON'T want this action to mark the editor as dirty
				GraphitiDcdMultipageEditor.this.setDirtyAllowed(false);

				// set property specifying diagram context (design, local, domain)
				Graphiti.getPeService().setPropertyValue(diagram, DUtil.DIAGRAM_CONTEXT, getDiagramContext(dcdResource));

				// link diagram and dcd
				PictogramLink link = PictogramsFactory.eINSTANCE.createPictogramLink();
				link.getBusinessObjects().add(dcd);
				diagram.setLink(link);
			}
		});

		// return editor input from diagram with sad diagram type
		return DiagramEditorInput.createEditorInput(diagram, DCDDiagramTypeProvider.PROVIDER_ID);
	}

	/**
	 * Returns the property value that should be set for the Diagram container's DIAGRAM_CONTEXT property.
	 * Indicates the mode the diagram is operating in.
	 * @return
	 */
	public String getDiagramContext(Resource dcdResource) {
		if (dcdResource.getURI().toString().matches(".*" + System.getenv("SDRROOT") + ".*")) {
			return DUtil.DIAGRAM_CONTEXT_TARGET_SDR;
		}

		return DUtil.DIAGRAM_CONTEXT_DESIGN;
	}

	protected void handleDocumentChange(final Resource resource) {
		super.handleDocumentChange(resource);
		for (final Object part : this.getDiagramEditor().getDiagramBehavior().getContentEditPart().getChildren()) {
			if (part instanceof EditPart) {
				((EditPart) part).refresh();
			}
		}
	}

	public DiagramEditor getDiagramEditor() {
		return this.diagramEditor;
	}

	@Override
	protected IContentOutlinePage createContentOutline() {
		// TODO what should we implement here? Anything?
		return null;
	}

	@Override
	public String getEditingDomainId() {
		return GraphitiDcdMultipageEditor.EDITING_DOMAIN_ID;
	}

	@Override
	protected AdapterFactory getSpecificAdapterFactory() {
		final ComposedAdapterFactory factory = new ComposedAdapterFactory();
		// TODO: How to handle this?
//		final DcdItemProviderAdapterFactoryAdapter dcdAdapter = new DcdItemProviderAdapterFactoryAdapter();
//		dcdAdapter.setComponentPlacementAdapter(new DevicesSectionComponentPlacementItemProvider(dcdAdapter));
//		factory.addAdapterFactory(dcdAdapter);
		factory.addAdapterFactory(new DcdItemProviderAdapterFactory());
		factory.addAdapterFactory(new EcoreItemProviderAdapterFactory());
		factory.addAdapterFactory(new SpdItemProviderAdapterFactory());
		factory.addAdapterFactory(new ScdItemProviderAdapterFactory());
		factory.addAdapterFactory(new PrfItemProviderAdapterFactory());
		return factory;
	}

	public IActionBars getActionBars() {
		return getActionBarContributor().getActionBars();
	}

	@Override
	public List<Object> getOutlineItems() {
		final List<Object> myList = new ArrayList<Object>();
		myList.add(this.overviewPage);
		myList.add(getDeviceConfiguration().getPartitioning());
		return myList;
	}

	protected void setDiagramEditor(final DiagramEditor diagramEditor) {
		this.diagramEditor = diagramEditor;
	}

	@Override
	protected void emfDoSave(IProgressMonitor progressMonitor) {
		diagramEditor.doSave(progressMonitor);
	}

	@Override
	public boolean isPersisted(final Resource resource) {
		if (resource == null || resource.getURI() == null) {
			return false;
		}
		if (getDeviceConfiguration() == null || getDeviceConfiguration().eResource() == null) {
			return false;
		}
		return resource.getURI().equals(getDeviceConfiguration().eResource().getURI()) && super.isPersisted(resource);
	}

	@Override
	public void doSave(final IProgressMonitor monitor) {
		final CleanUpComponentFilesAction cleanAction = new CleanUpComponentFilesAction();
		cleanAction.setRoot(getDeviceConfiguration());
		cleanAction.run();

		try {
			this.editorSaving = true;
			int activePage = getActivePage();
			if (textEditor.isDirty() && activePage == getPageCount() - 1) {
				textEditor.doSave(monitor);
				reload();
				emfDoSave(new SubProgressMonitor(monitor, 1));
			} else {
				commitPages(true);
				monitor.beginTask("Saving " + this.getTitle(), this.getPageCount() + 2);
				emfDoSave(new SubProgressMonitor(monitor, 1));
			}
			BasicCommandStack commandStack = (BasicCommandStack) diagramEditor.getEditingDomain().getCommandStack();
			commandStack.saveIsDone();
			editorDirtyStateChanged();
		} catch (final OperationCanceledException e) {
			// PASS
		} finally {
			monitor.done();
			this.editorSaving = false;
		}
	}

	@Override
	protected void resourceChanged(final IResource resource, final IResourceDelta delta) {
		// Make sure we don't call resource changed on a non sad resource
		if (this.isValidDcdResource(resource) && !delta.getResource().getWorkspace().isAutoBuilding()) {
			super.resourceChanged(resource, delta);
		}
		validate();
	}

	@Override
	public void reload() {
		super.reload();
		diagramEditor.getDiagramBehavior().getUpdateBehavior().setResourceChanged(true);
		diagramEditor.getDiagramBehavior().getUpdateBehavior().handleActivate();
	}

	/**
	 * Evaluate the given resource to determine if it is a resource that can be associated with the NodeEditor.
	 * 
	 * @param resource The IResource to evaluate
	 * @return <code> true </code> if this is a dcd resource; <code> false </code> otherwise
	 */
	private boolean isValidDcdResource(final IResource resource) {
		final String path = resource.getFullPath().toOSString();
		if (path.endsWith("dcd.xml")) {
			return true;
		}
		return false;
	}

	@Override
	@SuppressWarnings("restriction")
	protected TransactionalEditingDomain createEditingDomain() {

		final ResourceSet resourceSet = new ResourceSetImpl();
		final IWorkspaceCommandStack workspaceCommandStack = new GFWorkspaceCommandStackImpl(new DefaultOperationHistory());

		TransactionalEditingDomain domain = new TransactionalEditingDomainImpl(new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE),
			workspaceCommandStack, resourceSet);
		WorkspaceEditingDomainFactory.INSTANCE.mapResourceSet((TransactionalEditingDomain) domain);
		domain.setID(getEditingDomainId());

		// Create an adapter factory that yields item providers.
		//
		final ComposedAdapterFactory localAdapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		localAdapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		localAdapterFactory.addAdapterFactory(getSpecificAdapterFactory());
		localAdapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		((AdapterFactoryEditingDomain) domain).setAdapterFactory(localAdapterFactory);
		return domain;
	}

	public boolean isDirtyAllowed() {
		return isDirtyAllowed;
	}

	public void setDirtyAllowed(boolean isDirtyAllowed) {
		this.isDirtyAllowed = isDirtyAllowed;
	}
}