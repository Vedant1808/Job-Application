package com.ved.jobms.job.impl;

import com.ved.jobms.job.Job;
import com.ved.jobms.job.JobRepository;
import com.ved.jobms.job.JobService;
import com.ved.jobms.job.clients.CompanyClient;
import com.ved.jobms.job.clients.ReviewClient;
import com.ved.jobms.job.dto.JobDTO;
import com.ved.jobms.job.external.Company;
import com.ved.jobms.job.external.Review;
import com.ved.jobms.job.mapper.JobMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {

//    private List<Job> jobs=new ArrayList<>();

    JobRepository jobRepository;

    @Autowired
    RestTemplate restTemplate;

    private CompanyClient companyClient;

    private ReviewClient reviewClient;

    public JobServiceImpl(JobRepository jobRepository,CompanyClient companyClient,ReviewClient reviewClient) {
        this.jobRepository = jobRepository;
        this.companyClient=companyClient;
        this.reviewClient=reviewClient;
    }

    @Override
//    @CircuitBreaker(name="companyBreaker",fallbackMethod = "companyBreakerFallback")
//    @Retry(name="companyBreaker",fallbackMethod = "companyBreakerFallback")
    @RateLimiter(name="companyBreaker",fallbackMethod = "companyBreakerFallback")
    public List<JobDTO> findAll() {
        List<Job> jobs=jobRepository.findAll();
        List<JobDTO> jobDTOS =new ArrayList<>();

        return jobs.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<String> companyBreakerFallback(Exception e){
        List<String> list = new ArrayList<>();
        list.add("Dummy");
        return list;
    }

    private JobDTO convertToDto(Job job){

//        Company company=restTemplate.getForObject("http://COMPANY-SERVICE:8081/companies/"+job.getCompanyId(), Company.class);
      Company company= companyClient.getCompany(job.getCompanyId());

//        ResponseEntity<List<Review>> reviewResponse=restTemplate.exchange(
//                "http://REVIEW-SERVICE:8083/reviews?companyId=" + job.getCompanyId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<Review>>() {
//                });
//        List<Review> reviews=reviewResponse.getBody();
        List<Review> reviews=reviewClient.getReviews(job.getCompanyId());
        JobDTO jobDTO = JobMapper.mapToJobWithCompanyDto(job,company,reviews);
        return jobDTO;
    }

    @Override
    public JobDTO getJobById(Long id) {
        Job job=jobRepository.findById(id).orElse(null);
        return convertToDto(job);
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
