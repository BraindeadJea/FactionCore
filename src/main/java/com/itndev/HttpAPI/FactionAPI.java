package com.itndev.HttpAPI;


import com.itndev.FactionCore.Factions.Config;
import com.itndev.FactionCore.Factions.Faction;
import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FactionAPI extends HttpServlet {

    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();
        path = path.replaceFirst("/", "");
        /*String parts[] = path.split("/");
        if(parts.length < 3) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{ \"ERROR\": \"CANNOT READ PARAMETERS\"}");
            return;
        }
        if(!parts[0].equalsIgnoreCase("user") && !parts[0].equalsIgnoreCase("faction")) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("{ \"ERROR\": \"CANNOT READ PARAMETERS\"}");
            return;
        }*/

        if(true) {
            String FactionName = path.toLowerCase();
            if(FactionUtils.isExistingFaction(FactionName)){
                String FactionUUID = FactionUtils.getFactionUUID(FactionName);
                String FactionRealName = FactionUtils.getCappedFactionName(FactionName);
                Faction faction = new Faction(FactionUUID);
                String json = "{\n";
                json += "\"FactionName\": " + JSONObject.quote(FactionRealName) + ",\n";
                json += "\"FactionUUID\": " + JSONObject.quote(FactionUUID) + ",\n";
                json += "\"Leader\": " + faction.getFactionMembers(Config.Leader) + "\n";
                json += "\"Members\": " + "" + "\n";
                json += "}";
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println(json);
            }
            else {
                //That person wasn't found, so return an empty JSON object. We could also return an error.
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("{}");
            }
        }
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("{ \"status\": \"ok\"}");
    }
    /*@GET
    @Path("/{id}")
    public Response getID(String id) {
        String name = id;

        if(UserInfoUtils.hasJoined(name)){
            String UUID = UserInfoUtils.getPlayerUUID(name);
            String capsname = UserInfoUtils.getPlayerOrginName(name);
            String factionname = FactionUtils.isInFaction(UUID) ? FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) : "NOT_IN_FACTION";
            String rank = FactionUtils.getPlayerLangRank(UUID);
            String json = "{\n";
            json += "\"username\": " + JSONObject.quote(capsname) + ",\n";
            json += "\"uuid\": " + JSONObject.quote(UUID) + ",\n";
            json += "\"faction\": " + JSONObject.quote(factionname) + "\n";
            json += "\"rank\": " + JSONObject.quote(rank) + "\n";
            json += "}";
            return Response.status(Response.Status.OK).entity(json).build();
        }
        return Response.status(Response.Status.OK).entity("{}").build();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String requestUrl = request.getRequestURI();
        String name = requestUrl.substring("/user/".length());

        if(UserInfoUtils.hasJoined(name)){
            String UUID = UserInfoUtils.getPlayerUUID(name);
            String capsname = UserInfoUtils.getPlayerOrginName(name);
            String factionname = FactionUtils.isInFaction(UUID) ? FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) : "NOT_IN_FACTION";
            String rank = FactionUtils.getPlayerLangRank(UUID);
            String json = "{\n";
            json += "\"username\": " + JSONObject.quote(capsname) + ",\n";
            json += "\"uuid\": " + JSONObject.quote(UUID) + ",\n";
            json += "\"faction\": " + JSONObject.quote(factionname) + "\n";
            json += "\"rank\": " + JSONObject.quote(rank) + "\n";
            json += "}";
            response.getOutputStream().println(json);
        }
        else {
            //That person wasn't found, so return an empty JSON object. We could also return an error.
            response.getOutputStream().println("{}");
        }
    }*/
}
