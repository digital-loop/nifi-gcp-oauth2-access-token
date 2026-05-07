# Connect Apache NiFi to Google Search Console API

This example runs a [Sites: list](https://developers.google.com/webmaster-tools/v1/sites/list) request from the Google Search Console API.

## Requirements

1. Access to a Google (service) account through Application Default Credentials, Compute Engine Credentials or Service Account JSON.
2. The Google Cloud Platform (GCP) project that corresponds to the account has to enable the [Google Search Console API](https://console.cloud.google.com/apis/library/searchconsole.googleapis.com).
3. Access to a Search Console property (domain or url) with that account (otherwise the response will be empty).
4. This module must be [built](../../README.md#manual-build) and NiFi restarted.

## Use json flow definition

1. Drag a process group to the canvas.
2. In the pop-up dialog click on the "browse" icon.
3. Select the [Google_Search_Console_Example.json](./Google_Search_Console_Example.json) file.

## Configure controller services

1. Double click on the new process group to enter it.
2. Right click on the canvas and select "Controller Services" from the menu.
3. On the "GCPCredentialsControllerService" open the three-dot menu on the right and select "View Configuration".
4. Open "Properties" tab.
5. Set up [Google API authentication](https://docs.cloud.google.com/docs/authentication) (Application Default Credentials do not require any configuration).
6. Enable "GCPCredentialsControllerService" with "Service only".
7. Click on "Close" button.
8. Enable "GCPOauth2AccessTokenProvider" with "Service only" (no configuration required).
9. Click on the "InvokeHTTP" processor in the "Referenciong Components" panel.

## Test InvokeHTTP processor

1. Right click "InvokeHTTP" and select "Run Once" from menu.
2. When the processor has finished and the view has been refreshed it should say "Queued 1 (xxx bytes)" on the Connection.
3. Right click Connection and select "List Queue" from menu.
4. Download or view content (three-dot menu in last column).

## Troubleshooting

**Cause:** Missing or invalid GCP project

**Solution:**

1. Make sure your GCP project has the [Google Search Console API](https://console.cloud.google.com/apis/library/searchconsole.googleapis.com) enabled.
2. If your credentials do not contain a valid project id, you can set a custom project id with the "x-goog-user-project" [user-defined property](https://nifi.apache.org/nifi-docs/user-guide.html#properties-tab) on the InvokeHTTP processor (already added with empty value).
