package cs.hku.wallpaper_sdk.model;

import java.util.List;

public class ModelResp {
    private int status;
    private String message;
    private List<model> body;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<model> getBody() {
        return body;
    }

    public void setBody(List<model> body) {
        this.body = body;
    }

    public class model {
        private String name;
        private String classify;
        private String image_path;
        private String model_path;

        public String getModel_path() {
            return model_path;
        }

        public void setModel_path(String model_path) {
            this.model_path = model_path;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getClassify() {
            return classify;
        }

        public void setClassify(String classify) {
            this.classify = classify;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }
}
