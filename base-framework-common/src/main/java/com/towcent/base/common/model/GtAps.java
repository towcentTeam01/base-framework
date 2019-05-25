package com.towcent.base.common.model;

import java.io.Serializable;

public class GtAps {

    private GtAlert alert;

    public static class GtAlert implements Serializable {
        public String title;
        public String body;
        // 是否提示信息 (true:提示"收到1条新消息")
        public boolean promptFlag;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public boolean isPromptFlag() {
            return promptFlag;
        }

        public void setPromptFlag(boolean promptFlag) {
            this.promptFlag = promptFlag;
        }
    }

    public GtAlert getAlert() {
        return alert;
    }

    public void setAlert(GtAlert alert) {
        this.alert = alert;
    }
}
