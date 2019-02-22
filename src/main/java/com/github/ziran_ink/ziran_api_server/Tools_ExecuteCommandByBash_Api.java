package com.github.ziran_ink.ziran_api_server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import com.github.microprograms.micro_api_runtime.annotation.MicroApi;
import com.github.microprograms.micro_api_runtime.model.Request;
import com.github.microprograms.micro_api_runtime.model.Response;
import com.github.microprograms.micro_api_runtime.utils.MicroApiUtils;
import com.github.microprograms.micro_nested_data_model_runtime.Comment;
import com.github.microprograms.micro_nested_data_model_runtime.Required;
import com.github.ziran_ink.command.Command;
import com.github.ziran_ink.command.CommandResult;
import io.airlift.concurrent.Threads;

@MicroApi(comment = "工具 - 通过bash执行命令", version = "v0.0.3")
public class Tools_ExecuteCommandByBash_Api {

    private static final ExecutorService executor = Executors.newCachedThreadPool(Threads.daemonThreadsNamed("process-input-reader-%s"));

    private static void core(Req req, Resp resp) throws Exception {
        Command command = new Command("bash", "-c");
        if (StringUtils.isNoneBlank(req.getPathEnvironment())) {
            command = command.addEnvironment("PATH", req.getPathEnvironment());
        }
        if (StringUtils.isNoneBlank(req.getDirectory())) {
            command = command.setDirectory(req.getDirectory());
        }
        if (StringUtils.isNoneBlank(req.getCommand())) {
            command = command.addArgs(req.getCommand());
        }
        if (req.getTimeLimit() != null) {
            command = command.setTimeLimit(req.getTimeLimit(), TimeUnit.SECONDS);
        }
        if (req.getSuccessfulExitCode() != null) {
            command = command.setSuccessfulExitCodes(req.getSuccessfulExitCode());
        }
        CommandResult result = command.execute(executor);
        resp.setExitCode(result.getExitCode());
        resp.setCommandOutput(result.getCommandOutput());
    }

    public static Response execute(Request request) throws Exception {
        Req req = (Req) request;
        MicroApiUtils.throwExceptionIfBlank(req.getCommand(), "command");
        Resp resp = new Resp();
        core(req, resp);
        return resp;
    }

    public static class Req extends Request {

        @Comment(value = "path环境变量")
        @Required(value = false)
        private String pathEnvironment;

        public String getPathEnvironment() {
            return pathEnvironment;
        }

        public void setPathEnvironment(String pathEnvironment) {
            this.pathEnvironment = pathEnvironment;
        }

        @Comment(value = "工作目录")
        @Required(value = false)
        private String directory;

        public String getDirectory() {
            return directory;
        }

        public void setDirectory(String directory) {
            this.directory = directory;
        }

        @Comment(value = "时间限制（秒）")
        @Required(value = false)
        private Integer timeLimit;

        public Integer getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(Integer timeLimit) {
            this.timeLimit = timeLimit;
        }

        @Comment(value = "成功的退出码")
        @Required(value = false)
        private Integer successfulExitCode;

        public Integer getSuccessfulExitCode() {
            return successfulExitCode;
        }

        public void setSuccessfulExitCode(Integer successfulExitCode) {
            this.successfulExitCode = successfulExitCode;
        }

        @Comment(value = "命令")
        @Required(value = true)
        private String command;

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    public static class Resp extends Response {

        @Comment(value = "退出码")
        @Required(value = true)
        private Integer exitCode;

        public Integer getExitCode() {
            return exitCode;
        }

        public void setExitCode(Integer exitCode) {
            this.exitCode = exitCode;
        }

        @Comment(value = "命令输出")
        @Required(value = true)
        private String commandOutput;

        public String getCommandOutput() {
            return commandOutput;
        }

        public void setCommandOutput(String commandOutput) {
            this.commandOutput = commandOutput;
        }
    }
}
