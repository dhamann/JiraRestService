package com.github.cschulc.jirarestservice;

import com.github.cschulc.jirarestservice.domain.field.FieldBean;
import com.github.cschulc.jirarestservice.services.GroupRestService;
import com.github.cschulc.jirarestservice.services.GroupRestServiceImpl;
import com.github.cschulc.jirarestservice.services.IssueRestService;
import com.github.cschulc.jirarestservice.services.IssueRestServiceImpl;
import com.github.cschulc.jirarestservice.services.IssueSecuritySchemeRestService;
import com.github.cschulc.jirarestservice.services.IssueSecuritySchemeRestServiceImpl;
import com.github.cschulc.jirarestservice.services.NotificationSchemeRestService;
import com.github.cschulc.jirarestservice.services.NotificationSchemeRestServiceImpl;
import com.github.cschulc.jirarestservice.services.PermissionSchemeRestService;
import com.github.cschulc.jirarestservice.services.PermissionSchemeRestServiceImpl;
import com.github.cschulc.jirarestservice.services.ProjectRestService;
import com.github.cschulc.jirarestservice.services.ProjectRestServiceImpl;
import com.github.cschulc.jirarestservice.services.SearchRestService;
import com.github.cschulc.jirarestservice.services.SearchRestServiceImpl;
import com.github.cschulc.jirarestservice.services.SystemRestService;
import com.github.cschulc.jirarestservice.services.SystemRestServiceImpl;
import com.github.cschulc.jirarestservice.services.UserRestService;
import com.github.cschulc.jirarestservice.services.UserRestServiveImpl;
import com.github.cschulc.jirarestservice.util.HttpClientFactory;
import com.github.cschulc.jirarestservice.util.URIHelper;
import com.github.cschulc.jirarestservice.util.HttpClientFactory.TrustReductionStrategy;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

import static com.github.cschulc.jirarestservice.misc.RestParams.*;
import static com.github.cschulc.jirarestservice.misc.RestPaths.*;

public class JiraRestService {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    protected final ExecutorService executorService;

    private URI baseUri;
    private String username = StringUtils.EMPTY;
    private CloseableHttpClient client;
    private HttpHost proxy;
    private HttpClientContext context;

    private static Map<String, FieldBean> customfields;

    private static RequestConfig config;

    private IssueRestService issueRestService;

    private UserRestService userRestService;

    private GroupRestService groupRestService;

    private ProjectRestService projectRestService;

    private SystemRestService systemRestService;

    private SearchRestService searchRestService;

    private NotificationSchemeRestService notificationSchemeRestService;

    private PermissionSchemeRestService permissionSchemeRestService;

    private IssueSecuritySchemeRestService issueSecuritySchemeRestService;

    public JiraRestService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public int connect(URI uri, String username, String password) throws IOException, URISyntaxException, ExecutionException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return connect(uri, username, password, null, null);
    }

    public int connect(URI uri, String username, String password, TrustReductionStrategy certTrustStrategy) throws IOException, URISyntaxException, ExecutionException, InterruptedException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        return connect(uri, username, password, null, certTrustStrategy);
    }
    
    public int connect(URI uri, String username, String password, HttpHost proxyHost) throws IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
    	return connect(uri, username, password, proxyHost, null);
    }
    /**
     * Builds and configures a new client connection to JIRA.
     *
     * @param uri      = the login mask URI where JIRA is running
     * @param username = login name
     * @param password = login password
     * @return 200 succees, 401 for wrong credentials and 403 for captcha is needed, you have to login at the jira website
     * @throws KeyStoreException 
     * @throws NoSuchAlgorithmException 
     * @throws KeyManagementException 
     */
    public int connect(URI uri, String username, String password, HttpHost proxyHost, TrustReductionStrategy certTrustStrategy) throws IOException, URISyntaxException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        this.username = username;
        String host = uri.getHost();
        int port = getPort(uri.toURL());
        String scheme = HTTP;
        if (port == 443) scheme = HTTPS;
        HttpHost target = new HttpHost(host, port, scheme);
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(target.getHostName(), target.getPort()),
                new UsernamePasswordCredentials(username, password));
        
        if(null == certTrustStrategy) {
            client = HttpClientFactory.getDefaultClient(credsProvider);
        } else {
            switch (certTrustStrategy) {
            case TRUST_SELFSIGNED:
                client = HttpClientFactory.getTrustSelfsignedClient(credsProvider);
                break;
            case TRUST_ALL:
                client = HttpClientFactory.getTrustAllClient(credsProvider);
                break;
            }
        }

        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate BASIC scheme object and add it to the local
        // auth cache
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(target, basicAuth);
        // Add AuthCache to the execution context
        context = HttpClientContext.create();
        context.setAuthCache(authCache);
        this.baseUri = buildBaseURI(uri);
        if (proxyHost != null) {
            this.proxy = proxyHost;
            config = RequestConfig.custom().setProxy(proxyHost).build();
        }
        URIBuilder uriBuilder = URIHelper.buildPath(baseUri, USER);
        uriBuilder.addParameter(USERNAME, username);
        HttpGet method = new HttpGet(uriBuilder.build());
        method.addHeader(new BasicHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON));
        method.addHeader(new BasicHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON));
        if(config != null){
            method.setConfig(config);
        }
        CloseableHttpResponse response = client.execute(method, context);
        int statusCode = response.getStatusLine().getStatusCode();
        response.close();
        return statusCode;
    }

    /**
     * Extract port from URL
     *
     * @param endpointUrl The Url from which to extract the port
     * @return The Port from the URL. http Port 80 or https port 443
     */
    private int getPort(URL endpointUrl) {
        int port = (endpointUrl.getPort() != -1 ? endpointUrl.getPort() : endpointUrl.getDefaultPort());
        if (port != -1) {
            return port;
        }
        if (HTTPS.equals(endpointUrl.getProtocol())) {
            return 443;
        }
        return 80;
    }

    private URI buildBaseURI(URI uri) throws URISyntaxException {
        String path = uri.getPath();
        if (!path.isEmpty()) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            path = path.concat(BASE_REST_PATH);
        } else {
            path = BASE_REST_PATH;
        }
        return new URIBuilder(uri).setPath(path).build();
    }

    public static RequestConfig getConfig() {
        return config;
    }

    public static Map<String, FieldBean> getCustomfields() {
        if (customfields == null) {
            customfields = new HashMap<>();
        }
        return customfields;
    }

    public URI getBaseUri() {
        return baseUri;
    }

    public CloseableHttpClient getClient() {
        return client;
    }

    public void setClient(CloseableHttpClient client) {
        this.client = client;
    }

    public HttpClientContext getContext() {
        return context;
    }

    public String getUsername() {
        return username;
    }

    public IssueRestService getIssueRestService() {
        if (issueRestService == null) {
            issueRestService = new IssueRestServiceImpl(this, executorService);
        }
        return issueRestService;
    }

    public UserRestService getUserRestService() {
        if (userRestService == null) {
            userRestService = new UserRestServiveImpl(this, executorService);
        }
        return userRestService;
    }

    public GroupRestService getGroupRestService() {
        if(groupRestService == null){
            groupRestService = new GroupRestServiceImpl(this, executorService);
        }
        return groupRestService;
    }

    public ProjectRestService getProjectRestService() {
        if (projectRestService == null) {
            projectRestService = new ProjectRestServiceImpl(this, executorService);
        }
        return projectRestService;
    }

    public SystemRestService getSystemRestService() {
        if (systemRestService == null) {
            systemRestService = new SystemRestServiceImpl(this, executorService);
        }
        return systemRestService;
    }

    public SearchRestService getSearchRestService() {
        if (searchRestService == null) {
            searchRestService = new SearchRestServiceImpl(this, executorService);
        }
        return searchRestService;
    }

    public PermissionSchemeRestService getPermissionSchemeRestService() {
        if (permissionSchemeRestService == null) {
            permissionSchemeRestService = new PermissionSchemeRestServiceImpl(this, executorService);
        }
        return permissionSchemeRestService;
    }

    public NotificationSchemeRestService getNotificationSchemeRestService() {
        if (notificationSchemeRestService == null) {
            notificationSchemeRestService = new NotificationSchemeRestServiceImpl(this, executorService);
        }
        return notificationSchemeRestService;
    }

    public IssueSecuritySchemeRestService getIssueSecuritySchemeRestService() {
        if(issueSecuritySchemeRestService == null){
            issueSecuritySchemeRestService = new IssueSecuritySchemeRestServiceImpl(this, executorService);
        }
        return issueSecuritySchemeRestService;
    }

    public HttpHost getProxy() {
        return proxy;
    }
}
