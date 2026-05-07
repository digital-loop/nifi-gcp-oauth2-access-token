/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dloop.nifi.gcp.oauth2;

import com.google.auth.oauth2.GoogleCredentials;
import java.io.IOException;
import java.net.HttpURLConnection;
import org.apache.nifi.processors.gcp.credentials.service.GCPCredentialsControllerService;
import org.apache.nifi.processors.gcp.util.GoogleUtils;
import org.apache.nifi.processors.standard.InvokeHTTP;
import org.apache.nifi.reporting.InitializationException;
import org.apache.nifi.util.MockFlowFile;
import org.apache.nifi.util.TestRunner;
import org.apache.nifi.util.TestRunners;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestGCPOauth2AccessTokenProvider {

    private static final String GCP_CREDENTIALS_CONTROLLER_SERVICE =
        "test-gcp-credentials-controller-service";
    private static final String GCP_OAUTH2_ACCESS_TOKEN_PROVIDER =
        "test-gcp-oauth2-access-token-provider";

    @BeforeEach
    public void init() {}

    @Test
    public void testService() throws InitializationException {
        final TestRunner runner = TestRunners.newTestRunner(InvokeHTTP.class);

        final GCPCredentialsControllerService credentialsService =
            new GCPCredentialsControllerService();
        final GCPOauth2AccessTokenProvider tokenProviderService =
            new GCPOauth2AccessTokenProvider();

        // Set up GCP credentials service
        runner.addControllerService(
            GCP_CREDENTIALS_CONTROLLER_SERVICE,
            credentialsService
        );
        runner.assertValid(credentialsService);
        runner.enableControllerService(credentialsService);

        // Set up GCP access token provider
        runner.addControllerService(
            GCP_OAUTH2_ACCESS_TOKEN_PROVIDER,
            tokenProviderService
        );
        runner.setProperty(
            tokenProviderService,
            GoogleUtils.GCP_CREDENTIALS_PROVIDER_SERVICE,
            GCP_CREDENTIALS_CONTROLLER_SERVICE
        );
        runner.assertValid(tokenProviderService);
        runner.enableControllerService(tokenProviderService);

        // Check if Application Default Credentials exist
        try {
            GoogleCredentials.getApplicationDefault();
        } catch (IOException e) {
            return;
        }

        // Run whoami request against Google API
        runner.setProperty(
            InvokeHTTP.HTTP_URL.getName(),
            "https://www.googleapis.com/oauth2/v1/userinfo"
        );
        runner.setProperty(
            InvokeHTTP.REQUEST_OAUTH2_ACCESS_TOKEN_PROVIDER.getName(),
            GCP_OAUTH2_ACCESS_TOKEN_PROVIDER
        );
        runner.enqueue("");
        runner.run();

        runner.assertTransferCount(InvokeHTTP.RESPONSE, 1);

        MockFlowFile responseFlowFile = runner
            .getFlowFilesForRelationship(InvokeHTTP.RESPONSE)
            .get(0);
        responseFlowFile.assertAttributeEquals(
            InvokeHTTP.STATUS_CODE,
            String.valueOf(HttpURLConnection.HTTP_OK)
        );
    }
}
