package com.itndev.HttpAPI;

/*import com.itndev.FactionCore.Utils.Factions.FactionUtils;
import com.itndev.FactionCore.Utils.Factions.UserInfoUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class APImanager {

    @GetMapping(value = "/")
    public String getPage() {
        return "non existing page : please use http://<address>:<port>/user/<username> to search up player info";
    }

    @GetMapping(value = "/user/{id}")
    public String getUserInfo(@PathVariable String id) {

        if(UserInfoUtils.hasJoined(id)){
            String UUID = UserInfoUtils.getPlayerUUID(id);
            String capsname = UserInfoUtils.getPlayerOrginName(id);
            String factionname = FactionUtils.isInFaction(UUID) ? FactionUtils.getCappedFactionName(FactionUtils.getFactionName(FactionUtils.getPlayerFactionUUID(UUID))) : "NOT_IN_FACTION";
            String rank = FactionUtils.getPlayerLangRank(UUID);
            String json = "{\n";
            json += "\"username\": " + JSONObject.quote(capsname) + ",\n";
            json += "\"uuid\": " + JSONObject.quote(UUID) + ",\n";
            json += "\"faction\": " + JSONObject.quote(factionname) + "\n";
            json += "\"rank\": " + JSONObject.quote(rank) + "\n";
            json += "}";
            return json;
        }
        else {
            //That person wasn't found, so return an empty JSON object. We could also return an error.
            return "{}";
        }
    }

}*/
