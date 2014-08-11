/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.plugin.angularjs.core.client.wizard;

import com.codenvy.api.project.gwt.client.ProjectServiceClient;
import com.codenvy.api.project.shared.dto.ProjectDescriptor;
import com.codenvy.api.project.shared.dto.ProjectReference;
import com.codenvy.ide.api.event.ProjectActionEvent_2;
import com.codenvy.ide.api.projecttype.wizard.ProjectWizard;
import com.codenvy.ide.api.wizard.AbstractWizardPage;
import com.codenvy.ide.dto.DtoFactory;
import com.codenvy.ide.rest.AsyncRequestCallback;
import com.codenvy.ide.rest.DtoUnmarshallerFactory;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Florent Benoit
 */
@Singleton
public class AngularPagePresenter extends AbstractWizardPage implements AngularPageView.ActionDelegate {

    private AngularPageView        view;
    private ProjectServiceClient   projectServiceClient;
    private EventBus               eventBus;
    private DtoFactory             factory;
    private DtoUnmarshallerFactory dtoUnmarshallerFactory;

    @Inject
    public AngularPagePresenter(AngularPageView view, ProjectServiceClient projectServiceClient, EventBus eventBus,
                                DtoFactory factory, DtoUnmarshallerFactory dtoUnmarshallerFactory) {
        super("AngularJS project settings", null);
        this.view = view;
        this.projectServiceClient = projectServiceClient;
        this.eventBus = eventBus;
        this.factory = factory;
        this.dtoUnmarshallerFactory = dtoUnmarshallerFactory;
        view.setDelegate(this);
    }

    @Nullable
    @Override
    public String getNotice() {
        return null;
    }

    @Override
    public boolean isCompleted() {
        return true;
    }

    @Override
    public void focusComponent() {

    }

    @Override
    public void removeOptions() {

    }

    @Override
    public void go(AcceptsOneWidget container) {
        container.setWidget(view);
        ProjectDescriptor project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {
            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                @Override
                public void execute() {
                }
            });
        }
    }

    @Override
    public void commit(@NotNull final CommitCallback callback) {

        // TODO: Allow to pickup the runner name
        Map<String, List<String>> options = new HashMap<>();
        options.put("runner.name", Arrays.asList("grunt"));

        final ProjectDescriptor projectDescriptorToUpdate = factory.createDto(ProjectDescriptor.class);
        projectDescriptorToUpdate.withProjectTypeId(wizardContext.getData(ProjectWizard.PROJECT_TYPE).getProjectTypeId());

        projectDescriptorToUpdate.setAttributes(options);
        boolean visibility = wizardContext.getData(ProjectWizard.PROJECT_VISIBILITY);
        projectDescriptorToUpdate.setVisibility(visibility ? "public" : "private");
        final String name = wizardContext.getData(ProjectWizard.PROJECT_NAME);
        final ProjectDescriptor project = wizardContext.getData(ProjectWizard.PROJECT);
        if (project != null) {
            if (project.getName().equals(name)) {
                updateProject(project, projectDescriptorToUpdate, callback);
            } else {
                projectServiceClient.rename(project.getPath(), name, null, new AsyncRequestCallback<Void>() {
                    @Override
                    protected void onSuccess(Void result) {
                        project.setName(name);

                        updateProject(project, projectDescriptorToUpdate, callback);
                    }

                    @Override
                    protected void onFailure(Throwable exception) {
                        callback.onFailure(exception);
                    }
                });
            }

        } else {
            createProject(callback, projectDescriptorToUpdate, name);
        }
    }

    private void updateProject(final ProjectDescriptor project, ProjectDescriptor projectDescriptorToUpdate, final CommitCallback callback) {
        projectServiceClient.updateProject(project.getPath(), projectDescriptorToUpdate, new AsyncRequestCallback<ProjectDescriptor>() {
            @Override
            protected void onSuccess(ProjectDescriptor result) {
                ProjectReference projectToOpen = factory.createDto(ProjectReference.class)
                                                        .withPath(project.getPath())
                                                        .withName(project.getName());
                eventBus.fireEvent(ProjectActionEvent_2.createOpenProjectEvent(projectToOpen));
                callback.onSuccess();
            }

            @Override
            protected void onFailure(Throwable exception) {
                callback.onFailure(exception);
            }
        });
    }

    private void createProject(final CommitCallback callback, ProjectDescriptor projectDescriptor, final String name) {
        projectServiceClient
                .createProject(name, projectDescriptor,
                               new AsyncRequestCallback<ProjectDescriptor>(dtoUnmarshallerFactory.newUnmarshaller(ProjectDescriptor.class)) {


                                   @Override
                                   protected void onSuccess(ProjectDescriptor result) {
                                       ProjectReference projectToOpen = factory.createDto(ProjectReference.class)
                                                                               .withPath(result.getPath())
                                                                               .withName(result.getName());
                                       eventBus.fireEvent(ProjectActionEvent_2.createOpenProjectEvent(projectToOpen));
                                       callback.onSuccess();
                                   }

                                   @Override
                                   protected void onFailure(Throwable exception) {
                                       callback.onFailure(exception);
                                   }
                               }
                              );
    }

}
