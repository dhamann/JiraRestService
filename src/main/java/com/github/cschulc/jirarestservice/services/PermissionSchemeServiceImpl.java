package com.github.cschulc.jirarestservice.services;

import com.github.cschulc.jirarestservice.JiraRestService;
import com.github.cschulc.jirarestservice.domain.permission.PermissionScheme;
import com.github.cschulc.jirarestservice.domain.permission.PermissionSchemes;
import com.github.cschulc.jirarestservice.util.RestApiCall;
import com.google.gson.stream.JsonReader;
import org.apache.http.client.utils.URIBuilder;

import java.net.HttpURLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Christian Schulze
 */
public class PermissionSchemeServiceImpl extends BaseService implements PermissionSchemeService {

    public PermissionSchemeServiceImpl(JiraRestService restService, ExecutorService executorService) {
        super(restService);
        this.executorService = executorService;
    }

    @Override
    public Future<PermissionSchemes> getPermissionsSchemes() {
        return executorService.submit(() -> {
            URIBuilder uriBuilder = buildPath(PERMISSION_SCHEME);
            RestApiCall restApiCall = doGet(uriBuilder.build());
            int statusCode = restApiCall.getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                JsonReader jsonReader = restApiCall.getJsonReader();
                PermissionSchemes permissionScheme = gson.fromJson(jsonReader, PermissionSchemes.class);
                restApiCall.release();
                return permissionScheme;
            } else {
                throw restApiCall.buildException();
            }
        });
    }

    @Override
    public Future<PermissionScheme> getPermissionScheme(String id) {
        return executorService.submit(() -> {
            URIBuilder uriBuilder = buildPath(PERMISSION_SCHEME, id);
            uriBuilder.addParameter(EXPAND, "all");
            RestApiCall restApiCall = doGet(uriBuilder.build());
            int statusCode = restApiCall.getStatusCode();
            if (statusCode == HttpURLConnection.HTTP_OK) {
                JsonReader jsonReader = restApiCall.getJsonReader();
                PermissionScheme permissionScheme = gson.fromJson(jsonReader, PermissionScheme.class);
                restApiCall.release();
                return permissionScheme;
            } else {
                throw restApiCall.buildException();
            }
        });
    }
}
