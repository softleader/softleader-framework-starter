package {pkg}.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tw.com.softleader.domain.scheduling.ScheduleManager;
import tw.com.softleader.domain.scheduling.leader.Contender;

@RestController
@RequestMapping("/rest/jobs")
public class JobController {

  @Autowired
  private ScheduleManager manager;

  @RequestMapping("/pause")
  public void pause() {
    Optional.ofNullable(manager).ifPresent(ScheduleManager::pause);
  }

  @RequestMapping("/pause/{timeout}/{unit}")
  public void pause(@PathVariable("timeout") long timeout, @PathVariable("unit") TimeUnit unit) {
    if (manager != null)
      manager.pause(timeout, unit);
  }

  @RequestMapping("/resume")
  public void resume() {
    Optional.ofNullable(manager).ifPresent(ScheduleManager::resume);
  }

  @RequestMapping("/relinquish")
  public void relinquish() {
    Optional.ofNullable(manager).ifPresent(ScheduleManager::relinquishLeadership);
  }

  @RequestMapping
  public Contender getCurrent() {
    return Optional.ofNullable(manager).map(ScheduleManager::getCurrent).orElseGet(Contender::new);
  }

  @RequestMapping("/contenders")
  public Collection<Contender> getContenders() {
    return Optional.ofNullable(manager).map(ScheduleManager::getContenders)
        .orElseGet(ArrayList::new);
  }

}
