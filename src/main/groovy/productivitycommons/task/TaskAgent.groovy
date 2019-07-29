package productivitycommons.task

import java.awt.TrayIcon
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class TaskAgent {

    TaskAgentGui taskGui

    // todo this all needs to get fixed so that the TaskAgentGui is actually a manager of all tray notifications...
    static void task(@DelegatesTo(TaskAgent.class) Closure taskScript) {
        TaskAgent taskAgent = new TaskAgent()
        taskAgent.taskGui = new TaskAgentGui()
        TaskAgentGui.instance = taskAgent.taskGui
        LocalDateTime start = LocalDateTime.now()
        try {
            sleep(1000)
            taskScript.setDelegate()
            taskScript.setResolveStrategy(Closure.DELEGATE_FIRST)
            taskScript.call()
        } catch (Exception e) {
            LocalDateTime end = LocalDateTime.now()
            Duration duration = Duration.between(start, end)
            taskAgent.taskGui.setStatus("Oh no! Task failed at ${DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(end)}.", "Duration: ${duration}", TrayIcon.MessageType.ERROR)
            sleep(15000)
            taskAgent.taskGui.close()
        } finally {
            LocalDateTime end = LocalDateTime.now()
            Duration duration = Duration.between(start, end)
            taskAgent.taskGui.setStatus("Congratulations! Task successful at ${DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(end)}.", "Duration: ${duration}", TrayIcon.MessageType.INFO)
            sleep(15000)
            taskAgent.taskGui.close()
        }
    }
}
