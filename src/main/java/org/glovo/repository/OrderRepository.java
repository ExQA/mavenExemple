package org.glovo.repository;

import org.glovo.model.GlovoOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<GlovoOrder, Long> {
}
