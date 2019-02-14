# [Event scheduling](https://docs.corda.net/event-scheduling.html)

## Two Steps to follow:
  1. State needs to implement ```SchedulableState```[here](https://github.com/vardan10/Corda-schedulable-state/blob/master/heartbeat/src/main/kotlin/com/heartbeat/state/HeartState.kt#L20)<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```[here](https://github.com/vardan10/Corda-schedulable-state/blob/master/heartbeat/src/main/kotlin/com/heartbeat/state/HeartState.kt#L25)<br/>
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.

## Note
1. Activities associated with any consumed states are unscheduled.
2. Any newly produced states are then queried via the nextScheduledActivity method and if they do not return null then that activity is scheduled based on the content of the ScheduledActivity object returned.
