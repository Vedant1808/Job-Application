package com.ved.firstjobapp.job;
import  java.util.*;

public interface JobService {

    List<Job> findAll();
    Job getJobById(Long id);
    void createJob(Job job);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
