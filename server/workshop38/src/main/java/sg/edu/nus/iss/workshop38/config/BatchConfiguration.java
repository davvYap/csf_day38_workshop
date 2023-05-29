// package sg.edu.nus.iss.workshop38.config;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// import org.bson.Document;
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import
// org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.launch.support.RunIdIncrementer;
// import org.springframework.batch.core.repository.JobRepository;
// import
// org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.item.ItemReader;
// import org.springframework.batch.item.data.MongoItemWriter;
// import org.springframework.batch.item.support.ListItemReader;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.transaction.PlatformTransactionManager;

// import sg.edu.nus.iss.workshop38.model.ImageLikes;
// import sg.edu.nus.iss.workshop38.model.JobCompletionNotification;
// import sg.edu.nus.iss.workshop38.service.ImageService;
// import sg.edu.nus.iss.workshop38.util.BatchProcessor;

// @Configuration
// @EnableBatchProcessing
// public class BatchConfiguration {

// @Autowired
// private MongoTemplate mongoTemplate;

// @Autowired
// private ImageService imgSvc;

// @Autowired
// private JdbcTemplate jdbcTemplate;

// @Bean
// public JobRepository jobRepository() throws Exception {
// JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
// factory.setDataSource(jdbcTemplate.getDataSource());
// factory.afterPropertiesSet();
// return factory.getObject();
// }

// @Bean
// public ItemReader<Document> reader() throws IOException {
// List<String> keys = imgSvc.getImageKeysFromRedis();
// List<ImageLikes> imgLikes = new ArrayList<>();
// for (String key : keys) {
// ImageLikes imgLike = imgSvc.getImageLikes(key);
// imgLikes.add(imgLike);
// }

// List<Document> docs = imgLikes.stream()
// .map(img -> Document.parse(img.toJsonObject().toString()))
// .toList();

// return new ListItemReader<>(docs);
// }

// @Bean
// public BatchProcessor processor() {
// return new BatchProcessor();
// }

// @Bean
// public MongoItemWriter<Document> writer() {
// MongoItemWriter<Document> writer = new MongoItemWriter<>();
// writer.setTemplate(mongoTemplate);
// return writer;
// }

// @Bean
// public Step step1(PlatformTransactionManager transactionManager,
// MongoItemWriter<Document> writer) throws Exception {
// return new StepBuilder("step1", jobRepository()).<Document,
// Document>chunk(10,
// transactionManager)
// .reader(reader())
// .processor(processor())
// .writer(writer)
// .build();
// }

// @Bean
// public Job importImageLikesJob(JobRepository jobRepository,
// JobCompletionNotification listener,
// Step step1) throws Exception {
// return new JobBuilder("importImageLikesJob", jobRepository())
// .incrementer(new RunIdIncrementer())
// .listener(listener)
// .flow(step1)
// .end()
// .build();
// }

// }
