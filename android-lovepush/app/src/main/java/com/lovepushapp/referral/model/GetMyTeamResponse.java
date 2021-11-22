package com.lovepushapp.referral.model;

import java.util.List;

public class GetMyTeamResponse {
    private Integer status;
    private Data data;
    private String message;

    public Integer getStatus() {
    return status;
    }

    public void setStatus(Integer status) {
    this.status = status;
    }

    public Data getData() {
    return data;
    }

    public void setData(Data data) {
    this.data = data;
    }

    public String getMessage() {
    return message;
    }

    public void setMessage(String message) {
    this.message = message;
    }

    public class Data {

        private List<Level> level1 = null;
        private List<Level> level2 = null;
        private List<Level> level3 = null;
        private List<Level> level4 = null;
        private List<Level> level5 = null;

        public List<Level> getLevel1() {
            return level1;
        }

        public void setLevel1(List<Level> level1) {
            this.level1 = level1;
        }

        public List<Level> getLevel2() {
            return level2;
        }

        public void setLevel2(List<Level> level2) {
            this.level2 = level2;
        }

        public List<Level> getLevel3() {
            return level3;
        }

        public void setLevel3(List<Level> level3) {
            this.level3 = level3;
        }

        public List<Level> getLevel4() {
            return level4;
        }

        public void setLevel4(List<Level> level4) {
            this.level4 = level4;
        }

        public List<Level> getLevel5() {
            return level5;
        }

        public void setLevel5(List<Level> level5) {
            this.level5 = level5;
        }
    }

}