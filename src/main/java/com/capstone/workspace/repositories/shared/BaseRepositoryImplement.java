package com.capstone.workspace.repositories.shared;

import com.capstone.workspace.dtos.shared.PaginationDto;
import com.capstone.workspace.entities.partner.IPartnerEntity;
import com.capstone.workspace.entities.shared.BaseEntity;
import com.capstone.workspace.enums.user.UserType;
import com.capstone.workspace.helpers.shared.BeanHelper;
import com.capstone.workspace.models.auth.UserIdentity;
import com.capstone.workspace.models.shared.PaginationResponseModel;
import com.capstone.workspace.services.auth.IdentityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Transactional
public class BaseRepositoryImplement<T extends BaseEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {
    private final EntityManager entityManager;

    public BaseRepositoryImplement(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    public BaseRepositoryImplement(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.entityManager = em;
    }

    @Override
    public PaginationResponseModel<T> searchBy(
        String keyword,
        String[] searchFields,
        Map<String, Object> filterParams,
        Map<String, Sort.Direction> orderParams,
        PaginationDto pagination
    ) {
        StringBuilder queryString = new StringBuilder("SELECT e FROM " + getDomainClass().getName() + " e WHERE isDeleted = FALSE");

        IdentityService identityService = BeanHelper.getBean(IdentityService.class);
        UserIdentity userIdentity = identityService.getUserIdentity();

        if (userIdentity != null) {
            if (userIdentity.getPartnerId() == null) {
                if (userIdentity.getUsername() != null && userIdentity.getUserType() != UserType.ADMIN) {
                    queryString.append(" AND createdBy = ").append("'").append(userIdentity.getUsername()).append("'");
                }
            } else {
                if (IPartnerEntity.class.isAssignableFrom(getDomainClass())) {
                    queryString.append(" AND partnerId = ").append("'").append(userIdentity.getPartnerId()).append("'");
                }
                // TODO: Implement if have store id
            }
        }

        if (filterParams != null && !filterParams.isEmpty()) {
            for (Map.Entry<String, Object> entry: filterParams.entrySet()) {
                queryString.append(" AND ").append(entry.getKey()).append(" = ").append(String.valueOf(entry.getValue()));
            }
        }

        if (keyword != null && !keyword.isBlank() && searchFields != null && searchFields.length > 0) {
            queryString.append(" AND (");
            for (int i = 0; i < searchFields.length; i++) {
                if (i != 0) {
                    queryString.append(" OR ");
                }
                queryString.append(searchFields[i]).append(" ILIKE ").append("'%").append(keyword).append("%'");
            }
            queryString.append(")");
        }

        if (orderParams != null && !orderParams.isEmpty()) {
            Map.Entry firstEntry = orderParams.entrySet().stream().findFirst().get();
            queryString.append(" ORDER BY ").append(firstEntry.getKey()).append(" ").append(firstEntry.getValue());
        }

        int page = 1;
        int itemsPerPage = 10;
        if (pagination != null) {
            page = pagination.getPage();
            itemsPerPage = pagination.getItemsPerPage();
        }

        TypedQuery<T> typedQuery = this.entityManager.createQuery(queryString.toString(), getDomainClass());
        List<T> resultList = typedQuery.getResultList();
        int totalItems = resultList.size();

        List<T> data = resultList.stream()
            .skip((long) itemsPerPage * (page - 1))
            .limit(itemsPerPage)
            .toList();

        return PaginationResponseModel.<T>builder()
            .results(data)
            .page(page)
            .itemsPerPage(itemsPerPage)
            .totalItems(totalItems)
            .build();
    }
}