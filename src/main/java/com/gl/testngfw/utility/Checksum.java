package com.gl.testngfw.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Checksum class for generating checksum for any file can be used while uploading file via API
 */
public class Checksum {

    private static final Logger LOGGER = Logger.getLogger(Checksum.class.getName());
    private String input;
    private String output;
    private String len;

    public void execute() {
        InputStream is;
        byte[] mdbytes = null;
        try {
            is = new FileInputStream(getInput());
            long length = ((FileInputStream) is).getChannel().size();
            setLen(length + "");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] dataBytes = new byte[1024];
            int nread;
            int totalRead = 0;
            while ((nread = is.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
                totalRead += nread;
                if (totalRead >= length) {
                    break;
                }
            }
            mdbytes = md.digest();
            is.close();
            mdbytes = mdbytes != null ? mdbytes : new byte[0];
            LOGGER.log(Level.INFO, String.format("%032x", new BigInteger(1, mdbytes)));
            setOutput(String.format("%032x", new BigInteger(1, mdbytes)));
        } catch (NoSuchAlgorithmException | IOException e) {
            LOGGER.log(Level.WARNING, "", e);
        }
    }

    public String fileChecksum() {
        LOGGER.log(Level.INFO, getOutput());
        return getOutput();
    }

    public String length() {
        LOGGER.log(Level.INFO, getLen());
        return getLen();
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    private String getLen() {
        return len;
    }

    private void setLen(String len) {
        this.len = len;
    }


}
