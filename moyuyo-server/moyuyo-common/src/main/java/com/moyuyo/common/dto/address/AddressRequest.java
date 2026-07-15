package com.moyuyo.common.dto.address;

import lombok.Data;

@Data
public class AddressRequest {

  private String receiver;

  private String phone;

  private String country;

  private String province;

  private String city;

  private String district;

  private String detail;

  private String zipCode;

  private String tag;

  private Boolean isDefault;
}
