package com.github.cschulc.jirarestservice.util;

import com.github.cschulc.jirarestservice.domain.project.CreateProjectBean;
import com.github.cschulc.jirarestservice.gson.CreateProjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * UserBean: Christian Schulze (cschulc@outlook.com)
 * Date: 04.07.2018
 * Time: 15:55
 */
public class GsonHelper {

    private static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(CreateProjectBean.class, new CreateProjectSerializer()).create();

    public static String toJson(Object element){
        return gson.toJson(element);
    }
}
