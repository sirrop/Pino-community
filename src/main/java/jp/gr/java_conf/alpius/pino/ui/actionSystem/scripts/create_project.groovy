package jp.gr.java_conf.alpius.pino.ui.actionSystem.scripts

import javafx.scene.control.*
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.util.converter.NumberStringConverter
import jp.gr.java_conf.alpius.pino.application.ApplicationManager
import jp.gr.java_conf.alpius.pino.layer.DrawableLayer
import jp.gr.java_conf.alpius.pino.project.Project
import jp.gr.java_conf.alpius.pino.project.ProjectManager

import java.awt.color.ColorSpace
import java.awt.color.ICC_Profile
import java.nio.file.Paths

def dialog = new Dialog<ButtonType>()
def width = new TextField()
def height = new TextField()

def widthFormatter = new TextFormatter<>(new NumberStringConverter(), 500)
def heightFormatter = new TextFormatter<>(new NumberStringConverter(), 500)

dialog.dialogPane.stylesheets.add(Paths.get("data", "pino-white.css").toUri().toURL().toExternalForm())
width.textFormatter = widthFormatter
width.styleClass.add 'width'
height.textFormatter = heightFormatter
height.styleClass.add 'height'
def pane = new VBox()
dialog.dialogPane.content = pane
pane.children.addAll(
        new HBox(new Label("幅"), width),
        new HBox(new Label("高さ"), height)
)

dialog.dialogPane.buttonTypes.addAll ButtonType.OK, ButtonType.CANCEL
dialog.title = '新規プロジェクトを作成'

dialog.showAndWait().filter {
    it == ButtonType.OK
}.ifPresent {
    def canvas = ApplicationManager.app.root.canvas
    canvas.width = widthFormatter.value.doubleValue()
    canvas.height = heightFormatter.value.doubleValue()
    def project = Project.create(canvas.width, canvas.height, ICC_Profile.getInstance(ColorSpace.CS_sRGB))
    canvas.setTranslateX(0)
    canvas.setTranslateY(0)
    canvas.setScaleX(1)
    canvas.setScaleY(1)
    canvas.setRotate(0)
    ProjectManager.getInstance().project = project
    project.layer.add(new DrawableLayer())
}