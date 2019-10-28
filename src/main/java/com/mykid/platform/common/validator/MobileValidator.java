package com.mykid.platform.common.validator;

import com.mykid.platform.common.annotation.IsMobile;
import com.mykid.platform.common.entity.RegexpConstant;
import com.mykid.platform.common.utils.PlatformUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否为合法的手机号码
 *
 * @author MrBird
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

    @Override
    public void initialize(IsMobile isMobile) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StringUtils.isBlank(s)) {
                return true;
            } else {
                String regex = RegexpConstant.MOBILE_REG;
                return PlatformUtil.match(regex, s);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
