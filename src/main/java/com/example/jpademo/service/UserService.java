package com.example.jpademo.service;

import com.example.jpademo.entity.User;
import com.example.jpademo.repository.UserRepository;
import com.example.jpademo.request.UserCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;
	private final EntityManager entityManager;

	private static final String SHIFTS = "shifts";
	private static final String HAS_SHIFT_AFTER = "date";
	private static final String ID = "id";

	public List<User> search(UserCriteria userCriteria) {

		return findWithRepo(userCriteria);
//		return findWithQuery(userCriteria);

	}

	private List<User> findWithQuery(UserCriteria userCriteria) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
		Root<User> userRoot = criteriaQuery.from(User.class);
		criteriaQuery.distinct(true);

		List<Predicate> idPredicates = new ArrayList<>();

		userCriteria.getIds()
				.forEach(id -> idPredicates.add(criteriaBuilder.equal(userRoot.get(ID), id)));

		Predicate totalIdPredicate = !userCriteria.getIds().isEmpty() ? criteriaBuilder.or(idPredicates.toArray(new Predicate[0])) : null;

		Predicate datePredicate = null;
		if (userCriteria.getHasShiftAfter() != null) {
			Path<User> path;
			path = userRoot.join(SHIFTS, JoinType.INNER);
			datePredicate = criteriaBuilder.greaterThanOrEqualTo(path.get(HAS_SHIFT_AFTER), userCriteria.getHasShiftAfter());
		}

		Predicate finalPredicate = null;
		if (totalIdPredicate != null && datePredicate != null)
			finalPredicate = criteriaBuilder.and(totalIdPredicate, datePredicate);
		else if (totalIdPredicate != null) finalPredicate = totalIdPredicate;
		else if (datePredicate != null) finalPredicate = datePredicate;

		if (finalPredicate != null) {
			criteriaQuery.where(finalPredicate);
			return entityManager.createQuery(criteriaQuery).getResultList();
		} else {
			return new ArrayList<>();
		}
	}

	private List<User> findWithRepo(UserCriteria userCriteria) {
		List<Specification<User>> idSpecifications = new ArrayList<>();
		userCriteria.getIds()
				.forEach(id -> idSpecifications.add(Specification.where((root, query, cb) -> {
					query.distinct(true);
					return cb.equal(root.get(ID), id);
				})));

		Specification<User> totalIdSpecification = idSpecifications.size() > 0 ? idSpecifications.get(0) : null;
		for (int i = 1; i < idSpecifications.size(); i++) {
			totalIdSpecification = totalIdSpecification.or(idSpecifications.get(i));
		}

		Specification<User> dateSpecification = null;
		if (userCriteria.getHasShiftAfter() != null) {
			dateSpecification = Specification.where(
					(root, query, cb) -> {
						query.distinct(true);
						Path<User> path;
						path = root.join(SHIFTS, JoinType.INNER);
						return cb.greaterThanOrEqualTo(path.get(HAS_SHIFT_AFTER), userCriteria.getHasShiftAfter());
					});
		}

		Specification<User> finalSpecification = null;
		if (totalIdSpecification != null && dateSpecification != null)
			finalSpecification = totalIdSpecification.and(dateSpecification);
		else if (totalIdSpecification != null) finalSpecification = totalIdSpecification;
		else if (dateSpecification != null) finalSpecification = dateSpecification;

		if (finalSpecification != null) {
			return userRepository.findAll(finalSpecification);
		} else {
			return new ArrayList<>();
		}
	}
}
