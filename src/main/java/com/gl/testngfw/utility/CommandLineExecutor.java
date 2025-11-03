package com.gl.testngfw.utility;

import com.gl.testngfw.common.Constants;
import org.apache.commons.exec.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command line executor class for executing commands
 */

public class CommandLineExecutor {
    private static final Logger LOGGER = Logger.getLogger(CommandLineExecutor.class.getName());

    private CommandLineExecutor() {
    }

    public static String[] getCommandToExecute(String command) {
        String[] allCommand;
        String[] obj = {command};
        if (System.getProperty("os.name").contains("Windows")) {
            allCommand = concat(new String[]{"/c"}, obj);
        } else {
            allCommand = concat(new String[]{"-c"}, obj);
        }
        return allCommand;
    }

    /**
     * Executes a command
     *
     * @param command {@link java.lang.String}
     */
    public static List<String> runProcess(String command) {
        CommandOutputStream outputStream = new CommandOutputStream();
        try {
            CommandLine commandline = new CommandLine(getRuntime());
            commandline.addArguments(getCommandToExecute(command), false);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(Constants.COMMAND_EXECUTOR_TIMEOUT);
            executor.setWatchdog(executeWatchdog);
            DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            executor.setStreamHandler(streamHandler);
            executor.execute(commandline, handler);
            handler.waitFor();
        } catch (Exception e) {
            LOGGER.log(Level.INFO, "Run Process Method :: " + command + "\n" + e);
        }
        return outputStream.getLines();
    }


    /**
     * Executes a command
     *
     * @param command {@link java.lang.String}
     */
    public static void executeCommand(String command) {

        CommandOutputStream outputStream = new CommandOutputStream();
        try {
            CommandLine commandline = new CommandLine(getRuntime());
            commandline.addArguments(getCommandToExecute(command), false);
            DefaultExecutor executor = new DefaultExecutor();
            ExecuteWatchdog executeWatchdog = new ExecuteWatchdog(Constants.COMMAND_EXECUTOR_TIMEOUT);
            executor.setWatchdog(executeWatchdog);
            PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
            ExecuteResultHandler handler = new DefaultExecuteResultHandler();
            executor.setStreamHandler(streamHandler);
            executor.execute(commandline, handler);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, "Run Process Method :: " + command + "\n" + e);
        }
    }

    /**
     * Method to get executor command for windows/Mac system
     *
     * @return return commandline executor
     */
    private static String getRuntime() {
        String runtime;
        if (System.getProperty("os.name").contains("Windows")) {
            runtime = "cmd";
        } else {
            runtime = "/bin/sh";
        }
        return runtime;
    }

    /**
     * Method to concatenate command and executor application
     *
     * @param first  commandline application
     * @param second command to execute
     * @param <T>    Generic type
     * @return concatenate command
     */
    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
