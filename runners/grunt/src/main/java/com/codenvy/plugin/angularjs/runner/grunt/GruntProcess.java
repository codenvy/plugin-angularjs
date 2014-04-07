/**
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.core.util.CommandLine;
import com.codenvy.api.core.util.DownloadPlugin;
import com.codenvy.api.core.util.HttpDownloadPlugin;
import com.codenvy.api.project.server.ProjectEvent;
import com.codenvy.api.project.server.ProjectEventListener;
import com.codenvy.api.runner.RunnerException;
import com.codenvy.api.runner.internal.ApplicationLogger;
import com.codenvy.api.runner.internal.ApplicationProcess;
import com.codenvy.api.runner.internal.DeploymentSources;
import com.codenvy.commons.lang.ZipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * Application process used for Grunt
 *
 * @author Florent Benoit
 */
public class GruntProcess extends ApplicationProcess implements ProjectEventListener {

    /**
     * Logger.
     */
    private static final Logger LOG = LoggerFactory.getLogger(GruntProcess.class);

    /**
     * Result of the Runtime.exec command
     */
    private Process process;

    /**
     * Excutor service.
     */
    private ExecutorService executorService;

    /**
     * Directory where launch grunt.
     */
    private final File workDir;

    /**
     * Base URL for Rest API.
     */
    private String         baseURL;

    private DownloadPlugin downloadPlugin;

    /**
     * Build a new process for the following directory
     *
     * @param workDir
     *         the directory to start grunt
     */
    public GruntProcess(ExecutorService executorService, File workDir, String baseURL) {
        super();
        this.executorService = executorService;
        this.workDir = workDir;
        this.baseURL = baseURL;
        this.downloadPlugin = new HttpDownloadPlugin();
        ;
    }

    /**
     * Run the process is not yet done
     *
     * @throws RunnerException
     *         if command can't be launched
     */
    @Override
    public void start() throws RunnerException {
        if (process != null) {
            throw new IllegalStateException("Process is already started");
        }

        try {
            process = Runtime.getRuntime()
                             .exec(new CommandLine("grunt").add("serve").toShellCommand(), null, workDir);
        } catch (IOException e) {
            throw new RunnerException(e.getCause());
        }
    }

    /**
     * Stop the process
     *
     * @throws RunnerException
     */
    @Override
    public void stop() throws RunnerException {
        if (process == null) {
            throw new IllegalStateException("Process is not started yet");
        }
        process.destroy();
    }


    @Override
    public int waitFor() throws RunnerException {
        synchronized (this) {
            if (process == null) {
                throw new IllegalStateException("Process is not started yet");
            }
        }
        try {
            process.waitFor();
        } catch (InterruptedException ignored) {
        }
        return process.exitValue();
    }

    @Override
    public int exitCode() throws RunnerException {
        if (process == null) {
            return -1;
        }
        return process.exitValue();
    }

    @Override
    public boolean isRunning() throws RunnerException {
        return process != null;
    }

    @Override
    public ApplicationLogger getLogger() throws RunnerException {
        //FIXME : return a real logger
        return ApplicationLogger.DUMMY;
    }


    protected DeploymentSources downloadApplication(String url, File destFolder) throws RunnerException {
        final File downloadDir;
        try {
            downloadDir = Files.createTempDirectory(destFolder.toPath(), "updated").toFile();
        } catch (IOException e) {
            throw new RunnerException(e);
        }
        DownloadCallback downloadCallback = new DownloadCallback();
        downloadPlugin.download(url, downloadDir, downloadCallback);
        if (downloadCallback.getErrorHolder() != null) {
            throw new RunnerException(downloadCallback.getErrorHolder());
        }
        return downloadCallback.getResultHolder();
    }

    /**
     * There is a change on the project that we're monitoring, whatever the type of event is, we need to updated the runner.
     * @param event
     */
    @Override
    public void onEvent(ProjectEvent event) {
        if (event.getType() == ProjectEvent.EventType.UPDATED || event.getType() == ProjectEvent.EventType.CREATED) {
            // needs update
            update(event);
        }

    }

    /**
     * Update the current code through the executor service
     * Download the new source again and unpack.
     */
    protected void update(final ProjectEvent event) {
         executorService.execute(new Runnable() {
            @Override
            public void run()  {

                // connect to the project API URL
                int index = baseURL.indexOf(event.getProject());
                HttpURLConnection conn = null;
                try {
                    conn = (HttpURLConnection)new URL(baseURL.substring(0, index).concat("/file").concat(event.getProject()).concat("/").concat(
                            event.getPath())).openConnection();
                conn.setConnectTimeout(30 * 1000);
                conn.setRequestMethod("GET");
                conn.addRequestProperty("content-type", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to build connection", e);
                }

                // If file has been found, dump the content
                final int responseCode;
                try {
                    responseCode = conn.getResponseCode();
                } catch (IOException e) {
                    throw new RuntimeException("Unable to get response code", e);
                }
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    File updatedFile = new File(workDir, event.getPath());
                    byte[] buffer = new byte[8192];
                    try (InputStream input = conn.getInputStream(); OutputStream output = new FileOutputStream(updatedFile)) {
                            int bytesRead;
                            while ((bytesRead = input.read(buffer)) != -1) {
                                output.write(buffer, 0, bytesRead);
                            }
                    } catch (IOException ioe) {
                        throw new RuntimeException("Unable to send answer", ioe);
                    }
                }

            }
        });
    }

}
