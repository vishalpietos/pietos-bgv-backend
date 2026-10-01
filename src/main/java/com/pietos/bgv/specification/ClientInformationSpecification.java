package com.pietos.bgv.specification;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.pietos.bgv.entity.ClientInformation;
import com.pietos.bgv.enums.ClientStatus;

public class ClientInformationSpecification {

    public static Specification<ClientInformation> hasClientCode(String clientCode) {

        return (root, query, criteriaBuilder) ->
                clientCode == null || clientCode.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("clientCode")),
                                "%" + clientCode.toLowerCase() + "%");
    }

    public static Specification<ClientInformation> hasClientName(String clientName) {

        return (root, query, criteriaBuilder) ->
                clientName == null || clientName.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("clientName")),
                                "%" + clientName.toLowerCase() + "%");
    }

    public static Specification<ClientInformation> hasContactPerson(String contactPerson) {

        return (root, query, criteriaBuilder) ->
                contactPerson == null || contactPerson.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("contactPerson")),
                                "%" + contactPerson.toLowerCase() + "%");
    }
    
    public static Specification<ClientInformation> hasAbbreviation(String abbreviation) {

        return (root, query, criteriaBuilder) ->
        abbreviation == null || abbreviation.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("abbreviation")),
                                "%" + abbreviation.toLowerCase() + "%");
    }
    
    public static Specification<ClientInformation> hasCreatedAt(LocalDate clientSince) {

        return (root, query, criteriaBuilder) ->
                clientSince == null
                        ? null
                        : criteriaBuilder.between(
                                root.get("createdAt"),
                                clientSince.atStartOfDay(),
                                clientSince.plusDays(1).atStartOfDay());
    }

    public static Specification<ClientInformation> hasOfficialEmail(String officialEmail) {

        return (root, query, criteriaBuilder) ->
                officialEmail == null || officialEmail.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("officialEmail")),
                                "%" + officialEmail.toLowerCase() + "%");
    }

    public static Specification<ClientInformation> hasMobile(String mobile) {

        return (root, query, criteriaBuilder) ->
                mobile == null || mobile.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                root.get("mobile"),
                                "%" + mobile + "%");
    }

    public static Specification<ClientInformation> hasCountry(String country) {

        return (root, query, criteriaBuilder) ->
                country == null || country.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("country"), country);
    }

    public static Specification<ClientInformation> hasState(String state) {

        return (root, query, criteriaBuilder) ->
                state == null || state.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("state"), state);
    }

    public static Specification<ClientInformation> hasCity(String city) {

        return (root, query, criteriaBuilder) ->
                city == null || city.isBlank()
                        ? null
                        : criteriaBuilder.equal(root.get("city"), city);
    }

    public static Specification<ClientInformation> hasStatus(ClientStatus status) {

        return (root, query, criteriaBuilder) ->
                status == null
                        ? null
                        : criteriaBuilder.equal(root.get("status"), status);
    }

}