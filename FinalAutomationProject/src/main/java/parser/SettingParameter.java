package parser;

import model.SettingsData;

public class SettingParameter {
    public static Object getParameter(String parameterName, SettingsData settingsData){
        if (settingsData != null) {
            if (settingsData.getData().get(parameterName) != null) {
                return settingsData.getData().get(parameterName);
            }
            else{
                throw new NullPointerException("There are no parameter with name " + parameterName);
            }
        }
        else{
            throw new NullPointerException("Don't get class");
        }
    }
}
