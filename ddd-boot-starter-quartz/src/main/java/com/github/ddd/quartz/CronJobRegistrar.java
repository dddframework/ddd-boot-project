package com.github.ddd.quartz;

import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ranger
 */
@Slf4j
public class CronJobRegistrar
        implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);

        Set<String> basePackages = getBasePackages(importingClassMetadata);

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition component : candidateComponents) {
                if (component instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) component;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();

                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(
                                    CronJob.class.getCanonicalName());
                    String description = (String) attributes.get("description");
                    String cron = (String) attributes.get("cron");
                    int misfireInstruction = (int) attributes.get("misfireInstruction");
                    Class<?> clazz = null;
                    try {
                        clazz = Class.forName(component.getBeanClassName());
                    } catch (ClassNotFoundException ignored) {
                    }
                    if (!QuartzJobBean.class.isAssignableFrom(clazz)) {
                        throw new RuntimeException("被CronJob标记的类不是QuartzJobBean的子类");
                    }
                    Class<? extends Job> jobClass = (Class<? extends Job>) clazz;

                    JobDetail jobDetail = JobBuilder.newJob(jobClass)
                            .withIdentity(component.getBeanClassName() + "|jobDetail", Scheduler.DEFAULT_GROUP)
                            .withDescription(description)
                            .storeDurably()
                            .build();

                    registerJobDetailBean(registry, component, description, jobClass);

                    registerCronTrigger(registry, component, cron, jobDetail, misfireInstruction);
                }
            }
        }
    }

    private void registerCronTrigger(BeanDefinitionRegistry registry, BeanDefinition component,
                                     String cron, JobDetail jobDetail, int misfireInstruction) {
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(CronTriggerFactoryBean.class);
        definition.addPropertyValue("cronExpression", cron);
        definition.addPropertyValue("jobDetail", jobDetail);
        definition.addPropertyValue("misfireInstruction", misfireInstruction);
        String beanName = component.getBeanClassName() + "|cronTrigger";
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition abstractBeanDefinition = definition.getBeanDefinition();
        BeanDefinitionHolder holder = new BeanDefinitionHolder(abstractBeanDefinition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void registerJobDetailBean(BeanDefinitionRegistry registry, BeanDefinition component, String description, Class<? extends Job> jobClass) {
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(JobDetailFactoryBean.class);
        definition.addPropertyValue("jobClass", jobClass);
        definition.addPropertyValue("durability", true);
        definition.addPropertyValue("description", description);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        AbstractBeanDefinition abstractBeanDefinition = definition.getBeanDefinition();
        String beanName = component.getBeanClassName() + "|jobDetail";
        BeanDefinitionHolder holder = new BeanDefinitionHolder(abstractBeanDefinition, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableCronJob.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        assert attributes != null;
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class<?>[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false, this.environment);
        provider.addIncludeFilter(new AnnotationTypeFilter(CronJob.class));
        return provider;
    }
}
