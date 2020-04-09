package pusherserver.newpusherserver;

import com.pusher.rest.data.PresenceUser;
import com.pusher.rest.data.Result;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;



@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@AllArgsConstructor
@Slf4j
public class PusherController {
    @Autowired
    private PusherService pusherService;

    @PostMapping("/pusher/auth")
    public String authenticateUser(
            @RequestParam(value = "channel_name") String channelName,
            @RequestParam(value = "socket_id") String socketId,
            @RequestParam(value = "id") String userId) {
        log.info("channelName = {} & socketId = {} & userId = {} ", channelName, socketId,userId);

        if(channelName.startsWith("private")) {
            return pusherService.getAuthenticationString(channelName,socketId);
        } else{
            return pusherService.getAuthenticationString(channelName,socketId,new PresenceUser(userId));
        }
    }



    /*@RequestMapping(value="/pusher/auth",
            method=RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAuthenticationString(@RequestBody MultiValueMap<String, String> formData){
        String channelName = formData.getFirst("channel_name");
        String socketId = formData.getFirst("socket_id");
        String id = formData.getFirst("id");
        if(channelName.startsWith("private")) {
             return pusherService.getAuthenticationString(channelName,socketId);
        } else{
            return pusherService.getAuthenticationString(channelName,socketId,new PresenceUser(id));
        }
    }*/


    @GetMapping("/sendEventToPrivateChannel")
    public Result triggerData(){
       return pusherService.sendEventToPrivateChannel();
    }

    @GetMapping("/getOnlineUsers")
    public Result hello(){
        return pusherService.getOnlineUsers();
    }

}
