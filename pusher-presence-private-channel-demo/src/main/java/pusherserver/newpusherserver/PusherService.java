package pusherserver.newpusherserver;

import com.pusher.rest.Pusher;
import com.pusher.rest.data.PresenceUser;
import com.pusher.rest.data.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class PusherService {

    private Pusher pusher;

    private Pusher getPusher() {
        if (pusher != null) {
            return pusher;
        }
        instantiatePusherClient();
        return pusher;
    }

    private synchronized void instantiatePusherClient() {
        if (pusher == null) {

            pusher = new Pusher(Constants.APPID, Constants.KEY, Constants.SECRET);
            pusher.setCluster(Constants.CLUSTER);
            pusher.setEncrypted(true);
        }
    }

    public String getAuthenticationString(String channelName,String socketId) {
        return getPusher().authenticate(socketId, channelName);
    }

    public String getAuthenticationString(String channelName,String socketId, PresenceUser user) {
        return getPusher().authenticate(socketId, channelName,user);
    }

    public Result sendEventToPrivateChannel() {
        Map<String, String> payload= new HashMap<String, String>();
        payload.put("Time", (new Date()).toString());
        Result result = getPusher().trigger(Collections.singletonList(Constants.PRIVATE_CHANNEL), Constants.EVENT_NAME, payload);
        if (!result.getStatus().equals(Result.Status.SUCCESS)) {
            log.error("Failed to publish event for eventName {}, errorCode {}, message {}",
                    Constants.EVENT_NAME, result.getStatus().name(), result.getMessage());
        }

        log.info("Pusher Event is triggered for channel {} with eventname {} and data {}", Constants.PRIVATE_CHANNEL, Constants.EVENT_NAME, payload);
        return result;
    }


    public Result getOnlineUsers() {
        return pusher.get("/channels/" + Constants.PRESENCE_CHANNEL + "/users");
    }


}
