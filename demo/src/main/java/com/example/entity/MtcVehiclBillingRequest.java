package com.example.entity;

import lombok.Data;


/**
 * @author Si Lingchao
 * 多省通行的 MTC 车辆计费路径及通行费数据上传,路方请求的文件
 * @creat 2019-09-17 18:23
 */

@Data
public class MtcVehiclBillingRequest {


    private String provinceId;//省域编码
    private Integer mediaType;//通行介质类型
    private String mediaNo;//介质编码
    private String vehicleId;//收费车辆车牌号码+颜色
    private Integer vehicleType;//收费车辆车型
    private Integer vehicleClass;//车种
    private String tollIntervalsGroup;//收费单元编号组合
    private String transTimeGroup;//通过收费单元的时间组合
    private String chargefeeGroup;//收费单元实际收费金额组合
    private String chargeDiscountGroup;//收费单元优惠金额组合
    private String rateVersionGroup;//收费单元费率版本号组合
    private String enTollLaneId;//入口车道编号
    private String enTime;//入口交易发生的时间
    private String passId;//通行标识 ID




    private String roadFileName;
    private String sendNationFileName;
    private String receiveRoadTime;
    private String nationReceiveTime;
    private String nationSendTime;
    private Integer nationReturnCode;
    private String nationReturnMsg;
    private String returnRoadTime;
    private Integer returnRoadCode;
    private String returnRoadMsg;
    private Integer status;
    private String fileName;
    private String postTime;
    private String createTime;
   // private String info;
    private String providerId;//路方编号

}

