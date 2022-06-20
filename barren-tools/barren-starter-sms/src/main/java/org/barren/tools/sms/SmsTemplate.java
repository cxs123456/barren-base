package org.barren.tools.sms;

import org.barren.tools.sms.model.SmsData;
import org.barren.tools.sms.model.SmsInfo;
import org.barren.tools.sms.model.SmsResponse;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;

/**
 * @author cxs
 **/
public interface SmsTemplate {

    /**
     * 发送短信到手机号
     *
     * @param smsInfo
     * @return
     */
    default boolean send(SmsInfo smsInfo) {
        return this.sendMulti(smsInfo.getSmsData(), smsInfo.getPhones());
    }

    default boolean sendSingle(SmsData smsData, String phone) {
        return !StringUtils.isEmpty(phone) && this.sendMulti(smsData, Collections.singletonList(phone));
    }

    default boolean sendMulti(SmsData smsData, Collection<String> phones) {
        SmsResponse response = this.sendMessage(smsData, phones);
        return response.isSuccess();
    }

    SmsResponse sendMessage(SmsData smsData, Collection<String> phones);
}
