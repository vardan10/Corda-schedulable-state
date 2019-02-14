# Event scheduling

## Two Steps to follow:
  1. State needs to implement ```SchedulableState```<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```<br/>
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.

## Note
1. Activities associated with any consumed states are unscheduled.
2. Any newly produced states are then queried via the nextScheduledActivity method and if they do not return null then that activity is scheduled based on the content of the ScheduledActivity object returned.
