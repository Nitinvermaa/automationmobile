package com.gl.testngfw.utility;

import com.jcraft.jsch.*;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;

public class SSHHelper {
    private static final java.util.logging.Logger logger = Logger.getLogger(SSHHelper.class.getName());
    private static final String STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";
    private Session session = null;
    private Channel channel = null;
    private String username;
    private String password;
    private String hostIp;
    private String privateKey;
    private int port;

    /**
     * constructor
     *
     * @param username username
     * @param hostIp   ip address of remote machine
     * @param port     ssh port
     */
    public SSHHelper(String username, String hostIp, int port) {
        this.username = username;
        this.hostIp = hostIp;
        this.port = port;
    }

    /**
     * Method to create session
     *
     * @return session object
     * @throws JSchException exception
     */
    private Session createSession() throws JSchException {
        if (session == null) {
            JSch jsch = new JSch();
            if (getPrivateKey() == null || getPrivateKey().isEmpty()) {
                session.setPassword(password);
            } else {
                jsch.addIdentity(getPrivateKey());
            }
            session = jsch.getSession(username, hostIp, port);
            session.setConfig(STRICT_HOST_KEY_CHECKING, "no");
            logger.log(Level.INFO, "Establishing Connection...");
            logger.log(Level.INFO, "Connection established.");
            logger.log(Level.INFO, "Crating SFTP Channel.");
        }
        return session;
    }

    /**
     * Method to execute command on remote location
     *
     * @param command command to execute
     * @return output in string list
     * @throws JSchException exception
     * @throws IOException   exception
     */
    public List<String> executeCommand(String command) {
        long startTime = System.currentTimeMillis();
        List<String> line = new ArrayList<>();
        try {
            session = createSession();

            channel = session.openChannel("exec");
            ChannelExec ce = (ChannelExec) channel;
            ce.setCommand(command);
            ce.setErrStream(channel.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
            ce.connect();
            String temp;
            while ((temp = reader.readLine()) != null) {
                if ((System.currentTimeMillis() - startTime) > 180000L) {
                    logger.log(Level.INFO, "Command TimeOut : " + command);
                    break;
                }
                if (!temp.isEmpty()) {
                    line.add(temp);
                }
            }
        } catch (JSchException | IOException e) {
            logger.log(Level.WARNING, "Exception ", e);
        }
        return line;
    }

    /**
     * execute command on remote machine
     *
     * @param command list of commands to execute
     * @return list for string output
     */
    public List<List<String>> executeCommand(List<String> command) {
        List<List<String>> line = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        try {
            session = createSession();
            channel = session.openChannel("exec");
            ChannelExec ce = (ChannelExec) channel;
            for (String cmd : command) {
                ce.setCommand(cmd);
                ce.setErrStream(channel.getOutputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(ce.getInputStream()));
                ce.connect();
                String temp;
                List<String> output = new ArrayList<>();
                while ((temp = reader.readLine()) != null) {
                    if ((System.currentTimeMillis() - startTime) > 180000L) {
                        logger.log(Level.INFO, "Command TimeOut : " + command);
                        break;
                    }
                    if (!temp.isEmpty()) {
                        output.add(temp);
                    }
                }
                line.add(output);
            }
        } catch (JSchException | IOException e) {
            logger.log(Level.WARNING, "Exception ", e);
        }
        return line;
    }

    /**
     * copy file to remote location
     *
     * @param from local source location
     * @param to   remote location
     */
    private void upload(URI from, URI to) {
        try {
            session = createSession();

            java.util.Properties config = new java.util.Properties();
            config.put(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(config);
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");

            FileInputStream fis = new FileInputStream(new File(from));
            logger.log(Level.INFO, String.format("Uploading {%s} --> {%s}", from, to.getPath()));
            sftpChannel.connect(600);
            sftpChannel.cd(to.getPath());
            Thread.sleep(1000L);
            sftpChannel.put(fis, getName(from.getPath()));
        } catch (JSchException | FileNotFoundException | SftpException | InterruptedException e) {
            logger.log(Level.WARNING, "Exception ", e);
        }
    }

    /**
     * copy content form remote folder
     *
     * @param from Remote location
     * @param to   local destination location
     */
    private void download(URI from, URI to) {
        File out = new File(new File(to), getName(from.getPath()));
        try {
            session = createSession();

            java.util.Properties config = new java.util.Properties();
            config.put(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(config);
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            OutputStream os = new FileOutputStream(out);
            BufferedOutputStream bos = new BufferedOutputStream(os);

            logger.log(Level.INFO, String.format("Downloading {%s} --> {%s}", from.getPath(), to));
            sftpChannel.connect();
            sftpChannel.cd(getFullPath(from.getPath()));
            sftpChannel.get(getName(from.getPath()), bos);
        } catch (JSchException | SftpException | FileNotFoundException e) {
            logger.log(Level.WARNING, "Exception ", e);
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKeyFilePath) {
        this.privateKey = privateKeyFilePath;
    }

    /**
     * This method is called recursively to Upload the local folder content to
     * SFTP server
     *
     * @param sourcePath
     * @param destinationPath
     */
    public void uploadFolder(String sourcePath, String destinationPath) {
        File sourceFile = new File(sourcePath);
        try {
            session = createSession();

            java.util.Properties config = new java.util.Properties();
            config.put(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(config);

            if (!session.isConnected()) {
                session.connect();
            }
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            channel.connect();

            if (sourceFile.isFile()) {
                // copy if it is a file
                sftpChannel.cd(destinationPath);
                if (!sourceFile.getName().startsWith(".")) {
                    sftpChannel.put(new FileInputStream(sourceFile), sourceFile.getName(), ChannelSftp.OVERWRITE);
                }
            } else {
                if (sourceFile.exists()) {
                    File[] files = sourceFile.listFiles();
                    if (files != null) {
                        sftpChannel.cd(destinationPath);
                        // check if the directory is already existing
                        String path = destinationPath + "/" + sourceFile.getName();
                        // else create a directory
                        if (!isRemoteDirExist(sftpChannel, path)) {
                            logger.log(Level.INFO, "Creating dir " + sourceFile.getName());
                            sftpChannel.mkdir(sourceFile.getName());
                        }
                        for (File f : files) {
                            uploadFolder(f.getAbsolutePath(), path);
                        }
                    }
                } else {
                    logger.log(Level.INFO, "Directory Not Present");
                }
            }
        } catch (JSchException | SftpException | FileNotFoundException e) {
            logger.log(Level.WARNING, "Exception ", e);
        }
    }

    private Boolean isRemoteDirExist(ChannelSftp sftpChannel, String path) {
        try {
            sftpChannel.stat(path);
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING, "Exception: ", e);
            return false;
        }
    }

    public boolean createDestinationDir(String destinationFolder, boolean createFolder) {
        boolean folderExist = false;
        try {
            session = createSession();
            java.util.Properties config = new java.util.Properties();
            config.put(STRICT_HOST_KEY_CHECKING, "no");
            session.setConfig(config);

            if (!session.isConnected()) {
                session.connect();
            }
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            File destination = new File(sftpChannel.getHome() + "/" + destinationFolder);
            SftpATTRS attrs = null;
            attrs = sftpChannel.stat(destinationFolder);
            // else create a directory
            if (createFolder && attrs == null) {

                sftpChannel.mkdir(destination.getName());
                sftpChannel.cd(destination.getAbsolutePath());
                folderExist = true;
            } else {
                folderExist = attrs != null;
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "exception ", e);
        }
        return folderExist;
    }

    public void disconnectSession() {
        session.disconnect();
    }
}



