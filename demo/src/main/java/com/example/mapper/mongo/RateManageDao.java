package com.example.mapper.mongo;
import com.example.entity.MtcVehiclBillingRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * 费率管理及计费业务与路方
 *
 * @author Si Lingchao
 * @creat 2019-09-29 16:44
 */
@Component
public class RateManageDao{
    private Logger log = LoggerFactory.getLogger(RateManageDao.class);

    @Resource(name = "rateMongo")
    private MongoTemplate mongoTemplate;

    @Value("${MTC}")
    private String mtc;


    public void FPCI_PROVIDERID(MtcVehiclBillingRequest request) {
        try {
            mongoTemplate.insert(request, mtc);
        } catch (Exception e) {
            log.error("MTC 车辆计费路径及通行费数据上传-路方发送数据入库失败");
        }
    }





}
