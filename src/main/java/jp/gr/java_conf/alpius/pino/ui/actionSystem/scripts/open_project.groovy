package jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts

import jp.gr.java_conf.alpius.pino.application.ApplicationManager
import jp.gr.java_conf.alpius.pino.project.ProjectManager
import javafx.stage.FileChooser

def fc = new FileChooser()
fc.extensionFilters.add(new FileChooser.ExtensionFilter("プロジェクト", "*.pino"))
def file = fc.showOpenDialog(null)
if (file != null) {
    def prj = ProjectIO.read(file.toPath())
    ProjectManager.instance.project = prj

    def canvas = ApplicationManager.app.root.canvas
    canvas.setWidth(prj.width)
    canvas.setHeight(prj.height)
    canvas.setTranslateX(0)
    canvas.setTranslateY(0)
    canvas.setScaleX(1)
    canvas.setScaleY(1)
    canvas.setRotate(0)
}

