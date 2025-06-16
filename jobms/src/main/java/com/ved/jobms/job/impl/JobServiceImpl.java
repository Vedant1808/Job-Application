package com.ved.jobms.job.impl;

import com.ved.jobms.job.Job;
import com.ved.jobms.job.JobRepository;
import com.ved.jobms.job.JobService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

//    private List<Job> jobs=new ArrayList<>();

    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
        //        return jobs.stream().filter(job-> job.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public void createJob(Job job) {
           jobRepository.save(job);
    }


    @Override
    public boolean deleteJobById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
//        try {
//            jobRepository.deleteById(id);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//        Iterator<Job> iterator=jobs.iterator();
//        while(iterator.hasNext())
//        {
//            Job job=iterator.next();
//            if(job.getId().equals(id))
//            {
//                iterator.remove();
//                return true;
//            }
//        }
//        return false;
    }

    @Override
    public boolean updateJob(Long id, Job updatedJob) {
        Optional<Job> jobOptional = jobRepository.findById(id);

        if (jobOptional.isPresent()) {
            Job job = jobOptional.get();
            job.setTitle(updatedJob.getTitle());
            job.setDescription(updatedJob.getDescription());
            job.setMaxSalary(updatedJob.getMaxSalary());
            job.setMinSalary(updatedJob.getMinSalary());
            job.setLocation(updatedJob.getLocation());
            jobRepository.save(job);
            return true;
        }
        return false;
//         for(Job job:jobs)
//         {
//             if(job.getId().equals(id))
//             {
//                 job.setTitle(updatedJob.getTitle());
//                 job.setDescription(updatedJob.getDescription());
//                 job.setMaxSalary(updatedJob.getMaxSalary());
//                 job.setMinSalary(updatedJob.getMinSalary());
//                 job.setLocation(updatedJob.getLocation());
//                 return true;
//             }
//         }
//         return false;
    }
}
