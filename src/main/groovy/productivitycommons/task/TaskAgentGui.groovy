package productivitycommons.task

import productivitycommons.gui.TrayWidget

import static productivitycommons.gui.GuiUtils.setSystemLookAndFeel

/**
 * @author Alessandro Scarlatti
 * @since Sunday , 7/28/2019
 */
class TaskAgentGui extends TrayWidget {

    TaskAgent taskAgent

    static TaskAgentGui instance

    static {
        setSystemLookAndFeel()
    }

    @Override
    protected String getWidgetName() {
        return "Productivity Utility"
    }

    @Override
    protected String getWidgetAboutHtml() {
        return "Library of utilities to make a sophisticated or tedious workflow manageable as background processes."
    }

    static closeInstance() {
        if (instance != null)
            instance.close()
    }
}
