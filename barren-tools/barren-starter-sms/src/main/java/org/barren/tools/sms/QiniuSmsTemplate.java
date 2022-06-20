package org.barren.tools.sms;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.sms.SmsManager;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.barren.tools.sms.model.SmsData;
import org.barren.tools.sms.model.SmsResponse;
import org.barren.tools.sms.props.SmsProperties;
import org.springframework.http.HttpStatus;

import java.util.Collection;

/**
 * 七牛sms
 *
 * @author cxs
 **/
@Data
@AllArgsConstructor
public class QiniuSmsTemplate implements SmsTemplate {

    private SmsProperties smsProperties;
    private SmsManager smsManager;

    @Override
    public SmsResponse sendMessage(SmsData smsData, Collection<String> phones) {
        try {
            Response response = smsManager.sendMessage(smsProperties.getTemplateId(),
                    phones.toArray(new String[0]), smsData.getParams());
            return new SmsResponse(response.isOK(), response.statusCode, response.toString());
        } catch (QiniuException e) {
            e.printStackTrace();
            return new SmsResponse(Boolean.FALSE, HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
