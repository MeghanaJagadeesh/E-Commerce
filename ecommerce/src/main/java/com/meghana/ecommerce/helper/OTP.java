package com.meghana.ecommerce.helper;

import org.springframework.stereotype.Component;

@Component
public class OTP {

	public int checkLength(int otp) {
		int copy = otp;
		int count = 0;
		if (otp != 0) {
			count++;
			otp = otp / 10;
		}
		if (count == 6)
			return copy;
		else
			return copy + 10;
	}
}
