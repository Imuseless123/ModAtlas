package com.example.modatlas.models;

public class urlString {
    public static final String allMod= "[[\"project_type:mod\"]]";
    public static final String allModPack= "[[\"project_type:modpack\"]]";
    public static final String allDataPack= "[[\"project_type:datapack\"]]";
    public static final String allPlugin= "[[\"project_type:plugin\"]]";
    public static final String allShader= "[[\"project_type:shader\"]]";
    public static final String allResourcePack= "[[\"project_type:resourcepack\"]]";

    public static String getProjectType(String projectType){
        if(projectType.equals("mod")){
            return allMod;
        } else if (projectType.equals("modpack")) {
            return allModPack;
        } else if (projectType.equals("datapack")) {
            return allDataPack;
        } else if (projectType.equals("plugin")) {
            return allPlugin;
        } else if (projectType.equals("shader")) {
            return allShader;
        } else if (projectType.equals("resourcepack")) {
            return allResourcePack;
        }
        return projectType;
    }
}
