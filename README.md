# Event scheduling

Two Steps to follow:
  1. State needs to implement ```SchedulableState```<br/>
    Override method ```nextScheduledActivity``` which returns ```ScheduledActivity```<br/>
    
  2. Implement a ```FlowLogic``` to be executed by each node<br/>
    The FlowLogic must be annotated with ```@SchedulableFlow```.

