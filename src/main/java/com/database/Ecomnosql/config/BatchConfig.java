package com.database.Ecomnosql.config;

import com.database.Ecomnosql.model.Product;
import com.database.Ecomnosql.processor.ProductProcessor;
import com.database.Ecomnosql.repository.ProductRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    // ── Reader ───────────────────────────────────────────────
    @Bean
    public FlatFileItemReader<Product> reader() {
        BeanWrapperFieldSetMapper<Product> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Product.class);

        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("inventory_drop.csv"))
                .linesToSkip(1) // Skips the header row in CSV
                .delimited()
                .names("productId", "productName", "price", "stock", "sales")
                .fieldSetMapper(mapper)
                .build();
    }

    // ── Writer ───────────────────────────────────────────────
    @Bean
    public RepositoryItemWriter<Product> writer(ProductRepository repository) {
        return new RepositoryItemWriterBuilder<Product>()
                .repository(repository)
                .methodName("save")
                .build();
    }

    // ── Step ─────────────────────────────────────────────────
    @Bean
    public Step processProductsStep(JobRepository jobRepository,
                                    PlatformTransactionManager transactionManager,
                                    FlatFileItemReader<Product> reader,
                                    // Change the name here to match exactly:
                                    ProductProcessor myProductProcessor,
                                    RepositoryItemWriter<Product> writer) {
        return new StepBuilder("processProductsStep", jobRepository)
                .<Product, Product>chunk(10, transactionManager)
                .reader(reader)
                // This is the line that was missing your custom code:
                .processor(myProductProcessor)
                .writer(writer)
                .build();
    }

    // ── Job ──────────────────────────────────────────────────
    @Bean
    public Job importProductJob(JobRepository jobRepository, Step processProductsStep) {
        return new JobBuilder("importProductJob", jobRepository)
                .start(processProductsStep)
                .build();
    }
}