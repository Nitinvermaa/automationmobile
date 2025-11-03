package com.gl.testngfw.setup;

import com.gl.testngfw.api.StormTest;

import java.util.logging.Logger;

public class StormTestUtils {

	private static final Logger LOGGER = Logger.getLogger(StormTestUtils.class.getName());
    
	public static void connectStormTestServerAndReserveSlot(String serverIp, Integer... slots){
        StormTest stormTest = new StormTest();
        String nodeUrl = "http://127.0.0.1:8000/StromTestApplicationServer";
        for (Integer slot: slots) {
            boolean result = stormTest.init(serverIp, null, slot, nodeUrl);
            if(result) {
                result = stormTest.connectToServer();
                if (result) {
                    try {
                        result = stormTest.reserveSlot();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

 /*   public static void main(String[] args) {
        //connectStormTestServerAndReserveSlot("172.30.228.35", 2,3,4);
        connectStormTestServerAndReserveSlot("172.30.228.35", 3);
    }*/
}
