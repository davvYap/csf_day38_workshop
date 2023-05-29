// package sg.edu.nus.iss.workshop38.service;

// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.JobParameters;
// import org.springframework.batch.core.JobParametersBuilder;
// import org.springframework.batch.core.launch.JobLauncher;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// @Service
// public class BatchService {

// @Autowired
// private JobLauncher jobLauncher;

// @Autowired
// private Job myJob;

// public void runBatchJob() throws Exception {

// JobParameters jobParameter = new JobParametersBuilder()
// .addString("redisToMongo", String.valueOf(System.currentTimeMillis()))
// .toJobParameters();
// jobLauncher.run(myJob, jobParameter);
// }
// }
