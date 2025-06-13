package com.junnu.app.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.junnu.app.Models.Statistics;

@Repository
public interface StatisticsRepo extends JpaRepository<Statistics,Long> {
    List<Statistics> findAllByStatus(String state);   
}
