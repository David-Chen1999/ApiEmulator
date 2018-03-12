package cn.qs.android.server;

import ua.naiksoftware.stomp.client.StompCommand;
import ua.naiksoftware.stomp.client.StompMessage;

/**
 * Created by 15735 on 2017/2/21.
 */

public class HeartBeat extends StompMessage {

    public HeartBeat() {
        super(StompCommand.UNKNOWN, null, "\n");
    }

    @Override
    public String compile() {
        return getPayload();
    }

}
