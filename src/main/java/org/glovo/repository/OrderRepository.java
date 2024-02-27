package org.glovo.repository;

import org.glovo.entity.GlovoOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<GlovoOrder, Long> {
}
