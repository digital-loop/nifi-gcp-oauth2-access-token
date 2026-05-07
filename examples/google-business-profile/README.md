# Connect Apache NiFi to Google Business Profile API

This example runs a [Method: accounts.list](https://developers.google.com/my-business/reference/accountmanagement/rest/v1/accounts/list) request from the Google Business Profile API.

## Requirements

1. Access to a Google (service) account through Application Default Credentials, Compute Engine Credentials or Service Account JSON.
2. The GCP project that corresponds to the account has to enable the [My Business Account Management API](https://console.cloud.google.com/apis/api/mybusinessaccountmanagement.googleapis.com) (requires filling out a form and waiting for Google's acceptance).
3. Access to a Google Business Profile with that account (not necessary for this test call).
4. This module must be [built](../../README.md#manual-build) and NiFi restarted.

## Use json flow definition

1. Drag a process group to the canvas.
2. In the pop-up dialog click on the "browse" icon.
3. Select the [Google_Business_Profile_Example.json](./Google_Business_Profile_Example.json) file.

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
