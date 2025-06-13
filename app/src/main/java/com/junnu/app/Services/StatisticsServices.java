package com.junnu.app.Services;

import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.junnu.app.Models.Statistics;
import com.junnu.app.Repositories.StatisticsRepo;

@Service
public class StatisticsServices {
    @Autowired
    StatisticsRepo stats;
     public enum states{
        SUCCESSFUL,
        FAILED,
        EXPIRED,
        INVALID

    }
    public float getSuccessRate(){
        float result = 0.0f;
        long count = stats.count();
        System.out.println("Count is : " + count);
        List<Statistics> list = stats.findAllByStatus(states.SUCCESSFUL.name());
        System.out.println("success rows is : " + list.size());
        result = list.size() / (float)count;
        return result;
    }
    public float getFailureRate(){
        float result = 0.0f;
        long count = stats.count();
        List<Statistics> list = stats.findAllByStatus(states.FAILED.name());
        result = list.size() / (float)count;
        return result;
    }

    public float getExpiredRate(){
        float result = 0.0f;
        long count = stats.count();
        List<Statistics> list = stats.findAllByStatus(states.EXPIRED.name());
        result = list.size() / (float)count;
        return result;
    }
    public float getInvalidRate(){
        float result = 0.0f;
        long count = stats.count();
        List<Statistics> list = stats.findAllByStatus(states.INVALID.name());
        result = list.size() / (float)count;
        return result;
    }
}
