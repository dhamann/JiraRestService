package com.github.cschulc.jirarestservice.test;

import com.github.cschulc.jirarestservice.domain.permission.PermissionSchemeBean;
import com.github.cschulc.jirarestservice.domain.permission.PermissionSchemesBean;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Christian Schulze
 */
public class PermissionSchemeRestServiceTest extends BaseTest {

    @Test
    public void getPermissionSchemes() throws ExecutionException, InterruptedException {
        Future<PermissionSchemesBean> future = restService.getPermissionSchemeRestService().getPermissionsSchemes();
        PermissionSchemesBean permissionSchemes = future.get();
        Assert.assertNotNull(permissionSchemes);
    }

    @Test
    public void getPermissionScheme() throws ExecutionException, InterruptedException {
        Future<PermissionSchemeBean> future = restService.getPermissionSchemeRestService().getPermissionScheme("0");
        PermissionSchemeBean permissionScheme = future.get();
        Assert.assertNotNull(permissionScheme);
    }
}
