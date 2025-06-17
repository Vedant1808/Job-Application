package com.ved.jobms.job;
import com.ved.jobms.job.dto.JobWithCompanyDTO;

import  java.util.*;

public interface JobService {

    List<JobWithCompanyDTO> findAll();
    Job getJobById(Long id);
    void createJob(Job job);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
