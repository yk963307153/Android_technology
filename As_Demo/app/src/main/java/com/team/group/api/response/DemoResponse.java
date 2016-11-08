package com.team.group.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * 错误码
 */
public class DemoResponse {

    @SerializedName("error_code")
    private String errorCode;
    private String reason;

    private String result;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }


    //    @SerializedName("train_info")
//    private TrainInfoBean trainInfo;
//
//
//    private List<TrainInfoBean>  inquiries;
//    private List<StationListBean>  opinions;
//
//
//    String jsonArray = "[\"Android\",\"Java\",\"PHP\"]";
//    String[] strings = gson.fromJson(jsonArray, String[].class);
//    List<String> stringList = gson.fromJson(jsonArray, new TypeToken<List<String>>() {}.getType());
//
//
//
//    public static class TrainInfoBean {
//        private String name;
//        private String start;
//        private String end;
//        private String starttime;
//        private String endtime;
//        private String mileage;
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getStart() {
//            return start;
//        }
//
//        public void setStart(String start) {
//            this.start = start;
//        }
//
//        public String getEnd() {
//            return end;
//        }
//
//        public void setEnd(String end) {
//            this.end = end;
//        }
//
//        public String getStarttime() {
//            return starttime;
//        }
//
//        public void setStarttime(String starttime) {
//            this.starttime = starttime;
//        }
//
//        public String getEndtime() {
//            return endtime;
//        }
//
//        public void setEndtime(String endtime) {
//            this.endtime = endtime;
//        }
//
//        public String getMileage() {
//            return mileage;
//        }
//
//        public void setMileage(String mileage) {
//            this.mileage = mileage;
//        }
//    }
//
//    public static class StationListBean {
//        private String train_id;
//        private String station_name;
//        private String arrived_time;
//        private String leave_time;
//        private String mileage;
//        private String fsoftSeat;
//        private String ssoftSeat;
//        private String hardSead;
//        private String softSeat;
//        private String hardSleep;
//        private String softSleep;
//        private String wuzuo;
//        private String swz;
//        private String tdz;
//        private String gjrw;
//        private String stay;
//
//        public String getTrain_id() {
//            return train_id;
//        }
//
//        public void setTrain_id(String train_id) {
//            this.train_id = train_id;
//        }
//
//        public String getStation_name() {
//            return station_name;
//        }
//
//        public void setStation_name(String station_name) {
//            this.station_name = station_name;
//        }
//
//        public String getArrived_time() {
//            return arrived_time;
//        }
//
//        public void setArrived_time(String arrived_time) {
//            this.arrived_time = arrived_time;
//        }
//
//        public String getLeave_time() {
//            return leave_time;
//        }
//
//        public void setLeave_time(String leave_time) {
//            this.leave_time = leave_time;
//        }
//
//        public String getMileage() {
//            return mileage;
//        }
//
//        public void setMileage(String mileage) {
//            this.mileage = mileage;
//        }
//
//        public String getFsoftSeat() {
//            return fsoftSeat;
//        }
//
//        public void setFsoftSeat(String fsoftSeat) {
//            this.fsoftSeat = fsoftSeat;
//        }
//
//        public String getSsoftSeat() {
//            return ssoftSeat;
//        }
//
//        public void setSsoftSeat(String ssoftSeat) {
//            this.ssoftSeat = ssoftSeat;
//        }
//
//        public String getHardSead() {
//            return hardSead;
//        }
//
//        public void setHardSead(String hardSead) {
//            this.hardSead = hardSead;
//        }
//
//        public String getSoftSeat() {
//            return softSeat;
//        }
//
//        public void setSoftSeat(String softSeat) {
//            this.softSeat = softSeat;
//        }
//
//        public String getHardSleep() {
//            return hardSleep;
//        }
//
//        public void setHardSleep(String hardSleep) {
//            this.hardSleep = hardSleep;
//        }
//
//        public String getSoftSleep() {
//            return softSleep;
//        }
//
//        public void setSoftSleep(String softSleep) {
//            this.softSleep = softSleep;
//        }
//
//        public String getWuzuo() {
//            return wuzuo;
//        }
//
//        public void setWuzuo(String wuzuo) {
//            this.wuzuo = wuzuo;
//        }
//
//        public String getSwz() {
//            return swz;
//        }
//
//        public void setSwz(String swz) {
//            this.swz = swz;
//        }
//
//        public String getTdz() {
//            return tdz;
//        }
//
//        public void setTdz(String tdz) {
//            this.tdz = tdz;
//        }
//
//        public String getGjrw() {
//            return gjrw;
//        }
//
//        public void setGjrw(String gjrw) {
//            this.gjrw = gjrw;
//        }
//
//        public String getStay() {
//            return stay;
//        }
//
//        public void setStay(String stay) {
//            this.stay = stay;
//        }
//    }
}
