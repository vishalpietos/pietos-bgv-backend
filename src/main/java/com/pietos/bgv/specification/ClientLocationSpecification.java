package com.pietos.bgv.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pietos.bgv.entity.ClientLocation;
import com.pietos.bgv.enums.ClientLocationStatus;

public class ClientLocationSpecification {

    public static Specification<ClientLocation> hasClientId(Long clientId) {

        return (root, query, cb) ->
                clientId == null
                        ? null
                        : cb.equal(root.get("clientInformation").get("id"), clientId);
    }

    public static Specification<ClientLocation> hasLocationName(String locationName) {

        return (root, query, cb) ->
                locationName == null || locationName.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("locationName")),
                                "%" + locationName.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasCountry(String country) {

        return (root, query, cb) ->
                country == null || country.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("country")),
                                "%" + country.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasState(String state) {

        return (root, query, cb) ->
                state == null || state.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("state")),
                                "%" + state.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasCity(String city) {

        return (root, query, cb) ->
                city == null || city.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("city")),
                                "%" + city.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasOfficialEmail(String officialEmail) {

        return (root, query, cb) ->
                officialEmail == null || officialEmail.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("officialEmail")),
                                "%" + officialEmail.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasContactPerson(String contactPerson) {

        return (root, query, cb) ->
                contactPerson == null || contactPerson.isBlank()
                        ? null
                        : cb.like(
                                cb.lower(root.get("contactPerson")),
                                "%" + contactPerson.toLowerCase() + "%");
    }

    public static Specification<ClientLocation> hasMobile(String mobile) {

        return (root, query, cb) ->
                mobile == null || mobile.isBlank()
                        ? null
                        : cb.like(
                                root.get("mobile"),
                                "%" + mobile + "%");
    }

    public static Specification<ClientLocation> hasStatus(ClientLocationStatus status) {

        return (root, query, cb) ->
                status == null
                        ? null
                        : cb.equal(root.get("status"), status);
    }

}