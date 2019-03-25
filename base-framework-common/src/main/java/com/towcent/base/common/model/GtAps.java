package com.towcent.base.common.model;

import java.io.Serializable;

public class GtAps {

    private GtAlert alert;

    public static class GtAlert implements Serializable {
        public String title;
        public String body;
        // 是否提示信息 (1:提示"收到1条新消息")
        public String promptFlag;

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

        public String getPromptFlag() {
            return promptFlag;
        }

        public void setPromptFlag(String promptFlag) {
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
