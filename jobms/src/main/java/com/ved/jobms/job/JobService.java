package com.ved.jobms.job;
import com.ved.jobms.job.dto.JobDTO;

import  java.util.*;

public interface JobService {

    List<JobDTO> findAll();
    JobDTO getJobById(Long id);
    void createJob(Job job);
    boolean deleteJobById(Long id);
    boolean updateJob(Long id, Job updatedJob);
}
