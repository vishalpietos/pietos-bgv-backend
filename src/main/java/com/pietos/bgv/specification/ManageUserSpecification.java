package com.pietos.bgv.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pietos.bgv.dto.request.ManageUser.ManageUserSearchRequest;
import com.pietos.bgv.entity.ManageUser;

public class ManageUserSpecification {

    public static Specification<ManageUser> search(
            ManageUserSearchRequest request) {

        return (root, query, criteriaBuilder) -> {

            var predicate = criteriaBuilder.conjunction();

            // Client
            if (request.getClientId() != null) {
                predicate.getExpressions().add(
                        criteriaBuilder.equal(
                                root.get("clientInformation").get("id"),
                                request.getClientId()));
            }

            // Location
            if (request.getLocationId() != null) {
                predicate.getExpressions().add(
                        criteriaBuilder.equal(
                                root.get("clientLocation").get("id"),
                                request.getLocationId()));
            }

            // First Name
            if (request.getFirstName() != null
                    && !request.getFirstName().isBlank()) {

                predicate.getExpressions().add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("firstName")),
                                "%" + request.getFirstName().toLowerCase() + "%"));
            }

            // Last Name
            if (request.getLastName() != null
                    && !request.getLastName().isBlank()) {

                predicate.getExpressions().add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("lastName")),
                                "%" + request.getLastName().toLowerCase() + "%"));
            }

            // Email
            if (request.getEmail() != null
                    && !request.getEmail().isBlank()) {

                predicate.getExpressions().add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.join("systemUser").get("email")),
                                "%" + request.getEmail().toLowerCase() + "%"));
            }

            // Mobile
            if (request.getMobile() != null
                    && !request.getMobile().isBlank()) {

                predicate.getExpressions().add(
                        criteriaBuilder.like(
                                root.get("mobile"),
                                "%" + request.getMobile() + "%"));
            }

            // Active Status
            if (request.getIsActive() != null) {

                predicate.getExpressions().add(
                        criteriaBuilder.equal(
                                root.get("isActive"),
                                request.getIsActive()));
            }

            return predicate;
        };
    }
}