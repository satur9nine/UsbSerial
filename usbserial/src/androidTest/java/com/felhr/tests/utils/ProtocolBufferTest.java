package com.felhr.tests.utils;

import com.felhr.utils.ProtocolBuffer;

import junit.framework.TestCase;

import org.junit.Test;


public class ProtocolBufferTest extends TestCase {

    private final String onePacket = "$GPAAM,A,A,0.10,N,WPTNME*32\r\n";
    private final String twoPackets = "$GPAAM,A,A,0.10,N,WPTNME*32\r\n$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
    private final String splitPacket = "$GPAAM,A,A,0.10,N,WPTN";
    private final String oneHalfPacket = "$GPAAM,A,A,0.10,N,WPTNME*32\r\n$GPGGA,092750.000";

    private final String[] verySplit ={"$GPAAM,",
        "A",
        ",",
        "A,",
        "0",
        ".",
        "10,N",
        ",WPTNME*32\r\n"};

    private ProtocolBuffer protocolBuffer;
    private final String modeText = ProtocolBuffer.TEXT;
    private final String modeBinary = ProtocolBuffer.BINARY;

    @Test
    public void testOnePacket(){
        protocolBuffer = new ProtocolBuffer(modeText);
        protocolBuffer.setDelimiter("\r\n");
        protocolBuffer.appendData(onePacket.getBytes());

        boolean hasMoreData = protocolBuffer.hasMoreCommands();
        assertTrue(hasMoreData);
        String nextCommand = protocolBuffer.nextCommand();
        assertEquals(onePacket, nextCommand);
    }

    @Test
    public void testTwoPackets(){
        protocolBuffer = new ProtocolBuffer(modeText);
        protocolBuffer.setDelimiter("\r\n");
        protocolBuffer.appendData(twoPackets.getBytes());

        StringBuilder builder = new StringBuilder();

        while(protocolBuffer.hasMoreCommands()){
            builder.append(protocolBuffer.nextCommand());
        }
        assertEquals(twoPackets, builder.toString());
    }

    @Test
    public void testSplitPackets(){
        protocolBuffer = new ProtocolBuffer(modeText);
        protocolBuffer.setDelimiter("\r\n");
        protocolBuffer.appendData(splitPacket.getBytes());

        boolean hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);
    }

    @Test
    public void testOneHalfPacket(){
        protocolBuffer = new ProtocolBuffer(modeText);
        protocolBuffer.setDelimiter("\r\n");
        protocolBuffer.appendData(oneHalfPacket.getBytes());

        boolean hasMoreData = protocolBuffer.hasMoreCommands();
        assertTrue(hasMoreData);
        String nextCommand = protocolBuffer.nextCommand();
        assertEquals("$GPAAM,A,A,0.10,N,WPTNME*32\r\n", nextCommand);

        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        nextCommand = protocolBuffer.nextCommand();
        assertNull(nextCommand);
    }

    @Test
    public void testNMEA5(){
        protocolBuffer = new ProtocolBuffer(modeText);
        protocolBuffer.setDelimiter("\r\n");

        protocolBuffer.appendData(verySplit[0].getBytes());
        boolean hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[1].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[2].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[3].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[4].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[5].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[6].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertFalse(hasMoreData);

        protocolBuffer.appendData(verySplit[7].getBytes());
        hasMoreData = protocolBuffer.hasMoreCommands();
        assertTrue(hasMoreData);

        String command = protocolBuffer.nextCommand();
        assertEquals("$GPAAM,A,A,0.10,N,WPTNME*32\r\n", command);
    }
}
