package org.example.chat_client.Model;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ShowPass {
    public static void showPass(TextField passwordShown, TextField confirmpassShown, PasswordField password, PasswordField confirmpass) {
        if(!passwordShown.isVisible() && !confirmpassShown.isVisible()){
            passwordShown.setVisible(true);
            confirmpassShown.setVisible(true);
            passwordShown.setText(password.getText());
            confirmpassShown.setText(confirmpass.getText());
            password.setVisible(false);
            confirmpass.setVisible(false);
        }
        else{
            password.setVisible(true);
            confirmpass.setVisible(true);
            password.setText(passwordShown.getText());
            confirmpass.setText(confirmpassShown.getText());
            passwordShown.setVisible(false);
            confirmpassShown.setVisible(false);
        }
    }
}
