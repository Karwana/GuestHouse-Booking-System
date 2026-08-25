package org.nackademin.guesthousebookingsystem.client;

import lombok.RequiredArgsConstructor;
import org.nackademin.guesthousebookingsystem.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomerClient {

    private final RestClient restClient;

    @Value("${customer.service.url}")
    private String customerServiceUrl;

    public List<CustomerDto> getAllCustomers() {
        try {
            CustomerDto[] customers = restClient.get()
                    .uri(customerServiceUrl + "/api/customers")
                    .retrieve()
                    .body(CustomerDto[].class);
            return customers != null
                    ? Arrays.asList(customers)
                    : Collections.emptyList();
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }

    public CustomerDto getCustomerById(Long id) {
        try {
            return restClient.get()
                    .uri(customerServiceUrl + "/api/customers/" + id)
                    .retrieve()
                    .body(CustomerDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException(
                    "Kund med id " + id + " hittades inte");
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }

    public CustomerDto saveCustomer(CustomerDto customerDto) {
        try {
            return restClient.post()
                    .uri(customerServiceUrl + "/api/customers")
                    .body(customerDto)
                    .retrieve()
                    .body(CustomerDto.class);
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        try {
            return restClient.put()
                    .uri(customerServiceUrl + "/api/customers/" + id)
                    .body(customerDto)
                    .retrieve()
                    .body(CustomerDto.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException(
                    "Kund med id " + id + " hittades inte");
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }

    public void deleteCustomer(Long id) {
        try {
            restClient.delete()
                    .uri(customerServiceUrl + "/api/customers/" + id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound e) {
            throw new RuntimeException(
                    "Kund med id " + id + " hittades inte");
        } catch (HttpClientErrorException.Conflict e) {
            throw new IllegalStateException(
                    "Kan inte ta bort kund — det finns aktiva bokningar kopplade till kunden");
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }

    public boolean customerExists(Long id) {
        try {
            restClient.get()
                    .uri(customerServiceUrl + "/api/customers/" + id)
                    .retrieve()
                    .body(CustomerDto.class);
            return true;
        } catch (HttpClientErrorException.NotFound e) {
            return false;
        } catch (ResourceAccessException e) {
            throw new RuntimeException(
                    "Kundtjänsten är inte tillgänglig just nu, försök igen senare");
        }
    }
}