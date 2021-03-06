package com.github.cschulc.jirarestservice.domain;

import com.google.gson.annotations.Expose;


public class ErrorsBean {

    @Expose
    private String project;
    @Expose
    private String subtasks;
    @Expose
    private String attachment;
    @Expose
    private String issuelinks;
    @Expose
    private String issuetype;
    @Expose
    private String reporter;
    @Expose
    private String assignee;
    @Expose
    private String environment;
    @Expose
    private String filterName;
    @Expose
    private String versions;
    @Expose
    private String components;
    @Expose
    private String email;
    @Expose
    private String projectType;
    @Expose
    private String projectKey;
    @Expose
    private String projectName;

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(String subtasks) {
        this.subtasks = subtasks;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getIssuelinks() {
        return issuelinks;
    }

    public void setIssuelinks(String issuelinks) {
        this.issuelinks = issuelinks;
    }

    public String getIssuetype() {
        return issuetype;
    }

    public void setIssuetype(String issuetype) {
        this.issuetype = issuetype;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    public String getVersions() {
        return versions;
    }

    public void setVersions(String versions) {
        this.versions = versions;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
