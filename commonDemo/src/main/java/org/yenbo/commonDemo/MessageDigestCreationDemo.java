package org.yenbo.commonDemo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDigestCreationDemo {
    
    private static final Logger log = LoggerFactory.getLogger(MessageDigestCreationDemo.class);
    
    public static void main(String[] args) throws NoSuchAlgorithmException, CloneNotSupportedException {
        
        final int count = 1000000000;
        
        // get instance: time=123764 ms
        ZonedDateTime getInstanceStartTime = ZonedDateTime.now();
        
        for (int i = 0; i < count; i++) {
            MessageDigest.getInstance("MD5");
        }
        
        log.info("by getInstance(): count={}, time={} ms", count,
                ChronoUnit.MILLIS.between(getInstanceStartTime, ZonedDateTime.now()));
        
        // clone: time=61095 ms
        MessageDigest digest = MessageDigest.getInstance("MD5");
        
        ZonedDateTime cloneStartTime = ZonedDateTime.now();
        
        for (int i = 0; i < count; i++) {
            @SuppressWarnings("unused")
            MessageDigest clonedDigest = (MessageDigest) digest.clone();
        }
        
        log.info("by getInstance(): count={}, time={} ms", count, ChronoUnit.MILLIS.between(cloneStartTime, ZonedDateTime.now()));
    }

}
