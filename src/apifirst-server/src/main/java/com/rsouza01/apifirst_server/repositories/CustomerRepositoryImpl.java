package com.rsouza01.apifirst_server.repositories;

import java.util.Optional;
import java.util.UUID;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Repository;

import com.rsouza01.apifirst.model.AddressDto;
import com.rsouza01.apifirst.model.CustomerDto;
import com.rsouza01.apifirst.model.PaymentMethodDto;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

    private final Map<UUID, CustomerDto> entityMap = new HashMap<>();

    @Override
    public <S extends CustomerDto> S save(S entity) {
        UUID id = UUID.randomUUID();

        CustomerDto.CustomerDtoBuilder customerDtoBuilder = CustomerDto.builder();

        customerDtoBuilder.id(id);

        if (entity.getBillToAddress() != null) {
            customerDtoBuilder.billToAddress(AddressDto.builder()
                    .id(UUID.randomUUID())
                    .addressLine1(entity.getBillToAddress().getAddressLine1())
                    .addressLine2(entity.getBillToAddress().getAddressLine2())
                    .city(entity.getBillToAddress().getCity())
                    .state(entity.getBillToAddress().getState())
                    .zip(entity.getBillToAddress().getZip())
                    .dateCreated(entity.getDateCreated())
                    .dateUpdated(entity.getDateUpdated())
                    .build());
        }

        if (entity.getShipToAddress() != null) {
            customerDtoBuilder.shipToAddress(AddressDto.builder()
                    .id(UUID.randomUUID())
                    .addressLine1(entity.getShipToAddress().getAddressLine1())
                    .addressLine2(entity.getShipToAddress().getAddressLine2())
                    .city(entity.getShipToAddress().getCity())
                    .state(entity.getShipToAddress().getState())
                    .zip(entity.getShipToAddress().getZip())
                    .dateCreated(entity.getDateCreated())
                    .dateUpdated(entity.getDateUpdated())
                    .build());
        }

        if (entity.getPaymentMethods() != null) {
            customerDtoBuilder.paymentMethods(entity.getPaymentMethods()
                    .stream()
                    .map(paymentMethod -> PaymentMethodDto.builder()
                            .id(UUID.randomUUID())
                            .cvv(paymentMethod.getCvv())
                            .displayName(paymentMethod.getDisplayName())
                            .cardNumber(paymentMethod.getCardNumber())
                            .expiryMonth(paymentMethod.getExpiryMonth())
                            .expiryYear(paymentMethod.getExpiryYear())
                            .dateCreated(paymentMethod.getDateCreated())
                            .dateUpdated(paymentMethod.getDateUpdated())
                            .build())
                    .collect(Collectors.toList()));
        }

        CustomerDto customer = customerDtoBuilder.email(entity.getEmail())
                .name(entity.getName())
                .phone(entity.getPhone())
                .dateCreated(OffsetDateTime.now())
                .dateUpdated(OffsetDateTime.now())
                .build();

        entityMap.put(id, customer);

        return (S) customer;
    }

    @Override
    public <S extends CustomerDto> Iterable<S> saveAll(Iterable<S> entities) {
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDto> findById(UUID uuid) {
        return Optional.of(entityMap.get(uuid));
    }

    @Override
    public boolean existsById(UUID uuid) {
        return entityMap.get(uuid) != null;
    }

    @Override
    public Iterable<CustomerDto> findAll() {
        return entityMap.values();
    }

    @Override
    public Iterable<CustomerDto> findAllById(Iterable<UUID> uuids) {
        return StreamSupport.stream(uuids.spliterator(), false)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return entityMap.size();
    }

    @Override
    public void deleteById(UUID uuid) {
        entityMap.remove(uuid);
    }

    @Override
    public void delete(CustomerDto entity) {
        entityMap.remove(entity.getId());
    }

    @Override
    public void deleteAllById(Iterable<? extends UUID> uuids) {
        uuids.forEach(this::deleteById);
    }

    @Override
    public void deleteAll(Iterable<? extends CustomerDto> entities) {
        entities.forEach(this::delete);
    }

    @Override
    public void deleteAll() {
        entityMap.clear();
    }
}
