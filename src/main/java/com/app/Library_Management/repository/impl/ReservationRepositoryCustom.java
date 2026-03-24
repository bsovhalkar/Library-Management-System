package com.app.Library_Management.repository.impl;

import com.app.Library_Management.model.Reservation;
import com.app.Library_Management.payload.request.ReservationSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReservationRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Reservation> searchReservationsWithFilters(ReservationSearchRequest request, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Reservation> query = cb.createQuery(Reservation.class);
        Root<Reservation> root = query.from(Reservation.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getReservationStatus() != null) {
            predicates.add(cb.equal(root.get("reservationStatus"), request.getReservationStatus()));
        }

        if (request.getUserId() != null) {
            predicates.add(cb.equal(root.get("user").get("id"), request.getUserId()));
        }

        if (request.getBookId() != null) {
            predicates.add(cb.equal(root.get("book").get("id"), request.getBookId()));
        }

        if (request.getActiveOnly() != null && request.getActiveOnly()) {
            predicates.add(root.get("reservationStatus").in("PENDING", "AVAILABLE"));
        }

        if (!predicates.isEmpty()) {
            query.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        String sortBy = request.getSortBy() != null ? request.getSortBy() : "reservedAt";
        if (request.getSortDirection() != null && request.getSortDirection().equalsIgnoreCase("ASC")) {
            query.orderBy(cb.asc(root.get(sortBy)));
        } else {
            query.orderBy(cb.desc(root.get(sortBy)));
        }

        TypedQuery<Reservation> typedQuery = entityManager.createQuery(query);
        
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Reservation> reservations = typedQuery.getResultList();

        return new PageImpl<>(reservations, pageable, totalRows);
    }
}
