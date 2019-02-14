# [Event scheduling](https://docs.corda.net/event-scheduling.html)

## Two Steps to follow:
  1. State needs to implement ```SchedulableState``` [check this](https://github.com/vardan10/Corda-schedulable-state/blob/master/heartbeat/src/main/kotlin/com/heartbeat/state/HeartState.kt#L20)<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity``` [check this](https://github.com/vardan10/Corda-schedulable-state/blob/master/heartbeat/src/main/kotlin/com/heartbeat/state/HeartState.kt#L25)<br/>
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```. [check this](https://github.com/vardan10/Corda-schedulable-state/blob/master/heartbeat/src/main/kotlin/com/heartbeat/flow/HeartbeatFlow.kt#L23)

## Note
1. Activities associated with any **consumed states are unscheduled**.
2. Any newly produced states are then queried via the nextScheduledActivity method and **if they do not return null then that activity is scheduled** based on the content of the ScheduledActivity object returned.


## Example
```
override fun nextScheduledActivity(thisStateRef: StateRef, flowLogicRefFactory: FlowLogicRefFactory): ScheduledActivity? {
    val nextFixingOf = nextFixingOf() ?: return null

    // This is perhaps not how we should determine the time point in the business day, but instead expect the schedule to detail some of these aspects
    val instant = suggestInterestRateAnnouncementTimeWindow(index = nextFixingOf.name, source = floatingLeg.indexSource, date = nextFixingOf.forDay).fromTime!!
    return ScheduledActivity(flowLogicRefFactory.create("net.corda.irs.flows.FixingFlow\$FixingRoleDecider", thisStateRef), instant)
}
```
