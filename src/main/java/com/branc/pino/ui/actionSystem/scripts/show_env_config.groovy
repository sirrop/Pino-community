package com.branc.pino.ui.actionSystem.scripts

import com.branc.pino.ui.config.EnvConfigDialogController
import javafx.event.ActionEvent
import javafx.fxml.FXMLLoader
import javafx.scene.Node
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog

def loader = new FXMLLoader()
def parent = loader.load EnvConfigDialogController.class.getResourceAsStream("EnvConfigDialog.fxml")
EnvConfigDialogController controller = loader.controller

def dialog = new Dialog()
dialog.dialogPane.buttonTypes.addAll ButtonType.APPLY, ButtonType.CANCEL, ButtonType.OK
dialog.dialogPane.content = parent as Node
dialog.dialogPane.lookupButton(ButtonType.APPLY).addEventFilter(ActionEvent.ACTION) {
    controller.applyChange()
    it.consume()
}
dialog.showAndWait().filter {
    it == ButtonType.OK
}.ifPresent {
    controller.applyChange()
}