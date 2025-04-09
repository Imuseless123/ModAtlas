package com.example.modatlas.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class urlString {
    public static String facet = "[]";
//    public static final String allMod= "[[\"project_type:mod\"]]";
//    public static final String allModPack= "[[\"project_type:modpack\"]]";
//    public static final String allDataPack= "[[\"project_type:datapack\"]]";
//    public static final String allPlugin= "[[\"project_type:plugin\"]]";
//    public static final String allShader= "[[\"project_type:shader\"]]";
//    public static final String allResourcePack= "[[\"project_type:resourcepack\"]]";
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

    public static String getVersion(String version) {
        if (containsColon(version)){
            insertIntoFacet("[\""+version+"\"]");
        } else{
            insertIntoFacet("[\"versions:"+version+"\"]");
        }
        return facet;
    }

    public static String getProjectType(String projectType) {
        if (containsColon(projectType)){
            insertIntoFacet("[\""+projectType+"\"]");
        } else{
            insertIntoFacet("[\"project_type:"+projectType+"\"]");
        }
        return facet;
    }

    public static String removeVersion(String version) {
        if (containsColon(version)){
            insertIntoFacet("[\""+version+"\"]");
        } else{
            insertIntoFacet("[\"versions:"+version+"\"]");
        }
        return facet;
    }

    public static String removeProjectType(String projectType) {
        if (containsColon(projectType)){
            insertIntoFacet("[\""+projectType+"\"]");
        } else{
            insertIntoFacet("[\"project_type:"+projectType+"\"]");
        }
        return facet;
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

    private static boolean containsColon(String input) {
        return input != null && input.contains(":");
    }

}
