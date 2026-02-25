package com.delivery.food.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDto {

    @NotBlank(message = "Street is required")
    @Size(max = 255)
    private String street;

    @NotBlank(message = "House number is required")
    @Size(max = 50)
    private String house;

    @Size(max = 50)
    private String apartment;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;

    @Size(max = 500)
    private String comment;
}
