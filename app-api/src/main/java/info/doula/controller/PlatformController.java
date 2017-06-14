package info.doula.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;

import info.doula.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by hossaindoula on 6/15/2017.
 */

@Controller
public class PlatformController {

    @Autowired
    ConfigurationService configurationService;

    @Value(value = "${dynamic.properties.reload.access_key}")
    private String platformControlledAccessKey;

    @RequestMapping(value = "/wwwcheck")
    public String wwwcheck(Model model) {
        Map<String, Object> response = new LinkedHashMap<>();
        if (configurationService.isServiceIn()) {
            response.put("service", "in");
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 200);
        } else {
            response.put("service", "out");
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 503);
        }

        return "json";
    }

    @RequestMapping(value = "/healthcheck")
    public String healthcheck(Model model) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("health", "OK");
        model.addAttribute("responseData", response);
        model.addAttribute("responseStatus", 200);

        return "json";
    }

    @RequestMapping(value = "api/gethost")
    public String getHost(Model model) throws UnknownHostException {
        Map<String, Object> response;
        if (configurationService.isServiceIn()) {
            response = new LinkedHashMap<>();
            response.put("host", InetAddress.getLocalHost().getHostName());
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 200);
        } else {
            response = new LinkedHashMap<>();
            response.put("service", "out");
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 503);
        }

        return "json";
    }

    @RequestMapping(value = "api/management/reloadproperties")
    public String reloadProperties(Model model,
                                   @RequestParam("access_key") String accessKey) {
        Map<String, Object> response = new LinkedHashMap<>();
        if(platformControlledAccessKey.equals(accessKey)) {
            configurationService.reloadServiceStatus();
            String currentStatus = configurationService.isServiceIn() ? "in" : "out";
            response.put("service", currentStatus);
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 200);
        } else {
            response.put("error", "wrong_parameter");
            response.put("error_description", "invalid access_key");
            model.addAttribute("responseData", response);
            model.addAttribute("responseStatus", 400);
        }

        return "json";
    }
}
