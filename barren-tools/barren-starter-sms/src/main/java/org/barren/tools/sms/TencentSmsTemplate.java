package org.barren.tools.sms;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.barren.tools.sms.model.SmsData;
import org.barren.tools.sms.model.SmsResponse;
import org.barren.tools.sms.props.SmsProperties;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Collection;

/**
 * 腾讯sms
 *
 * @author cxs
 **/
@Data
public class TencentSmsTemplate implements SmsTemplate {
    private static final int SUCCESS = 0;
    private static final String NATION_CODE = "86";
    private final SmsProperties smsProperties;
    private final SmsMultiSender smsSender;

    @Override
    public SmsResponse sendMessage(SmsData smsData,
            Collection<String> phones) {
        try {
            Collection<String> values = smsData.getParams().values();
            SmsMultiSenderResult senderResult = smsSender.sendWithParam(
                    NATION_CODE, phones.toArray(new String[0]),
                    NumberUtils.toInt(smsProperties.getTemplateId()),
                    values.toArray(new String[0]),
                    smsProperties.getSignName(),
                    "", "");
            return new SmsResponse(senderResult.result == SUCCESS, senderResult.result, senderResult.toString());
        } catch (IOException | HTTPException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }

    /*public SmsCode sendValidate(SmsData smsData, String phone) {
        SmsCode smsCode = new SmsCode();
        boolean temp = sendSingle(smsData, phone);
        if (temp && StringUtil.isNotBlank(smsData.getKey())) {
            String id = StringUtil.randomUUID();
            String value = (String) smsData.getParams().get(smsData.getKey());
            bladeRedis.setEx(cacheKey(phone, id), value, Duration.ofMinutes(30L));
            smsCode.setId(id).setValue(value);
        } else {
            smsCode.setSuccess(Boolean.FALSE);
        }

        return smsCode;
    }*/

}
