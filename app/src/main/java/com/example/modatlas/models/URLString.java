package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URLString {
    public static String facet = "[]";
    public static String loader = "";
    public static final String modId= "[[\"project_type:mod\"]]";
    public static final String modPackId= "[[\"project_type:modpack\"]]";
    public static final String dataPackId= "[[\"project_type:datapack\"]]";
    public static final String pluginId= "[[\"project_type:plugin\"]]";
    public static final String shaderId= "[[\"project_type:shader\"]]";
    public static final String resourcePackId= "[[\"project_type:resourcepack\"]]";
//
//    public static String getProjectType(String projectType){
//        if(projectType.equals("mod")){
//            return allMod;
//        } else if (projectType.equals("modpack")) {
//            return allModPack;
//        } else if (projectType.equals("datapack")) {
//            return allDataPack;
//        } else if (projectType.equals("plugin")) {
//            return allPlugin;
//        } else if (projectType.equals("shader")) {
//            return allShader;
//        } else if (projectType.equals("resourcepack")) {
//            return allResourcePack;
//        }
//        return projectType;
//    }

    public static void setProjectType(String projectType){
        if (containsColon(projectType)){
            facet = projectType;
        } else{
            facet = "[[\"project_type:"+projectType+"\"]]";
        }
    }

//    public static String removeProjectType(String projectType) {
//        if (containsColon(projectType)){
//            insertIntoFacet("[\""+projectType+"\"]");
//        } else{
//            insertIntoFacet("[\"project_type:"+projectType+"\"]");
//        }
//        return facet;
//    }

    public static void addVersion(String version) {
        if (containsColon(version)){
            insertIntoFacet("[\""+version+"\"]");
        } else{
            insertIntoFacet("[\"versions:"+version+"\"]");
        }
    }

//    public static String removeVersion(String version) {
//        if (containsColon(version)){
//            insertIntoFacet("[\""+version+"\"]");
//        } else{
//            insertIntoFacet("[\"versions:"+version+"\"]");
//        }
//        return facet;
//    }

    public static void addCategory(String category){
        if (containsColon(category)){
            String temp = category.substring(category.indexOf(":") + 1).trim();
            insertIntoFacet("[\"categories:"+temp+"\"]");
        }
    }

    public static void addLoader(String loader){
        if (containsColon(loader)){
            insertIntoLoader(loader.substring(loader.indexOf(":") + 1).trim());
        }
    }

    public static void addFacet(String value){
        if (value.contains("versions")){
            addVersion(value);
        } else if (value.contains("categories") || value.contains("resolutions") || value.contains("features") || value.contains("performance impact")) {
            addCategory(value);
        } else if (value.contains("loader")) {
            addLoader(value);
        }
    }

    private static void insertIntoFacet(String value) {
        // Remove the closing bracket temporarily
        facet = facet.substring(0, facet.length() - 1);

        // Check if it's empty (i.e., just "[")
        if (!facet.equals("[")) {
            facet += ", ";
        }

        // Add the new value and re-append the closing bracket
        facet += value + "]";
    }

    private static void insertIntoLoader(String value){
        // Check if it's empty
        if (!loader.equals("")) {
            loader += ", ";
        }

        // Add the new value and re-append the closing bracket
        loader += value;
    }

    private static void removeFromFacet(String value) {
        // Remove brackets
        String content = facet.substring(1, facet.length() - 1);
        List<String> parts = new ArrayList<>(Arrays.asList(content.split(",\\s*")));

        // Remove the matching value
        parts = parts.stream()
                .filter(item -> !item.equals(value))
                .collect(Collectors.toList());

        // Reconstruct facet
        facet = "[" + String.join(", ", parts) + "]";
    }

    public static void resetFacet(){
        facet = "[]";
    }

    public static void resetLoader(){
        loader = "";
    }

    private static boolean containsColon(String input) {
        return input != null && input.contains(":");
    }
}
