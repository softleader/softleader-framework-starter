package {pkg}.config;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import tw.com.softleader.domain.scheduling.LeaderElectionTaskScheduler;
import tw.com.softleader.domain.scheduling.ScheduleManager;
import tw.com.softleader.domain.scheduling.ScheduleManagers;
import tw.com.softleader.domain.scheduling.leader.CuratorLeaderLatch;
import tw.com.softleader.domain.scheduling.leader.LeaderElection;
import tw.com.softleader.domain.scheduling.quartz.AutowiringSpringBeanJobFactory;
import tw.com.softleader.domain.scheduling.quartz.LeaderElectionJobFactory;

@Configuration
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = "{pkg}.job")
@PropertySource({"classpath:schedule.properties"})
public class I519SchedulingConfig {

  @Value("${zookeeper.connect.string}")
  private String connectString;

  @Value("${task.scheduler.pool.size}")
  private String poolSize;

  @Value("${zookeeper.root.path}")
  private String rootPath;

  @Primary
  @Bean
  public ScheduleManager scheduleManager() {
    return new ScheduleManagers();
  }

  @Bean
  public LeaderElection leaderElection() {
    final CuratorLeaderLatch election = new CuratorLeaderLatch();
    election.setConnectString(connectString);
    election.setRootPath(rootPath);
    return election;
  }

  @Bean
  public TaskScheduler taskScheduler(LeaderElection leaderElection) {
    final ThreadPoolTaskScheduler taskSchedule = new ThreadPoolTaskScheduler();
    taskSchedule.setPoolSize(Integer.parseInt(poolSize));
    taskSchedule.afterPropertiesSet();
    return new LeaderElectionTaskScheduler(leaderElection, taskSchedule);
  }

}
