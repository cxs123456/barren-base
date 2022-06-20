package org.barren.tools.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.barren.tools.sms.model.SmsData;
import org.barren.tools.sms.model.SmsResponse;
import org.barren.tools.sms.props.SmsProperties;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * 阿里云sms
 *
 * @author cxs
 **/
@Data
@Slf4j
@AllArgsConstructor
public class AliSmsTemplate implements SmsTemplate {

    private static final int SUCCESS = 200;
    private static final String FAIL = "fail";
    private static final String OK = "ok";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private static final String VERSION = "2017-05-25";
    private static final String ACTION = "SendSms";

    private SmsProperties smsProperties;
    private IAcsClient acsClient;

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain(DOMAIN);
        request.setSysVersion(VERSION);
        request.setSysAction(ACTION);
        request.putQueryParameter("PhoneNumbers", String.join(",", phones));
        request.putQueryParameter("TemplateCode", smsProperties.getTemplateId());
        request.putQueryParameter("TemplateParam", JSON.toJSONString(smsData.getParams()));
        request.putQueryParameter("SignName", smsProperties.getSignName());

        try {
            CommonResponse response = acsClient.getCommonResponse(request);
            JSONObject data = JSON.parseObject(response.getData());
            String code = FAIL;
            if (data != null) {
                code = String.valueOf(data.get("Code"));
            }
            return new SmsResponse(response.getHttpStatus() == SUCCESS
                    && code.equalsIgnoreCase(FAIL), response.getHttpStatus(), response.getData());
        } catch (ClientException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

}
